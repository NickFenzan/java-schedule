package com.millervein.schedule;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;

import com.millervein.schedule.domain.ResourceType;
import com.millervein.schedule.domain.ResourceUsage;
import com.millervein.schedule.domain.TimePeriod;

public class ResourceUsageTest {
	@Test
	public void equalityTest(){
		LocalDateTime now = LocalDateTime.now();
		ResourceUsage usage1 = ResourceUsage.create(ResourceType.create("test", "test"), TimePeriod.withDuration(now, Duration.ofMinutes(15)));
		ResourceUsage usage2 = ResourceUsage.create(ResourceType.create("test", "test"), TimePeriod.withDuration(now, Duration.ofMinutes(15)));
		assertTrue(usage1.equals(usage2));
	}
}
