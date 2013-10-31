package com.example.calendar;

public class Month {
	
	private int numberOfWeekCovered;
	private int numberOfDays;
	private int numberOfFirstDayInWeek;
	private int numberOfLastDayInWeek;
	private int numberOfMonthInYear;
	private String name;
	
	public Month(String name, int numberOfMonthInYear){
		this.name = name;
		this.numberOfMonthInYear = numberOfMonthInYear;
	}

}
