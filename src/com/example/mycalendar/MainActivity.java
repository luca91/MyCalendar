package com.example.mycalendar;

import com.example.database.MyCalendarDB;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

/**
 * This is the class of the main activity, called at the application start. 
 * @author Luca Bellettati
 *
 */
public class MainActivity extends Activity {
	
	private MyCalendarDB applicationDB;
	private CalendarView calendar; 
	private Spinner viewChoice; 
	private RelativeLayout outer; 
	private String currentCalendarLayout;
	private Resources appResources;
	
	/**
	 * The main activity is created.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //The UI environment is set
        setContentView(R.layout.activity_main);
        outer = (RelativeLayout) findViewById(R.id.CalendarContainer);
        viewChoice = (Spinner) findViewById(R.id.ViewChoice);

        //At the very first time, the database is created; then this only connect the app to it.
        applicationDB = new MyCalendarDB(this); 
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
    public void addEvent(MenuItem m){
    	Intent addEvent = new Intent(this, EventEditor.class);
    	EventEditor.setIsModify(false);
    	startActivity(addEvent);
    }
    
    /**
     * It start the activity where a calendar is created.
     * @param m the item clicked in the menu window
     */
    public void addCalendar(MenuItem m){
    	Intent addCalendar = new Intent(this, CalendarEditor.class);
    	CalendarEditor.setIsModify(false);
    	startActivity(addCalendar);
    }
    
    public void allCalendars(MenuItem m){
    	Intent allCalendar = new Intent(this, AllCalendarList.class);
    	startActivity(allCalendar);
    }
    
    public void allEvents(MenuItem m){
    	Intent allEvent = new Intent(this, AllEventsList.class);
    	startActivity(allEvent);
    }
    
}
