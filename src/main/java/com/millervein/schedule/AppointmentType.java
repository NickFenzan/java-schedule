package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;

public class AppointmentType {
	private String name;
	private ResourceUsageTemplateList staffUsageTemplate;

	public AppointmentType(String name, ResourceUsageTemplateList staffUsageTemplate) {
		super();
		this.name = name;
		this.staffUsageTemplate = staffUsageTemplate;
	}

	public String getName() {
		return name;
	}

	public ResourceUsageTemplateList getStaffUsageTemplate() {
		return staffUsageTemplate;
	}

	public TimePeriod timePeriodAt(LocalDateTime datetime) {
		return new TimePeriod(datetime, this.staffUsageTemplate.getTotalDuration());
	}

	public Duration getSmallestResourceDuration() {
		return this.staffUsageTemplate.getSmallestDuration();
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
