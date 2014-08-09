package com.mycalendar.activity;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class shown in a formatted way the information about an event.
 * @author Luca Bellettati
 *
 */
public class EventShow extends Activity {
	
	private TextView name;
	private TextView date;
	private TextView time;
	private Intent received;
	private RelativeLayout infoContainer;
	private MyCalendarDB db;

	/**
	 * It sets the field with the received values to show at the activity creation.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_show);
		db = MainActivity.getAppDB();
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		received = getIntent();
		Event e = db.getEventByID(received.getIntExtra(Event.ID, -1));
		name = (TextView) findViewById(R.id.Name);
		date = (TextView) findViewById(R.id.Date);
		infoContainer = (RelativeLayout) findViewById(R.id.EventInfoContainer);
		time = (TextView) findViewById(R.id.Time);
		int color = AppCalendar.colorFromStringToInt(db.getCalendarByName(e.getCalendar()).getColor());
		int textColor = -1;
		if(color == Color.WHITE || color == Color.CYAN || color == Color.YELLOW)
			textColor = Color.BLACK;
		else
			textColor = Color.WHITE;
		
		//setting text color for text views
		name.setTextColor(textColor);
		date.setTextColor(textColor);
		time.setTextColor(textColor);
		
		name.setText(e.getName());
//		if(received.getIntExtra(Event.ALL_DAY, -1) == 1){
//			if((received.getStringExtra(Event.S_DATE)).equals(received.getStringExtra(Event.E_DATE)))
//				date.setText(received.getStringExtra(Event.S_DATE));
//			else
//				date.setText(received.getStringExtra(Event.S_DATE) + "-" + received.getStringExtra(Event.E_DATE));
//			time.setText("All Day");
//		}
//		if((received.getStringExtra(Event.S_DATE)).equals(received.getStringExtra(Event.E_DATE))){
//			date.setText(received.getStringExtra(Event.S_DATE));
//			time.setText(received.getStringExtra(Event.S_TIME) + "-" +
//					received.getStringExtra(Event.E_TIME));
//		}
//		else{
//			date.setText(received.getStringExtra(Event.S_DATE) + ", " +
//					received.getStringExtra(Event.S_TIME));
//		time.setText(received.getStringExtra(Event.E_DATE) + ", " +
//				received.getStringExtra(Event.E_TIME));
//		}
		
		if(e.getStartDate().equals(e.getEndDate())){
			date.setText(e.getStartDate());
			time.setText(e.getStartTime() + "-" + e.getEndTime());
		}
		else{
			date.setText(e.getStartDate() + ", " + e.getStartTime());
		time.setText(e.getEndDate() + ", " + e.getEndTime());
		}
		infoContainer.setBackgroundColor(AppCalendar.colorFromStringToInt(db.getCalendarByName(e.getCalendar()).getColor()));		
	}
	
	/**
	 * When the back button is pressed, it starts the main activity.
	 */
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	 }
	
	public void modifyEvent(View v){
		Intent modifyEvent = new Intent(this, EventEditor.class);
		EventEditor.setIsModify(true);
		modifyEvent.putExtra(Event.NAME, received.getStringExtra(Event.NAME));
		modifyEvent.putExtra(Event.S_DATE, received.getStringExtra(Event.S_DATE));
		modifyEvent.putExtra(Event.E_DATE, received.getStringExtra(Event.E_DATE));
		modifyEvent.putExtra(Event.S_TIME, received.getStringExtra(Event.S_TIME));
		modifyEvent.putExtra(Event.E_TIME, received.getStringExtra(Event.E_TIME));
		modifyEvent.putExtra(Event.CALENDAR, received.getStringExtra(Event.CALENDAR));
		modifyEvent.putExtra(Event.ID, received.getIntExtra(Event.ID, -1));
		modifyEvent.putExtra(Event.ALL_DAY, received.getBooleanExtra(Event.ALL_DAY, false));
		startActivity(modifyEvent);
	}
	
	public void removeEvent(View v){
//		confirm.confirmDelete(received.getIntExtra(Event.ID, -1));
		final Context ctx = this;
		final int eventID = received.getIntExtra(Event.ID, -1);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Warning!");
		builder.setMessage("Are you sure you want to delete this event?");
//		setPositiveButton();
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
			}
		});
//		setNegativeButton();
		builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create();
		builder.show();
	}
	
	public void allEvents(View v){
		Intent toAllEvents = new Intent(this, AllEventsList.class);
		startActivity(toAllEvents);
	}
	
	public void createEvent(View v){
		Intent toEventEditor = new Intent(this, EventEditor.class);
		startActivity(toEventEditor);
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
