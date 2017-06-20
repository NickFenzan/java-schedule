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
		return appointmentTypes;
	}

	private static AppointmentType newPatient() {
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration runningTimer = Duration.ZERO;
		Duration medTechTime = Duration.ofMinutes(15);
		Duration ultrasoundTime = Duration.ofMinutes(45);
		Duration nurseTime = Duration.ofMinutes(30);
		Duration drTime = Duration.ofMinutes(30);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.MEDTECH, runningTimer, medTechTime));
		runningTimer = runningTimer.plus(medTechTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.ULTRASOUND, runningTimer, ultrasoundTime));
		resourceUsageList.add(new ResourceUsageTemplate(EquipmentType.ULTRASOUND, runningTimer, ultrasoundTime));
		runningTimer = runningTimer.plus(ultrasoundTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.NURSE, runningTimer, nurseTime));
		runningTimer = runningTimer.plus(nurseTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, runningTimer, drTime));
		runningTimer = runningTimer.plus(drTime);
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, Duration.ZERO, runningTimer));
		
		return new AppointmentType("New Patient", resourceUsageList, new BigDecimal(271.03));
	}
	
	private static ResourceUsageTemplateList longTermFollowUpResourceTemplate(){
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration runningTimer = Duration.ZERO;
		Duration medTechTime = Duration.ofMinutes(15);
		Duration ultrasoundTime = Duration.ofMinutes(45);
		Duration drTime = Duration.ofMinutes(15);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.MEDTECH, runningTimer, medTechTime));
		runningTimer = runningTimer.plus(medTechTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.ULTRASOUND, runningTimer, ultrasoundTime));
		resourceUsageList.add(new ResourceUsageTemplate(EquipmentType.ULTRASOUND, runningTimer, ultrasoundTime));
		runningTimer = runningTimer.plus(ultrasoundTime);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, runningTimer, drTime));
		runningTimer = runningTimer.plus(drTime);
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, Duration.ZERO, runningTimer));
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
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.ULTRASOUND, Duration.ZERO, ultrasoundTime));
		resourceUsageList.add(new ResourceUsageTemplate(EquipmentType.ULTRASOUND, Duration.ZERO, ultrasoundTime));
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, Duration.ZERO, ultrasoundTime));
		
		return new AppointmentType("1 Week Follow Up", resourceUsageList, new BigDecimal(0.00));
	}

	private static AppointmentType freeEvaluation(){
		ResourceUsageTemplateList resourceUsageList = new ResourceUsageTemplateList();
		Duration physicianTime = Duration.ofMinutes(15);
		resourceUsageList.add(new ResourceUsageTemplate(StaffType.PHYSICIAN, Duration.ZERO, physicianTime));
		resourceUsageList.add(new ResourceUsageTemplate(RoomType.CONSULT, Duration.ZERO, physicianTime));
		return new AppointmentType("Free Evaluation", resourceUsageList, new BigDecimal(0));
	}

	
}
