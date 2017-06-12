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
				smallestGap = (gap.compareTo(Duration.ZERO) > 0 && gap.compareTo(smallestGap) < 0) ? gap : smallestGap;
				iter.next();
			}
		}
		return smallestGap;
	}

	private void sortByStartTime() {
		this.sort((a, b) -> a.getTimePeriod().getStart().compareTo(b.getTimePeriod().getStart()));
	}

	public Duration maximumComprehensiveInterval() {
		Duration smallestDuration = smallestDuration();
		Duration smallestGap = smallestGap();
		if(smallestDuration != null && smallestGap != null){
			long gcdMins = gcd(smallestDuration.toMinutes(), smallestGap.toMinutes());
			return Duration.ofMinutes(gcdMins);
		}else if (smallestDuration != null){
			return smallestDuration;
		}else if (smallestGap != null){
			return smallestGap;
		}
		return null;
	}

	private static long gcd(long a, long b) {
		while (b > 0) {
			long temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
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
