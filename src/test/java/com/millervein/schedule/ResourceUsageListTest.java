package com.millervein.schedule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.millervein.schedule.domain.ResourceType;
import com.millervein.schedule.domain.ResourceUsage;
import com.millervein.schedule.domain.TimePeriod;
import com.millervein.schedule.domain.collections.ResourceUsageMultiset;

public class ResourceUsageListTest {
	Logger log = Logger.getGlobal();

	/**
	 * Template for the tests
	 * 
	 * @param now
	 * @return
	 */
	private ResourceUsageMultiset buildTestUsageList(LocalDateTime now) {
		return ResourceUsageMultiset.create(Lists.newArrayList(
				ResourceUsage.create(ResourceType.create("room", "consult"), TimePeriod.withDuration(now, Duration.ofMinutes(45))),
				ResourceUsage.create(ResourceType.create("staff", "medtech"), TimePeriod.withDuration(now, Duration.ofMinutes(15))),
				ResourceUsage.create(ResourceType.create("staff", "medtech"), TimePeriod.withDuration(now, Duration.ofMinutes(15))),
				ResourceUsage.create(ResourceType.create("staff", "ultrasound"),
						TimePeriod.withDuration(now.plusMinutes(15), Duration.ofMinutes(15))),
				ResourceUsage.create(ResourceType.create("staff", "physician"),
						TimePeriod.withDuration(now.plusMinutes(30), Duration.ofMinutes(15)))));

	}

	// Methods are unused, so i removed them for now
	// @Test
	// public void earliestUsageTest() {
	// LocalDateTime now = LocalDateTime.now();
	// ResourceUsageList usageList = this.buildTestUsageList(now);
	// ResourceUsage earliestUsage =
	// usageList.findEarliestUsage().orElseThrow(IllegalStateException::new);
	// assertTrue(earliestUsage.getStart().equals(now));
	// }
	//
	// @Test
	// public void latestUsageTest() {
	// LocalDateTime now = LocalDateTime.now();
	// ResourceUsageList usageList = this.buildTestUsageList(now);
	// ResourceUsage latestUsage =
	// usageList.latestUsage().orElseThrow(IllegalStateException::new);
	// assertTrue(latestUsage.getEnd().equals(now.plusMinutes(45)));
	// }

	@Test
	public void withResourceTypeTest() {
		LocalDateTime now = LocalDateTime.now();
		ResourceUsageMultiset usageList = this.buildTestUsageList(now);
		ResourceUsageMultiset filteredList = usageList.withResourceType(ResourceType.create("room", "consult"));
		ResourceUsageMultiset testList = ResourceUsageMultiset.create(Lists.newArrayList(
				ResourceUsage.create(ResourceType.create("room", "consult"), TimePeriod.withDuration(now, Duration.ofMinutes(45)))));
		assertTrue(filteredList.equals(testList));
		assertFalse(filteredList.equals(usageList));
	}

	@Test
	public void maxConcurrencyTest() {
		LocalDateTime now = LocalDateTime.now();
		ResourceUsageMultiset usageList = this.buildTestUsageList(now);
		Integer medTechConcurrency = usageList.withResourceType(ResourceType.create("staff", "medtech")).maxConcurrency();
		assertTrue(medTechConcurrency.equals(2));
		ResourceUsageMultiset usageList2 = ResourceUsageMultiset.create();
		Integer medTechConcurrency2 = usageList2.withResourceType(ResourceType.create("staff", "medtech")).maxConcurrency();
		assertTrue(medTechConcurrency2.equals(0));
	}

	@Test
	public void resourceTypesTest() {
		LocalDateTime now = LocalDateTime.now();
		Set<ResourceType> control = Sets.newHashSet(ResourceType.create("room", "consult"), ResourceType.create("staff", "medtech"), ResourceType.create("staff", "ultrasound"),
				ResourceType.create("staff", "physician"));
		Set<ResourceType> test = this.buildTestUsageList(now).resourceTypes();
		assertTrue(control.equals(test));
	}

	@Test
	public void typePartionedResourceUsageTest() {
		LocalDateTime now = LocalDateTime.now();
		Map<ResourceType, ResourceUsageMultiset> test = this.buildTestUsageList(now).toResourceTypeUsageMap();
		Map<ResourceType, ResourceUsageMultiset> control = Maps.newHashMap();
		control.put(ResourceType.create("room", "consult"), ResourceUsageMultiset.create(Lists.newArrayList(
				ResourceUsage.create(ResourceType.create("room", "consult"), TimePeriod.withDuration(now, Duration.ofMinutes(45))))));
		control.put(ResourceType.create("staff", "medtech"), ResourceUsageMultiset.create(Lists.newArrayList(
				ResourceUsage.create(ResourceType.create("staff", "medtech"), TimePeriod.withDuration(now, Duration.ofMinutes(15))),
				ResourceUsage.create(ResourceType.create("staff", "medtech"), TimePeriod.withDuration(now, Duration.ofMinutes(15))))));
		control.put(ResourceType.create("staff", "ultrasound"), ResourceUsageMultiset.create(Lists.newArrayList(ResourceUsage
				.create(ResourceType.create("staff", "ultrasound"), TimePeriod.withDuration(now.plusMinutes(15), Duration.ofMinutes(15))))));
		control.put(ResourceType.create("staff", "physician"), ResourceUsageMultiset.create(Lists.newArrayList(ResourceUsage
				.create(ResourceType.create("staff", "physician"), TimePeriod.withDuration(now.plusMinutes(30), Duration.ofMinutes(15))))));
		assertTrue(control.equals(test));
	}
	

}
