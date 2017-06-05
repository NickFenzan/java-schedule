package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentGenerator {
	private AppointmentGeneratorOptions options;
	private List<AppointmentType> appointmentTypes;

	public AppointmentGenerator(List<AppointmentType> appointmentTypes) {
		this(appointmentTypes, new AppointmentGeneratorOptions());
	}

	public AppointmentGenerator(List<AppointmentType> appointmentTypes, AppointmentGeneratorOptions options) {
		this.appointmentTypes = appointmentTypes;
		this.options = options;
	}

	public List<Appointment> generateAppointments(AppointmentType type) {
		Duration iterDuration = type.getSmallestResourceDuration();
		List<Appointment> appointments = new ArrayList<Appointment>();
		for (LocalDateTime iterTime = LocalDateTime.now().with(options.startTime); iterTime
				.isBefore(LocalDateTime.now().with(options.endTime)); iterTime = iterTime.plus(iterDuration)) {
			appointments.add(new Appointment(type, iterTime, new Patient()));
		}
		return appointments;
	}

	public List<AppointmentList> generateAppointments() throws Exception {
		Duration iterDuration = appointmentTypes.stream().map(type -> type.getSmallestResourceDuration())
				.reduce((smallest, current) -> current.compareTo(smallest) < 0 ? current : smallest)
				.orElseThrow(() -> new Exception("Can't generate appointments with no resource usage"));
		List<AppointmentList> solutionSets = new ArrayList<AppointmentList>();

		//Each time in range
		for (LocalDateTime iterTime = LocalDateTime.now().with(options.startTime); iterTime
				.isBefore(LocalDateTime.now().with(options.endTime)); iterTime = iterTime.plus(iterDuration)) {
			//Each Appointment Type
			for (AppointmentType apptType : this.appointmentTypes) {
				AppointmentList slotAppointments = new AppointmentList();
				while (slotAppointments.getConcurrencyByStaffType("Physician") < this.options.physicianLimit && slotAppointments.getConcurrencyByStaffType("Nurse") < this.options.physicianLimit) {
					slotAppointments.add(new Appointment(apptType, iterTime, new Patient()));
				}
				List<AppointmentList> toAdd = new ArrayList<AppointmentList>();
				for(AppointmentList solutionSet : solutionSets){
					AppointmentList combinedSet = new AppointmentList(solutionSet);
//					System.out.println(combinedSet);
					combinedSet.addAll(slotAppointments);
//					System.out.println(combinedSet);
//					System.out.println(combinedSet.getConcurrencyByStaffType("Physician"));
					if(combinedSet.getConcurrencyByStaffType("Physician") <= this.options.physicianLimit && slotAppointments.getConcurrencyByStaffType("Nurse") <= this.options.physicianLimit){
						toAdd.add(combinedSet);
					}
				}
				toAdd.add(slotAppointments);
				solutionSets.addAll(toAdd);
			}
		}
		
		Integer mostAppointments = solutionSets.stream().map(appts -> appts.size()).reduce(0,(max,current) -> (current > max) ? current : max);
		System.out.println(mostAppointments);
		return solutionSets.stream().filter(apptList -> apptList.size() < mostAppointments).collect(Collectors.toList());
	}
}
