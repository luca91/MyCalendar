package com.example.mycalendar;

import com.example.components.AppItem;
import com.example.database.MyCalendarDB;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This class manages the action to perform on the list of all calendar the user has in its application.
 * @author Luca Bellettati
 *
 */
public class AllCalendarList extends ItemList {
	
	/**
	 * It creates a list with all the calendar.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_calendar_list);
		db = new MyCalendarDB(this);
		current = savedInstanceState;
		itemList = (ListView) findViewById(android.R.id.list);
		itemList.setOnItemLongClickListener(this);
		itemList.setOnItemClickListener(this);
		ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getCalendarList());
		itemList.setAdapter(allCalendars);
		
	}
	
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}


	public AppItem getCurrentSelectedCalendar(){
		return currentItem;
	}
}