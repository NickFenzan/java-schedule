package com.millervein.schedule.domain;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "create")
public class ResourceUsageTemplate {
	@NonNull
	private ResourceType resourceType;
	@NonNull
	private Duration duration;
	@NonNull
	private Duration appointmentStartOffset;

	public TimePeriod timePeriodAt(LocalDateTime datetime) {
		return TimePeriod.withDuration(datetime.plus(this.appointmentStartOffset), this.duration);
	}

	public ResourceUsage toResourceUsage(LocalDateTime appointmentStart) {
		return ResourceUsage.fromTemplate(appointmentStart, this);
	}

}
