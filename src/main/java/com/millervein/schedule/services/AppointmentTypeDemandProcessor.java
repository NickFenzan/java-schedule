package com.millervein.schedule.services;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Multiset;
import com.google.inject.Inject;
import com.millervein.schedule.domain.Appointment;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.AppointmentTypeConversion;
import com.millervein.schedule.domain.collections.AppointmentMultiset;
import com.millervein.schedule.domain.collections.AppointmentTypeDemandPool;

public class AppointmentTypeDemandProcessor {
	private static final Double freeConversionRate = 0.0265;
	private static final Double newConversionRate = 0.0758;
	
	private Set<AppointmentTypeConversion> appointmentTypeConversionSet;

	@Inject
	public AppointmentTypeDemandProcessor(Set<AppointmentTypeConversion> appointmentTypeConversionSet) {
		this.appointmentTypeConversionSet = appointmentTypeConversionSet;
	}

	public AppointmentTypeDemandPool processAppointmentSet(AppointmentMultiset appointmentSet, AppointmentTypeDemandPool previousDemand) {
		AppointmentTypeDemandPool demandMinusAppointments = reduceDemandByAppointments(appointmentSet, previousDemand);
		AppointmentTypeDemandPool conversionDemand = conversionDemand(appointmentSet);
		//TODO Leads are hardcoded here
		AppointmentTypeDemandPool leadsDemand = leadsToDemand(200);
		return demandMinusAppointments.merge(conversionDemand).merge(leadsDemand);
	}
	
	private AppointmentTypeDemandPool reduceDemandByAppointments(AppointmentMultiset appointmentSet, AppointmentTypeDemandPool previousDemand){
		AppointmentTypeDemandPool newDemand = AppointmentTypeDemandPool.create(previousDemand);
		for(Multiset.Entry<AppointmentType> apptTypeOccurence : appointmentSet.toAppointmentTypeMultiset().entrySet()){
			String appointmentTypeName = apptTypeOccurence.getElement().getName();
			newDemand.reduceCount(appointmentTypeName, apptTypeOccurence.getCount());
		}
		return newDemand;
	}
	
	public AppointmentTypeDemandPool leadsToDemand(Integer leads){
		Integer freeCount = Long.valueOf(Math.round(leads * freeConversionRate)).intValue();
		Integer newCount = Long.valueOf(Math.round(leads * newConversionRate)).intValue();
		
		AppointmentTypeDemandPool demandPool = AppointmentTypeDemandPool.create();
		demandPool.put("Free Evaluation", freeCount);
		demandPool.put("New Patient", newCount);
		return demandPool;
	}

	private AppointmentTypeDemandPool conversionDemand(AppointmentMultiset appointmentSet) {
		AppointmentTypeDemandPool conversionDemand = AppointmentTypeDemandPool.create();
		for (Appointment appointment : appointmentSet) {
			for (AppointmentTypeConversion conversionRate : findConversionRates(appointment.getAppointmentType())) {
				conversionDemand.probabilityIncrement(conversionRate.getTo(), conversionRate.getConversionRate());
			}
		}
		return conversionDemand;
	}

	private Set<AppointmentTypeConversion> findConversionRates(AppointmentType appointmentType) {
		return appointmentTypeConversionSet.stream().filter(t -> t.getFrom().equals(appointmentType.getName()))
				.collect(Collectors.toSet());
	}

}
