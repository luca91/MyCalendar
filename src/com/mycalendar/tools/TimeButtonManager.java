package com.mycalendar.tools;

import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.FragmentManager;
import android.content.Context;
import android.widget.Button;

import com.mycalendar.activity.EventEditor;
import com.mycalendar.time_and_date.DatePickerFragment;
import com.mycalendar.time_and_date.TimePickerFragment;

public class TimeButtonManager {
	
	private String currentStartDate;
	private String currentStartTime;
	private String currentEndDate;
	private String currentEndTime;
	private Button startDate;
	private Button startTime;
	private Button endDate;
	private Button endTime;
	private boolean dateAlreadySet = false;
	private boolean timeAlreadySet = false;
	private FragmentManager manager;
	@SuppressWarnings("unused")
	private Context c;
	private boolean isFinder = false;
	private int sDay;
	private int sMonth;
	private int sYear;
	private int sHours;
	private int sMinutes;
	private int eDay;
	private int eMonth;
	private int eYear;
	private int eHours;
	private int eMinutes;
	private int[] endDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private Calendar startCalendar;
	private Calendar endCalendar;
	private Calendar current;
	private boolean isAllDayChecked;
	
	public TimeButtonManager(FragmentManager fm, Context ctx){
		manager = fm;
		c = ctx;
	}
	
	public void setButtons(Button sDate, Button eDate, Button sTime, Button eTime){
		startDate = sDate;
		endDate = eDate;
		startTime = sTime;
		endTime = eTime;
	}
	
//	public void setStartDateValues(int day, int month, int year){
	public void setStartDateValues(){
		sDay = startCalendar.get(Calendar.DAY_OF_MONTH);
		sMonth = startCalendar.get(Calendar.MONTH)+1;
		sYear = startCalendar.get(Calendar.YEAR);
		setDate("start");
	}
	
//	public void setEndDateValues(int day, int month, int year){
	public void setEndDateValues(){
		eDay = endCalendar.get(Calendar.DAY_OF_MONTH);
		eMonth = endCalendar.get(Calendar.MONTH)+1;
		eYear = endCalendar.get(Calendar.YEAR);
		setDate("end");
	}
	
	public void setStartTimeValues(){
		if(!timeAlreadySet && !EventEditor.getIsModify()){
			startCalendar.add(Calendar.HOUR_OF_DAY, 1);
			sHours = startCalendar.get(Calendar.HOUR_OF_DAY);
			sMinutes = 0;
		}
		else{
			sHours = startCalendar.get(Calendar.HOUR_OF_DAY); 
			sMinutes = startCalendar.get(Calendar.MINUTE);
		}
		setTime("start");
	}
	
	public void setEndTimeValues(){
		if(!timeAlreadySet && !EventEditor.getIsModify()){
			endCalendar.add(Calendar.HOUR_OF_DAY, 2);
			eHours = endCalendar.get(Calendar.HOUR_OF_DAY);
			eMinutes = 0;
		}
		else{
			eHours = endCalendar.get(Calendar.HOUR_OF_DAY);
			eMinutes = endCalendar.get(Calendar.MINUTE);
		}
		setTime("end");
	}

	/**
	 * It transform the three components of the date (year, month and day) into a string with the form "dd/mm/yyyy" and stores it in the respective String variable, depending on the tag value.
	 * @param year the year to set
	 * @param month the month to set
	 * @param day the day to set
	 * @param tag which date to set
	 */
	public void setDate(String tag){
		if(tag.equals("start"))
			currentStartDate = formatDate("start");
		else
			currentEndDate = formatDate("end");
		
	}
	
	public void setDate(String date, String tag){
		if(tag.equals("start"))
			currentStartDate = date;
		else
			currentEndDate = date;
	}
	
	/**
	 * It updates the text shown in the date buttons, depending on the tag value.
	 * @param text the text to set
	 * @param tag which date button to set
	 */
	public void setDateButtonText(String tag){
		if(tag.equals("start"))
			startDate.setText(currentStartDate);
		else
			endDate.setText(currentEndDate);
	}
	
	/**
	 * It transform the two components of the time (hours and minutes) into a string of the form "hh:mm" and stores it in the respective String variable, depending on the tag value.
	 * @param hours the hours to set 
	 * @param minutes the minute to set
	 * @param tag which time to set
	 */
	public void setTime(String tag){
		if(tag.equals("start"))
			currentStartTime = formatTime(tag);
		else
			currentEndTime = formatTime(tag);
	}
	
	public void setTime(String time, String tag){
		if(tag.equals("start"))
			currentStartTime = time;
		else
			currentEndTime = time;
	}
	
	/**
	 * It updates the text shown in the time buttons, depending on the tag value.
	 * @param text the text to set
	 * @param tag which time button to set
	 */
	public void setTimeButtonText(String tag){
		if(tag.equals("start"))
			startTime.setText(currentStartTime);
		else
			endTime.setText(currentEndTime);
	}
	
	/**
	 * Create and show the picker for the start date.
	 * @param v the current view
	 */
	public void showStartDatePicker(){
		DatePickerFragment date = new DatePickerFragment();
		date.setButtonManager(this);
		date.show(manager, "startDatePicker");
	}
	
	/**
	 * Create and show the picker for the start time.
	 * @param v the current view
	 */
	public void showStartTimePicker(){
		TimePickerFragment time = new TimePickerFragment();
		time.setButtonManager(this);
		time.show(manager, "startTimePicker");
	}
	
	/**
	 * Create and show the picker for the end date.
	 * @param v the current view
	 */
	public void showEndDatePicker(){
		DatePickerFragment date = new DatePickerFragment();
		date.setButtonManager(this);
		date.show(manager, "endDatePicker");
	}
	
	/**
	 * Create and show the picker for the end time.
	 * @param v the current view
	 */
	public void showEndTimePicker(){
		TimePickerFragment time = new TimePickerFragment();
		time.setButtonManager(this);
		time.show(manager, "endTimePicker");
	}
	
	/**
	 * It sets the flag which control the change of the date.
	 * @param value
	 */
	public void setDateAlreadySetFlag(boolean value){
		this.dateAlreadySet = value;
	}
	
	/**
	 * It get the flag which control the change of the date.
	 */
	public boolean getDateAlreadySet(){
		return this.dateAlreadySet;
	}
	
	/**
	 * It sets the flag which control the change of the time.
	 * @param value the value to set
	 */
	public void setTimeAlreadySetFlag(boolean value){
		this.timeAlreadySet = value;
	}
	
	/**
	 * It gets the flag which control the change of the time.
	 * @param value the value to set
	 */
	public boolean getTimeAlreadySet(){
		return this.timeAlreadySet;
	}
	
	/**
	 * The string value of the date is split in the three components, which are stored in an array.
	 * @param tag which time has to be parse into the array 
	 * @return int []
	 */
	public int[] getDateToken(String tag){
		StringTokenizer tokenizer; 
		if(tag.equals("start"))
			tokenizer = new StringTokenizer(currentStartDate, "/");
		else
			tokenizer = new StringTokenizer(currentEndDate, "/");
		int[] dateToken = new int[3];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		dateToken[2] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
	
	/**
	 * The string value of the time is splitted in the two components, which are stored in an array.
	 * @param tag which time has to be parse into the array
	 * @return int[]
	 */
	public int[] getTimeToken(String tag){
		StringTokenizer tokenizer;
		if(tag.equals("start"))
			tokenizer = new StringTokenizer(currentStartTime, ":");
		else
			tokenizer = new StringTokenizer(currentEndTime, ":");
		int[] dateToken = new int[2];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
	
	/**
	 * It get the current value of the start date.
	 * @return String
	 */
	public String getCurrentStartDate(){
		return this.currentStartDate;
	}
	
	/**
	 * It get the current value of the end date.
	 * @return String
	 */
	public String getCurrentEndDate(){
		return this.currentEndDate;
	}
	
	/**
	 * It get the current value of the start time.
	 * @return String
	 */
	public String getCurrentStartTime(){
		return this.currentStartTime;
	}
	
	/**
	 * It get the current value of the end time.
	 * @return String
	 */
	public String getCurrentEndTime(){
		return this.currentEndTime;
	}
	
	public String formatDate(String tag){
		String date;
		int day;
		int month;
		int year;
		if(tag.equals("start")){
			day = sDay;
			month = sMonth;
			year = sYear;
		}
		else{
			day = eDay;
			month = eMonth;
			year = eYear;
		}
		if(day<10 && month>=10){
			date = "0"+day+"/"+(month)+"/"+year;
		 }
		 else if(day<10 && month<10){
			 date = "0"+day+"/"+"0"+month+"/"+year;
		 }
		 else if(day>=10 && month<10){
			 date = day+"/"+"0"+month+"/"+year;
		 }
		 else{
			 date = day+"/"+month+"/"+year;
		 }
		return date;
	}
	
	public String formatTime(String tag){
		String time;
		int hours; 
		int minutes;
		if(tag.equals("start")){
			hours = sHours;
			minutes = sMinutes;
		}
		else{
			hours = eHours;
			minutes = eMinutes;
		}
		if(hours<10 && minutes>=10){
			time = "0"+hours+":"+minutes;
		}
		else if(hours >= 10 && minutes < 10){
			time = hours+":"+"0"+minutes;
		}
		else if(hours < 10 && minutes < 10){
			time = "0"+hours+":"+"0"+minutes;
		}
		else if(hours == 0 && minutes < 10){
			time = "00"+":"+"0"+minutes;
		}
		else if(hours == 0 && minutes >= 10){
			time = "00"+":"+"0"+minutes;
		}
		else if(hours < 10 && minutes == 10){
			time = "0"+hours+":"+"00";
		}
		else if(hours >= 0 && minutes >= 10){
			time = hours+":"+minutes;
		}
		else if(hours == 0 && minutes == 0){
			time = "00"+":"+"00";
		}
		else{
			time = hours+":"+minutes;
		}
		return time;
	}
	
	public void setDateAndTime(){
		setDate("start");
		setTime("start");
		setDate("end");
		setTime("end");
	}
	
	public void setButtonState(){
		setDateButtonText("start");
		setTimeButtonText("start");
		setDateButtonText("end");
		setTimeButtonText("end");
	}
	
	public void setIsFinder(boolean f){
		isFinder = f;
	}
	
	public  boolean getIsFinder(){
		return isFinder;
	}
	
	public boolean isEndDay(int day, int month){
		if(endDays[month] == day)
			return true;
		return false;
	}
	
	public void setStartCalendar(Calendar c){
		startCalendar = c;
		setStartDateValues();
		setStartTimeValues();
	}
	
	public void setEndCalendar(Calendar c){
		endCalendar = c;
		setEndDateValues();
		setEndTimeValues();
	}
	
	public void setCurrentCalendar(Calendar c){
		current = c;
	}
	
	public boolean checkDateStartValidity(){
		if(current.compareTo(startCalendar) <= 0)
			return true;
		return false;
	}
	
	public boolean checkDateEndValidity(){
		if(current.compareTo(endCalendar) <= 0 && endCalendar.compareTo(startCalendar) > 0)
			return true;
		return false;
	}
	
	public int checkCalendarPrecedence(){
		return startCalendar.compareTo(endCalendar);
	}
	
	public boolean validateSelectedDate(){
		current = Calendar.getInstance();
		if(!checkDateStartValidity() || !checkDateEndValidity())
			return false;
		return true;
	}
	
	public int getHours(String tag){
		if(tag.equals("start"))
			return sHours;
		else
			return eHours;
	}
	
	public int getMinutes(String tag){
		if(tag.equals("start"))
			return sMinutes;
		else
			return eMinutes;
	}
	
	public int getDay(String tag){
		if(tag.equals("start"))
			return sDay;
		else
			return eDay;
	}
	
	public int getMonth(String tag){
		if(tag.equals("start"))
			return sMonth;
		else
			return eMonth;
	}
	
	public int getYear(String tag){
		if(tag.equals("start"))
			return sYear;
		else
			return eYear;
	}
	
	public Calendar getCalendar(String tag){
		switch(tag){
		case "start":
			return startCalendar;
			
		case "end":
			return endCalendar;
			
		case "current":
			return current;
		}
		return null;
	}
	
	public String calendarToString(String tag){
		if(tag.equals("start"))
			return getCurrentStartDate() + " " + getCurrentStartTime();
		else if(tag.equals("end"))
			return getCurrentEndDate() + " " + getCurrentEndTime();
		else if(tag.equals("current"))
			return current.get(Calendar.DAY_OF_MONTH) + "/" + (current.get(Calendar.MONTH)+1) + "/" + current.get(Calendar.YEAR) + " " + current.get(Calendar.HOUR_OF_DAY) + ":" + current.get(Calendar.MINUTE);
		return null;
	}
	
	public void setIsAllDayChecked(boolean b){
		isAllDayChecked = b;
	}
	
	public boolean getIsAllDayChecked(){
		return isAllDayChecked;
	}
}
