package com.example.mycalendar;


import com.example.auxiliary.AppItem;
import com.example.auxiliary.MyCalendarDB;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AllEventsList extends ItemList {
	

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_events_list);
		db = new MyCalendarDB(this);
		itemList = (ListView) findViewById(android.R.id.list);
		itemList.setOnItemLongClickListener(this);
		itemList.setOnItemClickListener(this);
		ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getEventList());
		itemList.setAdapter(allCalendars);
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
				itemList.setAdapter(allCalendars);
			}
			break;
		
		}	
	}
	
	public AppItem getCurrentSelectedEvent(){
		return currentItem;
	}
}
