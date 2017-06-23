package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.BinaryOperator;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents bounded period of time
 * 
 * @author nick
 *
 */
public class TimePeriod {
	private LocalDateTime start;
	private LocalDateTime end;
	private Duration duration;

	private TimePeriod(LocalDateTime start, LocalDateTime end) {
		checkNotNull(start);
		checkNotNull(end);
		checkArgument(end.isAfter(start));
		this.start = start;
		this.end = end;
		this.duration = Duration.between(start, end);
	}

	private TimePeriod(LocalDateTime start, Duration duration) {
		this.start = checkNotNull(start);
		this.duration = checkNotNull(duration);
		this.end = start.plus(duration);
	}

	/**
	 * Produces a time period between specified start and end.
	 * 
	 * @param start
	 * @param end
	 */
	public static TimePeriod withRange(LocalDateTime start, LocalDateTime end) {
		return new TimePeriod(start, end);
	}

	/**
	 * Produces a time period by adding time to the specified start
	 * 
	 * @param start
	 * @param duration
	 */
	public static TimePeriod withDuration(LocalDateTime start, Duration duration) {
		return new TimePeriod(start, duration);
	}
	
	public static BinaryOperator<TimePeriod> earliest(){
		return (a,b)-> a.startsBefore(b) ? a : b;
	}
	
	public static BinaryOperator<TimePeriod> latest(){
		return (a,b)-> a.endsAfter(b) ? a : b;
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

	public boolean includes(LocalDateTime time) {
		return (time.isEqual(start) || time.isAfter(start)) && time.isBefore(end);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimePeriod other = (TimePeriod) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimePeriod [start=" + start + ", end=" + end + ", duration=" + duration + "]";
	}

}
