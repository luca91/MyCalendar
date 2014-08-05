package com.mycalendar.components;

public class Reminder {
	
	public final static String REMINDER_INTENT = "com.mycalendar.components.REMINDER_INTENT";
	
	private int id;
	private int uriID;
	private int eventID;
	private int eventIDUri;
	private int remTimeChosen;
	
//	public Reminder(int uriID, int eventIDUri, int timeChosen){
//		this.uriID = uriID;
//		this.eventIDUri = eventIDUri;
//		remTimeChosen = timeChosen;
//	}
	
	public Reminder(int eventID, int timeChosen){
		this.eventID = eventID;
		remTimeChosen = timeChosen;
	}
	
	public void setEventID(int id){
		eventID = id;
	}
	
	public int getEventID(){
		return eventID;
	}
	
	public void setUriID(int id){
		uriID = id;
	}
	
	public int getUriID(){
		return uriID;
	}
	
	public void setEventIDUri(int id){
		eventIDUri = id;
	}
	
	public int getEventIDUri(){
		return eventIDUri;
	}
	
	public void setRemTimeChosen(int time){
		remTimeChosen = time;
	}
	
	public int getRemTimChosen(){
		return remTimeChosen;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
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
}
