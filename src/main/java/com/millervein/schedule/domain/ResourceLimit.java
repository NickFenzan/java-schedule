package com.millervein.schedule.domain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor="create")
@EqualsAndHashCode(of = { "resourceType" })
public class ResourceLimit {
	@NonNull
	ResourceType resourceType;
	@NonNull
	Integer limit;
}
