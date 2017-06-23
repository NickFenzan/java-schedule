package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class ResourceUsageTemplateList extends ArrayList<ResourceUsageTemplate> {

	public ResourceUsageList toResourceUsageList(LocalDateTime appointmentStart) {
		return new ResourceUsageList(this.stream().map(resUseTemp -> resUseTemp.toResourceUsage(appointmentStart))
				.collect(Collectors.toList()));
	}

	public Duration getSmallestDuration() {
		return this.stream().map(resUsage -> resUsage.getDuration())
				.reduce((smallest, next) -> next.compareTo(smallest) < 0 ? next : smallest).orElse(Duration.ZERO);
	}

	public Duration getTotalDuration() {
		LocalDateTime now = LocalDateTime.now();
		TimePeriod latestPeriod = this.stream().map(resUsage -> resUsage.timePeriodAt(now))
				.reduce(TimePeriod.withDuration(now, Duration.ZERO), (latest, next) -> next.endsAfter(latest) ? next : latest);
		return Duration.between(now, latestPeriod.getEnd());
	}
}
