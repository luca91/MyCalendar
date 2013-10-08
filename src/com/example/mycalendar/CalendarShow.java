package com.example.mycalendar;

import com.example.auxiliary.AppCalendar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This class takes care of showing the details about a calendar.
 * @author Luca Bellettati
 *
 */
public class CalendarShow extends Activity {
	
	private TextView calendar;

	/**
	 * Sets the layout environment.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_show);
		Intent received = getIntent();
		calendar = (TextView) findViewById(R.id.Name);
		calendar.setText(received.getStringExtra(AppCalendar.C_NAME));
		String color = received.getStringExtra(AppCalendar.C_COLOR);
		if(color.equals("White") || color.equals("Cyan") || color.equals("Yellow"))
			calendar.setTextColor(Color.BLACK);
		else
			calendar.setTextColor(Color.WHITE);
		calendar.setBackgroundColor(AppCalendar.colorFromStringToInt(color));
	}
	
	/**
	 * When the back button is pressed, it starts the main activity.
	 */
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}

}
