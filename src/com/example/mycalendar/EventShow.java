package com.example.mycalendar;

import com.example.auxiliary.Event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

	/**
	 * It sets the field with the received values to show at the activity creation.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_show);
		received = getIntent();
		name = (TextView) findViewById(R.id.Name);
		date = (TextView) findViewById(R.id.Date);
		time = (TextView) findViewById(R.id.Time);
		name.setText(received.getStringExtra(Event.NAME));
		if((received.getStringExtra(Event.S_DATE)).equals(received.getStringExtra(Event.E_DATE))){
			date.setText(received.getStringExtra(Event.S_DATE));
			time.setText(received.getStringExtra(Event.S_TIME) + "-" +
					received.getStringExtra(Event.E_TIME));
		}
		else{
			date.setText("From " + received.getStringExtra(Event.S_DATE) + " at " +
					received.getStringExtra(Event.S_TIME));
		time.setText("To " + received.getStringExtra(Event.E_DATE) + " at " +
				received.getStringExtra(Event.E_TIME));
		}
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
		startActivity(modifyEvent);
	}
}
