package com.millervein.schedule;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class AppointmentType {
	private String name;
	private ResourceUsageTemplateList resourceUsageTemplate;
	private BigDecimal value;

	public AppointmentType(String name, ResourceUsageTemplateList resourceUsageTemplate, BigDecimal value) {
		this.name = name;
		this.resourceUsageTemplate = resourceUsageTemplate;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public ResourceUsageTemplateList getResourceUsageTemplate() {
		return resourceUsageTemplate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public TimePeriod timePeriodAt(LocalDateTime datetime) {
		return TimePeriod.withDuration(datetime, this.resourceUsageTemplate.getTotalDuration());
	}

	public Duration getSmallestResourceDuration() {
		return this.resourceUsageTemplate.getSmallestDuration();
	}

	/**
	 * Returns the maximum number of appointments possible at a given time,
	 * given its staff limits
	 * 
	 * @param time
	 * @param appointmentType
	 * @param staffLimits
	 * @return
	 */
	public Integer maximumConcurrentAppointmentCount(ResourceTypeLimits resourceLimits) {
		AppointmentList appointments = AppointmentList.create();
		for (AppointmentList uncheckedAppointments = AppointmentList.create(appointments); uncheckedAppointments
				.valid(resourceLimits); uncheckedAppointments.add(new Appointment(this, LocalDateTime.now()))) {
			appointments = AppointmentList.create(uncheckedAppointments);
		}
		return appointments.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AppointmentType other = (AppointmentType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppointmentType [name=" + name + "]";
	}

}
