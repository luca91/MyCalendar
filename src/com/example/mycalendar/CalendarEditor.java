package com.example.mycalendar;

import com.example.auxiliary.AppCalendar;
import com.example.auxiliary.MyCalendarDB;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * It sets and manages the creation of a new calendar.
 * @author Luca Bellettati
 *
 */
public class CalendarEditor extends Activity implements AdapterView.OnItemSelectedListener {
	
	private EditText name;
	private Spinner color;
	private String currentName;
	private String currentColor;
	private MyCalendarDB db;
	private String[] colors;
	private static boolean isModify;
	private Intent received;
	
	/**
	 * It is called when the activity is created and set the layout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_editor);
		received = getIntent();
		db = new MyCalendarDB(this);
		name = (EditText) findViewById(R.id.CalendarName);
		color = (Spinner) findViewById(R.id.CalendarColor);
		if(isModify)
			setCreateEnvironment();
		else
			setModifyEnvironment();
	}
	
	/**
	 * It creates a calendar instance and called the activity to show the details about it.
	 * @param v the current view
	 */
	public void createCalendar(View v){
		currentName = name.getText().toString();
		AppCalendar aCalendar = new AppCalendar(currentName, currentColor);
		long result = db.addCalendar(aCalendar);
		if(result != -1){
			Toast.makeText(this, "Calendar added.", Toast.LENGTH_LONG).show();
			Intent showCalendar = new Intent(this, CalendarShow.class);
			showCalendar.putExtra(AppCalendar.C_NAME, currentName);
			showCalendar.putExtra(AppCalendar.C_COLOR, currentColor);
			startActivity(showCalendar);
		}
		else
			Toast.makeText(this, "Calendar not added: try again.", Toast.LENGTH_LONG).show();
	}
	
	public void saveModification(View v){
		currentName = name.getText().toString();
		AppCalendar oldCalendar = new AppCalendar(received.getStringExtra(AppCalendar.C_NAME), AppCalendar.C_COLOR);
		AppCalendar updatedCalendar = new AppCalendar(currentName, currentColor);
		long result = db.updateCalendar(oldCalendar, updatedCalendar);
		if(result != -1){
			Toast.makeText(this, "Calendar modified.", Toast.LENGTH_LONG).show();
			Intent showUpdates = new Intent(this, CalendarShow.class);
			showUpdates.putExtra(AppCalendar.C_NAME, currentName);
			showUpdates.putExtra(AppCalendar.C_COLOR, currentColor);
			startActivity(showUpdates);
			setIsModify(false);
		}
		else
			Toast.makeText(this, "Calendar not updated.", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Called when an item in the spinner is selected.
	 */
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		currentColor = (String) parent.getItemAtPosition(pos);
    }
	
	/**
	 * Called when nothing in the spinner is selected.
	 */
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub	
	}
	
	/**
	 * When the back button is pressed, it starts the main activity.
	 */
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}
	
	/**
	 * It start the activity to view the list of all calendars.
	 * @param v the current view
	 */
	public void viewAllCalendars(View v){
		Intent viewAllCalendars = new Intent(this, AllCalendarList.class);
		startActivity(viewAllCalendars);
	}
	
	/**
	 * It sets the isModify flag to the value passed.
	 * @param value the value to set
	 */
	public static void setIsModify(boolean value){
		isModify = value;
	}
	
	/**
	 * Returns the value of the isModify flag.
	 * @return boolean
	 */
	public static boolean getIsModify(){
		return isModify;
	}
	
	/**
	 * It set the layout of the editor in the case a new calendar is to be created.
	 */
	public void setCreateEnvironment(){
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.calendar_colors, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		color.setAdapter(adapter);
		color.setOnItemSelectedListener(this);
		Resources resources = getResources();
		colors = resources.getStringArray(R.array.calendar_colors);
		color.setPrompt(colors[6]);
	}
	
	/**
	 * It sets the layout when a calendar is to be modify.
	 */
	public void setModifyEnvironment(){
		received = getIntent();
		Toast.makeText(this, received.getStringExtra(AppCalendar.C_NAME)+", "+received.getStringExtra(AppCalendar.C_COLOR), Toast.LENGTH_LONG).show();
		name.setText(received.getStringExtra(AppCalendar.C_NAME));
		color.setPrompt(received.getStringExtra(AppCalendar.C_COLOR));
		setIsModify(false);
	}
}
