package com.millervein.schedule;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ForwardingSet;

public class SolutionSet extends ForwardingSet<AppointmentList> {

	private Set<AppointmentList> delegate;

	public static SolutionSet create() {
		return new SolutionSet();
	}

	public static SolutionSet create(Collection<? extends AppointmentList> c) {
		return new SolutionSet(c);
	}

	private SolutionSet() {
		this.delegate = new HashSet<AppointmentList>();
	}

	private SolutionSet(Collection<? extends AppointmentList> c) {
		this.delegate = new HashSet<AppointmentList>(c);
	}

	@Override
	protected Set<AppointmentList> delegate() {
		return delegate;
	}

	public void combine(SolutionSet toAdd) {
		SolutionSet combined = SolutionSet.create();
		if (delegate.isEmpty()) {
			this.delegate = toAdd.delegate;
		}
		for (AppointmentList sourceSolution : delegate) {
			// If you want to keep "incomplete" solutions uncomment this
			// merged.add(sourceSolution);
			for (AppointmentList toAddSolution : toAdd) {
				AppointmentList tempSolution = AppointmentList.create(sourceSolution);
				tempSolution.addAll(toAddSolution);
				combined.add(tempSolution);
			}
		}
		this.delegate = combined;
	}

	public void filterInvalid(ResourceTypeLimits resourceLimits) {
		this.delegate = this.delegate.stream().filter(apptList -> apptList.valid(resourceLimits))
				.collect(Collectors.toSet());
	}

	public BigDecimal value() {
		BigDecimal value = BigDecimal.ZERO;
		for (AppointmentList appointmentList : delegate) {
			value = value.add(appointmentList.value());
		}
		return value;
	}

	public void filterSuboptimalValue() {
		BigDecimal maxValue = BigDecimal.ZERO;
		for (AppointmentList appointmentList : delegate) {
			BigDecimal appointmentListValue = appointmentList.value();
			maxValue = (appointmentListValue.compareTo(maxValue) > 0) ? appointmentListValue : maxValue;
		}
		Set<AppointmentList> newSet = new HashSet<AppointmentList>();
		for (AppointmentList appointmentList : delegate) {
			if(appointmentList.value().equals(maxValue)){
				newSet.add(appointmentList);
			}
		}
		this.delegate = newSet;
	}


	public void filterSuboptimalAppointmentCount() {
		Integer maxAppointments = delegate.stream().map(s -> s.size()).reduce(0, (a, b) -> b > a ? b : a);
		this.delegate = delegate.stream().filter(s -> s.size() == maxAppointments).collect(Collectors.toSet());
	}
	
	public void filterAppointmentTypeDemands(AppointmentTypeDemands demands){
		
	}

}
