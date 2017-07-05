package com.millervein.schedule.seed;

import java.util.Set;

import com.google.common.collect.Sets;
import com.millervein.schedule.domain.ResourceLimit;
import com.millervein.schedule.domain.ResourceType;

public abstract class ResourceLimitSeeder {
	
	public static Set<ResourceLimit> create(){
		Set<ResourceLimit> resourceLimits = Sets.newHashSet();
		resourceLimits.addAll(staffLimits());
		resourceLimits.addAll(roomLimits());
		resourceLimits.addAll(equipmentLimits());
		return resourceLimits;
	}
	

	/**
	 * Creates preset staffing limits
	 * 
	 * @return
	 */
	private static Set<ResourceLimit> staffLimits() {
		return Sets.newHashSet(
				ResourceLimit.create(ResourceType.create("staff", "medtech"), 2),
				ResourceLimit.create(ResourceType.create("staff", "ultrasound"), 2),
				ResourceLimit.create(ResourceType.create("staff", "nurse"), 2),
				ResourceLimit.create(ResourceType.create("staff", "physician"), 1)
				);
	}

	/**
	 * Creates preset room limits
	 * 
	 * @return
	 */
	private static Set<ResourceLimit> roomLimits() {
		return Sets.newHashSet(
				ResourceLimit.create(ResourceType.create("room", "consult"), 5),
				ResourceLimit.create(ResourceType.create("room", "procedure"), 2)
				);
	}

	/**
	 * Creates preset equipment limits
	 * 
	 * @return
	 */
	private static Set<ResourceLimit> equipmentLimits() {
		return Sets.newHashSet(
				ResourceLimit.create(ResourceType.create("equipment", "ultrasound"), 5),
				ResourceLimit.create(ResourceType.create("equipment", "cryo"), 2),
				ResourceLimit.create(ResourceType.create("equipment", "laser"), 1),
				ResourceLimit.create(ResourceType.create("equipment", "rf"), 1)
				);
	}

}
