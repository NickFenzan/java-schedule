package com.millervein.schedule.domain;

import java.time.LocalDateTime;

import lombok.NonNull;
import lombok.Value;

/**
 * Associates a time period with a resource type, indicating usage during this
 * period
 * 
 * @author nick
 *
 */
@Value(staticConstructor="create")
public class ResourceUsage {
	@NonNull
	private final ResourceType resourceType;
	@NonNull
	private final TimePeriod timePeriod;

	public static ResourceUsage fromTemplate(LocalDateTime usageStart, ResourceUsageTemplate template) {
		return new ResourceUsage(template.getResourceType(), template.timePeriodAt(usageStart));
	}
	
}
