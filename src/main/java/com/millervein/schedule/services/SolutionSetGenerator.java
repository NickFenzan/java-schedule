package com.millervein.schedule.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.millervein.schedule.domain.Appointment;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.ResourceLimit;
import com.millervein.schedule.domain.collections.AppointmentMultiset;
import com.millervein.schedule.domain.collections.AppointmentTypeDemandPool;
import com.millervein.schedule.domain.collections.SolutionSet;

public class SolutionSetGenerator {
	private Set<AppointmentType> appointmentTypes;
	private Set<ResourceLimit> resourceTypeLimits;
	private Map<AppointmentType, Integer> maxConcurrentAppointments;

	/**
	 * Solution Set Generators produce SolutionSets (Set<AppointmentList>) for a
	 * provided list of AppointmentTypes, constrained by available resources and
	 * demand for those appointments
	 * 
	 * @param appointmentTypes
	 * @param resourceTypeLimits
	 * @param appointmentTypeDemands
	 */
	@Inject
	public SolutionSetGenerator(Set<AppointmentType> appointmentTypes, Set<ResourceLimit> resourceTypeLimits) {
		this.appointmentTypes = appointmentTypes;
		this.resourceTypeLimits = resourceTypeLimits;
		this.maxConcurrentAppointments = this.maxConcurrentAppointmentCountTypeMap(this.resourceTypeLimits);
	}
	

	/**
	 * Finds a list of all valid combinations of appointment types and times
	 * 
	 * @param times
	 * @param appointmentTypes
	 * @param staffLimits
	 * @return
	 */
	public SolutionSet generateAppointmentsForTimes(SortedSet<LocalDateTime> times, AppointmentTypeDemandPool demandPool) {
		SolutionSet solutions = SolutionSet.create();
		
		Integer resourceLimitFiltered = 0;
		Integer demandFiltered = 0;
		Integer valueFiltered = 0;
		Integer apptCountFiltered = 0;
		
		for (LocalDateTime time : times) {
//			System.out.println(time);
			SolutionSet timeSolutions = generateAppointmentsForTime(time);
			solutions = solutions.combine(timeSolutions);
			Integer count = solutions.size();
			solutions = solutions.filterInvalidForResourceLimits(resourceTypeLimits);
			resourceLimitFiltered += count - solutions.size();
			count = solutions.size();
			solutions = solutions.filterAppointmentTypeDemands(demandPool);
			demandFiltered += count - solutions.size();
			count = solutions.size();
			solutions = solutions.filterSuboptimalValue();
			valueFiltered += count - solutions.size();
			count = solutions.size();
			/* Not doing this has given more solutions, but of the same value */
			solutions = solutions.filterSuboptimalAppointmentCount();
			apptCountFiltered += count - solutions.size();
			count = solutions.size();
		}
		
		System.out.println("Resource Limit Filtered: " + resourceLimitFiltered);
		System.out.println("Demand Filtered: " + demandFiltered);
		System.out.println("Value Filtered: " + valueFiltered);
		System.out.println("Appointment Count Filtered: " + apptCountFiltered);

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
	private SolutionSet generateAppointmentsForTime(LocalDateTime time) {
		SolutionSet solutions = SolutionSet.create();
		List<AppointmentType> appointmentTypes = Lists.newArrayList(this.appointmentTypes);
		appointmentTypes.sort((a,b)->a.getValue().compareTo(b.getValue()));
		for (AppointmentType appointmentType : appointmentTypes) {
			Integer limit = this.maxConcurrentAppointments.get(appointmentType);
			SolutionSet appointmentTypeSet = appointmentFactorial(appointmentType, time, limit, true);
			solutions = solutions.combine(appointmentTypeSet).filterInvalidForResourceLimits(this.resourceTypeLimits);
		}

		return solutions;
	}

	private SolutionSet appointmentFactorial(AppointmentType appointmentType, LocalDateTime time, Integer count,
			boolean includeEmpty) {
		SolutionSet appointmentLists = SolutionSet.create();
		if (includeEmpty)
			appointmentLists.add(AppointmentMultiset.create());
		while (count > 0) {
			appointmentLists.add(generateAppointments(appointmentType, time, count));
			count--;
		}
		return appointmentLists;
	}

	private AppointmentMultiset generateAppointments(AppointmentType appointmentType, LocalDateTime time, Integer count) {
		AppointmentMultiset testSolution = AppointmentMultiset.create();
		while (count > 0) {
			testSolution.add(Appointment.create(appointmentType, time));
			count--;
		}
		return testSolution;
	}

	/**
	 * Returns the maximum number of appointments possible at a given time,
	 * given its staff limits
	 * 
	 * @param time
	 * @param appointmentType
	 * @param staffLimits
	 * @return
	 */
	private Integer maximumConcurrentAppointmentCount(Set<ResourceLimit> resourceLimits,
			AppointmentType appointmentType) {
		AppointmentMultiset appointments = AppointmentMultiset.create();
		for (AppointmentMultiset uncheckedAppointments = AppointmentMultiset.create(appointments); uncheckedAppointments
				.resourceLimitsValid(resourceLimits); uncheckedAppointments
						.add(Appointment.create(appointmentType, LocalDateTime.now()))) {
			appointments = AppointmentMultiset.create(uncheckedAppointments);
		}
		return appointments.size();
	}

	/**
	 * Returns maximumConcurrentAppointmentCount for a list of appointment
	 * types.
	 * 
	 * @param time
	 * @param appointmentTypes
	 * @param staffLimits
	 * @return
	 */
	private Map<AppointmentType, Integer> maxConcurrentAppointmentCountTypeMap(Set<ResourceLimit> resourceLimits) {
		Map<AppointmentType, Integer> maxConcurrent = new HashMap<AppointmentType, Integer>();
		for (AppointmentType appointmentType : this.appointmentTypes) {
			maxConcurrent.put(appointmentType, maximumConcurrentAppointmentCount(resourceLimits, appointmentType));
		}

		return maxConcurrent;
	}

}
