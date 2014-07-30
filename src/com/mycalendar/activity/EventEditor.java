package com.mycalendar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.mycalendar.R;
import com.mycalendar.components.Event;
import com.mycalendar.components.Reminder;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.AppDialogs;
import com.mycalendar.tools.TimeButtonManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the class to create a new event and modify a already existing one.
 * @author Luca Bellettati
 *
 */
public class EventEditor extends Activity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
	
	private Spinner calendar;
	private EditText eventName;
	private Button startDate;
	private Button startTime;
	private Button endDate;
	private Button endTime;
	private CheckBox allDay;
	private Spinner flexibility;
	private EditText flexibilityRange;
	private Spinner reminder;
	private Spinner repetition;
	private EditText notesArea;
	private MyCalendarDB db;
	private String currentCalendar;
	private int flexRange;
	private String flexPref;
	private Calendar current;
	private ArrayAdapter<String> calendarAdapter;
	private ArrayAdapter<CharSequence> reminderAdapter;
	private ArrayAdapter<CharSequence> repetitionAdapter;
	private ArrayAdapter<CharSequence> flexibilityAdapter;
	private static boolean isModify;
	private static boolean isFromFinder;
	private ArrayList<String> calendars;
	private AppDialogs dialog;
	private RelativeLayout elemsContainer;
	private String timeChosen;
	private Event anEvent;
	private int repetitionChosen;
	private int id;
	private TextView flexText;
	private TimeButtonManager manager;
	private TextView rangeMinText;
	
	/**
	 * It sets the layout of the activity used to add an event to the agenda
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_editor);
		db = MainActivity.getAppDB();
		
		
		//The button of the layout are connected to an object in the class
		elemsContainer = (RelativeLayout) findViewById(R.id.Editor);
		calendar = (Spinner) findViewById(R.id.Calendar);
		eventName = (EditText) findViewById(R.id.EventName);
		startDate = (Button) findViewById(R.id.StartDate);
		startTime = (Button) findViewById(R.id.StartTime);
		endDate = (Button) findViewById(R.id.EndDate);
		endTime = (Button) findViewById(R.id.EndTime);
		allDay = (CheckBox) findViewById(R.id.allDay);
		flexibility = (Spinner) findViewById(R.id.flexible);
		flexibilityRange = (EditText) findViewById(R.id.flexibility_range);
		notesArea = (EditText) findViewById(R.id.notesArea);
		reminder = (Spinner) findViewById(R.id.reminderSpinner);
		repetition = (Spinner) findViewById(R.id.repeatSpinner);
		allDay.setOnCheckedChangeListener(this);
		calendarAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.getCalendarList());
		calendarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		calendar.setOnItemSelectedListener(this);
		calendar.setAdapter(calendarAdapter);
		flexText = (TextView) findViewById(R.id.flexibility_text);
		rangeMinText = (TextView) findViewById(R.id.flexibility_min);
		setReminderOptionsAdapter();
		setRepetitionOptionsAdapter();
		setFlexibilityPreferenceAdapter();
		
		//checks if an event has to be create or modify
		if(!getIsModify()){
			setCreateEnvironment();
		}
		else if(isFromFinder){
			Intent received = getIntent();
			setEventFromFinderEnvironment(received);
		}
		else {
			Intent received = getIntent();
			anEvent = db.getEventByID(received.getIntExtra(Event.ID, -1));
			setModifyEnvironment();
		}
	}
	
	/**
	 * Called when the "Create" button in the layout is pressed. It create an Event instance, stores it in the database and send all the information to the "EventShow" activity.
	 * @param v the current view
	 */
	public void createEvent(View v){
		Toast.makeText(this, manager.calendarToString("start") + "\n" + manager.calendarToString("end") + "\n" + manager.calendarToString("current"), Toast.LENGTH_LONG).show();
		if(manager.validateSelectedDate()){
			String name = "";
			if(eventName.getText().toString().isEmpty()){
				dialog = new AppDialogs(this);
				dialog.emptyEventNameAlert();
			}	 
			else{
				name = eventName.getText().toString();
				if(db.checkEventUnique(name, manager.getCurrentStartDate(), manager.getCurrentStartTime(), db.getCalendarByName(currentCalendar).getID())){
					dialog = new AppDialogs(this);
					dialog.alreadyExistingEventAlert();
				}
				else{
					anEvent = new Event(name, 
							manager.getCurrentStartDate(), 
							manager.getCurrentEndDate(), 
							manager.getCurrentStartTime(), 
							manager.getCurrentEndTime(), 
							currentCalendar);
					if(!notesArea.getText().toString().isEmpty())
						anEvent.setNotes(notesArea.getText().toString());
					if(allDay.isChecked()){
						anEvent.setAllDay(1);
						manager.setTime("00:00", "end");
						manager.setTime("00:00", "start");
					}
					anEvent.setFlexibility(flexPref);
					if(!flexPref.equals("None"))
						anEvent.setFlexibilityRange(Integer.valueOf(flexibilityRange.getText().toString()));
					anEvent.setRepetition(repetitionChosen);
					Reminder rem = getReminderObject();
					rem.setRemTimeChosen(parseReminderTime());
					anEvent.setReminder(rem);
					db.addReminder(rem);
					long result;
					if(!isModify)
						result = db.addEvent(anEvent);
					else{
						anEvent.setId(id);
						result = db.updateEvent(anEvent);
					}
					if (result != -1){
		//				db.addReminder(db.getSingleEvent(anEvent).getId(), calculateReminder(calculateMinutesForReminder()), "");
						Intent showEvent = new Intent(this, EventShow.class);
						showEvent.putExtra(Event.NAME, anEvent.getName());
						showEvent.putExtra(Event.S_DATE, manager.getCurrentStartDate());
						showEvent.putExtra(Event.E_DATE, manager.getCurrentEndDate());
						showEvent.putExtra(Event.S_TIME, manager.getCurrentStartTime());
						showEvent.putExtra(Event.E_TIME, manager.getCurrentEndTime());
						showEvent.putExtra(Event.CALENDAR, currentCalendar);
						showEvent.putExtra(Event.ID, (int) result);
						showEvent.putExtra(Event.ALL_DAY, anEvent.getAllDay());
						startActivity(showEvent);
					}
					else{
		//				dialog = new AppDialogs(this);
		//				dialog.alreadyExistingEventAlert();
					}
				}
			}
		}
		else{
			AppDialogs invalidDate = new AppDialogs(this);
			invalidDate.wrongDateAlert();
		}
	}
	
	
	
	/**
	 * Create and show the picker for the start date.
	 * @param v the current view
	 */
	public void startDatePicker(View v){
		manager.showStartDatePicker();
	}
	
	/**
	 * Create and show the picker for the start time.
	 * @param v the current view
	 */
	public void startTimePicker(View v){
		manager.showStartTimePicker();
	}
	
	/**
	 * Create and show the picker for the end date.
	 * @param v the current view
	 */
	public void endDatePicker(View v){
		manager.showEndDatePicker();
	}
	
	/**
	 * Create and show the picker for the end time.
	 * @param v the current view
	 */
	public void endTimePicker(View v){
		manager.showEndTimePicker();
	}
	
	/**
	 * It set the environment for the creation of an event.
	 */
	public void setCreateEnvironment(){
		//A instance of the current time is stored in the local Calendar object
		current = Calendar.getInstance();
		manager = new TimeButtonManager(getFragmentManager(), this);
		
		//setting calendar
		manager.setCurrentCalendar((Calendar) current.clone());
//		manager.setStartCalendar(new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH)+1, current.get(Calendar.HOUR_OF_DAY), 0));
//		manager.setEndCalendar(new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH)+1, current.get(Calendar.HOUR_OF_DAY), 0));
		manager.setStartCalendar((Calendar) current.clone());
		manager.setEndCalendar((Calendar) current.clone());
		
		Toast.makeText(this, manager.calendarToString("start") + "\n" + manager.calendarToString("end") + "\n" + manager.calendarToString("current"), Toast.LENGTH_LONG).show();
		
		//setting the button
		manager.setButtons(startDate, endDate, startTime, endTime);
		
		//Sets all the buttons
		manager.setButtonState();
		
		calendars = db.getCalendarList();
		currentCalendar = calendars.get(0);
		Toast.makeText(this, "Repetition: " + repetitionChosen, Toast.LENGTH_SHORT).show();
		timeChosen = (String) reminder.getItemAtPosition(0);
		repetitionChosen = 0;
		flexPref = (String) flexibility.getItemAtPosition(0);
		flexibilityRange.setVisibility(View.INVISIBLE);
		flexRange = 30;
	}
	
	/**
	 * It set the environment for the modification of an event.
	 */
	public void setModifyEnvironment(){
		Intent received = getIntent();
		id = received.getIntExtra(Event.ID, -1);
		Event anEvent = db.getEventByID(id);
		current = Calendar.getInstance();
		manager = new TimeButtonManager(getFragmentManager(), this);
		manager.setCurrentCalendar((Calendar) current.clone());
		manager.setButtons(startDate, endDate, startTime, endTime);
		manager.setDate(anEvent.getStartDate(), "start");
		manager.setDate(anEvent.getEndDate(), "end");
		manager.setTime(anEvent.getStartTime(), "start");
		manager.setTime(anEvent.getEndTime(), "end");
		int[] startDate = manager.getDateToken("start");
		int[] endDate = manager.getDateToken("end");
		int[] startTime = manager.getTimeToken("start");
		int[] endTime = manager.getTimeToken("end");
		manager.setStartCalendar(new GregorianCalendar(startDate[2],startDate[1]-1, startDate[0], startTime[0], startTime[1]));
		manager.setEndCalendar(new GregorianCalendar(endDate[2],startDate[1]-1, endDate[0], endTime[0], endTime[1]));
		manager.setButtonState();
		currentCalendar = anEvent.getCalendar();
		calendar.setSelection(getItemSelectedPosition(currentCalendar, db.getCalendarList().toArray(new String[] {})), false);
		eventName.setText(anEvent.getName());
		flexPref = anEvent.getFlexibility();
		repetitionChosen = anEvent.getRepetition();
//		timeChosen = db.getReminder(anEvent.getReminderID()).getTimeChosen();
		manager.setDateButtonText("start");
		manager.setDateButtonText("end");
		manager.setTimeButtonText("start");
		manager.setTimeButtonText("end");
		if(anEvent.getAllDay() == 1){
			allDay.setChecked(true);
			flexibility.setVisibility(View.INVISIBLE);
			flexibilityRange.setVisibility(View.INVISIBLE);
			flexText.setVisibility(View.INVISIBLE);
			rangeMinText.setVisibility(View.INVISIBLE);
		}
		flexibility.setSelection(getItemSelectedPosition(flexPref, getResources().getStringArray(R.array.flexibility_option)), false);
		flexRange = anEvent.getFlexibilityRange();
		flexibilityRange.setText(String.valueOf(flexRange));
		repetition.setSelection(repetitionChosen, false);
		reminder.setSelection(getItemSelectedPosition(timeChosen, getResources().getStringArray(R.array.reminder_options)), false);
		notesArea.setText("");
	}
	
	public void setEventFromFinderEnvironment(Intent received){
		current = Calendar.getInstance();
//		manager = new TimeButtonManager(getFragmentManager(), this, current);
		manager.setButtons(startDate, endDate, startTime, endTime);
		manager.setDate(received.getStringExtra(Event.S_DATE), "start");
		manager.setTime(received.getStringExtra(Event.S_TIME), "start");
		manager.setDate(received.getStringExtra(Event.E_DATE), "end");
		manager.setTime(received.getStringExtra(Event.E_TIME), "end");
		
	}
 
	public static void setIsModify(boolean b) {
		isModify = b;
	}
	
	public static boolean getIsModify(){
		return isModify;
	}
	
	public static void setIsFromFinder(boolean b){
		isFromFinder = b;
	}
	
	public static boolean getIsFromFinder(){
		return isFromFinder;
	}
	
	public void viewAllEvents(View v){
		Intent eventsList = new Intent(this, AllEventsList.class);
		startActivity(eventsList);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if(arg0.getId() == R.id.Calendar)
			currentCalendar = (String) arg0.getItemAtPosition(arg2);
		else if(arg0.getId() == R.id.reminderSpinner)
			timeChosen = (String) arg0.getItemAtPosition(arg2);
		else if(arg0.getId() == R.id.repeatSpinner)
			repetitionChosen = arg2;
		else{
			flexPref = (String) arg0.getItemAtPosition(arg2);
			if(flexPref.equals("None")){
				flexibilityRange.setVisibility(View.INVISIBLE);
				rangeMinText.setVisibility(View.INVISIBLE);
			}
			else{
				flexibilityRange.setVisibility(View.VISIBLE);
				rangeMinText.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		elemsContainer.removeView(startDate);
//		elemsContainer.removeView(endDate);
//		elemsContainer.removeView(startTime);
//		elemsContainer.removeView(endTime);
		if(isChecked){
//			setStartDateButtonAllDayChecked();
//			setEndDateButtonAllDayChecked();
			startTime.setText("All day");
			endTime.setText("All day");
			rangeMinText.setVisibility(View.INVISIBLE);
			flexibility.setVisibility(View.INVISIBLE);
			flexibilityRange.setVisibility(View.INVISIBLE);
			TextView flexText = (TextView) findViewById(R.id.flexibility_text);
			flexText.setVisibility(View.INVISIBLE);
		}
		else{
//			setStartDateButtonAllDayNotChecked();
//			setEndDateButtonAllDayNotChecked();
//			setStartTimeButtonAllDayNotChecked();
//			setEndTimeButtonAllDayNotChecked();
			manager.setTimeButtonText("start");
			manager.setTimeButtonText("end");
			flexibility.setVisibility(View.VISIBLE);
			flexibility.setSelection(0, false);
			flexibilityRange.setVisibility(View.INVISIBLE);
			flexibilityRange.setText("0");
			flexText.setVisibility(View.VISIBLE);
		}
	}

	public void setStartDateButtonAllDayChecked(){
		RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		dateParams.addRule(RelativeLayout.BELOW, R.id.EventName);
		dateParams.addRule(RelativeLayout.RIGHT_OF, R.id.From);
		dateParams.setMargins(10, 20, 0, 0);
		elemsContainer.addView(startDate, dateParams);
	}
	
	public void setEndDateButtonAllDayChecked(){
		RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		dateParams.addRule(RelativeLayout.BELOW, R.id.To);
		dateParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.StartDate);
		elemsContainer.addView(endDate, dateParams);
	}
	
	public void setStartDateButtonAllDayNotChecked(){
		RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dateParams.addRule(RelativeLayout.BELOW, R.id.EventName);
		dateParams.setMargins(10, 20, 0, 0);
		elemsContainer.addView(startDate, dateParams);
	}
	
	public void setEndDateButtonAllDayNotChecked(){
		RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dateParams.addRule(RelativeLayout.BELOW, R.id.StartDate);
		dateParams.addRule(RelativeLayout.BELOW, R.id.To);
		dateParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.StartDate);
		elemsContainer.addView(endDate, dateParams);
	}
	
	public void setStartTimeButtonAllDayNotChecked(){
		RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dateParams.addRule(RelativeLayout.BELOW, R.id.From);
		dateParams.addRule(RelativeLayout.RIGHT_OF, R.id.StartDate);
		dateParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		dateParams.setMargins(0, 20, 0, 0);
		elemsContainer.addView(startTime, dateParams);
	}
	
	public void setEndTimeButtonAllDayNotChecked(){
		RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dateParams.addRule(RelativeLayout.BELOW, R.id.StartTime);
		dateParams.addRule(RelativeLayout.RIGHT_OF, R.id.EndDate);
		dateParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		elemsContainer.addView(endTime, dateParams);
	}
	
	public String calculateReminder(int time){
		int[] dateToken = anEvent.getDateToken("start");
		int[] timeToken = anEvent.getTimeToken("start");
		Calendar c = new GregorianCalendar(dateToken[2], dateToken[1], dateToken[0], timeToken[0], timeToken[1]);
		c.add(Calendar.MINUTE, -time);
		return c.toString();
	}
	
	public int calculateMinutesForReminder(){
		switch(timeChosen){
		case "No reminder":
			return 0;
		case "15 min":
			return 15;
		case "30 min":
			return 30;
		case "1 hour":
			return 60;
		case "1 week":
			return 10080;
		}
		return 0;
	}
	
	public void updateDateAllDayEvents(){
//		manager.setCurrentCalendar(c);
		manager.setDate("end");
//		manager.setTime(0, 0, "start");
//		manager.setTime(0, 0, "end");
	}
	
	public Calendar createCalendarFromDateTime(){
		int[] dateToken = anEvent.getDateToken("start");
		int[] timeToken = anEvent.getTimeToken("start");
		return new GregorianCalendar(dateToken[2], dateToken[1], dateToken[0], timeToken[0], timeToken[1]);
	}
	
	public void setReminderOptionsAdapter(){
		reminderAdapter = ArrayAdapter.createFromResource(this, R.array.reminder_options, android.R.layout.simple_spinner_item);
		reminderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		reminder.setOnItemSelectedListener(this);
		reminder.setAdapter(reminderAdapter);
	}
	
	public void setRepetitionOptionsAdapter(){
		repetitionAdapter = ArrayAdapter.createFromResource(this, R.array.repetition_options, android.R.layout.simple_spinner_item);
		repetitionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		repetition.setOnItemSelectedListener(this);
		repetition.setAdapter(repetitionAdapter);
	}
	
	public void setFlexibilityPreferenceAdapter(){
		flexibilityAdapter = ArrayAdapter.createFromResource(this, R.array.flexibility_option, android.R.layout.simple_spinner_item);
		flexibilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		flexibility.setOnItemSelectedListener(this);
		flexibility.setAdapter(flexibilityAdapter);
	}
	
	public int getItemSelectedPosition(String item, String[] array){
		for(int i = 0; i < array.length; i++){
			if(array[i].equals(item))
				return i;
		}
		return -1;
	}
	
	/**
	 * 
	 * @return the amount of minutes corresponding to the chosen value
	 */
	public int parseReminderTime(){
		switch(timeChosen){
		case "15 min":
			return 15;
		case "30 min":
			return 30;
		case "1 hour":
			return 60;
		case "1 day":
			return 1440;
		case "1 week":
			return 10080;
		case "No reminder":
			return 0;
		}
		return -1;
	}
	
	public Reminder getReminderObject(){
		Reminder result = null;
//		Calendar rem = (Calendar) manager.getCalendar("start").clone();
		Calendar rem = manager.getCalendar("start");
//		rem.add(Calendar.MINUTE, -parseReminderTime());
		result = new Reminder(rem.get(Calendar.DAY_OF_MONTH)+"/"+rem.get(Calendar.MONTH)+"/"+rem.get(Calendar.YEAR), rem.get(Calendar.HOUR_OF_DAY)+":"+rem.get(Calendar.MINUTE), id);
		Toast.makeText(this, "Reminder: " + result.toString(), Toast.LENGTH_LONG).show();
		return result;
	}
}