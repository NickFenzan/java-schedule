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
	public String toString() {
		return "AppointmentType [name=" + name + "]";
	}


}
