package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

public class ResourceUsageTemplate {
	private ResourceType resourceType;
	private Duration duration;
	private Duration appointmentStartOffset;

	public ResourceUsageTemplate(ResourceType resourceType, Duration duration) {
		this(resourceType, duration, Duration.ZERO);
	}
	
	public ResourceUsageTemplate(ResourceType resourceType, Duration duration, Duration appointmentStartOffset) {
		checkNotNull(resourceType);
		checkNotNull(duration);
		checkArgument(duration.compareTo(Duration.ZERO) > 0);
		checkNotNull(appointmentStartOffset);
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
		return TimePeriod.withDuration(datetime.plus(this.appointmentStartOffset), this.duration);
	}
	
	public ResourceUsage toResourceUsage(LocalDateTime appointmentStart){
		return ResourceUsage.fromTemplate(appointmentStart, this);
	}

	@Override
	public String toString() {
		return "ResourceUsageTemplate [resourceType=" + resourceType + ", duration=" + duration
				+ ", appointmentStartOffset=" + appointmentStartOffset + "]";
	}

}
