package com.millervein.schedule;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ForwardingSet;

public class AppointmentTypeSet extends ForwardingSet<AppointmentType> {
	private final Set<AppointmentType> delegate;

	public static AppointmentTypeSet create() {
		return new AppointmentTypeSet();
	}

	public static AppointmentTypeSet create(Collection<? extends AppointmentType> c) {
		return new AppointmentTypeSet(c);
	}

	private AppointmentTypeSet() {
		this.delegate = new HashSet<AppointmentType>();
	}

	private AppointmentTypeSet(Collection<? extends AppointmentType> c) {
		this.delegate = new HashSet<AppointmentType>(c);
	}

	@Override
	protected Set<AppointmentType> delegate() {
		return delegate;
	}

	/**
	 * Returns maximumConcurrentAppointmentCount for a list of appointment
	 * types.
	 * 
	 * @param time
	 * @param appointmentTypes
	 * @param staffLimits
	 * @return
	 */
	public Map<AppointmentType, Integer> maxConcurrentAppointmentCountTypeMap(ResourceTypeLimits resourceLimits) {
		Map<AppointmentType, Integer> maxConcurrent = new HashMap<AppointmentType, Integer>();
		for (AppointmentType appointmentType : delegate) {
			maxConcurrent.put(appointmentType, appointmentType.maximumConcurrentAppointmentCount(resourceLimits));
		}
		return maxConcurrent;
	}

}
