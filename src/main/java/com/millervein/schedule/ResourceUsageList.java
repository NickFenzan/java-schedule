package com.millervein.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class ResourceUsageList extends ArrayList<ResourceUsage> {
	
	public ResourceUsageList() {
		super();
	}

	public ResourceUsageList(Collection<? extends ResourceUsage> c) {
		super(c);
	}

	public ResourceUsageList getStaffTypeUsageList(String staffType){
		return new ResourceUsageList(this.stream().filter(resUse -> resUse.getResourceType() == staffType).collect(Collectors.toList()));
	}
	
	public Integer getStaffTypeConcurrency(String staffType){
		ResourceUsageList staffTypeList = this.getStaffTypeUsageList(staffType);
		Integer highestOverlap = 0;
		for(ResourceUsage staffUsage : staffTypeList){
			Integer currentOverlap = 0;
			for (ResourceUsage otherStaffUsage : staffTypeList){
				if (staffUsage.getTimePeriod().overlaps(otherStaffUsage.getTimePeriod())){
					currentOverlap++;
				}
			}
			highestOverlap = (currentOverlap > highestOverlap) ? currentOverlap : highestOverlap;
		}
		return highestOverlap;
	}
	
	
}
