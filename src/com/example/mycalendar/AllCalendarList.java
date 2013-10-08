package com.example.mycalendar;

import com.example.auxiliary.AppCalendar;
import com.example.auxiliary.AppDialogs;
import com.example.auxiliary.MyCalendarDB;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class manages the action to perform on the list of all calendar the user has in its application.
 * @author Luca Bellettati
 *
 */
public class AllCalendarList extends ListActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
	
	private ListView calendarList;
	private MyCalendarDB db;
	private Bundle current;
	private String currentSelectedCalendar;
	
	/**
	 * It creates a list with all the calendar.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_calendar_list);
		db = new MyCalendarDB(this);
		current = savedInstanceState;
		calendarList = (ListView) findViewById(android.R.id.list);
		calendarList.setOnItemLongClickListener(this);
		calendarList.setOnItemClickListener(this);
		ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getCalendarList());
		calendarList.setAdapter(allCalendars);
		
	}
	
	/**
	 * It manages the click of an item of the list view.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		Intent showDetails = new Intent(this, CalendarShow.class);
		String calendarName = (String) parent.getItemAtPosition(position);
		AppCalendar aCalendar = db.getSingleCalendar(calendarName);
		showDetails.putExtra(AppCalendar.C_NAME, aCalendar.getName());
		showDetails.putExtra(AppCalendar.C_COLOR, aCalendar.getColor());
		startActivity(showDetails);
	}

	/**
	 * It manages the long click of an item of the list view.
	 */
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		currentSelectedCalendar = (String) parent.getItemAtPosition(position);
		AppDialogs aDialog = new AppDialogs(this, this);
		
		//the dialog is shown and the action performed
		aDialog.onCreateDialog(current);
		int action = aDialog.getItemClicked();
		Toast.makeText(this, "Action: "+action, Toast.LENGTH_LONG).show();
		//performClickedAction(calendarName, action);
		return true;
	}
	
	/**
	 * Performs the selected actions from the dialog.
	 * @param calendarName the name of the calendar on which to perform the action 
	 * @param action the action to perform
	 */
	public void performClickedAction(String calendarName, int action){
		switch (action){
		
		//Delete
		case 0:
			int actionResutl = db.removeCalendar(calendarName);
			if(actionResutl != 0){
				Toast.makeText(this, "Calendar removed successfully.", Toast.LENGTH_LONG).show();
				ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getEventList());
				calendarList.setAdapter(allCalendars);
			}
			break;
		
		//Modify
		case 1:
			Intent modifyCalendar = new Intent(this, CalendarEditor.class);
			AppCalendar aCalendar = db.getSingleCalendar(calendarName);
			modifyCalendar.putExtra(AppCalendar.C_NAME, aCalendar.getName());
			modifyCalendar.putExtra(AppCalendar.C_COLOR, aCalendar.getColor());
			CalendarEditor.setIsModify(true);
			startActivity(modifyCalendar);
		}
	}
	
	@Override
	public void onBackPressed(){
		Intent calendarList = new Intent(this, CalendarEditor.class);
		startActivity(calendarList);
	}

	public String getCurrentSelectedCalendar(){
		return this.currentSelectedCalendar;
	}
}