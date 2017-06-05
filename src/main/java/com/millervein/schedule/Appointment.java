package com.millervein.schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {
	private UUID id;
	private AppointmentType appointmentType;
	private TimePeriod timePeriod;
	private Patient patient;
	private ResourceUsageList staffUsage;

	public Appointment(AppointmentType appointmentType, LocalDateTime start, Patient patient) {
		this.id = UUID.randomUUID();
		this.appointmentType = appointmentType;
		this.timePeriod = appointmentType.timePeriodAt(start);
		this.patient = patient;
		this.staffUsage = this.appointmentType.getStaffUsageTemplate().toResourceUsageList(start);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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


	public Patient getPatient() {
		return patient;
	}


	public ResourceUsageList getStaffUsage() {
		return staffUsage;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Appointment [appointmentType=" + appointmentType + ", timePeriod=" + timePeriod + "]";
	}


}
