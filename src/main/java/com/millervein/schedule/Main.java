package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class Main {
	/**
	 * Creates preset time range
	 * 
	 * @return
	 */
	private static SortedSet<LocalDateTime> timeRange() {
		LocalDateTime start = LocalDateTime.now().with(LocalTime.of(8, 0));
		LocalDateTime end = LocalDateTime.now().with(LocalTime.of(17, 0));
		Duration interval = Duration.ofMinutes(15);
		return TimeRange.generateTimeRange(start, end, interval);
	}

	/**
	 * Creates preset staffing limits
	 * 
	 * @return
	 */
	private static ResourceTypeLimitMap<StaffType> staffLimits() {
		ResourceTypeLimitMap<StaffType> limits = ResourceTypeLimitMap.create();
		limits.put(StaffType.NURSE, 1);
		limits.put(StaffType.PHYSICIAN, 1);
		return limits;
	}

	/**
	 * Creates preset room limits
	 * 
	 * @return
	 */
	private static ResourceTypeLimitMap<RoomType> roomLimits() {
		ResourceTypeLimitMap<RoomType> limits = ResourceTypeLimitMap.create();
		limits.put(RoomType.CONSULT, 1);
		limits.put(RoomType.PROCEDURE, 1);
		return limits;
	}

	/**
	 * Creates collection of preset limits
	 * 
	 * @return
	 */
	private static ResourceTypeLimits resourceTypeLimits() {
		ResourceTypeLimits resourceTypeLimits = ResourceTypeLimits.create();
		resourceTypeLimits.put(StaffType.class, staffLimits());
		resourceTypeLimits.put(RoomType.class, roomLimits());
		return resourceTypeLimits;
	}

	/**
	 * Creates nurse heavy appointment type
	 * 
	 * @return
	 */
	private static AppointmentType nurseHeavy() {
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate(StaffType.NURSE, Duration.ofMinutes(0), Duration.ofMinutes(30)));
		rList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, Duration.ofMinutes(15), Duration.ofMinutes(15)));
		rList.add(new ResourceUsageTemplate(RoomType.CONSULT, Duration.ZERO, Duration.ofMinutes(45)));
		return new AppointmentType("Nurse Heavy", rList);
	}

	/**
	 * Creates physician heavy appointment type
	 * 
	 * @return
	 */
	private static AppointmentType physicianHeavy() {
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate(StaffType.NURSE, Duration.ofMinutes(0), Duration.ofMinutes(15)));
		rList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, Duration.ofMinutes(15), Duration.ofMinutes(30)));
		rList.add(new ResourceUsageTemplate(RoomType.CONSULT, Duration.ZERO, Duration.ofMinutes(45)));
		return new AppointmentType("Physician Heavy", rList);
	}

	/**
	 * Creates preset list of appointment types
	 * 
	 * @return
	 */
	private static AppointmentTypeSet appointmentTypes() {
		AppointmentTypeSet appointmentTypes = AppointmentTypeSet.create();
		appointmentTypes.add(nurseHeavy());
		appointmentTypes.add(physicianHeavy());
		return appointmentTypes;
	}

	public static void main(String[] args) throws Exception {
		SortedSet<LocalDateTime> timeRange = timeRange();
		ResourceTypeLimits resourceTypeLimits = resourceTypeLimits();
		AppointmentTypeSet appointmentTypes = appointmentTypes();

		timeRange = TimeRange.generateTimeRange(LocalDateTime.now().with(LocalTime.of(8, 0)), LocalDateTime.now().with(LocalTime.of(10, 00)), Duration.ofMinutes(15));
		
		SolutionSetGenerator ag = new SolutionSetGenerator(appointmentTypes, resourceTypeLimits);
		SolutionSet solutionSet = ag.generateAppointmentsForTimes(timeRange);

		printSolutions(solutionSet);
		printSolutionCount(solutionSet);
		printMaxAppointments(solutionSet);
	}

	private static void printSolutionCount(SolutionSet solutionSet) {
		Integer solutionCount = solutionSet.size();
		System.out.println("");
		System.out.println("Solution Count: " + solutionCount);
	}

	private static void printMaxAppointments(SolutionSet solutionSet) {
		Integer maxAppointments = solutionSet.stream().map(s -> s.size()).reduce(0, (a, b) -> b > a ? b : a);
		System.out.println("");
		System.out.println("Maximum Total Appointments: " + maxAppointments);
	}

	private static void printSolutions(SolutionSet solutionSet) {
		Integer i = 1;
		for (AppointmentList appointmentList : solutionSet) {
			System.out.println("Solution " + i + ":" + " - " + appointmentList.size());
			List<Appointment> appointments = appointmentList.stream()
					.sorted((a, b) -> a.getTimePeriod().getStart().compareTo(b.getTimePeriod().getStart()))
					.collect(Collectors.toList());
			for (Appointment appointment : appointments) {
				System.out.println(appointment.getAppointmentType().getName() + "|"
						+ appointment.getTimePeriod().getStart().format(DateTimeFormatter.ISO_LOCAL_TIME));
			}
			i++;
		}
	}

}
