package com.mycalendar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.example.mycalendar.R;
import com.mycalendar.calendar.CalendarAdapter;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;
import com.mycalendar.tools.TimeButtonManager;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TimeFinder extends ListActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{
	
	private MyCalendarDB db;
	private TimeButtonManager manager;
	private Button startDate;
	private ListView list;
	private EditText range;
	private ArrayList<Event> potentialList;
	private ArrayList<Event> existing;
	private int rangeValue;
	private RelativeLayout container;
	private Event clicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_finder);
		db = MainActivity.getAppDB();
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		manager = new TimeButtonManager(getFragmentManager(),this);
		manager.setIsFinder(true);
		initializeViews();
		Calendar c = Calendar.getInstance();
		manager.setButtons(startDate, null, null, null);
		manager.setStartCalendar(c);
		manager.setDate("start");
		manager.setDateButtonText("start");
	}
	
	public void findTime(View v){
		potentialList = new ArrayList<Event>();
		rangeValue = Integer.parseInt(range.getText().toString());
		int[] date = manager.getDateToken("start");
		existing = db.getEventsByDayFinder(date[0], date[1], date[2]);
		Event e1 = null;
		Event e2 = null;
		int[] d1e, d2s, t1e, t2s = null;
		Calendar c1, c2 = null;
		long freeTime = -1;
		if(existing.size() > 0){
			
			e1 = existing.get(0);
			d1e = e1.getDateToken("start");
			t1e = e1.getTimeToken("start");
			c1 = new GregorianCalendar(d1e[2], d1e[1]-1, d1e[0], 0, 0);
			c2 = new GregorianCalendar(d1e[2], d1e[1]-1, d1e[0], t1e[0], t1e[1]);
			if(!e1.getStartTime().equals("00:00") && 
					(c2.getTimeInMillis() - c1.getTimeInMillis() >= (rangeValue*1000)) && 
					(e1.getStartDate().equals(startDate.getText().toString())))
				potentialList.add(new Event("No name 0", startDate.getText().toString(), e1.getEndDate(), MyCalendarDB.formatTime(0, 0), e1.getStartTime(), e1.getCalendar()));
			
			for(int i = 0; i+1 < existing.size(); i++){
				e1 = existing.get(i);
				e2 = existing.get(i+1);
				d1e = e1.getDateToken("end");
				d2s = e2.getDateToken("start");
				t1e = e1.getTimeToken("end");
				t2s = e2.getTimeToken("start");
				c1 = new GregorianCalendar(d1e[2], d1e[1], d1e[0], t1e[0], t1e[1]);
				c2 = new GregorianCalendar(d2s[2], d2s[1], d2s[0], t2s[0], t2s[1]);
				long l1 = c1.getTimeInMillis();
				long l2 = c2.getTimeInMillis();
				freeTime = l2-l1;
				long rangeInMill = rangeValue*1000;
				if(freeTime >= rangeInMill && l2 > l1){
					Event pot = new Event("No name " + (i+1), e1.getEndDate(), e2.getStartDate(), e1.getEndTime(), e2.getStartTime(), e1.getCalendar());
					potentialList.add(pot);
				}
			}
			e2 = existing.get(existing.size()-1);
			d2s = e2.getDateToken("end");
			t2s = e2.getTimeToken("end");
			c1 = new GregorianCalendar(d2s[2], d2s[1]-1, d2s[0], t2s[0], t2s[1]);
			c2 = new GregorianCalendar(d2s[2], d2s[1]-1, d2s[0], 0, 0);
			c2.add(Calendar.DAY_OF_MONTH, +1);
			if(!e2.getEndTime().equals("00:00") && 
					(c2.getTimeInMillis() - c1.getTimeInMillis() >= (rangeValue*1000)) &&
					e2.getEndDate().equals(startDate.getText().toString()))
				potentialList.add(new Event("No name " + existing.size(), startDate.getText().toString(), e2.getEndDate(),e2.getEndTime(), MyCalendarDB.formatTime(23, 0), e2.getCalendar()));
			if(potentialList.size() > 0){
				CalendarAdapter adapter = new CalendarAdapter(this, potentialList, false);
				list.setOnItemClickListener(this);
				list.setAdapter(adapter);
			}
			else
				Toast.makeText(this, "No free time in the selected period. Edit your choice to find one.", Toast.LENGTH_LONG).show();
		}
		else{
			TextView noResult = new TextView(this);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			rp.addRule(RelativeLayout.BELOW, R.id.find_time);
			noResult.setText("All day is free.");
			noResult.setTextAppearance(this, android.R.style.TextAppearance_Large);
			noResult.setGravity(Gravity.CENTER);
			noResult.setBackgroundResource(R.drawable.button_state);
			noResult.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 Intent toEditor = new Intent(getApplicationContext(), EventEditor.class);
					 EventEditor.setIsFromFinder(true);
					 startActivity(toEditor);
				}
			});
			list.setVisibility(View.INVISIBLE);
			container.addView(noResult, rp);
		}
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_finder, menu);
		return true;
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
	
	public void goToEventEditor(View v){
		Intent toEventEditor = new Intent(this, EventEditor.class);
		toEventEditor.putExtra(Event.S_DATE, manager.getCurrentStartDate());
		toEventEditor.putExtra(Event.S_TIME, manager.getCurrentStartTime());
		toEventEditor.putExtra(Event.E_DATE, manager.getCurrentEndDate());
		toEventEditor.putExtra(Event.E_TIME, manager.getCurrentEndTime());
		startActivity(toEventEditor);
	}
	
	public void initializeViews(){
		startDate = (Button) findViewById(R.id.start_date_finder);
		list = (ListView) findViewById(android.R.id.list);
		range = (EditText) findViewById(R.id.range_finder);
		container = (RelativeLayout) findViewById(R.id.time_finder);
	}
	
	/**
	 * Create and show the picker for the start date.
	 * @param v the current view
	 */
	public void startDatePicker(View v){
		manager.showStartDatePicker();
	}
	
	/**
	 * Create and show the picker for the start time.
	 * @param v the current view
	 */
	public void startTimePicker(View v){
		manager.showStartTimePicker();
	}
	
	/**
	 * Create and show the picker for the end date.
	 * @param v the current view
	 */
	public void endDatePicker(View v){
		manager.showEndDatePicker();
	}
	
	/**
	 * Create and show the picker for the end time.
	 * @param v the current view
	 */
	public void endTimePicker(View v){
		manager.showEndTimePicker();
	}
	
	public int calculateEventDuration(Event e){
		if(e.getStartDate().equals(e.getEndDate())){
			int[] sTime = e.getTimeToken("start");
			int[] eTime = e.getTimeToken("end");
			return convertTimeToMinutes(eTime[0], eTime[1]) - convertTimeToMinutes(sTime[0], sTime[1]);
		}
		else{
			
		}
		return 0;
	}
	
	public int convertTimeToMinutes(int hour, int minutes){
		return (hour*60) + minutes;
	}
	
	public int[] getDateToken(String date){
		StringTokenizer tokenizer = new StringTokenizer(date, "/");
		int[] dateToken = new int[3];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		dateToken[2] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
	
	public int[] getTimeToken(String time){
		StringTokenizer tokenizer = new StringTokenizer(time, ":");
		int[] dateToken = new int[2];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
	
	public int compareDate(String d1, String d2){
		int[] eventDateToken = getDateToken(d1);
		Calendar eventDate = new GregorianCalendar(eventDateToken[2], eventDateToken[1], eventDateToken[0]);
		int[] chosenDateToken = getDateToken(d2);
		Calendar chosenDate = new GregorianCalendar(chosenDateToken[2], chosenDateToken[1], chosenDateToken[0]);
		return eventDate.compareTo(chosenDate);
	}
	
	public int compareTime(String t1, String t2){
		int[] token1 = getTimeToken(t1);
		int[] token2 = getTimeToken(t2);
		int i1 = convertTimeToMinutes(token1[0], token1[1]);
		int i2 = convertTimeToMinutes(token2[0], token2[1]);
		return i2-i1;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		clicked = potentialList.get(arg2);
		EventEditor.setIsFromFinder(true);
		Intent toEditor = new Intent(this, EventEditor.class);
		toEditor.putExtra(Event.S_DATE, clicked.getStartDate());
		toEditor.putExtra(Event.S_TIME, clicked.getStartTime());
		toEditor.putExtra(Event.E_DATE, clicked.getEndDate());
		toEditor.putExtra(Event.E_TIME, clicked.getEndTime());
		startActivity(toEditor);
	} 
}

