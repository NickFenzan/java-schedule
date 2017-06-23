package com.millervein.schedule;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.common.collect.Lists;

public class ResourceUsageListTest {
	Logger log = Logger.getGlobal();

	/**
	 * Template for the tests
	 * 
	 * @param now
	 * @return
	 */
	private ResourceUsageList buildTestUsageList(LocalDateTime now) {
		return ResourceUsageList.create(Lists.newArrayList(
				ResourceUsage.create(RoomType.CONSULT, TimePeriod.withDuration(now, Duration.ofMinutes(45))),
				ResourceUsage.create(StaffType.MEDTECH, TimePeriod.withDuration(now, Duration.ofMinutes(15))),
				ResourceUsage.create(StaffType.MEDTECH, TimePeriod.withDuration(now, Duration.ofMinutes(15))),
				ResourceUsage.create(StaffType.ULTRASOUND,
						TimePeriod.withDuration(now.plusMinutes(15), Duration.ofMinutes(15))),
				ResourceUsage.create(StaffType.PHYSICIAN,
						TimePeriod.withDuration(now.plusMinutes(30), Duration.ofMinutes(15)))));

	}

	@Test
	public void earliestUsageTest() {
		LocalDateTime now = LocalDateTime.now();
		ResourceUsageList usageList = this.buildTestUsageList(now);
		ResourceUsage earliestUsage = usageList.findEarliestUsage().orElseThrow(IllegalStateException::new);
		assertTrue(earliestUsage.getStart().equals(now));
	}

	@Test
	public void latestUsageTest() {
		LocalDateTime now = LocalDateTime.now();
		ResourceUsageList usageList = this.buildTestUsageList(now);
		ResourceUsage latestUsage = usageList.latestUsage().orElseThrow(IllegalStateException::new);
		assertTrue(latestUsage.getEnd().equals(now.plusMinutes(45)));
	}

	@Test
	public void resourceUsageCountAtTimeTest() {
		LocalDateTime now = LocalDateTime.now();
		ResourceUsageList usageList = this.buildTestUsageList(now);
		assertTrue(usageList.resourceUsageCountAtTime(now).equals(3));
	}

	@Test
	public void filterResourceTypeTest() {
		LocalDateTime now = LocalDateTime.now();
		ResourceUsageList usageList = this.buildTestUsageList(now);
		ResourceUsageList filteredList = usageList.filterResourceType(RoomType.CONSULT);
		ResourceUsageList testList = ResourceUsageList.create(Lists.newArrayList(
				ResourceUsage.create(RoomType.CONSULT, TimePeriod.withDuration(now, Duration.ofMinutes(45)))));
		assertTrue(filteredList.equals(testList));
		assertFalse(filteredList.equals(usageList));
	}
	
	@Test
	public void resourceTypeConcurrencyTest() {
		LocalDateTime now = LocalDateTime.now();
		ResourceUsageList usageList = this.buildTestUsageList(now);
		Integer medTechConcurrency = usageList.getResourceTypeConcurrency(StaffType.MEDTECH);
		assertTrue(medTechConcurrency.equals(2));
		ResourceUsageList usageList2 = ResourceUsageList.create();
		Integer medTechConcurrency2 = usageList2.getResourceTypeConcurrency(StaffType.MEDTECH);
		
		assertTrue(medTechConcurrency2.equals(0));
	}
	
}
