package com.millervein.schedule;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.SortedSet;

public class SolutionSetGenerator {
	private AppointmentTypeSet appointmentTypes;
	private ResourceTypeLimits resourceTypeLimits;
	private Map<AppointmentType, Integer> maxConcurrentAppointments;

	public SolutionSetGenerator(AppointmentTypeSet appointmentTypes, ResourceTypeLimits resourceTypeLimits) {
		this.appointmentTypes = appointmentTypes;
		this.resourceTypeLimits = resourceTypeLimits;
		this.maxConcurrentAppointments = this.appointmentTypes
				.maxConcurrentAppointmentCountTypeMap(this.resourceTypeLimits);
	}

	/**
	 * Finds a list of all valid combinations of appointment types and times
	 * 
	 * @param times
	 * @param appointmentTypes
	 * @param staffLimits
	 * @return
	 */
	public SolutionSet generateAppointmentsForTimes(SortedSet<LocalDateTime> times) {
		SolutionSet solutions = SolutionSet.create();

		for (LocalDateTime time : times) {
			System.out.println(time);
			SolutionSet timeSolutions = generateAppointmentsForTime(time);
//			System.out.println("Time Solutions");
//			System.out.println(timeSolutions.size());
			solutions.combine(timeSolutions);
//			System.out.println("Combined Solutions");
//			System.out.println(solutions.size());
			solutions.filterInvalid(this.resourceTypeLimits);
//			System.out.println("Filtered Invalid Solutions");
//			System.out.println(solutions.size());
//			System.out.println("Filtered Suboptimal Solutions");
			solutions.filterSuboptimalValue();
			solutions.filterSuboptimalAppointmentCount();
//			System.out.println(solutions.size());
		}

		return solutions;
	}

	/**
	 * Finds a list of appointment list starting at a given time constrained by
	 * staff resources
	 * 
	 * @param time
	 * @param appointmentTypes
	 * @param staffLimits
	 * @param maxConcurrent
	 * @return
	 */
	public SolutionSet generateAppointmentsForTime(LocalDateTime time) {
		SolutionSet solutions = SolutionSet.create();

		for (AppointmentType appointmentType : this.appointmentTypes) {
			Integer limit = this.maxConcurrentAppointments.get(appointmentType);
			SolutionSet appointmentTypeSet = appointmentFactorial(appointmentType, time, limit, true);
			solutions.combine(appointmentTypeSet);
			solutions.filterInvalid(this.resourceTypeLimits);
		}

		return solutions;
	}

	private SolutionSet appointmentFactorial(AppointmentType appointmentType, LocalDateTime time, Integer count,
			boolean includeEmpty) {
		SolutionSet appointmentLists = SolutionSet.create();
		if (includeEmpty)
			appointmentLists.add(AppointmentList.create());
		while (count > 0) {
			appointmentLists.add(generateAppointments(appointmentType, time, count));
			count--;
		}
		return appointmentLists;
	}

	private AppointmentList generateAppointments(AppointmentType appointmentType, LocalDateTime time, Integer count) {
		AppointmentList testSolution = AppointmentList.create();
		while (count > 0) {
			testSolution.add(new Appointment(appointmentType, time));
			count--;
		}
		return testSolution;
	}

}
