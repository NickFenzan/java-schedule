package com.millervein.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public interface HasTimePeriod {
	TimePeriod getTimePeriod();
	
	static BinaryOperator<? extends HasTimePeriod> earliest(){
		return (a,b) -> a.getTimePeriod().startsBefore(b.getTimePeriod()) ? a : b;
	}
	
	static BinaryOperator<? extends HasTimePeriod> latest(){
		return (a,b) -> a.getTimePeriod().endsAfter(b.getTimePeriod()) ? a : b;
	}
	
	public static Predicate<? extends HasTimePeriod> includesTime(LocalDateTime time){
		return p -> p.getTimePeriod().includes(time);
	}
	
	default LocalDateTime getStart(){
		return getTimePeriod().getStart();
	}
	default LocalDateTime getEnd(){
		return getTimePeriod().getEnd();
	}
	default Duration getDuration(){
		return getTimePeriod().getDuration();
	}
	default boolean startsBefore(HasTimePeriod other){
		return getTimePeriod().startsBefore(other.getTimePeriod());
	}
	default boolean startsBefore(TimePeriod other){
		return getTimePeriod().startsBefore(other);
	}
	default boolean endsAfter(TimePeriod other){
		return getTimePeriod().endsAfter(other);
	}
	default boolean endsAfter(HasTimePeriod other){
		return getTimePeriod().endsAfter(other.getTimePeriod());
	}
	default boolean overlaps(TimePeriod other){
		return getTimePeriod().overlaps(other);
	}
	default boolean overlaps(HasTimePeriod other){
		return getTimePeriod().overlaps(other.getTimePeriod());
	}
	default boolean includes(LocalDateTime time){
		return getTimePeriod().includes(time);
	}
}
