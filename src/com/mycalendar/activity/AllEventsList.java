package com.mycalendar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.mycalendar.R;
import com.mycalendar.calendar.CalendarAdapter;
import com.mycalendar.components.AppItem;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner; 
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class AllEventsList extends ItemList implements AdapterView.OnItemSelectedListener {
	
	private MyCalendarDB db;	
	private Spinner viewSelection;
	private ArrayList<Event> eventsList;
	private Event clicked;
	private RelativeLayout container;
	CalendarAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_events_list);
		db = MainActivity.getAppDB();
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		Toast.makeText(this, "Events number: " + db.getEventList().size(), Toast.LENGTH_SHORT).show();
		viewSelection = (Spinner) findViewById(R.id.viewTypeAllEvents);
		itemList = (ListView) findViewById(android.R.id.list);
		container = (RelativeLayout) findViewById(R.id.eventsListObjectContainer);
		itemList.setOnItemLongClickListener(this);
		itemList.setOnItemClickListener(this);
		Calendar c = (GregorianCalendar) Calendar.getInstance();
		eventsList = db.getEventsFromDay(c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.MONTH)+1), c.get(Calendar.YEAR), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
//		eventsList = db.getEventList();
		ArrayAdapter<CharSequence> types = ArrayAdapter.createFromResource(this, R.array.view_types, android.R.layout.simple_spinner_item);
		types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		viewSelection.setOnItemSelectedListener(this);
		viewSelection.setAdapter(types);
		viewSelection.setSelection(3, false);
		if(eventsList.size() > 0){
			adapter = new CalendarAdapter(this, eventsList, false);
			itemList.setAdapter(adapter);
		}
		else{
			TextView noResult = new TextView(this);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			rp.addRule(RelativeLayout.BELOW, R.id.find_time);
			noResult.setText("No events.");
			noResult.setTextAppearance(this, android.R.style.TextAppearance_Large);
			noResult.setGravity(Gravity.CENTER);
			itemList.setVisibility(View.INVISIBLE);
			container.addView(noResult, rp);
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		clicked = eventsList.get(arg2);
		startActivity(setIntentExtra(new Intent(this, EventShow.class)));
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		clicked = eventsList.get(arg2);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Choose an action:");
		dialog.setItems(R.array.edit_actions, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				performClickedAction(which);
			}
		});
		dialog.create();
		dialog.show();
		return false;
	}

	@Override
	public void performClickedAction(int action){
		final Context ctx = this;
		switch (action){
		
		//Delete
		case 0:
//			confirm.confirmDelete(clicked.getId());
//			actionResult = (int) db.removeEvent(clicked.getId());
//			if(actionResult != 0){
			final int eventID = clicked.getId();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Warning!");
			builder.setMessage("Are you sure you want to delete this calendar?");
//			setPositiveButton();
			builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MyCalendarDB db = MainActivity.getAppDB();
					int res1 = (int) db.removeEvent(eventID);
					int res2 = -1;
					if(db.getReminderByEventID(eventID) != null)
						res2 = (int) db.removeReminder(eventID);
					if(res1 > 0 && res2 > 0)
						Toast.makeText(ctx, "Event removed correctly", Toast.LENGTH_SHORT).show();
					Intent toHome = new Intent(ctx, MainActivity.class);
					startActivity(toHome);
//					adapter.setEventsList(db.getEventList());
//					itemList.setAdapter(adapter);
				}
			});
//			setNegativeButton();
			builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create();
			builder.show();
//			}
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
//			EventsView.setPosition(position);
			Intent toSelectedView = new Intent(this, EventsView.class);
			startActivity(toSelectedView);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//Do nothing!!!
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
	
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
}
