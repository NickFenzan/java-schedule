package com.millervein.schedule.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Multiset;
import com.millervein.schedule.domain.Appointment;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.collections.AppointmentMultiset;
import com.millervein.schedule.domain.collections.SolutionSet;

import lombok.Data;

@Data(staticConstructor="create")
public class SolutionSetInformationPrinter {
	private final SolutionSet solutionSet;

	public SolutionSetInformationPrinter printSolutionCount() {
		Integer solutionCount = solutionSet.size();
		System.out.println("Solution Count: " + solutionCount);
		return this;
	}

	public SolutionSetInformationPrinter printMaxAppointments() {
		Integer maxAppointments = solutionSet.stream().map(s -> s.size()).reduce(0, (a, b) -> b > a ? b : a);
		System.out.println("Maximum Total Appointments: " + maxAppointments);
		return this;
	}

	public SolutionSetInformationPrinter printMaxValue() {
		BigDecimal maxValue = solutionSet.stream().map(s -> s.totalValue()).reduce(new BigDecimal(0),
				(a, b) -> (b.compareTo(a) > 0) ? b : a);
		System.out.println("Maximum Total Value: " + maxValue.setScale(2,RoundingMode.HALF_UP).toString());
		return this;
	}

	public SolutionSetInformationPrinter printSolutions() {
		Integer i = 1;
		for (AppointmentMultiset appointmentList : solutionSet) {
			System.out.println("Solution " + i + ":" + " - " + appointmentList.totalValue());
			List<Appointment> appointments = appointmentList.stream()
					.sorted((a, b) -> a.getTimePeriod().getStart().compareTo(b.getTimePeriod().getStart()))
					.collect(Collectors.toList());
			for (Appointment appointment : appointments) {
				System.out.println(appointment.getAppointmentType().getName() + "|"
						+ appointment.getTimePeriod().getStart().format(DateTimeFormatter.ISO_LOCAL_TIME));
			}
			printAppointmentListTypes(appointmentList.toAppointmentTypeMultiset());
			i++;
		}
		return this;
	}
	
	private void printAppointmentListTypes(Multiset<AppointmentType> appointmentTypes){
		for(Multiset.Entry<AppointmentType> e : appointmentTypes.entrySet()){
			System.out.print(e.getElement().getName() + ": " + e.getCount() + "|");
		}
		System.out.println("");
	}
}
