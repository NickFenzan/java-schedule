package com.millervein.schedule.seed;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.ResourceType;
import com.millervein.schedule.domain.ResourceUsageTemplate;

public class AppointmentTypeSeeder {

	public static Set<AppointmentType> create() {
		Set<AppointmentType> appointmentTypes = Sets.newHashSet();
		appointmentTypes.add(freeEvaluation());
		appointmentTypes.add(newPatient());
		appointmentTypes.add(followUpOneWeek());
		appointmentTypes.add(followUp3Month());
		appointmentTypes.add(followUp6Month());
		appointmentTypes.add(followUpYearly());
		appointmentTypes.add(veinEraseLegs());
		appointmentTypes.add(procedure());
		return appointmentTypes;
	}

	private static AppointmentType newPatient() {
		List<ResourceUsageTemplate> resourceUsageList = Lists.newArrayList();
		Duration runningTimer = Duration.ZERO;
		Duration medTechTime = Duration.ofMinutes(15);
		Duration ultrasoundTime = Duration.ofMinutes(45);
		Duration nurseTime = Duration.ofMinutes(30);
		Duration drTime = Duration.ofMinutes(30);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "medtech"), medTechTime, runningTimer));
		runningTimer = runningTimer.plus(medTechTime);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "ultrasound"), ultrasoundTime, runningTimer));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("equipment", "ultrasound"), ultrasoundTime, runningTimer));
		runningTimer = runningTimer.plus(ultrasoundTime);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "nurse"), nurseTime, runningTimer));
		runningTimer = runningTimer.plus(nurseTime);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "physician"), drTime, runningTimer));
		runningTimer = runningTimer.plus(drTime);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("room", "consult"), runningTimer, Duration.ZERO));
		
		return AppointmentType.create("New Patient", resourceUsageList, new BigDecimal(271.03));
	}
	
	private static List<ResourceUsageTemplate> longTermFollowUpResourceTemplate(){
		List<ResourceUsageTemplate> resourceUsageList = Lists.newArrayList();
		Duration runningTimer = Duration.ZERO;
		Duration medTechTime = Duration.ofMinutes(15);
		Duration ultrasoundTime = Duration.ofMinutes(45);
		Duration drTime = Duration.ofMinutes(15);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "medtech"), medTechTime, runningTimer));
		runningTimer = runningTimer.plus(medTechTime);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "ultrasound"), ultrasoundTime, runningTimer));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("equipment", "ultrasound"), ultrasoundTime, runningTimer));
		runningTimer = runningTimer.plus(ultrasoundTime);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "physician"), drTime, runningTimer));
		runningTimer = runningTimer.plus(drTime);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("room", "consult"), runningTimer, Duration.ZERO));
		return resourceUsageList;
	}
	
	private static AppointmentType followUp3Month() {
		return AppointmentType.create("3 Month Follow Up", longTermFollowUpResourceTemplate(), new BigDecimal(203.46));
	}
	
	private static AppointmentType followUp6Month() {
		return AppointmentType.create("6 Month Follow Up", longTermFollowUpResourceTemplate(), new BigDecimal(200.99));
	}
	
	private static AppointmentType followUpYearly() {
		return AppointmentType.create("Yearly Follow Up", longTermFollowUpResourceTemplate(), new BigDecimal(212.74));
	}
	
	private static AppointmentType followUpOneWeek() {
		List<ResourceUsageTemplate> resourceUsageList = Lists.newArrayList();
		Duration ultrasoundTime = Duration.ofMinutes(15);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "ultrasound"), ultrasoundTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("equipment", "ultrasound"), ultrasoundTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("room", "consult"), ultrasoundTime, Duration.ZERO));
		
		return AppointmentType.create("1 Week Follow Up", resourceUsageList, new BigDecimal(0.00));
	}

	private static AppointmentType freeEvaluation(){
		List<ResourceUsageTemplate> resourceUsageList = Lists.newArrayList();
		Duration physicianTime = Duration.ofMinutes(15);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "physician"), physicianTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("room", "consult"), physicianTime, Duration.ZERO));
		return AppointmentType.create("Free Evaluation", resourceUsageList, new BigDecimal(0));
	}

	private static AppointmentType veinEraseLegs() {
		List<ResourceUsageTemplate> resourceUsageList = Lists.newArrayList();
		Duration appointmentTime = Duration.ofMinutes(45);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("room", "procedure"), appointmentTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "medtech"), appointmentTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "nurse"), appointmentTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("equipment", "cryo"), appointmentTime, Duration.ZERO));
		
		return AppointmentType.create("VeinErase Legs", resourceUsageList, new BigDecimal(395.00));
	}
	
	private static AppointmentType procedure() {
		List<ResourceUsageTemplate> resourceUsageList = Lists.newArrayList();
		Duration appointmentTime = Duration.ofMinutes(45);
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("room", "procedure"), appointmentTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "medtech"), appointmentTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "nurse"), appointmentTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("staff", "physician"), appointmentTime, Duration.ZERO));
		resourceUsageList.add(ResourceUsageTemplate.create(ResourceType.create("equipment", "laser"), appointmentTime, Duration.ZERO));
		
		return AppointmentType.create("Procedure", resourceUsageList, new BigDecimal(3000.00));
	}
	
}
