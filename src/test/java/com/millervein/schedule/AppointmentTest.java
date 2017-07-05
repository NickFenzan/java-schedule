package com.millervein.schedule;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.millervein.schedule.domain.Appointment;
import com.millervein.schedule.domain.AppointmentType;
import com.millervein.schedule.domain.ResourceType;
import com.millervein.schedule.domain.ResourceUsageTemplate;

public class AppointmentTest {

	@Test
	public void equalityTest() {
		AppointmentType dummyAppointmentType = AppointmentType.create("test", Lists.newArrayList(ResourceUsageTemplate
				.create(ResourceType.create("test", "test"), Duration.ofMinutes(15), Duration.ZERO)), BigDecimal.ZERO);
		LocalDateTime now = LocalDateTime.now();
		Appointment appointment1 = Appointment.create(dummyAppointmentType, now);
		Appointment appointment2 = Appointment.create(dummyAppointmentType, now);
		assertTrue(appointment1.equals(appointment2));
	}
}
