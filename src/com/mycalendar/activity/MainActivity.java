package com.mycalendar.activity;

import java.util.Calendar;

import com.example.mycalendar.R;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.AppDialogs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


/**
 * This is the class of the main activity, called at the application start. 
 * @author Luca Bellettati
 *
 */
public class MainActivity extends Activity {
	

	private static MyCalendarDB applicationDB;
	
	/**
	 * Variable for setting actual view
	 */
	public static String defaultView;
	
	/**
	 * Variable that maintain the actual view for events
	 */
	public static String actualView;
	
	/**
	 * Variable that maintain the actual calendar for events view
	 */
	public static Calendar actualCalendar;
	private AppDialogs d;
	
	/**
	 * The main activity is created.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //The UI environment is set
        setContentView(R.layout.activity_main);

        //At the very first time, the database is created; then this only connect the app to it.
        applicationDB = new MyCalendarDB(this); 
        if(applicationDB.getSettingByName("def_view") == null)
        	applicationDB.addSetting("def_view", "Month");
        if(applicationDB.getSettingByName("def_calendar") == null)
        	applicationDB.addSetting("def_calendar", "No default");
        defaultView = applicationDB.getSettingByName("def_view");
//        actualView = applicationDB.getSettingByName("last_view");
        actualCalendar = Calendar.getInstance();
    }

    /**
     * Inflates the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * It start the activity where an event is created.
     * @param m the item clicked in the menu window
     */
    public void addEvent(View v){
		if(applicationDB.getCalendarList().size() < 1){
			Intent toCalendarEditor = new Intent(this, CalendarEditor.class);
			new AppDialogs(this).noCalendarDialog("No calendar exists. Create one before adding events.", toCalendarEditor, this);
		}
		else{
			Intent addEvent = new Intent(this, EventEditor.class);
	    	EventEditor.setIsModify(false);
	    	startActivity(addEvent);
		}
    }
    
    /**
     * It start the activity where a calendar is created.
     * @param m the item clicked in the menu window
     */
    public void addCalendar(View v){
    	Intent addCalendar = new Intent(this, CalendarEditor.class);
    	CalendarEditor.setIsModify(false);
    	startActivity(addCalendar);
    }
    
    public void allCalendars(View v){
    	if(applicationDB.getCompleteCalendarList().size() > 0){
	    	Intent allCalendar = new Intent(this, AllCalendarList.class);
	    	startActivity(allCalendar);
    	}
    	else{
    		d = new AppDialogs(this);
    		d.setTitle("Warning!");
    		d.setMessage("No calendar to display.");
    		d.setPositiveButton();
    		d.createAndShowDialog();
    	}
    }
    
    public void allEvents(View v){
		Intent allEvent;
		if(actualView == null)
			actualView = defaultView;
			
    	if(actualView.equals("Day")){
    		allEvent = new Intent(this, EventsView.class);
    		startActivity(allEvent);
    	}
    	if(actualView.equals("Week")){
    		allEvent = new Intent(this, EventsView.class);
			startActivity(allEvent);
    	}
    	if(actualView.equals("Month")){
    		allEvent = new Intent(this, EventsView.class);
			startActivity(allEvent);
    	}
    	if(actualView.equals("List")){
    		allEvent = new Intent(this, AllEventsList.class);
			startActivity(allEvent);
    	}
    }
    
    public void timeFinder(View v){
    	Intent toFinder = new Intent(this, TimeFinder.class);
    	startActivity(toFinder);
    }
    
    public void allReminders(View v){
    	Intent allReminders = new Intent(this, ReminderList.class);
    	startActivity(allReminders);
    }
    
    public static void setActualView(String view){
    	actualView = view;
    }
    
    public static String getActualView(){
    	return actualView;
    }
    
    public static MyCalendarDB getAppDB(){
    	return applicationDB;
    }
    
    public void settings(View v){
    	Intent settings = new Intent(this, SettingsActivity.class);
    	startActivity(settings);
    }
    
}
