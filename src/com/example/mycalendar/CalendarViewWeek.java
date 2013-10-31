package com.example.mycalendar;

import com.example.calendar.Utility;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CalendarViewWeek extends Activity implements AdapterView.OnItemSelectedListener {
	
	private Spinner viewType;
	private String selectedView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_view_week);
		ArrayAdapter<CharSequence> adapterView = ArrayAdapter.createFromResource(this, R.array.view_types, android.R.layout.simple_spinner_item);
		adapterView.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		viewType = (Spinner) findViewById(R.id.viewType);
		viewType.setOnItemSelectedListener(this);
		viewType.setAdapter(adapterView);
		viewType.setSelection(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_view_week, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		selectedView = (String) arg0.getItemAtPosition(arg2);
		Utility.changeView(arg1, this, this, selectedView, "Week");
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
