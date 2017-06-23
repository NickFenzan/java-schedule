package com.millervein.schedule;

import java.math.BigDecimal;
import java.time.Duration;

public class AppointmentTypeSetBuilder {

	public static AppointmentTypeSet build() {
		AppointmentTypeSet appointmentTypes = AppointmentTypeSet.create();
		appointmentTypes.add(freeEvaluation());
		appointmentTypes.add(newPatient());
		appointmentTypes.add(followUpOneWeek());
		appointmentTypes.add(followUp3Month());
		appointmentTypes.add(followUp6Month());
		appointmentTypes.add(followUpYearly());
		appointmentTypes.add(veinEraseLegs());
		return appointmentTypes;
	}

	private static AppointmentType newPatient() {
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration runningTimer = Duration.ZERO;
		Duration medTechTime = Duration.ofMinutes(15);
		Duration ultrasoundTime = Duration.ofMinutes(45);
		Duration nurseTime = Duration.ofMinutes(30);
		Duration drTime = Duration.ofMinutes(30);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.MEDTECH, medTechTime, runningTimer));
		runningTimer = runningTimer.plus(medTechTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.ULTRASOUND, ultrasoundTime, runningTimer));
		resourceUsageList.add(new ResourceUsageTemplate(EquipmentType.ULTRASOUND, ultrasoundTime, runningTimer));
		runningTimer = runningTimer.plus(ultrasoundTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.NURSE, nurseTime, runningTimer));
		runningTimer = runningTimer.plus(nurseTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, drTime, runningTimer));
		runningTimer = runningTimer.plus(drTime);
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, runningTimer, Duration.ZERO));
		
		return new AppointmentType("New Patient", resourceUsageList, new BigDecimal(271.03));
	}
	
	private static ResourceUsageTemplateList longTermFollowUpResourceTemplate(){
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration runningTimer = Duration.ZERO;
		Duration medTechTime = Duration.ofMinutes(15);
		Duration ultrasoundTime = Duration.ofMinutes(45);
		Duration drTime = Duration.ofMinutes(15);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.MEDTECH, medTechTime, runningTimer));
		runningTimer = runningTimer.plus(medTechTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.ULTRASOUND, ultrasoundTime, runningTimer));
		resourceUsageList.add(new ResourceUsageTemplate(EquipmentType.ULTRASOUND, ultrasoundTime, runningTimer));
		runningTimer = runningTimer.plus(ultrasoundTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, drTime, runningTimer));
		runningTimer = runningTimer.plus(drTime);
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, runningTimer, Duration.ZERO));
		return resourceUsageList;
	}
	
	private static AppointmentType followUp3Month() {
		return new AppointmentType("3 Month Follow Up", longTermFollowUpResourceTemplate(), new BigDecimal(203.46));
	}
	
	private static AppointmentType followUp6Month() {
		return new AppointmentType("6 Month Follow Up", longTermFollowUpResourceTemplate(), new BigDecimal(200.99));
	}
	
	private static AppointmentType followUpYearly() {
		return new AppointmentType("Yearly Follow Up", longTermFollowUpResourceTemplate(), new BigDecimal(212.74));
	}
	
	private static AppointmentType followUpOneWeek() {
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration ultrasoundTime = Duration.ofMinutes(15);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.ULTRASOUND, ultrasoundTime, Duration.ZERO));
		resourceUsageList.add(new ResourceUsageTemplate(EquipmentType.ULTRASOUND, ultrasoundTime, Duration.ZERO));
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, ultrasoundTime, Duration.ZERO));
		
		return new AppointmentType("1 Week Follow Up", resourceUsageList, new BigDecimal(0.00));
	}

	private static AppointmentType freeEvaluation(){
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration physicianTime = Duration.ofMinutes(15);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, physicianTime, Duration.ZERO));
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, physicianTime, Duration.ZERO));
		return new AppointmentType("Free Evaluation", resourceUsageList, new BigDecimal(0));
	}

	private static AppointmentType veinEraseLegs() {
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration appointmentTime = Duration.ofMinutes(45);
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.PROCEDURE, appointmentTime, Duration.ZERO));
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.MEDTECH, appointmentTime, Duration.ZERO));
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.NURSE, appointmentTime, Duration.ZERO));
		resourceUsageList.add(new ResourceUsageTemplate(EquipmentType.CRYO, appointmentTime, Duration.ZERO));
		
		return new AppointmentType("VeinErase Legs", resourceUsageList, new BigDecimal(395.00));
	}
	
}
