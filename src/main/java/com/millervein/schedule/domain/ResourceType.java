package com.millervein.schedule.domain;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "create")
public class ResourceType {
	@NonNull
	String type;
	@NonNull
	String name;
}
