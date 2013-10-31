package com.example.mycalendar;

import java.util.GregorianCalendar;

import com.example.calendar.DayAdapter;
import com.example.calendar.Utility;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class CalendarViewDay extends ListActivity implements AdapterView.OnItemSelectedListener {
	
	private ListView hoursList;
	private Spinner viewType;
	private String selectedView;
	private GregorianCalendar instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_view_day);
		String[] hours = {"0AM","1AM","2AM","3AM","4AM","5AM","6AM","7AM","8AM","9AM","10AM","11AM","12AM","1PM","2PM","3PM","4PM","5PM","6PM","7PM","8PM","9PM","10PM","11PM","12PM" };
		DayAdapter adapter = new DayAdapter(this, R.layout.day_item, hours);
		hoursList = (ListView) findViewById(android.R.id.list);
		hoursList.setAdapter(adapter);
		viewType = (Spinner) findViewById(R.id.viewType);
		ArrayAdapter<CharSequence> adapterView = ArrayAdapter.createFromResource(this, R.array.view_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		viewType.setOnItemSelectedListener(this);
		viewType.setAdapter(adapterView);
		viewType.setSelection(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_view_day, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		selectedView = (String) arg0.getItemAtPosition(arg2);
		Utility.changeView(arg1, this, this, selectedView, "Day");
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
