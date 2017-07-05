package com.millervein.schedule.domain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "create")
@EqualsAndHashCode(of = { "appointmentType" })
public class AppointmentTypeDemand {
	@NonNull
	AppointmentType appointmentType;
	@NonNull
	Integer demand;
}
