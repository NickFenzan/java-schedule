package com.millervein.schedule.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.SortedSet;

import com.google.inject.Inject;
import com.millervein.schedule.domain.collections.AppointmentMultiset;
import com.millervein.schedule.domain.collections.AppointmentTypeDemandPool;
import com.millervein.schedule.domain.collections.SolutionSet;

public class ScheduleGenerator {
	private SolutionSetGenerator ssg;
	private AppointmentTypeDemandProcessor appointmentTypeDemandProcessor;

	@Inject
	private ScheduleGenerator(SolutionSetGenerator ssg, AppointmentTypeDemandProcessor appointmentTypeDemandProcessor) {
		this.ssg = ssg;
		this.appointmentTypeDemandProcessor = appointmentTypeDemandProcessor;
	}
	
	private static SortedSet<LocalDateTime> timeRange() {
		LocalDateTime start = LocalDateTime.now().with(LocalTime.of(8, 0));
		LocalDateTime end = LocalDateTime.now().with(LocalTime.of(17, 0));
		Duration interval = Duration.ofMinutes(15);
		return TimeRangeGenerator.generateTimeRange(start, end, interval);
	}
	
	public void generateDays(Integer days){
		AppointmentTypeDemandPool demandPool = appointmentTypeDemandProcessor.leadsToDemand(200);
		demandPool = testATDP();
		for(Integer day = 1; day <= days; day++) {
			System.out.println("Day " + day);
			SolutionSet solutionSet = ssg.generateAppointmentsForTimes(timeRange(), demandPool);
			SolutionSetInformationPrinter.create(solutionSet).printSolutions().printMaxAppointments().printMaxValue();
			AppointmentMultiset solution = solutionSet.stream().findFirst().get();
			demandPool = appointmentTypeDemandProcessor.processAppointmentSet(solution, demandPool);
			System.out.println("Demand:" + demandPool.toString());
			System.out.println(AppointmentMultiset.filteredDemand);
			AppointmentMultiset.filteredDemand.clear();
			System.out.println("----------------------------------------------------------------");
		}
	}
	
	private AppointmentTypeDemandPool testATDP(){
		AppointmentTypeDemandPool atdp = AppointmentTypeDemandPool.create();
		atdp.put("1 Week Follow Up", 4);
		atdp.put("6 Month Follow Up", 5);
		atdp.put("Procedure", 25);
		atdp.put("VeinErase Legs", 3);
		atdp.put("3 Month Follow Up", 0);
		atdp.put("New Patient", 62);
		atdp.put("Yearly Follow Up", 1);
		atdp.put("Free Evaluation", 21);
		return atdp;
	}
	
}
