package com.millervein.schedule.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.millervein.schedule.domain.collections.ResourceUsageMultiset;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(of = { "start", "appointmentType" })
public class Appointment {
	@NonNull
	LocalDateTime start;
	@NonNull
	AppointmentType appointmentType;
	@NonNull
	TimePeriod timePeriod;
	@NonNull
	ResourceUsageMultiset resourceUsage;

	private Appointment(AppointmentType appointmentType, LocalDateTime start) {
		this.appointmentType = appointmentType;
		this.start = start;
		this.resourceUsage = appointmentType.toResourceUsageList(start);
		this.timePeriod = this.resourceUsage.toTimePeriod().get();
	}

	public static Appointment create(AppointmentType appointmentType, LocalDateTime start) {
		return new Appointment(appointmentType, start);
	}

	public BigDecimal getValue() {
		return this.getAppointmentType().getValue();
	}

}
