package com.millervein.schedule;

import java.util.UUID;

public class Patient {
	private UUID id;
	private String name;
	public Patient() {
		super();
		this.id = UUID.randomUUID();
		this.name = "Patient " + this.id.toString();
	}
	
	public String getName(){
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
