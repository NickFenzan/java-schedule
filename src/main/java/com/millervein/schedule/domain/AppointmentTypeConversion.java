package com.millervein.schedule.domain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor="create")
@EqualsAndHashCode(of = { "from", "to" })
public class AppointmentTypeConversion {
	@NonNull
	String from;
	@NonNull
	String to;
	@NonNull
	Double conversionRate;
}
