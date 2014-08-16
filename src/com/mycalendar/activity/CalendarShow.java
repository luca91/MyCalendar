package com.mycalendar.activity;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.AppDialogs;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;

/**
 * This class takes care of showing the details about a calendar.
 * @author Luca Bellettati
 *
 */
@SuppressLint("DefaultLocale")
public class CalendarShow extends Activity {
	
	private TextView calendar;
	private Intent received;
	private final MyCalendarDB db = MainActivity.getAppDB();

	/**
	 * Sets the layout environment.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_show);
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		received = getIntent();
		calendar = (TextView) findViewById(R.id.Name);
		calendar.setText(received.getStringExtra(AppCalendar.CAL_NAME));
		int color = AppCalendar.colorFromStringToInt(received.getStringExtra(AppCalendar.CAL_COLOR));
		if(color == Color.WHITE || color == Color.CYAN || color == Color.YELLOW)
			calendar.setTextColor(Color.BLACK);
		else
			calendar.setTextColor(Color.WHITE);
		calendar.setBackgroundColor(color);
	}
	
	/**
	 * When the back button is pressed, it starts the main activity.
	 */
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}
	
	public void modifyCalendar(View v){
		Intent toEditor = new Intent(this, CalendarEditor.class);
		toEditor.putExtra(AppCalendar.CAL_NAME, received.getStringExtra(AppCalendar.CAL_NAME));
		toEditor.putExtra(AppCalendar.CAL_COLOR, received.getStringExtra(AppCalendar.CAL_COLOR));
		toEditor.putExtra(AppCalendar.CAL_ID, received.getIntExtra(AppCalendar.CAL_ID, -1));
		CalendarEditor.setIsModify(true);
		startActivity(toEditor);
	}
	
	public void viewCalendars(View v){
		Intent toAllCalendars = new Intent(this, AllCalendarList.class);
		startActivity(toAllCalendars);
	}
	
	public void createCalendar(View v){
		Intent toEditor = new Intent(this, CalendarEditor.class);
		startActivity(toEditor);
	}
	
	public void removeCalendar(View v){
		final Context ctx = this;
		final int calendarID = received.getIntExtra(AppCalendar.CAL_ID, -1);
		if(db.getCalendarList().size() > 1){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Warning!");
			builder.setMessage("Are you sure you want to delete this calendar?");
			builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					db.removeCalendar(calendarID);
					Intent toHome = new Intent(ctx, MainActivity.class);
					startActivity(toHome);
				}
			});
			builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create();
			builder.show();
		}
		else{
			AppDialogs noRemove = new AppDialogs(this);
			noRemove.setTitle("Warning! Only one calendar!");
			noRemove.setMessage("The calendar cannot be removed since it is the only existing one.");
			noRemove.setPositiveButton();
			noRemove.createAndShowDialog();
		}
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
