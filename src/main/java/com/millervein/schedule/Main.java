package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Main {

	public static void main(String[] args) throws Exception {
		SortedSet<LocalDateTime> timeRange = timeRange();
		Set<AppointmentType> appointmentTypes = appointmentTypes();
		Map<String, Integer> staffLimits = staffLimits();

		Set<Multiset<Appointment>> appointmentSets = solutionsForTimesAndAppointmentTypes(timeRange, appointmentTypes,
				staffLimits);

		Integer solutionCount = appointmentSets.size();
		Integer maxAppointments = appointmentSets.stream().map(s -> s.size()).reduce(0, (a, b) -> b > a ? b : a);
		Set<Multiset<Appointment>> longestSolutions = appointmentSets.stream().filter(s -> s.size() == maxAppointments)
				.collect(Collectors.toSet());
		Integer filteredSolutionCount = longestSolutions.size();

		Integer i = 1;
		for (Multiset<Appointment> solution : appointmentSets) {
			System.out.println("Solution " + i + ":" + " - " + solution.size());
			Main.validAppointmentListCheck(solution, staffLimits);
			for (Appointment appointment : solution) {
				System.out.println(appointment.getAppointmentType().getName() + "|"
						+ appointment.getTimePeriod().getStart().format(DateTimeFormatter.ISO_LOCAL_TIME));
			}
			i++;
		}

		 System.out.println("maxAppointments");
		 System.out.println(maxAppointments);
		 System.out.println("solutionCount");
		 System.out.println(solutionCount);
		 System.out.println("filteredSolutionCount");
		 System.out.println(filteredSolutionCount);

	}

	/**
	 * Prints the unique appointment type combinations to the console
	 * 
	 * @param solutions
	 */
	private static void printUniqueAppointmentTypeCombos(List<List<Appointment>> solutions) {
		Set<Multiset<AppointmentType>> atypesols = solutions.stream().map(appts -> {
			List<AppointmentType> appttypes = appts.stream().map(appt -> appt.getAppointmentType())
					.collect(Collectors.toList());
			return HashMultiset.create(appttypes);
		}).collect(Collectors.toSet());
		for (Multiset<AppointmentType> appointmentSet : atypesols) {
			System.out.println(appointmentSet);
		}
	}

	/**
	 * Filters empty solutions from the list
	 * 
	 * @param solutions
	 * @return
	 */
	private static List<List<Appointment>> filterEmptySolutions(List<List<Appointment>> solutions) {
		return solutions.stream().filter(a -> a.size() > 0).collect(Collectors.toList());
	}

	/**
	 * Finds a list of all valid combinations of appointment types and times
	 * 
	 * @param times
	 * @param appointmentTypes
	 * @param staffLimits
	 * @return
	 */
	private static Set<Multiset<Appointment>> solutionsForTimesAndAppointmentTypes(SortedSet<LocalDateTime> times,
			Set<AppointmentType> appointmentTypes, Map<String, Integer> staffLimits) {
		Set<Multiset<Appointment>> solutions = new HashSet<Multiset<Appointment>>();
		for (LocalDateTime time : times) {
			Set<Multiset<Appointment>> timeSolutions = solutionsForTimeAndAppointmentTypes(time, appointmentTypes,
					staffLimits);
			solutions = mergeSolutionLists(solutions, timeSolutions, staffLimits);
			// This is filtering solutions that don't look to be as good as some
			// others. This prevents from us going down an exponential amount of
			// blind alleys.
			// Doing this every pass seems to be fine so long as the times are
			// in order. (Which they are due to the sorted set
			solutions = filterSuboptimalSolutions(solutions);
		}
		return solutions;
	}

	private static Set<Multiset<Appointment>> filterSuboptimalSolutions(Set<Multiset<Appointment>> solutions) {
		Integer maxAppointments = solutions.stream().map(s -> s.size()).reduce(0, (a, b) -> b > a ? b : a);
		return solutions.stream().filter(s -> s.size() == maxAppointments).collect(Collectors.toSet());
	}

	private static Set<Multiset<Appointment>> mergeSolutionLists(Set<Multiset<Appointment>> source,
			Set<Multiset<Appointment>> toAdd, Map<String, Integer> staffLimits) {
		Set<Multiset<Appointment>> merged = new HashSet<Multiset<Appointment>>();
		if (source.isEmpty()) {
			return toAdd;
		}
		for (Multiset<Appointment> sourceSolution : source) {
			// If you want to keep "incomplete" solutions uncomment this
			// merged.add(sourceSolution);
			for (Multiset<Appointment> toAddSolution : toAdd) {
				Multiset<Appointment> tempSolution = HashMultiset.create(sourceSolution);
				tempSolution.addAll(toAddSolution);
				if (validAppointmentListCheck(tempSolution, staffLimits)) {
					merged.add(tempSolution);
				}
			}
		}
		return merged;
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
	private static Set<Multiset<Appointment>> solutionsForTimeAndAppointmentTypes(LocalDateTime time,
			Set<AppointmentType> appointmentTypes, Map<String, Integer> staffLimits) {
		Map<AppointmentType, Integer> maxConcurrent = maximumConcurrentAppointmentCountForAppointmentTypes(time,
				appointmentTypes, staffLimits);
		Set<Multiset<Appointment>> solutions = new HashSet<Multiset<Appointment>>();
		for (AppointmentType appointmentType : appointmentTypes) {
			Integer limit = maxConcurrent.get(appointmentType);
			solutions = addValidAppointmentsToSolutionsByAppointmentTypeAndTime(solutions, appointmentType, limit, time,
					staffLimits);
		}
		return solutions;
	}

	/**
	 * Generates a list appointment lists. It does this by attempting to add as
	 * many appointments of a given type to each of the solutions that have
	 * passed and been added previously. The limit argument is not strictly
	 * necessary. It really serves as an upper-limit starting place for the
	 * function to attempt to add appointments.
	 * 
	 * @param solutions
	 * @param appointmentType
	 * @param limit
	 * @param time
	 * @param staffLimits
	 * @return
	 */
	private static Set<Multiset<Appointment>> addValidAppointmentsToSolutionsByAppointmentTypeAndTime(
			Set<Multiset<Appointment>> solutions, AppointmentType appointmentType, Integer limit, LocalDateTime time,
			Map<String, Integer> staffLimits) {
		Set<Multiset<Appointment>> newSolutions = new HashSet<Multiset<Appointment>>(solutions);
		if (solutions.isEmpty()) {
			solutions.add(HashMultiset.create());
		}
		for (Multiset<Appointment> existingSolution : solutions) {
			appendMultipleValidAppointmentsToSolutionBasedOnLimit(time, appointmentType, limit, staffLimits,
					existingSolution, newSolutions);
		}
		return newSolutions;
	}

	/**
	 * Starting at the limit of max appointment count per type, function
	 * attempts to add as many appointments as it can to a solution
	 * 
	 * @param time
	 * @param appointmentType
	 * @param limit
	 * @param staffLimits
	 * @param existingSolution
	 * @param newSolutions
	 */
	private static void appendMultipleValidAppointmentsToSolutionBasedOnLimit(LocalDateTime time,
			AppointmentType appointmentType, Integer limit, Map<String, Integer> staffLimits,
			Multiset<Appointment> existingSolution, Set<Multiset<Appointment>> newSolutions) {
		for (Integer i = limit; i >= 0; i = i - 1) {
			appendMultipleValidAppointmentsToSolution(time, appointmentType, i, staffLimits, existingSolution,
					newSolutions);
		}
	}

	/**
	 * This function adds a set number of appointments to a solution, adding it
	 * to the set if valid
	 * 
	 * @param time
	 * @param appointmentType
	 * @param appointmentCount
	 * @param staffLimits
	 * @param existingSolution
	 * @param newSolutions
	 */
	private static void appendMultipleValidAppointmentsToSolution(LocalDateTime time, AppointmentType appointmentType,
			Integer appointmentCount, Map<String, Integer> staffLimits, Multiset<Appointment> existingSolution,
			Set<Multiset<Appointment>> newSolutions) {
		Multiset<Appointment> testSolution = HashMultiset.create(existingSolution);
		for (Integer j = appointmentCount; j > 0; j = j - 1) {
			testSolution.add(new Appointment(appointmentType, time));
		}
		if (validAppointmentListCheck(testSolution, time, staffLimits)) {
			newSolutions.add(testSolution);
			// There may be a potential for some kind of optimization
			// here. The function could stop adding lower numbers of
			// appointments after it has already found a high numbered
			// solution. The issue with this is that it breaks the top
			// level iteration. Everything trys to add to the one and
			// only full solution.
		}
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
	private static Map<AppointmentType, Integer> maximumConcurrentAppointmentCountForAppointmentTypes(
			LocalDateTime time, Set<AppointmentType> appointmentTypes, Map<String, Integer> staffLimits) {
		Map<AppointmentType, Integer> maxConcurrent = new HashMap<AppointmentType, Integer>();
		for (AppointmentType appointmentType : appointmentTypes) {
			maxConcurrent.put(appointmentType, maximumConcurrentAppointmentCount(time, appointmentType, staffLimits));
		}
		return maxConcurrent;
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
	private static Integer maximumConcurrentAppointmentCount(LocalDateTime time, AppointmentType appointmentType,
			Map<String, Integer> staffLimits) {
		Multiset<Appointment> appointments = HashMultiset.create();
		for (Multiset<Appointment> uncheckedAppointments = HashMultiset.create(appointments); validAppointmentListCheck(
				uncheckedAppointments, time,
				staffLimits); uncheckedAppointments.add(new Appointment(appointmentType, time))) {
			appointments = HashMultiset.create(uncheckedAppointments);
		}
		return appointments.size();
	}

	private static boolean validAppointmentListCheck(Multiset<Appointment> appointments,
			Map<String, Integer> staffLimits) {
		for (Map.Entry<String, Integer> staffLimit : staffLimits.entrySet()) {
			if (!validAppointmentListCheck(appointments, staffLimit.getKey(), staffLimit.getValue()))
				return false;
		}
		return true;
	}

	/**
	 * Checks to make sure an appointment set is valid. At this point, this
	 * means is under the staff limit.
	 * 
	 * @param appointments
	 * @param time
	 * @param staffLimits
	 * @return
	 */
	private static boolean validAppointmentListCheck(Multiset<Appointment> appointments, LocalDateTime time,
			Map<String, Integer> staffLimits) {
		for (Map.Entry<String, Integer> staffLimit : staffLimits.entrySet()) {
			if (!validAppointmentListCheck(appointments, time, staffLimit.getKey(), staffLimit.getValue()))
				return false;
		}
		return true;
	}

	/**
	 * Checks to make sure an appointment set is valid. At this point, this
	 * means is under the staff limit.
	 * 
	 * @param appointments
	 * @param time
	 * @param staffType
	 * @param staffLimit
	 * @return
	 */
	private static boolean validAppointmentListCheck(Multiset<Appointment> appointments, LocalDateTime time,
			String staffType, Integer staffLimit) {
		return staffTypeUsage(appointments, time, staffType) <= staffLimit;
	}

	private static boolean validAppointmentListCheck(Multiset<Appointment> appointments, String staffType,
			Integer staffLimit) {
		return staffTypeUsage(appointments, staffType) <= staffLimit;
	}

	/**
	 * Counts the amount of staff members used at a given time
	 * 
	 * @param appointments
	 * @param time
	 * @param staffType
	 * @return
	 */
	private static Integer staffTypeUsage(Multiset<Appointment> appointments, LocalDateTime time, String staffType) {
		Integer usageCount = 0;
		for (Appointment appointment : appointments) {
			ResourceUsageList staffUsages = appointment.getStaffUsage().getStaffTypeUsageList(staffType);
			usageCount += resourceUsageCountAtTime(staffUsages, time);
		}
		return usageCount;
	}

	private static Integer staffTypeUsage(Multiset<Appointment> appointments, String staffType) {
		Integer usageCount = 0;
		try {
			LocalDateTime earliestTime = appointments.stream().map(a -> a.getStaffUsage().earliestUsage())
					.min((time1, time2) -> time1.compareTo(time2))
					.orElseThrow(() -> new IllegalStateException("List didn't have an earliest time"));
			LocalDateTime latestTime = appointments.stream().map(a -> a.getStaffUsage().latestUsage())
					.max((time1, time2) -> time1.compareTo(time2))
					.orElseThrow(() -> new IllegalStateException("List didn't have an latest time"));
			// This is a potential place for optimization. It could use a larger
			// interval than 1 minute in some cases, but figuring out what that
			// is and when can be tricky
			// Duration interval = appointments.stream().map(a ->
			// a.getStaffUsage().maximumComprehensiveInterval())
			// .min((dur1, dur2) -> dur1.compareTo(dur2)).orElseThrow(() -> new
			// IllegalStateException());
			Duration interval = Duration.ofMinutes(1);
			Set<LocalDateTime> appointmentResourceUseageTimeRange = generateTimeRange(earliestTime, latestTime,
					interval);
			for (LocalDateTime time : appointmentResourceUseageTimeRange) {
				Integer timeUsage = staffTypeUsage(appointments, time, staffType);
				usageCount = (timeUsage > usageCount) ? timeUsage : usageCount;
			}
		} catch (Exception e) {
			return usageCount;
		}
		return usageCount;
	}

	/**
	 * Counts the amount of resources in the list that overlap a given time
	 * 
	 * @param staffUsages
	 * @param time
	 * @return
	 */
	private static Integer resourceUsageCountAtTime(ResourceUsageList staffUsages, LocalDateTime time) {
		Integer usageCount = 0;
		for (ResourceUsage staffUsage : staffUsages) {
			TimePeriod usagePeriod = staffUsage.getTimePeriod();
			if (usagePeriod.includes(time)) {
				usageCount++;
			}
		}
		return usageCount;
	}

	/**
	 * Creates preset staffing limits
	 * 
	 * @return
	 */
	private static Map<String, Integer> staffLimits() {
		Map<String, Integer> limits = new HashMap<String, Integer>();
		limits.put("Nurse", 1);
		limits.put("Physician", 1);
		return limits;
	}

	private static SortedSet<LocalDateTime> generateTimeRange(LocalDateTime start, LocalDateTime end,
			Duration interval) {
		SortedSet<LocalDateTime> timeRange = new TreeSet<LocalDateTime>();
		for (LocalDateTime iterTime = start; iterTime.isBefore(end); iterTime = iterTime.plus(interval)) {
			timeRange.add(iterTime);
		}
		return timeRange;
	}

	/**
	 * Creates preset time range
	 * 
	 * @return
	 */
	private static SortedSet<LocalDateTime> timeRange() {
		LocalDateTime start = LocalDateTime.now().with(LocalTime.of(8, 0));
		LocalDateTime end = LocalDateTime.now().with(LocalTime.of(17, 0));
		Duration interval = Duration.ofMinutes(15);
		return generateTimeRange(start, end, interval);
	}

	/**
	 * Creates nurse heavy appointment type
	 * 
	 * @return
	 */
	public static AppointmentType nurseHeavy() {
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate("Nurse", rList.getTotalDuration(), Duration.ofMinutes(30)));
		rList.add(new ResourceUsageTemplate("Physician", rList.getTotalDuration(), Duration.ofMinutes(15)));
		return new AppointmentType("Nurse Heavy", rList);
	}

	/**
	 * Creates physician heavy appointment type
	 * 
	 * @return
	 */
	public static AppointmentType physicianHeavy() {
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate("Nurse", rList.getTotalDuration(), Duration.ofMinutes(15)));
		rList.add(new ResourceUsageTemplate("Physician", rList.getTotalDuration(), Duration.ofMinutes(30)));
		return new AppointmentType("Physician Heavy", rList);
	}

	/**
	 * Creates preset list of appointment types
	 * 
	 * @return
	 */
	public static Set<AppointmentType> appointmentTypes() {
		Set<AppointmentType> appointmentTypes = new HashSet<AppointmentType>();
		appointmentTypes.add(nurseHeavy());
		appointmentTypes.add(physicianHeavy());
		return appointmentTypes;
	}

}
