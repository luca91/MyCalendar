package com.example.mycalendar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.components.AppCalendar;
import com.example.components.AppDialogs;
import com.example.components.AppItem;
import com.example.database.MyCalendarDB;

public abstract class ItemList extends ListActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

	public MyCalendarDB db;
	protected ListView itemList;
	public AppItem currentItem;
	public Bundle current;
	
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
		currentItem = (AppItem) parent.getItemAtPosition(position);
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
	public void performClickedAction(String itemName, int action){
		switch (action){
		
		//Delete
		case 0:
			int actionResutl = db.removeCalendar(itemName);
			if(actionResutl != 0){
				Toast.makeText(this, "Calendar removed successfully.", Toast.LENGTH_LONG).show();
				ArrayAdapter<String> allCalendars = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getCalendarList());
				itemList.setAdapter(allCalendars);
			}
			break;
		
		//Modify
		case 1:
			Intent modifyCalendar = new Intent(this, CalendarEditor.class);
			AppCalendar aCalendar = db.getSingleCalendar(itemName);
			modifyCalendar.putExtra(AppCalendar.C_NAME, aCalendar.getName());
			modifyCalendar.putExtra(AppCalendar.C_COLOR, aCalendar.getColor());
			CalendarEditor.setIsModify(true);
			startActivity(modifyCalendar);
		}
	}
	
	public String getItemName(){
		return this.currentItem.getName();
	}
}
