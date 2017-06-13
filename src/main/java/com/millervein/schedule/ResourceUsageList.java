package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

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

	public Duration smallestDuration() {
		return this.stream().map(r -> r.getTimePeriod().getDuration()).min((dur1, dur2) -> dur1.compareTo(dur2))
				.orElseThrow(() -> new IllegalStateException());
	}

	public Duration smallestGap() {
		Duration smallestGap = null;
		this.sortByStartTime();
		PeekingIterator<ResourceUsage> iter = Iterators.peekingIterator(this.iterator());
		while (iter.hasNext()) {
			ResourceUsage current = iter.next();
			while (iter.hasNext()) {
				ResourceUsage next = iter.peek();
				Duration gap = Duration.between(current.getTimePeriod().getEnd(), next.getTimePeriod().getStart());
				if(gap.compareTo(Duration.ZERO) > 0){
					if (smallestGap == null){
						smallestGap = gap;
					} else if (gap.compareTo(smallestGap) < 0){
						smallestGap = gap;
					}
				}
				iter.next();
			}
		}
		return smallestGap;
	}

	private void sortByStartTime() {
		this.sort((a, b) -> a.getTimePeriod().getStart().compareTo(b.getTimePeriod().getStart()));
	}

	public Duration maximumComprehensiveInterval() {
		Duration smallestDifferenceBetweenResourceStart = null;
		this.sortByStartTime();
		PeekingIterator<ResourceUsage> iter = Iterators.peekingIterator(this.iterator());
		while (iter.hasNext()) {
			ResourceUsage current = iter.next();
			while (iter.hasNext()) {
				ResourceUsage next = iter.peek();
				Duration gap = Duration.between(current.getTimePeriod().getStart(), next.getTimePeriod().getStart());
				if(gap.compareTo(Duration.ZERO) > 0){
					if (smallestDifferenceBetweenResourceStart == null){
						smallestDifferenceBetweenResourceStart = gap;
					} else if (gap.compareTo(smallestDifferenceBetweenResourceStart) < 0){
						smallestDifferenceBetweenResourceStart = gap;
					}
				}
				iter.next();
			}
		}
		return smallestDifferenceBetweenResourceStart;
	}

	public ResourceUsageList getStaffTypeUsageList(String staffType) {
		return new ResourceUsageList(
				this.stream().filter(resUse -> resUse.getResourceType() == staffType).collect(Collectors.toList()));
	}

	public Integer getStaffTypeConcurrency(String staffType) {
		ResourceUsageList staffTypeList = this.getStaffTypeUsageList(staffType);
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

}
