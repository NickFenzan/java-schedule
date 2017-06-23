package com.millervein.schedule;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ForwardingMap;

public class AppointmentTypeDemands extends ForwardingMap<AppointmentType, Integer> {
	private final Map<AppointmentType, Integer> delegate;

	@Override
	protected Map<AppointmentType, Integer> delegate() {
		return this.delegate;
	}

	private AppointmentTypeDemands() {
		this.delegate = new HashMap<AppointmentType, Integer>();
	}

	private AppointmentTypeDemands(Map<? extends AppointmentType, ? extends Integer> m) {
		this.delegate = new HashMap<AppointmentType, Integer>(m);
	}

	public static AppointmentTypeDemands create() {
		return new AppointmentTypeDemands();
	}

	public static AppointmentTypeDemands create(Map<? extends AppointmentType, ? extends Integer> m) {
		return new AppointmentTypeDemands(m);
	}

}
