package com.mycalendar.activity;

import java.util.ArrayList;

import com.example.mycalendar.R;
import com.mycalendar.calendar.CalendarAdapter;
import com.mycalendar.components.AppItem;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner; 
import android.widget.Toast;

import com.mycalendar.tools.AppDialogs;

public class AllEventsList extends ItemList implements AdapterView.OnItemSelectedListener {
	
	private MyCalendarDB db;	
	private Spinner viewSelection;
	private ArrayList<Event> eventsList;
	private Event clicked;
	private int actionResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_events_list);
		db = new MyCalendarDB(this);
		viewSelection = (Spinner) findViewById(R.id.viewTypeAllEvents);
		itemList = (ListView) findViewById(android.R.id.list);
		itemList.setOnItemLongClickListener(this);
		itemList.setOnItemClickListener(this);
		eventsList = db.getEventList();
		ArrayAdapter<CharSequence> types = ArrayAdapter.createFromResource(this, R.array.view_types, android.R.layout.simple_spinner_item);
		types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		viewSelection.setOnItemSelectedListener(this);
		viewSelection.setAdapter(types);
		viewSelection.setSelection(3, false);
		Toast.makeText(this, "Events number: " + db.getEventList().size(), Toast.LENGTH_LONG).show();
		if(db.checkEvents()){
			CalendarAdapter adapter = new CalendarAdapter(this, eventsList);
			itemList.setAdapter(adapter);
		}
		else
//			AppDialogs d = new AppDialogs(this, this);
			Toast.makeText(this, "No Events", Toast.LENGTH_LONG).show();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		clicked = eventsList.get(arg2);
		startActivity(setIntentExtra(new Intent(this, EventShow.class)));
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		clicked = eventsList.get(arg2);
		AppDialogs dialog = new AppDialogs(this, this);
		dialog.setTitle("Choose an action:");
		dialog.setEditItems();
		dialog.createAndShowDialog();
		performClickedAction(dialog.getItemClicked());
		return false;
	}

	@Override
	public void performClickedAction(int action){
		switch (action){
		
		//Delete
		case 0:
			actionResult = (int) db.removeEvent(clicked.getId());
			if(actionResult != 0){
				ArrayAdapter<Event> allCalendars = new ArrayAdapter<Event>(getApplicationContext(), android.R.layout.simple_list_item_1, db.getEventList());
				itemList.setAdapter(allCalendars);
			}
			break;
			
			
		//Modify
		case 1:
			startActivity(setIntentExtra(new Intent(this, EventEditor.class)));
		}	
	}
	
	public AppItem getCurrentSelectedEvent(){
		return currentItem;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String viewT = (String) parent.getItemAtPosition(position);
		if(!viewT.equals("List")){
			EventsView.setActualView(viewT);
			EventsView.setPosition(position);
			Intent toSelectedView = new Intent(this, EventsView.class);
			startActivity(toSelectedView);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}
	
	public Intent setIntentExtra(Intent i){
		i.putExtra(Event.NAME, clicked.getName());
		i.putExtra(Event.S_DATE, clicked.getStartDate());
		i.putExtra(Event.E_DATE, clicked.getEndDate());
		i.putExtra(Event.S_TIME, clicked.getStartTime());
		i.putExtra(Event.E_TIME, clicked.getEndTime());
		i.putExtra(Event.CALENDAR, clicked.getCalendar());
		i.putExtra(Event.ID, (int) clicked.getId());
		i.putExtra(Event.ALL_DAY, clicked.getAllDay());
		return i;
	}
	
	
}
