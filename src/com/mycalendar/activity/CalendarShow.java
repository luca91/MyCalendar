package com.mycalendar.activity;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

/**
 * This class takes care of showing the details about a calendar.
 * @author Luca Bellettati
 *
 */
public class CalendarShow extends Activity {
	
	private TextView calendar;
	private Intent received;

	/**
	 * Sets the layout environment.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_show);
		received = getIntent();
		calendar = (TextView) findViewById(R.id.Name);
		calendar.setText(received.getStringExtra(AppCalendar.CAL_NAME));
		String color = received.getStringExtra(AppCalendar.CAL_COLOR);
		if(color.equals("White") || color.equals("Cyan") || color.equals("Yellow"))
			calendar.setTextColor(Color.BLACK);
		else
			calendar.setTextColor(Color.WHITE);
		calendar.setBackgroundColor(Color.parseColor(color));
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
		toEditor.putExtra(AppCalendar.CAL_EMAIL, received.getStringExtra(AppCalendar.CAL_EMAIL));
		CalendarEditor.setIsModify(true);
		startActivity(toEditor);
	}
	
	public void viewCalendars(View v){
		Intent toAllCalendars = new Intent(this, AllCalendarList.class);
		startActivity(toAllCalendars);
	}

}
