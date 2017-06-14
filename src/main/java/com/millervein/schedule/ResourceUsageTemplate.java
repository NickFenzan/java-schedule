package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;

public class ResourceUsageTemplate {
	private ResourceType resourceType;
	private Duration duration;
	private Duration appointmentStartOffset;

	public ResourceUsageTemplate(ResourceType resourceType, Duration appointmentStartOffset, Duration duration) {
		super();
		this.resourceType = resourceType;
		this.appointmentStartOffset = appointmentStartOffset;
		this.duration = duration;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public Duration getDuration() {
		return duration;
	}

	public Duration getAppointmentStartOffset() {
		return appointmentStartOffset;
	}

	public TimePeriod timePeriodAt(LocalDateTime datetime) {
		return new TimePeriod(datetime.plus(this.appointmentStartOffset), this.duration);
	}
	
	public ResourceUsage toResourceUsage(LocalDateTime appointmentStart){
		return new ResourceUsage(appointmentStart, this);
	}

	@Override
	public String toString() {
		return "ResourceUsageTemplate [resourceType=" + resourceType + ", duration=" + duration
				+ ", appointmentStartOffset=" + appointmentStartOffset + "]";
	}

}
