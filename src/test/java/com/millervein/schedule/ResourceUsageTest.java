package com.millervein.schedule;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;

public class ResourceUsageTest {
	private enum TestResourceType implements ResourceType{
		TEST
	}
	@Test
	public void equalityTest(){
		LocalDateTime now = LocalDateTime.now();
		ResourceUsage usage1 = ResourceUsage.create(TestResourceType.TEST, TimePeriod.withDuration(now, Duration.ofMinutes(15)));
		ResourceUsage usage2 = ResourceUsage.create(TestResourceType.TEST, TimePeriod.withDuration(now, Duration.ofMinutes(15)));
		assertTrue(usage1.equals(usage2));
	}
}
