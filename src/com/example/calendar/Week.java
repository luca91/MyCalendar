package com.example.calendar;

import java.util.ArrayList;

public class Week {
	
	private ArrayList<Day> week;
	private int[] activeDaysInWeek;
	
	public Week(int...activeDaysInWeek){
		this.activeDaysInWeek = new int[activeDaysInWeek.length];
		for (int i = 0; i < activeDaysInWeek.length; i++)
			this.activeDaysInWeek[i] = activeDaysInWeek[i];
	}
	
	public Week(){	}
	
	

}
