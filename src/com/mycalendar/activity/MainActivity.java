package com.mycalendar.activity;

import com.example.mycalendar.R;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.AppDialogs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**
 * This is the class of the main activity, called at the application start. 
 * @author Luca Bellettati
 *
 */
public class MainActivity extends Activity {
	
	@SuppressWarnings("unused")
	private static MyCalendarDB applicationDB;
	
	public static String defaultView;
	public static String actualView;
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
        defaultView = "List";
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
    	EventsView.setActualView(defaultView);
    	if(defaultView.equals("Day")){
    		allEvent = new Intent(this, EventsView.class);
    		startActivity(allEvent);
    	}
    	if(defaultView.equals("Week")){
    		allEvent = new Intent(this, EventsView.class);
			startActivity(allEvent);
    	}
    	if(defaultView.equals("Month")){
    		allEvent = new Intent(this, EventsView.class);
			startActivity(allEvent);
    	}
    	if(defaultView.equals("List")){
    		allEvent = new Intent(this, AllEventsList.class);
			startActivity(allEvent);
    	}
    }
    
    public void timeFinder(View v){
    	Intent toFinder = new Intent(this, TimeFinder.class);
    	startActivity(toFinder);
    }
    
    public void settings(MenuItem m){
    	Intent settings = new Intent(this, Settings.class);
    	startActivity(settings);
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
    
}
