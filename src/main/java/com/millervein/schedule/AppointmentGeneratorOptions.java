package com.millervein.schedule;

import java.time.LocalTime;

public class AppointmentGeneratorOptions {
	public LocalTime startTime = LocalTime.of(8, 0);
	public LocalTime endTime = LocalTime.of(17, 0);
	public Integer physicianLimit = 1;
}
