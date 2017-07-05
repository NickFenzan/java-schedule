package com.millervein.schedule.domain.collections;

import java.util.Map;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;

public class AppointmentTypeDemandPool extends ForwardingMap<String, Integer> {
	private Map<String, Integer> delegate;

	@Override
	protected Map<String, Integer> delegate() {
		return delegate;
	}

	private AppointmentTypeDemandPool() {
		delegate = Maps.newHashMap();
	}

	private AppointmentTypeDemandPool(Map<? extends String, ? extends Integer> map) {
		delegate = Maps.newHashMap(map);
	}

	public static AppointmentTypeDemandPool create() {
		return new AppointmentTypeDemandPool();
	}

	public static AppointmentTypeDemandPool create(Map<? extends String, ? extends Integer> map) {
		return new AppointmentTypeDemandPool(map);
	}

	public AppointmentTypeDemandPool merge(AppointmentTypeDemandPool other) {
		AppointmentTypeDemandPool newPool = AppointmentTypeDemandPool.create(this.delegate);
		other.forEach((k, v) -> newPool.merge(k, v, (a, b) -> a + b));
		return newPool;
	}

	public void probabilityIncrement(String appointmentType, Double probability) {
		if (probability - Math.random() > 0) {
			delegate.merge(appointmentType, 1, (a, b) -> a + b);
		}
	}

	public void reduceCount(String appointmentType, Integer count) {
		Integer currentCount = delegate.getOrDefault(appointmentType, 0);
		Integer newCount = currentCount - count;
		delegate.put(appointmentType, (newCount > 0) ? newCount : 0);
	}

}
