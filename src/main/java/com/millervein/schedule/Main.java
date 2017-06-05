package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
//		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
//		rList.add(new ResourceUsageTemplate("Med Tech", rList.getTotalDuration(), Duration.ofMinutes(15)));
//		rList.add(new ResourceUsageTemplate("Ultrasound", rList.getTotalDuration(), Duration.ofMinutes(45)));
//		rList.add(new ResourceUsageTemplate("Nurse", rList.getTotalDuration(), Duration.ofMinutes(30)));
//		rList.add(new ResourceUsageTemplate("Physician", rList.getTotalDuration(), Duration.ofMinutes(30)));
//		
//		AppointmentType newPatientType = new AppointmentType("New Patient", rList);
		
		
		
		List<AppointmentType> appointmentTypes  = new ArrayList<AppointmentType>();
		appointmentTypes.add(nurseHeavy());
		appointmentTypes.add(physicianHeavy());
		
		AppointmentGeneratorOptions agOptions = new AppointmentGeneratorOptions();
		agOptions.startTime = LocalTime.of(8, 0);
		agOptions.endTime = LocalTime.of(10, 15);
		
		
		AppointmentGenerator ag = new AppointmentGenerator(appointmentTypes, agOptions);
		List<AppointmentList> appointments = ag.generateAppointments();
		
		for(Integer i = 0; i < appointments.size(); i++){
			AppointmentList appointmentList = appointments.get(i);
//			System.out.println("Solution: " + i);
//			System.out.println(appointmentList);
		}
		
		
//		AppointmentValidator av = new AppointmentValidator();
//		List<Appointment> allAppointments = ag.generateAppointments(newPatientType);
//		List<Appointment> validAppointments = av.filterInvalid(allAppointments);
//		for(Appointment appt : validAppointments){
//			System.out.println(appt);
//		}
		
	}
	
	public static AppointmentType nurseHeavy(){
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate("Nurse", rList.getTotalDuration(), Duration.ofMinutes(30)));
		rList.add(new ResourceUsageTemplate("Physician", rList.getTotalDuration(), Duration.ofMinutes(15)));
		return new AppointmentType("Nurse Heavy", rList);
	}
	
	public static AppointmentType physicianHeavy(){
		ResourceUsageTemplateList rList = new ResourceUsageTemplateList();
		rList.add(new ResourceUsageTemplate("Nurse", rList.getTotalDuration(), Duration.ofMinutes(15)));
		rList.add(new ResourceUsageTemplate("Physician", rList.getTotalDuration(), Duration.ofMinutes(30)));
		return new AppointmentType("Physician Heavy", rList);
	}
	
}
