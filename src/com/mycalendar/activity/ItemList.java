package com.mycalendar.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.AppItem;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.AppDialogs;

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
//		RelativeLayout item = (RelativeLayout) parent.getItemAtPosition(position);
//		String calendarName = ((TextView) item.getChildAt(0)).getText().toString();
		String calendarName = ((AppCalendar) parent.getItemAtPosition(position)).getName();
		AppCalendar aCalendar = db.getSingleCalendarByName(calendarName);
		showDetails.putExtra(AppCalendar.CAL_NAME, aCalendar.getName());
		showDetails.putExtra(AppCalendar.CAL_COLOR, aCalendar.getColor());
		showDetails.putExtra(AppCalendar.CAL_ID, aCalendar.getID());
		startActivity(showDetails);
	}

	/**
	 * It manages the long click of an item of the list view.
	 */
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		currentItem = (AppItem) parent.getItemAtPosition(position);
		AppDialogs aDialog = new AppDialogs(this, this);
		aDialog.setEditItems();
		aDialog.setTitle("Choose an action");
		
		//the dialog is shown and the action performed
//		aDialog.onCreateDialog(current);
		int action = aDialog.getItemClicked();
		Toast.makeText(this, "Action: "+action, Toast.LENGTH_LONG).show();
		performClickedAction(action);
		return true;
	}
	
	/**
	 * Performs the selected actions from the dialog.
	 * @param calendarName the name of the calendar on which to perform the action 
	 * @param action the action to perform
	 */
	public void performClickedAction(int action){
		//To override
	}
	
	public String getItemName(){
		return this.currentItem.getName();
	}
}
