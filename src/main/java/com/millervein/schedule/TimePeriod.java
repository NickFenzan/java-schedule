package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimePeriod {
	private LocalDateTime start;
	private LocalDateTime end;
	private Duration duration;

	public TimePeriod(LocalDateTime start, LocalDateTime end) {
		super();
		this.start = start;
		this.end = end;
		this.duration = Duration.between(start, end);
	}

	public TimePeriod(LocalDateTime start, Duration duration) {
		super();
		this.start = start;
		this.duration = duration;
		this.end = start.plus(duration);
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public Duration getDuration() {
		return duration;
	}

	public boolean startsBefore(TimePeriod other) {
		return this.start.isBefore(other.start);
	}

	public boolean endsAfter(TimePeriod other) {
		return this.end.isAfter(other.end);
	}

	public boolean overlaps(TimePeriod other) {
		return !((other.end.isBefore(this.start) || other.end.isEqual(this.start))
				|| (other.start.isAfter(this.end) || other.start.isEqual(this.end)));
	}
	
	public boolean includes(LocalDateTime time){
		return (time.isEqual(start) || time.isAfter(start)) && time.isBefore(end);
	}

	@Override
	public String toString() {
		return "TimePeriod [start=" + start + ", end=" + end + ", duration=" + duration + "]";
	}

}
