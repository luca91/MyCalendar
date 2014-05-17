package com.mycalendar.activity;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.database.MyCalendarDB;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
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
	private static boolean isModify = false;
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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.calendar_colors, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		color.setOnItemSelectedListener(this);
		color.setAdapter(adapter);
		if(!isModify)
			setCreateEnvironment();
		else
			setModifyEnvironment();
	}
	
	/**
	 * It creates a calendar instance and called the activity to show the details about it.
	 * @param v the current view
	 */
	public void createModifyCalendar(View v){
		if(!isModify){
			createCalendar();
		}
		else {
			modifyCalendar();
		}
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
		Toast.makeText(this, received.getStringExtra(AppCalendar.CAL_NAME)+", "+received.getStringExtra(AppCalendar.CAL_COLOR), Toast.LENGTH_LONG).show();
		name.setText(received.getStringExtra(AppCalendar.CAL_NAME));
		color.setPrompt(received.getStringExtra(AppCalendar.CAL_COLOR));
	}
	
	public void createCalendar(){
		currentName = name.getText().toString();
		AppCalendar aCalendar = new AppCalendar(currentName, currentColor);
		long result = db.addCalendar(aCalendar);
		if(result != -1){
			Toast.makeText(this, "Calendar added.", Toast.LENGTH_LONG).show();
			Intent showCalendar = new Intent(this, CalendarShow.class);
			showCalendar.putExtra(AppCalendar.CAL_NAME, currentName);
			int colorAsInt = AppCalendar.colorFromStringToInt(currentColor);
			showCalendar.putExtra(AppCalendar.CAL_COLOR, String.valueOf(Integer.toHexString(colorAsInt)));
			Toast.makeText(this, db.getCalendarByID((int) result).getID(), Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "ID: " + (int) result, Toast.LENGTH_SHORT).show();
			startActivity(showCalendar);
		}
		else
			Toast.makeText(this, "Calendar not added: try again.", Toast.LENGTH_LONG).show();
	}
	
	public void modifyCalendar(){
		currentName = name.getText().toString();
		AppCalendar oldCalendar = new AppCalendar(received.getStringExtra(AppCalendar.CAL_NAME), AppCalendar.CAL_COLOR);
		AppCalendar updatedCalendar = new AppCalendar(currentName, currentColor);
		long result = db.updateCalendar(oldCalendar, updatedCalendar);
		if(result != -1){
			Toast.makeText(this, "Calendar modified.", Toast.LENGTH_LONG).show();
			Intent showUpdates = new Intent(this, CalendarShow.class);
			showUpdates.putExtra(AppCalendar.CAL_NAME, currentName);
			showUpdates.putExtra(AppCalendar.CAL_COLOR, currentColor);
			startActivity(showUpdates);
			setIsModify(false);
		}
		else
			Toast.makeText(this, "Calendar not updated.", Toast.LENGTH_LONG).show();
	}
}
