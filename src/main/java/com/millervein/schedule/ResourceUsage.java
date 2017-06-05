package com.millervein.schedule;

import java.time.LocalDateTime;

public class ResourceUsage {
	private String resourceType;
	private TimePeriod timePeriod;

	public ResourceUsage(LocalDateTime appointmentStart, ResourceUsageTemplate template){
		this.resourceType = template.getResourceType();
		this.timePeriod = template.timePeriodAt(appointmentStart);
	}
	
	public ResourceUsage(String resourceType, TimePeriod timePeriod) {
		super();
		this.resourceType = resourceType;
		this.timePeriod = timePeriod;
	}

	public String getResourceType() {
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
