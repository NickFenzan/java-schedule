package com.millervein.schedule;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Associates a time period with a resource type, indicating usage during this
 * period
 * 
 * @author nick
 *
 */
public class ResourceUsage implements HasTimePeriod {
	private ResourceType resourceType;
	private TimePeriod timePeriod;

	private ResourceUsage(ResourceType resourceType, TimePeriod timePeriod) {
		this.resourceType = checkNotNull(resourceType);
		this.timePeriod = checkNotNull(timePeriod);
	}

	public static ResourceUsage create(ResourceType resourceType, TimePeriod timePeriod) {
		return new ResourceUsage(resourceType, timePeriod);
	}

	public static ResourceUsage fromTemplate(LocalDateTime usageStart, ResourceUsageTemplate template) {
		checkNotNull(usageStart);
		checkNotNull(template);
		return new ResourceUsage(template.getResourceType(), template.timePeriodAt(usageStart));
	}
	
	@SuppressWarnings("unchecked")
	public static BinaryOperator<ResourceUsage> earliest() {
		return (BinaryOperator<ResourceUsage>) HasTimePeriod.earliest();
	}
	
	@SuppressWarnings("unchecked")
	public static BinaryOperator<ResourceUsage> latest() {
		return (BinaryOperator<ResourceUsage>) HasTimePeriod.latest();
	}
	
	public static Predicate<ResourceUsage> isResourceType(ResourceType resourceType){
		return p -> p.getResourceType().equals(resourceType);
	}
	
	@SuppressWarnings("unchecked")
	public static Predicate<ResourceUsage> includesTime(LocalDateTime time){
		return (Predicate<ResourceUsage>) HasTimePeriod.includesTime(time);
	}
	
	public ResourceType getResourceType() {
		return resourceType;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	@Override
	public int hashCode() {
		return Objects.hash(resourceType, timePeriod);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceUsage other = (ResourceUsage) obj;
		if (!resourceType.equals(other.resourceType) || !timePeriod.equals(other.timePeriod)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ResourceUsage [resourceType=" + resourceType + ", timePeriod=" + timePeriod + "]";
	}
}
