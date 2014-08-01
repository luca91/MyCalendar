package com.mycalendar.components;

public class Reminder {
	
	private int id;
	private int eventID;
	private String reminderDateTime;
	private int remTimeChosen;
	
	public Reminder(String dateTime, int eventID){
		reminderDateTime = dateTime;
		this.eventID = eventID;
	}
	
	public void setDateTime(String dateTime){
		reminderDateTime = dateTime;
	}
	
	public void setEventID(int id){
		eventID = id;
	}
	
	public String getDateTime(){
		return reminderDateTime;
	}
	
	public int getEventID(){
		return eventID;
	}
	
	public int getID(){
		return id;
	}
	
	public void setRemTimeChosen(int time){
		remTimeChosen = time;
	}
	
	public int getRemTimChosen(){
		return remTimeChosen;
	}
	
	public String getReminderTextFromTimeChosen(){
		switch(remTimeChosen){
		case 15:
			return "15 min";
		case 30:
			return "30 min";
		case 60:
			return "1 hour";
		case 1440:
			return "1 day";
		case 10080:
			return "1 week";
		case 0:
			return "No reminder";
		}
		return null;
	}
	
	public String toString(){
		return reminderDateTime + " " + remTimeChosen;
	}

}
