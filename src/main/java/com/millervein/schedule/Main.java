package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Main {

	public static void main(String[] args) throws Exception {
		List<LocalDateTime> timeRange = timeRange();
		List<AppointmentType> appointmentTypes = appointmentTypes();
		Map<String, Integer> staffLimits = staffLimits();

//		Map<LocalDateTime, List<Multiset<Appointment>>> solutions = solutionsForTimesAndAppointmentTypes(timeRange,
//				appointmentTypes, staffLimits);
		
		List<Multiset<Appointment>> appointmentSets = solutionsForTimeAndAppointmentTypes(timeRange.get(0),
				appointmentTypes, staffLimits);
		for(Multiset<Appointment> appointmentSet : appointmentSets){
			for(Appointment appointment : appointmentSet){
				System.out.print(appointment.getAppointmentType().getName() + " | ");
			}
			System.out.println("");
		}

	}

	private static Map<LocalDateTime, List<Multiset<Appointment>>> solutionsForTimesAndAppointmentTypes(
			List<LocalDateTime> times, List<AppointmentType> appointmentTypes, Map<String, Integer> staffLimits) {
		Map<LocalDateTime, List<Multiset<Appointment>>> solutions = new HashMap<LocalDateTime, List<Multiset<Appointment>>>();
		
		return solutions;
	}

	private static List<Multiset<Appointment>> solutionsForTimeAndAppointmentTypes(LocalDateTime time,
			List<AppointmentType> appointmentTypes, Map<String, Integer> staffLimits) {
		List<Multiset<Appointment>> solutions = new ArrayList<Multiset<Appointment>>();
		for(AppointmentType appointmentType : appointmentTypes){
			Multiset<Appointment> solution = solutionsForTimeAndAppointmentType(time, appointmentType, staffLimits);
			solutions.add(solution);
		}
		return solutions;
	}
	
	private static List<Multiset<Appointment>> factorAppointmentTimeSets(List<Multiset<Appointment>> appointmentTypeSets){
		List<Multiset<Appointment>> solutions = new ArrayList<Multiset<Appointment>>();
		
		return solutions;
	}

	private static Multiset<Appointment> solutionsForTimeAndAppointmentType(LocalDateTime time,
			AppointmentType appointmentType, Map<String, Integer> staffLimits) {
		Multiset<Appointment> appointments = HashMultiset.create();
		for(Multiset<Appointment> uncheckedAppointments = HashMultiset.create(appointments);validAppointmentSetCheck(uncheckedAppointments, time, staffLimits); uncheckedAppointments.add(new Appointment(appointmentType, time))){
			appointments = HashMultiset.create(uncheckedAppointments);
		}
		return appointments;
	}

	private static boolean validAppointmentSetCheck(Multiset<Appointment> appointments, LocalDateTime time, Map<String, Integer> staffLimits){
		for(Map.Entry<String, Integer> staffLimit : staffLimits.entrySet()){
			if(!validAppointmentSetCheck(appointments, time, staffLimit.getKey(), staffLimit.getValue()))
				return false;
		}
		return true;
	}
	
	private static boolean validAppointmentSetCheck(Multiset<Appointment> appointments, LocalDateTime time, String staffType, Integer staffLimit){
//		System.out.println(appointments);
//		System.out.println(time);
//		System.out.println(staffType);
//		System.out.println(staffTypeUsage(appointments, time, staffType));
//		System.out.println(staffLimit);
//		System.out.println(staffTypeUsage(appointments, time, staffType) <= staffLimit);
//		System.out.println("");
		return staffTypeUsage(appointments, time, staffType) <= staffLimit;
	}
	
	private static Integer staffTypeUsage(Multiset<Appointment> appointments, LocalDateTime time, String staffType){
		Integer usageCount = 0;
		for(Appointment appointment :appointments){
			ResourceUsageList staffUsages = appointment.getStaffUsage().getStaffTypeUsageList(staffType);
			usageCount += staffUsageAtTime(staffUsages, time);
		}
		return usageCount;
	}
	
	private static Integer staffUsageAtTime(ResourceUsageList staffUsages, LocalDateTime time){
		Integer usageCount = 0;
		for(ResourceUsage staffUsage : staffUsages){
			TimePeriod usagePeriod = staffUsage.getTimePeriod();
			if(usagePeriod.includes(time)){
				usageCount++;
			}
		}
		return usageCount;
	}
	
	private static Map<String, Integer> staffLimits(){
		Map<String, Integer> limits = new HashMap<String, Integer>();
		limits.put("Nurse", 3);
		limits.put("Physician", 3);
		return limits;
	}

	private static List<LocalDateTime> timeRange() {
		LocalDateTime start = LocalDateTime.now().with(LocalTime.of(8, 0));
		LocalDateTime end = LocalDateTime.now().with(LocalTime.of(12, 0));
		Duration interval = Duration.ofMinutes(15);

		List<LocalDateTime> timeRange = new ArrayList<LocalDateTime>();
		for (LocalDateTime iterTime = start; iterTime.isBefore(end); iterTime = iterTime.plus(interval)) {
			timeRange.add(iterTime);
		}
		return timeRange;
	}

	public static AppointmentType nurseHeavy() {
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate("Nurse", rList.getTotalDuration(), Duration.ofMinutes(30)));
		rList.add(new ResourceUsageTemplate("Physician", rList.getTotalDuration(), Duration.ofMinutes(15)));
		return new AppointmentType("Nurse Heavy", rList);
	}

	public static AppointmentType physicianHeavy() {
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate("Nurse", rList.getTotalDuration(), Duration.ofMinutes(15)));
		rList.add(new ResourceUsageTemplate("Physician", rList.getTotalDuration(), Duration.ofMinutes(30)));
		return new AppointmentType("Physician Heavy", rList);
	}

	public static List<AppointmentType> appointmentTypes() {
		ArrayList<AppointmentType> appointmentTypes = new ArrayList<AppointmentType>();
		appointmentTypes.add(nurseHeavy());
		appointmentTypes.add(physicianHeavy());
		return appointmentTypes;
	}

}
