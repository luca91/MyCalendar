package com.example.mycalendar;

import java.util.Calendar;
import java.util.StringTokenizer;

import com.example.components.AppDialogs;
import com.example.components.Event;
import com.example.database.MyCalendarDB;
import com.example.time_and_date.DatePickerFragment;
import com.example.time_and_date.TimePickerFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This is the class to create a new event and modify a already existing one.
 * @author Luca Bellettati
 *
 */
public class EventEditor extends Activity implements AdapterView.OnItemSelectedListener {
	
	private Spinner calendar;
	private EditText eventName;
	private Button startDate;
	private Button startTime;
	private Button endDate;
	private Button endTime;
	private MyCalendarDB db = new MyCalendarDB(this); 
	private boolean dateAlreadySet = false;
	private boolean timeAlreadySet = false;
	private String currentCalendar;
	private String currentStartDate;
	private String currentStartTime;
	private String currentEndDate;
	private String currentEndTime;
	private Calendar current;
	private String tag;
	private ArrayAdapter<String> calendarAdapter;
	private static boolean isModify;
	private String[] calendars;
	private AppDialogs dialog;
	
	/**
	 * It sets the layout of the activity used to add an event to the agenda
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_editor);
		
		//The button of the layout are connected to an object in the class
		eventName = (EditText) findViewById(R.id.EventName);
		startDate = (Button) findViewById(R.id.StartDate);
		startTime = (Button) findViewById(R.id.StartTime);
		endDate = (Button) findViewById(R.id.EndDate);
		endTime = (Button) findViewById(R.id.EndTime);
		calendar = (Spinner) findViewById(R.id.Calendar);
		calendarAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.getCalendarList());
		calendar.setAdapter(calendarAdapter);
		
		//checks if an event has to be create or modify
		if(!getIsModify())
			setCreateEnvironment();
		else {
			Intent received = getIntent();
			setModifyEnvironment(received);
		}
	}
	
	/**
	 * Called when the "Create" button in the layout is pressed. It create an Event instance, stores it in the database and send all the information to the "EventShow" activity.
	 * @param v the current view
	 */
	public void createEvent(View v){
		if(eventName.getText().toString().equals(""))
			Toast.makeText(this, "Empty event not created.", Toast.LENGTH_LONG).show();
		else{
			Event anEvent = new Event(eventName.getText().toString(), 
					currentStartDate, 
					currentEndDate, 
					currentStartTime, 
					currentEndTime, 
					currentCalendar,
					tag);
			long result = db.addEvent(anEvent);
			if (result != -1){
				Intent showEvent = new Intent(this, EventShow.class);
				showEvent.putExtra(Event.NAME, anEvent.getName());
				showEvent.putExtra(Event.S_DATE, currentStartDate);
				showEvent.putExtra(Event.E_DATE, currentEndDate);
				showEvent.putExtra(Event.S_TIME, currentStartTime);
				showEvent.putExtra(Event.E_TIME, currentEndTime);
				showEvent.putExtra(Event.CALENDAR, currentCalendar);
				startActivity(showEvent);
			}
			else
				Toast.makeText(this, "Event not created: try again.", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * It transform the three components of the date (year, month and day) into a string with the form "dd/mm/yyyy and stores it in the respective String variable, depending on the tag value.
	 * @param year the year to set
	 * @param month the month to set
	 * @param day the day to set
	 * @param tag which date to set
	 */
	public void setDate(int year, int month, int day, String tag){
		String date = "";
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
	public void setDateButtonText(String text, String tag){
		if(tag.equals("start"))
			startDate.setText(text);
		else
			endDate.setText(text);
	}
	
	/**
	 * It transform the two components of the time (hours and minutes) into a string of the form "hh:mm" and stores it in the respective String variable, depending on the tag value.
	 * @param hours the hours to set 
	 * @param minutes the minute to set
	 * @param tag which time to set
	 */
	public void setTime(int hours, int minutes, String tag){
		String time;
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
			time = hours+":"+"00";
		}
		else if(hours == 0 && minutes == 0){
			time = "00"+":"+"00";
		}
		else{
			time = hours+":"+minutes;
		}
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
	public void setTimeButtonText(String text, String tag){
		if(tag.equals("start"))
			startTime.setText(currentStartTime);
		else
			endTime.setText(currentEndTime);
	}
	
	/**
	 * Create and show the picker for the start date.
	 * @param v the current view
	 */
	public void showStartDatePicker(View v){
		DatePickerFragment date = new DatePickerFragment();
		date.setParent(this, this.getBaseContext());
		date.show(getFragmentManager(), "startDatePicker");
	}
	
	/**
	 * Create and show the picker for the start time.
	 * @param v the current view
	 */
	public void showStartTimePicker(View v){
		TimePickerFragment time = new TimePickerFragment();
		time.setParent(this, this.getBaseContext());
		time.show(getFragmentManager(), "startTimePicker");
	}
	
	/**
	 * Create and show the picker for the end date.
	 * @param v the current view
	 */
	public void showEndDatePicker(View v){
		DatePickerFragment date = new DatePickerFragment();
		date.setParent(this, this.getBaseContext());
		date.show(getFragmentManager(), "endDatePicker");
	}
	
	/**
	 * Create and show the picker for the end time.
	 * @param v the current view
	 */
	public void showEndTimePicker(View v){
		TimePickerFragment time = new TimePickerFragment();
		time.setParent(this, this.getBaseContext());
		time.show(getFragmentManager(), "endTimePicker");
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
	
	/**
	 * It get the current instance of the calendar.
	 * @return String
	 */
	public Calendar getCurrent(){
		return this.current;
	}
	
	/**
	 * It set the environment for the creation of an event.
	 */
	public void setCreateEnvironment(){
		//A instance of the current time is stored in the local Calendar object
		current = Calendar.getInstance();
				
		//The texts of the various buttons are set with the current date and time
		setDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH)+1, current.get(Calendar.DAY_OF_MONTH), "start");
		setDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH)+1, current.get(Calendar.DAY_OF_MONTH), "end");
		setDateButtonText(getCurrentStartDate(), "start");
		setDateButtonText(getCurrentEndDate(), "end");
		if(current.get(Calendar.HOUR_OF_DAY) == 23){
			setTime(0, 0, "start");
			setTime(1, 0,"end");
		}
		else{
			//The hour for a new event is always the next round one
			setTime(current.get(Calendar.HOUR_OF_DAY)+1, 0, "start"); 
			setTime(current.get(Calendar.HOUR_OF_DAY)+2, 0, "end");
		}
		setTimeButtonText(currentStartTime, "start");
		setTimeButtonText(currentEndTime, "end");
		calendars = db.getCalendarList();
		if(calendars.length < 1){
			Intent toCalendarEditor = new Intent(this, CalendarEditor.class);
			dialog = new AppDialogs(this);
			dialog.noEventDialog("No calendar exists. Create one before adding events.", toCalendarEditor);
			startActivity(toCalendarEditor);
		}
		else
			currentCalendar = calendars[0];
	}
	
	/**
	 * It set the environment for the modification of an event.
	 */
	public void setModifyEnvironment(Intent received){
		current = Calendar.getInstance();
		currentStartDate = received.getStringExtra(Event.S_DATE);
		currentEndDate = received.getStringExtra(Event.E_DATE);
		currentStartTime = received.getStringExtra(Event.S_TIME);
		currentEndTime = received.getStringExtra(Event.E_TIME);
		currentCalendar = received.getStringExtra(Event.CALENDAR);
		calendar.setPrompt(currentCalendar);
		eventName.setText(received.getStringExtra(Event.NAME));
		setDateButtonText(currentStartDate, "start");
		setDateButtonText(currentEndDate, "end");
		setTimeButtonText(currentStartTime, "start");
		setTimeButtonText(currentEndTime, "end");
	}

	public static void setIsModify(boolean b) {
		isModify = b;
	}
	
	public static boolean getIsModify(){
		return isModify;
	}
	
	public void viewAllEvents(View v){
		Intent eventsList = new Intent(this, AllEventsList.class);
		startActivity(eventsList);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		currentCalendar = (String) arg0.getItemAtPosition(arg2);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
