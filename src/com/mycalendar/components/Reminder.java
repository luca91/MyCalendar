package com.mycalendar.components;

public class Reminder {
	
	private int id;
	private int eventID;
	private String reminderDate;
	private String reminderTime;
	private int remTimeChosen;
	
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
	
	public String dateAndTimeToString(){
		return reminderDate + " " + reminderTime;
	}
	public void setRemTimeChosen(int time){
		remTimeChosen = time;
	}
	
	public int getRemTimChosen(){
		return remTimeChosen;
	}

}
