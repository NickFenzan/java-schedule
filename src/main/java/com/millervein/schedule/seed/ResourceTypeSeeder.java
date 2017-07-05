package com.millervein.schedule.seed;

import java.util.Set;

import com.google.common.collect.Sets;
import com.millervein.schedule.domain.ResourceType;

public abstract class ResourceTypeSeeder {
	public static Set<ResourceType> create(){
		Set<ResourceType> resourceTypes = Sets.newHashSet();
		
		resourceTypes.add(ResourceType.create("staff", "medtech"));
		resourceTypes.add(ResourceType.create("staff", "ultrasound"));
		resourceTypes.add(ResourceType.create("staff", "nurse"));
		resourceTypes.add(ResourceType.create("staff", "physician"));
		
		resourceTypes.add(ResourceType.create("equipment", "ultrasound"));
		resourceTypes.add(ResourceType.create("equipment", "cryo"));
		resourceTypes.add(ResourceType.create("equipment", "laser"));
		resourceTypes.add(ResourceType.create("equipment", "rf"));
		
		resourceTypes.add(ResourceType.create("room", "consult"));
		resourceTypes.add(ResourceType.create("room", "procedure"));
		
		return resourceTypes;
	}
}
