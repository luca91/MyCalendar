package com.mycalendar.activity;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.AppDialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	private Button modifyButton;
	private Button removeButton;

	/**
	 * It sets the field with the received values to show at the activity creation.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_show);
		db = new MyCalendarDB(this);
		received = getIntent();
		name = (TextView) findViewById(R.id.Name);
		date = (TextView) findViewById(R.id.Date);
		infoContainer = (RelativeLayout) findViewById(R.id.EventInfoContainer);
		modifyButton = (Button) findViewById(R.id.Modify);
		removeButton = (Button) findViewById(R.id.removeEnventButton);
		time = (TextView) findViewById(R.id.Time);
		name.setText(received.getStringExtra(Event.NAME));
		if(received.getBooleanExtra(Event.ALL_DAY, false)){
			date.setText(received.getStringExtra(Event.S_DATE) + "-" + received.getStringExtra(Event.E_DATE));
			time.setText("All Day");
		}
		else if((received.getStringExtra(Event.S_DATE)).equals(received.getStringExtra(Event.E_DATE))){
			date.setText(received.getStringExtra(Event.S_DATE));
			time.setText(received.getStringExtra(Event.S_TIME) + "-" +
					received.getStringExtra(Event.E_TIME));
		}
		else{
			date.setText(received.getStringExtra(Event.S_DATE) + ", " +
					received.getStringExtra(Event.S_TIME));
		time.setText(received.getStringExtra(Event.E_DATE) + ", " +
				received.getStringExtra(Event.E_TIME));
		}
		infoContainer.setBackgroundColor(AppCalendar.colorFromStringToInt(db.getCalendarByName(received.getStringExtra(Event.CALENDAR)).getColor()));		
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
		startActivity(modifyEvent);
	}
	
	public void removeEvent(View v){
		AppDialogs confirm = new AppDialogs(this);
		
	}
}
