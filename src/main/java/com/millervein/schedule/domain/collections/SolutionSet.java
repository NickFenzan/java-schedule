package com.millervein.schedule.domain.collections;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.ResourceLimit;

public class SolutionSet extends ForwardingSet<AppointmentMultiset> {

	private Set<AppointmentMultiset> delegate;

	public static SolutionSet create() {
		return new SolutionSet();
	}

	public static SolutionSet create(Collection<? extends AppointmentMultiset> c) {
		return new SolutionSet(c);
	}

	private SolutionSet() {
		this.delegate = new HashSet<AppointmentMultiset>();
	}

	private SolutionSet(Collection<? extends AppointmentMultiset> c) {
		this.delegate = new HashSet<AppointmentMultiset>(c);
	}

	@Override
	protected Set<AppointmentMultiset> delegate() {
		return delegate;
	}
	
	public static Collector<AppointmentMultiset, ?, SolutionSet> collector(){
		return Collectors.toCollection(SolutionSet::create);
	}

	public SolutionSet combine(SolutionSet toAdd) {
		if (delegate.isEmpty()) {
			return toAdd;
		}
		SolutionSet combined = SolutionSet.create();
		for (AppointmentMultiset sourceSolution : delegate) {
			for (AppointmentMultiset toAddSolution : toAdd) {
				AppointmentMultiset tempSolution = AppointmentMultiset.create(sourceSolution);
				tempSolution.addAll(toAddSolution);
				combined.add(tempSolution);
			}
		}
		return combined;
	}

	public SolutionSet filterInvalidForResourceLimits(Set<ResourceLimit> resourceLimits) {
		return delegate.stream().filter(AppointmentMultiset.invalidResourceLimits(resourceLimits))
				.collect(Collectors.toCollection(SolutionSet::create));
	}

	public BigDecimal maxValue() {
		return delegate.stream().map(a->a.totalValue()).reduce(BigDecimal::max).orElse(BigDecimal.ZERO);
	}
	
	public SolutionSet filterSuboptimalValue() {
		BigDecimal maxValue = maxValue();
		return delegate.stream().filter(a->a.totalValue().equals(maxValue)).collect(collector());
	}
	
	public Integer maxAppointmentCount() {
		return delegate.stream().map(s -> s.size()).reduce(0, (a, b) -> b > a ? b : a);
	}

	public SolutionSet filterSuboptimalAppointmentCount() {
		Integer maxAppointments = maxAppointmentCount();
		return delegate.stream().filter(s -> s.size() == maxAppointments).collect(collector());
	}

	public SolutionSet filterAppointmentTypeDemands(AppointmentTypeDemandPool demandPool) {
		return delegate.stream().filter(AppointmentMultiset.invalidAppointmentTypeDemand(demandPool))
				.collect(Collectors.toCollection(SolutionSet::create));
	}
	
	public BigDecimal value() {
		BigDecimal value = BigDecimal.ZERO;
		for (AppointmentMultiset appointmentList : delegate) {
			value = value.add(appointmentList.totalValue());
		}
		return value;
	}

}
