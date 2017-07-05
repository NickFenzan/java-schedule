package com.millervein.schedule.seed;

import java.util.Set;

import com.google.common.collect.Sets;
import com.millervein.schedule.domain.AppointmentTypeConversion;

public class AppointmentTypeConversionSeeder {
	public static Set<AppointmentTypeConversion> create() {
		return Sets.newHashSet(
				AppointmentTypeConversion.create("New Patient", "Procedure", .51),
				AppointmentTypeConversion.create("New Patient", "VeinErase Legs", .04),
				//
				AppointmentTypeConversion.create("Free Evaluation", "New Patient", .40),
				AppointmentTypeConversion.create("Free Evaluation", "VeinErase Legs", .11),
				//
				AppointmentTypeConversion.create("Procedure", "1 Week Follow Up", .93),
				//
				AppointmentTypeConversion.create("1 Week Follow Up", "Procedure", .44),
				AppointmentTypeConversion.create("1 Week Follow Up", "3 Month Follow Up", .28),
				AppointmentTypeConversion.create("1 Week Follow Up", "6 Month Follow Up", .02),
				AppointmentTypeConversion.create("1 Week Follow Up", "Yearly Follow Up", .01),
				AppointmentTypeConversion.create("1 Week Follow Up", "VeinErase Legs", .03),
				//
				AppointmentTypeConversion.create("3 Month Follow Up", "Procedure", .09),
				AppointmentTypeConversion.create("3 Month Follow Up", "3 Month Follow Up", .06),
				AppointmentTypeConversion.create("3 Month Follow Up", "6 Month Follow Up", .43),
				AppointmentTypeConversion.create("3 Month Follow Up", "Yearly Follow Up", .04),
				AppointmentTypeConversion.create("3 Month Follow Up", "VeinErase Legs", .03),
				//
				AppointmentTypeConversion.create("6 Month Follow Up", "Procedure", .06),
				AppointmentTypeConversion.create("6 Month Follow Up", "6 Month Follow Up", .05),
				AppointmentTypeConversion.create("6 Month Follow Up", "Yearly Follow Up", .41),
				AppointmentTypeConversion.create("6 Month Follow Up", "VeinErase Legs", .04),
				//
				AppointmentTypeConversion.create("Yearly Follow Up", "Procedure", .09),
				AppointmentTypeConversion.create("Yearly Follow Up", "Yearly Follow Up", .41),
				AppointmentTypeConversion.create("Yearly Follow Up", "VeinErase Legs", .04)
				);
	}
}
