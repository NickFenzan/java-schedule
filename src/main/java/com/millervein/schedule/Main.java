package com.millervein.schedule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.millervein.schedule.services.ScheduleGenerator;

public class Main {
	/**
	 * Creates preset time range
	 * 
	 * @return
	 */
	

	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(new MainModule());
		ScheduleGenerator scheduleGenerator = injector.getInstance(ScheduleGenerator.class);
		scheduleGenerator.generateDays(30);
	}

}
