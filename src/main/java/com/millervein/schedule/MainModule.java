package com.millervein.schedule;

import java.util.Set;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.AppointmentTypeConversion;
import com.millervein.schedule.domain.ResourceLimit;
import com.millervein.schedule.domain.ResourceType;
import com.millervein.schedule.seed.AppointmentTypeConversionSeeder;
import com.millervein.schedule.seed.AppointmentTypeSeeder;
import com.millervein.schedule.seed.ResourceLimitSeeder;
import com.millervein.schedule.seed.ResourceTypeSeeder;

public class MainModule extends AbstractModule {

	@Override
	protected void configure() {

	}

//	@Provides
//	DBI provideDBI() {
//		return new DBI("jdbc:mariadb://10.1.1.133/schedule","schedule","schedule");
//	}

	@Provides
	public Set<ResourceType> resourceTypes(){
		return ResourceTypeSeeder.create();
	}
	
	@Provides
	public Set<AppointmentType> appointmentTypes(){
		return AppointmentTypeSeeder.create();
	}
	
	@Provides
	public Set<ResourceLimit> resourceLimits(){
		return ResourceLimitSeeder.create();
	}
	
	@Provides
	public Set<AppointmentTypeConversion> appointmentTypeDemand(){
		return AppointmentTypeConversionSeeder.create();
	}
}
