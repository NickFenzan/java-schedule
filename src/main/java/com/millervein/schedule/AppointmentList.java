package com.millervein.schedule;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ForwardingMultiset;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class AppointmentList extends ForwardingMultiset<Appointment> {
	private final Multiset<Appointment> delegate;

	public static AppointmentList create() {
		return new AppointmentList();
	}

	public static AppointmentList create(Iterable<? extends Appointment> elements) {
		return new AppointmentList(elements);
	}

	private AppointmentList() {
		this.delegate = HashMultiset.create();
	}

	private AppointmentList(Iterable<? extends Appointment> elements) {
		this.delegate = HashMultiset.create(elements);
	}

	@Override
	protected Multiset<Appointment> delegate() {
		return delegate;
	}

	public Integer resourceTypeUsage(ResourceType resourceType) {
		Integer usageCount = 0;
		LocalDateTime earliestTime = this.earliestTime();
		LocalDateTime latestTime = this.latestTime();
		// This is a potential place for optimization. It could use a larger
		// interval than 1 minute in some cases, but figuring out what that
		// is and when can be tricky
		Duration interval = Duration.ofMinutes(1);
		if(earliestTime == null || latestTime == null || interval == null){
			return usageCount;
		}
		Set<LocalDateTime> appointmentResourceUseageTimeRange = TimeRange.generateTimeRange(earliestTime, latestTime,
				interval);
		for (LocalDateTime time : appointmentResourceUseageTimeRange) {
			Integer timeUsage = resourceTypeUsageAtTime(resourceType, time);
			usageCount = (timeUsage > usageCount) ? timeUsage : usageCount;
		}
		return usageCount;
	}

	private Integer resourceTypeUsageAtTime(ResourceType resourceType, LocalDateTime time) {
		Integer usageCount = 0;
		for (Appointment appointment : delegate) {
			ResourceUsageList resourceUsages = appointment.getResourceUsage().getResourceTypeUsageList(resourceType);
			usageCount += resourceUsages.resourceUsageCountAtTime(time);
		}
		return usageCount;
	}

	private Map<ResourceType, Integer> resourceTypeUsage(){
		ResourceUsageList allResources = new ResourceUsageList();
		for(Appointment appointment : delegate) {
			allResources.addAll(appointment.getResourceUsage());
		}
		return allResources.getResourceTypeConcurrency();
	}
	
	public boolean valid(ResourceTypeLimits resourceLimits) {
		//Loop through the different categories(Staff, rooms, equipment)
		for (Map.Entry<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>> resourceCategoryEntry : resourceLimits
				.entrySet()) {
			Class<? extends ResourceType> resourceCategory = resourceCategoryEntry.getKey();
			ResourceTypeLimitMap<? extends ResourceType> resourceLimitMap = resourceCategoryEntry.getValue();
			
			//Loop through the entries for that category
			for(Map.Entry<? extends ResourceType, Integer> resourceTypeLimitEntry : resourceLimitMap.entrySet()){
				ResourceType resourceType = resourceTypeLimitEntry.getKey();
				Integer resourceLimit = resourceTypeLimitEntry.getValue();
				if (this.resourceTypeUsage(resourceType) > resourceLimit) {
//					System.out.println("Not enough " + resourceType.toString() + "s");
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean resourceLimitsValid(ResourceTypeLimits resourceLimits) {
		Map<ResourceType, Integer> resourceUsages = this.resourceTypeUsage();
		for(java.util.Map.Entry<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>> limit : resourceLimits.entrySet()){
			
		}
		return true;
	}

	public Map<AppointmentType, Integer> appointmentTypeUsage(){
		Map<AppointmentType, Integer> typeUsage = new HashMap<AppointmentType, Integer>();
		for(Appointment appointment : delegate){
			AppointmentType type = appointment.getAppointmentType();
			Integer currentTypeCount = typeUsage.getOrDefault(type, 0);
			typeUsage.put(type, currentTypeCount + 1);
		}
		return typeUsage;
	}
	
 	public BigDecimal value(){
		BigDecimal value = BigDecimal.ZERO;
		for(Appointment appointment : delegate){
			value = value.add(appointment.getValue());
		}
		return value;
	}

	public LocalDateTime earliestTime() {
		return delegate.stream().map(a -> a.getResourceUsage().earliestUsage())
				.min((time1, time2) -> time1.compareTo(time2)).orElse(null);
	}

	public LocalDateTime latestTime() {
		return delegate.stream().map(a -> a.getResourceUsage().latestUsage())
				.max((time1, time2) -> time1.compareTo(time2)).orElse(null);
	}

}
