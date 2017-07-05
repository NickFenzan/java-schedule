package com.millervein.schedule.domain;

import static com.google.common.base.Preconditions.checkArgument;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.NonNull;
import lombok.Value;

/**
 * Represents bounded period of time
 * 
 * @author nick
 *
 */
@Value
public class TimePeriod {
	@NonNull
	LocalDateTime start;
	@NonNull
	LocalDateTime end;
	@NonNull
	Duration duration;

	private TimePeriod(LocalDateTime start, LocalDateTime end) {
		checkArgument(end.isAfter(start));
		this.start = start;
		this.end = end;
		this.duration = Duration.between(start, end);
	}

	private TimePeriod(LocalDateTime start, Duration duration) {
		this.start = start;
		this.duration = duration;
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

}
