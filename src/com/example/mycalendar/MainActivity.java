package com.example.mycalendar;

import com.example.database.MyCalendarDB;

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
	private MyCalendarDB applicationDB;
	
	public static String defaultView;
	
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
        defaultView = "Month";
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
    	Intent addEvent = new Intent(this, EventEditor.class);
    	EventEditor.setIsModify(false);
    	startActivity(addEvent);
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
    	Intent allCalendar = new Intent(this, AllCalendarList.class);
    	startActivity(allCalendar);
    }
    
    public void allEvents(View v){
    	Intent allEvent;
    	if(defaultView.equals("Day")){
    		allEvent = new Intent(this, CalendarViewDay.class);
    		startActivity(allEvent);
    	}
    	if(defaultView.equals("Week")){
    		allEvent = new Intent(this, CalendarViewWeek.class);
			startActivity(allEvent);
    	}
    	if(defaultView.equals("Month")){
    		allEvent = new Intent(this, CalendarViewMonth.class);
			startActivity(allEvent);
    	}
    	if(defaultView.equals("Year")){
    		allEvent = new Intent(this, CalendarViewYear.class);
			startActivity(allEvent);
    	}
    }
    
    public void importCalendar(View v){
    	Intent importCalendar = new Intent(this, ImportCalendar.class);
    	startActivity(importCalendar);
    }
    
    public void exportCalendar(View v){
    	Intent exportCalendar = new Intent(this, ExportCalendar.class);
    	startActivity(exportCalendar);
    }
    
    public void setting(MenuItem m){
    	Intent settings = new Intent(this, Settings.class);
    	startActivity(settings);
    }
    
}
