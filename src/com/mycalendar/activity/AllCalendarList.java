package com.mycalendar.activity;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.AppItem;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.SwipeDetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class manages the action to perform on the list of all calendar the user has in its application.
 * @author Luca Bellettati
 *
 */
public class AllCalendarList extends ItemList implements OnTouchListener {
	
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
		SwipeDetector sd = new SwipeDetector();
//		sd.setActions("Edit", "Delete");
		itemList.setOnTouchListener(sd);
		
	}
	
	@Override
	public void performClickedAction(int action){
switch (action){
		
		//Delete
		case 0:
			int actionResutl = db.removeCalendar(0);
			if(actionResutl != 0){
				Toast.makeText(this, "Calendar removed successfully.", Toast.LENGTH_LONG).show();
				ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getCalendarList());
				itemList.setAdapter(allCalendars);
			}
			break;
		
		//Modify
		case 1:
			Intent modifyCalendar = new Intent(this, CalendarEditor.class);
			AppCalendar aCalendar = db.getSingleCalendarByName("itemName");
			modifyCalendar.putExtra(AppCalendar.CAL_NAME, aCalendar.getName());
			modifyCalendar.putExtra(AppCalendar.CAL_COLOR, aCalendar.getColor());
			CalendarEditor.setIsModify(true);
			startActivity(modifyCalendar);
		}
	}
	
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}


	public AppItem getCurrentSelectedCalendar(){
		return currentItem;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}