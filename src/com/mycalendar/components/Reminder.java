package com.mycalendar.components;

public class Reminder {
	
	private int id;
	private int eventID;
	private String reminderDate;
	private String reminderTime;
	
	public Reminder(String date, String time, int eventID){
		reminderDate = date;
		reminderTime = time;
		this.eventID = eventID;
	}
	
	public void setDate(String date){
		reminderDate = date;
	}
	
	public void setTime(String time){
		reminderTime = time;
	}
	
	public void setEventID(int id){
		eventID = id;
	}
	
	public String getDate(){
		return reminderDate;
	}
	
	public String getTime(){
		return reminderTime;
	}
	
	public int getEventID(){
		return eventID;
	}
	
	public int getID(){
		return id;
	}

}
