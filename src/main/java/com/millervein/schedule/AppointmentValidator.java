package com.millervein.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentValidator {

	private AppointmentValidatorOptions options;

	public AppointmentValidator() {
		this.options = new AppointmentValidatorOptions();
	}

	public AppointmentValidator(AppointmentValidatorOptions options) {
		this.options = options;
	}

	public List<Appointment> filterInvalid(List<Appointment> appointments) {
		return appointments.stream()
		.filter(a->this.validStartTime(a))
		.filter(a->this.validEndTime(a))
		.collect(Collectors.toList());
	}
	
	private boolean validStartTime(Appointment appointment){
		LocalDateTime appointmentStart = appointment.getTimePeriod().getStart();
		return appointmentStart.compareTo(appointmentStart.with(options.startTime)) >= 0;
	}
	
	private boolean validEndTime(Appointment appointment){
		LocalDateTime appointmentEnd = appointment.getTimePeriod().getEnd();
		return appointmentEnd.compareTo(appointmentEnd.with(options.endTime)) <= 0;
	}
}
