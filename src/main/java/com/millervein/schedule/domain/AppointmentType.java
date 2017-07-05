package com.millervein.schedule.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.millervein.schedule.domain.collections.ResourceUsageMultiset;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor="create")
@EqualsAndHashCode(of = { "name" })
public class AppointmentType {
	@NonNull
	String name;
	@NonNull
	List<ResourceUsageTemplate> resourceUsageTemplate;
	@NonNull
	BigDecimal value;

	public ResourceUsageMultiset toResourceUsageList(LocalDateTime start) {
		return resourceUsageTemplate.stream().map(t -> t.toResourceUsage(start))
				.collect(Collectors.toCollection(ResourceUsageMultiset::create));
	}

}
