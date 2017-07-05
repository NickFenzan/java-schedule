package com.millervein.schedule.domain.collections;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.ForwardingMultiset;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.millervein.schedule.domain.Appointment;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.ResourceLimit;

public class AppointmentMultiset extends ForwardingMultiset<Appointment> {
	/**
	 * Logs when an appointment is filtered due to demand. Can be cleared at anytime to reset the count.
	 */
	public static Map<String, Integer> filteredDemand = Maps.newHashMap();
	
	private final Multiset<Appointment> delegate;

	public static AppointmentMultiset create() {
		return new AppointmentMultiset();
	}

	public static AppointmentMultiset create(Iterable<? extends Appointment> elements) {
		return new AppointmentMultiset(elements);
	}

	private AppointmentMultiset() {
		this.delegate = HashMultiset.create();
	}

	private AppointmentMultiset(Iterable<? extends Appointment> elements) {
		this.delegate = HashMultiset.create(elements);
	}

	@Override
	protected Multiset<Appointment> delegate() {
		return delegate;
	}

	// End Boilerplate
	// -----------------------------------------------------------
	
	/**
	 * Collects all of the resources used for the appointment set into one resource usage list
	 * @return
	 */
	public ResourceUsageMultiset toResourceUsageList() {
		return delegate.stream().map(a -> a.getResourceUsage()).flatMap(ResourceUsageMultiset::stream)
				.collect(Collectors.toCollection(ResourceUsageMultiset::create));
	}

	/**
	 * Checks the appointment set against a set of resource limits
	 * @param resourceLimits
	 * @return
	 */
	public boolean resourceLimitsValid(Set<ResourceLimit> resourceLimits) {
		ResourceUsageMultiset resourceUsageList = this.toResourceUsageList();
		for(ResourceLimit resourceLimit : resourceLimits){
			Integer maxResourceTypeUsage = resourceUsageList.withResourceType(resourceLimit.getResourceType()).maxConcurrency();
			if(maxResourceTypeUsage > resourceLimit.getLimit())
				return false;
		}
		return true;
	}
	
	public boolean appointmentTypeDemandValid(AppointmentTypeDemandPool demandPool){
		for(Multiset.Entry<AppointmentType> appointmentType : toAppointmentTypeMultiset().entrySet()){
			Integer demand = demandPool.get(appointmentType.getElement().getName());
			if(demand == null || appointmentType.getCount() > demand){
				filteredDemand.merge(appointmentType.getElement().getName(), 1, (a,b)->a+b);
				return false;
			}
		}
		return true;
	}

	/**
	 * Sums the value of each of the Appointments in the set
	 * @return
	 */
	public BigDecimal totalValue() {
		return delegate.stream().map(a->a.getValue()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}
	
	public static Predicate<AppointmentMultiset> invalidResourceLimits(Set<ResourceLimit> resourceLimits){
		return a -> a.resourceLimitsValid(resourceLimits);
	}
	
	public static Predicate<AppointmentMultiset> invalidAppointmentTypeDemand(AppointmentTypeDemandPool demandPool){
		return a -> a.appointmentTypeDemandValid(demandPool);
	}
	
	public Multiset<AppointmentType> toAppointmentTypeMultiset(){
		return delegate.stream().map(a->a.getAppointmentType()).collect(Collectors.toCollection(HashMultiset::create));
	}
	
}
