package com.millervein.schedule.domain.collections;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.ForwardingMultiset;
import com.google.common.collect.Multiset;
import com.millervein.schedule.domain.ResourceType;
import com.millervein.schedule.domain.ResourceUsage;
import com.millervein.schedule.domain.TimePeriod;

public class ResourceUsageMultiset extends ForwardingMultiset<ResourceUsage> {
	final Multiset<ResourceUsage> delegate;

	@Override
	protected Multiset<ResourceUsage> delegate() {
		return this.delegate;
	}

	private ResourceUsageMultiset() {
		this.delegate = ConcurrentHashMultiset.create();
	}

	private ResourceUsageMultiset(Iterable<? extends ResourceUsage> elements) {
		this.delegate = ConcurrentHashMultiset.create(elements);
	}

	public static ResourceUsageMultiset create() {
		return new ResourceUsageMultiset();
	}

	public static ResourceUsageMultiset create(Collection<? extends ResourceUsage> c) {
		return new ResourceUsageMultiset(c);
	}
	// End boilerplate
	// -------------------------------------------------------------

	private static Collector<ResourceUsage, ?, ResourceUsageMultiset> collector() {
		return Collectors.toCollection(ResourceUsageMultiset::create);
	}

	private Function<ResourceUsage, Integer> concurrency() {
		return resource -> delegate.stream().filter(a -> a.getTimePeriod().overlaps(resource.getTimePeriod()))
				.mapToInt(a -> 1).sum();
	}

	/**
	 * Returns a new ResourceUsageList containing only resources of the
	 * specified type
	 * 
	 * @param resourceType
	 * @return
	 */
	public ResourceUsageMultiset withResourceType(ResourceType resourceType) {
		return delegate.stream().filter(r->r.getResourceType().equals(resourceType)).collect(collector());
	}

	/**
	 * Returns a new ResourceUsageList containing only resources of that are
	 * being used at a given time
	 * 
	 * @param resourceType
	 * @return
	 */
	public ResourceUsageMultiset atTime(LocalDateTime time) {
		return delegate.stream().filter(a->a.getTimePeriod().includes(time)).collect(collector());
	}

	/**
	 * Reduces a ResourceUsageList to a count of the most concurrent resource
	 * usage
	 * 
	 * @return
	 */
	public Integer maxConcurrency() {
		return delegate.stream().map(concurrency()).reduce(0, Integer::max);
	}

	/**
	 * Returns a set of all the resource types included
	 * 
	 * @return
	 */
	public Set<ResourceType> resourceTypes() {
		return this.stream().map(r -> r.getResourceType()).distinct().collect(Collectors.toSet());
	}

	/**
	 * Returns a map of resource usage lists, partitioned by resource type
	 * 
	 * @return
	 */
	public Map<ResourceType, ResourceUsageMultiset> toResourceTypeUsageMap() {
		Map<ResourceType, ResourceUsageMultiset> map = new HashMap<ResourceType, ResourceUsageMultiset>();
		for (ResourceUsage usage : delegate) {
			ResourceType type = usage.getResourceType();
			ResourceUsageMultiset typeUsage = map.getOrDefault(type, new ResourceUsageMultiset());
			typeUsage.add(usage);
			map.put(type, typeUsage);
		}
		return map;
	}

	public Map<ResourceType, Integer> toResourceTypeConcurrencyMap() {
		Map<ResourceType, Integer> typeConcurrency = new HashMap<ResourceType, Integer>();
		for (Map.Entry<ResourceType, ResourceUsageMultiset> entry : toResourceTypeUsageMap().entrySet()) {
			ResourceType type = entry.getKey();
			ResourceUsageMultiset usages = entry.getValue();
			typeConcurrency.put(type, usages.maxConcurrency());
		}
		return typeConcurrency;
	}

	public Optional<ResourceUsage> earliestUsage() {
		return delegate.stream().reduce((a, b) -> a.getTimePeriod().startsBefore(b.getTimePeriod()) ? a : b);
	}

	public Optional<ResourceUsage> latestUsage() {
		return delegate.stream().reduce((a,b)-> a.getTimePeriod().endsAfter(b.getTimePeriod()) ? a : b);
	}

	public Optional<TimePeriod> toTimePeriod() {
		try {
			return Optional.of(TimePeriod.withRange(earliestUsage().get().getTimePeriod().getStart(), latestUsage().get().getTimePeriod().getEnd()));
		} catch (NoSuchElementException e) {
			return Optional.empty();
		}
	}
}
