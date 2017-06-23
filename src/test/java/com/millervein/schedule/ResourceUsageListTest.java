package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;

public class ResourceUsageListTest {
	
	private ResourceUsageList buildTestUsageList(){
		ResourceUsageList usageList = new ResourceUsageList();
		usageList.add(ResourceUsage.create(RoomType.CONSULT, TimePeriod.withDuration(LocalDateTime.now(),Duration.ofMinutes(45))));
		return usageList;
	}

	public void test() {
	}
}
