package com.millervein.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class AppointmentList extends ArrayList<Appointment> {

	public AppointmentList() {
		super();
	}

	public AppointmentList(Collection<? extends Appointment> c) {
		super(c);
	}

	public Integer getConcurrencyByStaffType(String type) {
		ResourceUsageList staffTypeUsage = this.stream().map(appt -> appt.getStaffUsage().getStaffTypeUsageList(type))
				.reduce(new ResourceUsageList(), (allList, apptList) -> {
					allList.addAll(apptList);
					return allList;
				});
		return staffTypeUsage.getStaffTypeConcurrency(type);
	}

}
