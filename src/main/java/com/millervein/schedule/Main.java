package com.millervein.schedule;

import java.math.BigDecimal;
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
		limits.put(StaffType.MEDTECH, 2);
		limits.put(StaffType.ULTRASOUND, 2);
		limits.put(StaffType.NURSE, 2);
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
		limits.put(RoomType.CONSULT, 5);
		limits.put(RoomType.PROCEDURE, 2);
		return limits;
	}

	/**
	 * Creates preset equipment limits
	 * @return
	 */
	private static ResourceTypeLimitMap<EquipmentType> equipmentLimits() {
		ResourceTypeLimitMap<EquipmentType> limits = ResourceTypeLimitMap.create();
		limits.put(EquipmentType.ULTRASOUND, 5);
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
		resourceTypeLimits.put(EquipmentType.class, equipmentLimits());
		return resourceTypeLimits;
	}

	public static void main(String[] args) throws Exception {
		SortedSet<LocalDateTime> timeRange = timeRange();
		ResourceTypeLimits resourceTypeLimits = resourceTypeLimits();
		AppointmentTypeSet appointmentTypes = AppointmentTypeSetBuilder.build();

		timeRange = TimeRange.generateTimeRange(LocalDateTime.now().with(LocalTime.of(8, 0)), LocalDateTime.now().with(LocalTime.of(17, 00)), Duration.ofMinutes(15));
		
		SolutionSetGenerator ag = new SolutionSetGenerator(appointmentTypes, resourceTypeLimits);
		SolutionSet solutionSet = ag.generateAppointmentsForTimes(timeRange);

		printSolutions(solutionSet);
		printSolutionCount(solutionSet);
		printMaxAppointments(solutionSet);
		printMaxValue(solutionSet);
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
	
	private static void printMaxValue(SolutionSet solutionSet) {
		BigDecimal maxValue = solutionSet.stream().map(s -> s.value()).reduce(new BigDecimal(0), (a, b) -> (b.compareTo(a) > 0) ? b : a);
		System.out.println("");
		System.out.println("Maximum Total Value: " + maxValue);
	}

	private static void printSolutions(SolutionSet solutionSet) {
		Integer i = 1;
		for (AppointmentList appointmentList : solutionSet) {
			System.out.println("Solution " + i + ":" + " - " + appointmentList.value());
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
