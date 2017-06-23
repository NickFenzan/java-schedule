package com.millervein.schedule;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.ForwardingMultiset;
import com.google.common.collect.Multiset;

public class ResourceUsageList extends ForwardingMultiset<ResourceUsage> {
	final Multiset<ResourceUsage> delegate;
	
	@Override
	protected Multiset<ResourceUsage> delegate() {
		return this.delegate;
	}

	private ResourceUsageList() {
		this.delegate = ConcurrentHashMultiset.create();
	}
	
	private ResourceUsageList(Iterable<? extends ResourceUsage> elements) {
		this.delegate = ConcurrentHashMultiset.create(elements);
	}
	
	public static ResourceUsageList create(){
		return new ResourceUsageList();
	}
	
	public static ResourceUsageList create(Collection<? extends ResourceUsage> c){
		return new ResourceUsageList(c);
	}
	
	public static Collector<ResourceUsage, ?, ResourceUsageList> collector(){
		return Collectors.toCollection(ResourceUsageList::create);
	}
	
	public Optional<ResourceUsage> findEarliestUsage() {
		return delegate.stream().reduce(ResourceUsage.earliest());
	}

	public Optional<ResourceUsage> latestUsage() {
		return delegate.stream().reduce(ResourceUsage.latest()); 
	}

	public Integer resourceUsageCountAtTime(LocalDateTime time) {
		return delegate.stream().filter(ResourceUsage.includesTime(time)).mapToInt(a->1).sum();
	}
	
	public ResourceUsageList filterResourceType(ResourceType resourceType){
		return delegate.stream().filter(ResourceUsage.isResourceType(resourceType)).collect(collector());
	}
	
	//This needs to be renamed/relocated
	public static Function<ResourceUsage, Integer> maxConcurrency(Multiset<ResourceUsage> resourceList){
		return resource -> resourceList.stream().filter(ResourceUsage.isResourceType(resource.getResourceType())).filter(a->a.overlaps(resource)).mapToInt(a->1).sum();
	}

	public Integer getResourceTypeConcurrency(ResourceType resourceType) {
		return delegate.stream().map(ResourceUsageList.maxConcurrency(delegate)).reduce(0, Integer::max);
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
