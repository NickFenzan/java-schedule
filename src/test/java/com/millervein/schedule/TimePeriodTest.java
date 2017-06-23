package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TimePeriodTest {
	@Test(expected = NullPointerException.class)
	public void nullStartTest() {
		TimePeriod.withRange(null, LocalDateTime.now());
	}

	@Test(expected = IllegalArgumentException.class)
	public void endAfterStartTest() {
		TimePeriod.withRange(LocalDateTime.now().plusMinutes(15), LocalDateTime.now());
	}

	@Test
	public void startsBeforeTest() {
		TimePeriod earlier = TimePeriod.withDuration(LocalDateTime.now(), Duration.ofMinutes(15));
		TimePeriod later = TimePeriod.withDuration(LocalDateTime.now().plusMinutes(15), Duration.ofMinutes(15));
		assertTrue(earlier.startsBefore(later));
		assertFalse(later.startsBefore(earlier));
	}

	@Test
	public void endsAfterTest() {
		TimePeriod earlier = TimePeriod.withDuration(LocalDateTime.now(), Duration.ofMinutes(15));
		TimePeriod later = TimePeriod.withDuration(LocalDateTime.now().plusMinutes(15), Duration.ofMinutes(15));
		assertFalse(earlier.endsAfter(later));
		assertTrue(later.endsAfter(earlier));
	}

	@Test
	public void overlapsTest() {
		// 8:00 - 8:30
		TimePeriod start = TimePeriod.withDuration(LocalDateTime.now(), Duration.ofMinutes(30));
		// 8:15 - 8:45
		TimePeriod middle = TimePeriod.withDuration(LocalDateTime.now().plusMinutes(15), Duration.ofMinutes(30));
		// 8:30 - 9:00
		TimePeriod end = TimePeriod.withDuration(LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(15));
		// 7:00 - 10:00
		TimePeriod bigGuy = TimePeriod.withDuration(LocalDateTime.now().minusMinutes(60), Duration.ofMinutes(180));
		// 8:05 - 8:10
		TimePeriod littleGuy = TimePeriod.withDuration(LocalDateTime.now().plusMinutes(5), Duration.ofMinutes(5));

		assertTrue(start.overlaps(start));
		assertTrue(start.overlaps(middle));
		assertFalse(start.overlaps(end));
		assertTrue(start.overlaps(bigGuy));
		assertTrue(start.overlaps(littleGuy));
	}
	
	@Test
	public void includesTest() {
		TimePeriod period = TimePeriod.withDuration(LocalDateTime.now(), Duration.ofMinutes(30));
		LocalDateTime before = LocalDateTime.now().minusMinutes(15);
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime middle = LocalDateTime.now().plusMinutes(15);
		LocalDateTime end = LocalDateTime.now().plusMinutes(30);
		LocalDateTime after = LocalDateTime.now().plusMinutes(45);
		
		assertFalse(period.includes(before));
		assertTrue(period.includes(start));
		assertTrue(period.includes(middle));
		assertFalse(period.includes(end));
		assertFalse(period.includes(after));
	}

}
