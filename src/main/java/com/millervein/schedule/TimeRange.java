package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

public class TimeRange {
	public static SortedSet<LocalDateTime> generateTimeRange(LocalDateTime start, LocalDateTime end,
			Duration interval) {
		SortedSet<LocalDateTime> timeRange = new TreeSet<LocalDateTime>();
		for (LocalDateTime iterTime = start; iterTime.isBefore(end); iterTime = iterTime.plus(interval)) {
			timeRange.add(iterTime);
		}
		return timeRange;
	}

}
