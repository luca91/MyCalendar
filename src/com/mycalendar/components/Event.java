package com.mycalendar.components;

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
	public static final String ID = "com.example.mycalendar.ID";
	public static final String ALL_DAY = "com.example.mycalendr.ALL_DAY";
	public static final String NOTES = "com.example.mycalendr.NOTES";
	public static final String FLEX = "com.example.myacalendar.FLEX";
	private String name;
	private String startDate, endDate, startTime, endTime;
	private int id;
	private int allDay;
	private String notes;
	private String calendar;
	private int flexibilityRange;
	private String flexPref;
	private Reminder reminder;
	
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
		super.setName(name);
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.calendar = calendar;
		id = -1;
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
	
	public void setNotes(String notes){
		this.notes = notes;
	}
	
	public String getNotes(){
		return notes;
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
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setAllDay(int allDay){
		this.allDay = allDay;
	}
	
	public int getAllDay(){
		return allDay;
	}
	
	public void setReminder(Reminder r){
		this.reminder = r;
	}
	
	public Reminder getReminder(){
		return reminder;
	}
	
	public void setFlexibilityRange(int fr){
		flexibilityRange = fr;
	}
	
	public int getFlexibilityRange(){
		return flexibilityRange;
	}
	
	public void setStartDate(String date){
		startDate = date;
	}
	
	public void setEndDate(String date){
		endDate = date;
	}
	
	public void setStartTime(String time){
		startTime = time;
	}
	
	public void setEndTime(String time){
		endTime = time;
	}
	
	public void setFlexPref(String pref){
		flexPref = pref;
	}
	
	public String getFlexPref(){
		return flexPref;
	}
	
	public void setCalendar(String c){
		calendar = c;
	}
}