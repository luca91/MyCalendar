package com.example.auxiliary;

import java.util.StringTokenizer;

/**
 *This class manage the information about an event. 
 * @author Luca Bellettati
 *
 */
public class Event extends AppItem {
	
	/**
	 * String constant for the event name.
	 */
	public static final String NAME = "com.example.mycalendar.NAME";
	
	/**
	 * String constant for the event start date.
	 */
	public static final String S_DATE = "com.example.mycalendar.S_DATE";
	
	/**
	 * String constant for the event end date.
	 */
	public static final String E_DATE = "com.example.mycalendar.E_DATE";
	
	/**
	 * String constant for the event start time.
	 */
	public static final String S_TIME = "com.example.mycalendar.S_TIME";
	
	/**
	 * String constant for the event end time.
	 */
	public static final String E_TIME = "com.example.mycalendar.E_TIME";
	
	/**
	 * String constant for the event calendar.
	 */
	public static final String CALENDAR = "com.example.mycalendar.CALENDAR";
	private String name, calendar;
	private String startDate, endDate, startTime, endTime;
	
	/**
	 * 
	 * @param name event name
	 * @param startDate event start date
	 * @param endDate event end date
	 * @param startTime event start time
	 * @param endTime event end time
	 * @param calendar event calendar
	 */
	public Event(String name, String startDate, String endDate, String startTime, String endTime, String calendar) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.calendar = calendar;
		super.setName(name);
	}
	
	/**
	 * It return the value of the name of this event.
	 * @return String
	 */
	public String getName(){
		return this.name;
	}
	
	public String getStartDate(){
		return this.startDate;
	}
	
	public String getEndDate(){
		return this.endDate;
	}
	
	public String getStartTime(){
		return this.startTime;
	}
	
	public String getEndTime(){
		return this.endTime;
	}
	
	public String getCalendar(){
		return this.calendar;
	}
	
	public int[] getDateToken(String tag){
		StringTokenizer tokenizer; 
		if(tag.equals("start"))
			tokenizer = new StringTokenizer(startDate, "/");
		else
			tokenizer = new StringTokenizer(endDate, "/");
		int[] dateToken = new int[3];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		dateToken[2] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
	
	public int[] getTimeToken(String tag){
		StringTokenizer tokenizer;
		if(tag.equals("start"))
			tokenizer = new StringTokenizer(startTime, ":");
		else
			tokenizer = new StringTokenizer(endTime, ":");
		int[] dateToken = new int[2];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
	
	public String toString(){
		return name + "(" + calendar + ")" + "\n" + "from " + startDate + " at " + startTime + "\n" + "to " + endDate + " to " + endTime + "\n";
	}
	
	public int[] getDateTokenAsDB(String tag){
		StringTokenizer tokenizer; 
		if(tag.equals("start"))
			tokenizer = new StringTokenizer(startDate, "-");
		else
			tokenizer = new StringTokenizer(endDate, "-");
		int[] dateToken = new int[3];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		dateToken[2] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
}
