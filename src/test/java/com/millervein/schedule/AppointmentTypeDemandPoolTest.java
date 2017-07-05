package com.millervein.schedule;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.millervein.schedule.domain.collections.AppointmentTypeDemandPool;

public class AppointmentTypeDemandPoolTest {
	@Test
	public void mergeTest() {
		AppointmentTypeDemandPool pool1 = AppointmentTypeDemandPool.create();
		pool1.put("new patient", 1);
		pool1.put("free consult", 2);

		AppointmentTypeDemandPool pool2 = AppointmentTypeDemandPool.create();
		pool2.put("new patient", 2);
		pool2.put("free consult", 1);

		AppointmentTypeDemandPool pool3 = AppointmentTypeDemandPool.create();
		pool3.put("new patient", 3);
		pool3.put("free consult", 3);

		assertTrue(pool3.equals(pool1.merge(pool2)));

	}
}
