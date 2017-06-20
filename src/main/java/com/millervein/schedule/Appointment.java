package com.millervein.schedule;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Appointment {
	//This is only in here to make hash and equals easier than it would be for timePeriod
	private LocalDateTime start;
	private AppointmentType appointmentType;
	private TimePeriod timePeriod;
	private ResourceUsageList resourceUsage;

	public Appointment(AppointmentType appointmentType, LocalDateTime start) {
		this.appointmentType = appointmentType;
		this.start = start;
		this.timePeriod = appointmentType.timePeriodAt(start);
		this.resourceUsage = this.appointmentType.getResourceUsageTemplate().toResourceUsageList(start);
	}

	public AppointmentType getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(AppointmentType appointmentType) {
		this.appointmentType = appointmentType;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public ResourceUsageList getResourceUsage() {
		return resourceUsage;
	}
	
	public BigDecimal getValue(){
		return this.getAppointmentType().getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appointmentType == null) ? 0 : appointmentType.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Appointment other = (Appointment) obj;
		if (appointmentType == null) {
			if (other.appointmentType != null)
				return false;
		} else if (!appointmentType.equals(other.appointmentType))
			return false;
		if (timePeriod == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return appointmentType.getName() + " - " + timePeriod.getStart().toString();
//		return "Appointment [appointmentType=" + appointmentType + ", timePeriod=" + timePeriod + "]";
	}

}
