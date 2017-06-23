package com.millervein.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class ResourceUsageList extends ArrayList<ResourceUsage> {

	public ResourceUsageList() {
		super();
	}

	public ResourceUsageList(Collection<? extends ResourceUsage> c) {
		super(c);
	}

	public LocalDateTime earliestUsage() {
		return this.stream().map(r -> r.getTimePeriod().getStart()).min((time1, time2) -> time1.compareTo(time2))
				.orElseThrow(() -> new IllegalStateException());
	}

	public LocalDateTime latestUsage() {
		return this.stream().map(r -> r.getTimePeriod().getEnd()).max((time1, time2) -> time1.compareTo(time2))
				.orElseThrow(() -> new IllegalStateException());
	}

	public void add(ResourceType resourceType, TimePeriod timePeriod){
		this.add(ResourceUsage.create(resourceType, timePeriod));
	}
	

	/**
	 * Counts the amount of resources in the list that overlap a given time
	 * 
	 * @param staffUsages
	 * @param time
	 * @return
	 */
	public Integer resourceUsageCountAtTime(LocalDateTime time) {
		Integer usageCount = 0;
		for (ResourceUsage resourceUsage : this) {
			TimePeriod usagePeriod = resourceUsage.getTimePeriod();
			if (usagePeriod.includes(time)) {
				usageCount++;
			}
		}
		return usageCount;
	}

	public ResourceUsageList getResourceTypeUsageList(ResourceType staffType) {
		ResourceUsageList newList = new ResourceUsageList();
		for(ResourceUsage resourceUsage : this){
			if(resourceUsage.getResourceType().equals(staffType))
				newList.add(resourceUsage);
		}
		return newList;
	}

	public Integer getResourceTypeConcurrency(ResourceType staffType) {
		ResourceUsageList staffTypeList = this.getResourceTypeUsageList(staffType);
		Integer highestOverlap = 0;
		for (ResourceUsage staffUsage : staffTypeList) {
			Integer currentOverlap = 0;
			for (ResourceUsage otherStaffUsage : staffTypeList) {
				if (staffUsage.getTimePeriod().overlaps(otherStaffUsage.getTimePeriod())) {
					currentOverlap++;
				}
			}
			highestOverlap = (currentOverlap > highestOverlap) ? currentOverlap : highestOverlap;
		}
		return highestOverlap;
	}
	
	public Set<ResourceType> resourceTypes(){
		return this.stream().map(r -> r.getResourceType()).distinct().collect(Collectors.toSet());
	}
	
	public Map<ResourceType, ResourceUsageList> typePartionedResourceUsage(){
		Map<ResourceType, ResourceUsageList> map = new HashMap<ResourceType, ResourceUsageList>();
		for(ResourceUsage usage : this){
			ResourceType type = usage.getResourceType();
			ResourceUsageList typeUsage = map.getOrDefault(type, new ResourceUsageList());
			typeUsage.add(usage);
			map.put(type, typeUsage);
		}
		return map;
	}
	
	public Map<ResourceType, Integer> getResourceTypeConcurrency(){
		Map<ResourceType, Integer> typeConcurrency = new HashMap<ResourceType, Integer>();
		for(Map.Entry<ResourceType, ResourceUsageList> entry : typePartionedResourceUsage().entrySet()){
			ResourceType type = entry.getKey();
			ResourceUsageList usages = entry.getValue();
			typeConcurrency.put(type, usages.concurrency());
		}
		return typeConcurrency;
	}
	
	public Integer concurrency(){
		Integer highestOverlap = 0;
		for (ResourceUsage usage : this) {
			Integer currentOverlap = 0;
			for (ResourceUsage otherUsage : this) {
				if (usage.getTimePeriod().overlaps(otherUsage.getTimePeriod())) {
					currentOverlap++;
				}
			}
			highestOverlap = (currentOverlap > highestOverlap) ? currentOverlap : highestOverlap;
		}
		return highestOverlap;
	}

}
