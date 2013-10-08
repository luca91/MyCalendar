package com.example.mycalendar;

import com.example.auxiliary.AppCalendar;
import com.example.auxiliary.Event;
import com.example.auxiliary.MyCalendarDB;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AllEventsList extends ListActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
	
	private ListView eventsList;
	private MyCalendarDB db;
	private Bundle current;
	private String currentSelectedEvent;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_events_list);
		db = new MyCalendarDB(this);
		eventsList = (ListView) findViewById(android.R.id.list);
		eventsList.setOnItemLongClickListener(this);
		eventsList.setOnItemClickListener(this);
		ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getEventList());
		eventsList.setAdapter(allCalendars);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void performClickedAction(String calendarName, int action){
		switch (action){
		
		//Delete
		case 0:
			int actionResutl = db.removeCalendar(calendarName);
			if(actionResutl != 0){
				Toast.makeText(this, "Event removed successfully.", Toast.LENGTH_LONG).show();
				ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getCalendarList());
				eventsList.setAdapter(allCalendars);
			}
			break;
		
		//Modify
		/*case 1:
			modifyCalendar = new Intent(this, CalendarEditor.class);
			Event anEvent = db.getSingleEvent();
			modifyCalendar.putExtra(Event.NAME, anEvent.getName());
			modifyCalendar.putExtra(AppCalendar.C_COLOR, aCalendar.getColor());
			CalendarEditor.setIsModify(true);
			startActivity(modifyCalendar); */
		}
		
		}
	
	public String getCurrentSelectedEvent(){
		return this.currentSelectedEvent;
	}
}
