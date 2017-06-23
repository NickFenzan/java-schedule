package com.millervein.schedule;

import java.time.LocalDateTime;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Associates a time period with a resource type, indicating usage during this
 * period
 * 
 * @author nick
 *
 */
public class ResourceUsage {
	private ResourceType resourceType;
	private TimePeriod timePeriod;

	private ResourceUsage(ResourceType resourceType, TimePeriod timePeriod) {
		this.resourceType = checkNotNull(resourceType);
		this.timePeriod = checkNotNull(timePeriod);
	}
	
	public static ResourceUsage create(ResourceType resourceType, TimePeriod timePeriod){
		return new ResourceUsage(resourceType, timePeriod); 
	}
	
	public static ResourceUsage fromTemplate(LocalDateTime usageStart, ResourceUsageTemplate template){
		checkNotNull(usageStart);
		checkNotNull(template);
		return new ResourceUsage(template.getResourceType(), template.timePeriodAt(usageStart));
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	@Override
	public String toString() {
		return "ResourceUsage [resourceType=" + resourceType + ", timePeriod=" + timePeriod + "]";
	}

}
