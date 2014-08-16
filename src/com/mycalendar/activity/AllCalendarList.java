package com.mycalendar.activity;

import java.util.ArrayList;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.AppItem;
import com.mycalendar.tools.AppDialogs;
import com.mycalendar.tools.CalendarListAdapter;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
		db = MainActivity.getAppDB();
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		current = savedInstanceState;
		itemList = (ListView) findViewById(android.R.id.list);
//		itemList.setOnItemLongClickListener(this);
		itemList.setOnItemClickListener(this);
//		ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getCalendarList());
		ArrayList<AppCalendar> calendars = db.getCompleteCalendarList();
		CalendarListAdapter adapter = new CalendarListAdapter(this, R.layout.calendar_item, calendars);
		itemList.setAdapter(adapter);
	}
	

	
	@Override
	public void performClickedAction(int action){
		switch (action){
		
		//Delete
		case 0:
			int actionResult = 0;
			if(db.getCalendarList().size() > 1)
				actionResult = db.removeCalendar(0);
			else{
				AppDialogs noRemove = new AppDialogs(this);
				noRemove.setTitle("Warning! Only one calendar!");
				noRemove.setMessage("The calendar cannot be removed since it is the only existing one.");
				noRemove.setPositiveButton();
				noRemove.createAndShowDialog();
			}
			if(actionResult != 0){
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
			modifyCalendar.putExtra(AppCalendar.CAL_ID, aCalendar.getID());
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