package com.mycalendar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.example.mycalendar.R;
import com.mycalendar.calendar.CalendarAdapter;
import com.mycalendar.calendar.Utility;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventsView extends Activity implements OnItemSelectedListener, OnClickListener{
	
	private static int position;
	private static String actualView;
	private TextView selectedTimePeriod;
	private Calendar calendar;
	private Spinner selectViewType;
	private LinearLayout hours;
	private ArrayList<Event> list;
	private ArrayList<TextView> tvs;
	private LinearLayout weekDayGrid;
	private LinearLayout daysContainer;
	ArrayAdapter<CharSequence> adapterViewTypes;
	private MyCalendarDB db;
	private RelativeLayout eventsObjectContainer;
	private int firstWeekOfMonthDayInYear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		db = MainActivity.getAppDB();
		calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		setInitialState(actualView);
	}
	
	public void setInitialState(String type){
		switch (type){
		case "Day":
			setContentView(R.layout.activity_calendar_view_day);
			selectViewType = (Spinner) findViewById(R.id.viewTypeDay);
			selectedTimePeriod = (TextView) findViewById(R.id.selectedDay);
			hours = (LinearLayout) findViewById(R.id.hoursContainerDay);
			weekDayGrid = (LinearLayout) findViewById(R.id.backgroundAsGridDay);
			eventsObjectContainer = (RelativeLayout) findViewById(R.id.eventsContainerDay);
			list = db.getEventsByDay(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
			setDayLayoutObjects();
			break;
		case "Week":
			setContentView(R.layout.activity_calendar_view_week);
			selectViewType = (Spinner) findViewById(R.id.viewTypeWeek);
			selectedTimePeriod = (TextView) findViewById(R.id.selectedWeek);
			hours = (LinearLayout) findViewById(R.id.hoursContainerWeek);
			weekDayGrid = (LinearLayout) findViewById(R.id.backgroundAsGridWeek);
			daysContainer = (LinearLayout) findViewById(R.id.weekDaysContainer);
			setWeekLayoutObjects();
			break;
		case "Month":
			setContentView(R.layout.activity_calendar_view_month);
			selectViewType = (Spinner) findViewById(R.id.viewTypeMonth);
			selectedTimePeriod = (TextView) findViewById(R.id.selectedMonth);
			weekDayGrid = (LinearLayout) findViewById(R.id.backgroundAsGridMonth);
			daysContainer = (LinearLayout) findViewById(R.id.monthDaysContainer);
			setMonthLayoutObjects();
			break;
		}
		adapterViewTypes = ArrayAdapter.createFromResource(this, R.array.view_types, android.R.layout.simple_spinner_item);
		adapterViewTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectViewType.setAdapter(adapterViewTypes);
		selectViewType.setSelection(position, false);
		selectViewType.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String viewT = (String) parent.getItemAtPosition(position);
		actualView = viewT;
		if(viewT.equals("List")) {
			Intent toList = new Intent(this, AllEventsList.class);
			startActivity(toList);
		}
		else{
			setPosition(position);
			setInitialState(viewT);
		}
	}
	
	public void setDayLayoutObjects(){
		setDay();
		setHours();
		setGridItems();
	}
	
	public void setWeekLayoutObjects(){
		setHours();
		Calendar c = Calendar.getInstance();
		int startDay = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DAY_OF_WEEK, -(startDay-1));
		setDaysOfWeek(c, 60, 74);
		setGridItems();
		
	}
	
	public void setMonthLayoutObjects(){
		calendar = Calendar.getInstance();
		int startDay = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DAY_OF_MONTH, -startDay);
		setGridItems();
	}
	
	public int setDayView(){
		return R.layout.activity_calendar_view_day;
	}
	
	public int setMonthView(){
		return R.layout.activity_calendar_view_month;
	}
	
	public int setWeekView(){
		return R.layout.activity_calendar_view_week;
	}
	
	public void setDay(){
		selectedTimePeriod.setText(Utility.parseWeekDayToString(calendar.get(Calendar.DAY_OF_WEEK)) + ", " 
		+ calendar.get(Calendar.DAY_OF_MONTH) + " "
		+ Utility.parseMonthToString(calendar.get(Calendar.MONTH)) + " " 
		+ calendar.get(Calendar.YEAR));
	}
	
	public void setDaysOfWeek(Calendar weekBegin, int emptySpace, int spaceLeft){
		for(int i = 0; i <= 7; i++){
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams lp = null;
			if(i == 0){
				lp = new LinearLayout.LayoutParams(new LayoutParams(emptySpace, LayoutParams.MATCH_PARENT));
				tv.setBackgroundColor(Color.GRAY);
			}
			else{
				weekBegin.add(Calendar.DAY_OF_WEEK, 1);
				lp = new LinearLayout.LayoutParams(new LayoutParams(getWeekGridItemWidth(spaceLeft), LayoutParams.MATCH_PARENT));
				tv.setText(Utility.parseWeekDayToString(weekBegin.get(Calendar.DAY_OF_WEEK)) + " " + weekBegin.get(Calendar.DAY_OF_MONTH));
				if(weekBegin.get(Calendar.DAY_OF_YEAR) < firstWeekOfMonthDayInYear)
					tv.setBackgroundColor(Color.WHITE);
				else
					tv.setBackgroundColor(Color.GRAY);
				tv.setTextSize(12f);
			}
			lp.setMargins(0, 0, 2, 2);
			tv.setLayoutParams(lp);
			tv.setGravity(Gravity.CENTER);
			if(weekBegin.get(Calendar.DAY_OF_YEAR) < firstWeekOfMonthDayInYear)
				tv.setTextColor(Color.GRAY);
			else
				tv.setTextColor(Color.WHITE);
			daysContainer.addView(tv);
		}
	}
	
	public void setGridItems(){
		switch(actualView){
		case "Day":
			weekDayGrid.setBackgroundColor(Color.LTGRAY);
			LinearLayout.LayoutParams glp = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
			glp.setMargins(0, 0, 2, 2);
			for(int i = 0; i < 24; i++){
				Button dayHour = new Button(this);
				dayHour.setBackgroundColor(Color.WHITE);
				dayHour.setClickable(true);
				weekDayGrid.addView(dayHour, glp);
			}
			break;
		case "Week":
			weekDayGrid.setBackgroundColor(Color.LTGRAY);
			LinearLayout.LayoutParams hourLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
			LinearLayout.LayoutParams dayHourLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(getWeekGridItemWidth(74), 100));
			hourLayoutParams.setMargins(0, 0, 2, 2);
			dayHourLayoutParams.setMargins(0, 0, 2, 0);
			for(int i = 0; i < 24; i++){
				LinearLayout weekHour = new LinearLayout(this);
				weekHour.setOrientation(LinearLayout.HORIZONTAL);
				weekHour.setBackgroundColor(Color.LTGRAY);
				for(int j = 0; j < 7; j++){
					Button dayHour = new Button(this);
					dayHour.setClickable(true);
					dayHour.setBackgroundColor(Color.WHITE);
					weekHour.addView(dayHour, dayHourLayoutParams);
				}
				weekDayGrid.addView(weekHour, hourLayoutParams);
			}
			break;
		case "Month":
			weekDayGrid.setBackgroundColor(Color.LTGRAY);
			LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 200));
			LinearLayout.LayoutParams weekDayLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(getWeekGridItemWidth(14), 200));
			LinearLayout.LayoutParams daysLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 40));
			weekLayoutParams.setMargins(0, 0, 2, 2);
			weekDayLayoutParams.setMargins(2, 0, 0, 0);
			int weekCount = getWeekCountPerMonth();			
			calendar = Calendar.getInstance();
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			int monthStart = dayOfMonth-(dayOfMonth-1);
			calendar.add(Calendar.DAY_OF_YEAR, -monthStart);
			firstWeekOfMonthDayInYear = calendar.get(Calendar.DAY_OF_YEAR);
			int monthStartWeekDay;
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			monthStartWeekDay = dayOfWeek-(dayOfWeek-1);
			calendar.add(Calendar.DAY_OF_WEEK, -monthStartWeekDay);
			for(int i = 0; i < weekCount; i++){
				daysContainer = new LinearLayout(this);
				setDaysOfWeek(calendar, 0, 14);
				LinearLayout week = new LinearLayout(this);
				week.setBackgroundColor(Color.LTGRAY);
				for(int j = 0; j < 7; j++){
					TextView day = new TextView(this);
					day.setBackgroundColor(Color.WHITE);
					week.addView(day, weekDayLayoutParams);
				}
				weekDayGrid.addView(daysContainer, daysLayoutParams);
				weekDayGrid.addView(week, weekLayoutParams);
			}
		}
	}
	
	
	public void setCalendar(Calendar c){
		calendar = c;
	}
	
	public Calendar getCalendar(){
		return calendar;
	}
	
	public void setHours(){
		hours.setBackgroundColor(Color.LTGRAY);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		for(int i = 0; i <= 23; i++){
			TextView tv = new TextView(this);
			if(i<10)
				tv.setText("0" + String.valueOf(i));
			else
				tv.setText(String.valueOf(i));
//			tv.setRotation(-90f);
			tv.setBackgroundColor(Color.GRAY);
//			tv.setBackgroundColor(Color.parseColor("#e04e4e"));
//			tv.setPadding(5, 2, 5, 2);
			lp.setMargins(2, 0, 2, 2);
			tv.setLayoutParams(lp);
			tv.setGravity(Gravity.TOP);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(12f);
			hours.addView(tv);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public void setEventsShapes(){
		
	}
	
	public static void setPosition(int i){
		position = i;
	}
	
	public static void setActualView(String v){
		actualView = v;
	}
	
	public static String getActualView(){
		return actualView;
	}

	public int getWeekGridItemWidth(int m){
		Display d = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		d.getSize(p);
		int dayWidth = p.x - m;
		return dayWidth/7;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	public void setEventsObjectShape(){
		for(int i = 0; i < list.size(); i++){
			Event current = list.get(i);
			TextView object = new TextView(this);
			object.setText(current.getName());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, calculateObejctHeight(current)));
			rp.setMargins(2, calculateObjectPosition(current), 2, 0);
			AppCalendar ac = db.getCalendarByName(current.getName());
			object.setBackgroundColor(AppCalendar.colorFromStringToInt(ac.getColor()));
			eventsObjectContainer.addView(object, rp);
		}
	}
	
	public int calculateObejctHeight(Event e){
		int[] timeStart = e.getTimeToken("start");
		int[] timeEnd = e.getTimeToken("End");
		int hourInterval = timeEnd[0]-timeStart[0];
		int addHeight = 100/(60/(timeEnd[1]-timeStart[1]));
		int height = (100 * hourInterval) + (100 * addHeight) + (2 * hourInterval);
		return height;
	}
	
	public int calculateObjectPosition(Event e){
		int[] timeStart = e.getTimeToken("start");
		int[] timeEnd = e.getTimeToken("End");
		int hourInterval = timeEnd[0]-timeStart[0];
		int addMargin = 100/(60/timeStart[1]);
		int height = (100 * hourInterval) + (100 * addMargin) + (2 * timeStart[0]);
		return height;
	}
	
	//Return the number of weeks in the current month
	public int getWeekCountPerMonth(){
		Calendar c = Calendar.getInstance();
		int currentDay = c.get(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DAY_OF_MONTH, -(currentDay-1));
		int firstWeekOfMonth = calendar.get(Calendar.WEEK_OF_YEAR);
		int lastDay = getLastDayOfMonth(c.get(Calendar.MONTH)+1);
		int daysToEnd = lastDay-currentDay;
		c.add(Calendar.DAY_OF_MONTH, daysToEnd);
		int lastWeekOfMonth = c.get(Calendar.WEEK_OF_YEAR);
		return lastWeekOfMonth - firstWeekOfMonth + 1;
	}
	
	public int getLastDayOfMonth(int month){
		switch(month){
		case 1: case 3: case 5: case 7: case 8: case 10: case 12:
			return 31;
		case 2:
			if(((GregorianCalendar) calendar).isLeapYear(calendar.get(Calendar.YEAR)))
				return 29;
			else
				return 28;
		case 4: case 6: case 9: case 11:
			return 30;
		}
		return -1;
	}
	
	public int getWeekStartDayInYear(){
		Calendar c = calendar;
		int daysFromMonthStart = calendar.get(Calendar.DAY_OF_MONTH)-(calendar.get(Calendar.DAY_OF_MONTH)-1);
		c.add(Calendar.DAY_OF_MONTH, -daysFromMonthStart);
		int daysFromWeekStart = c.get(Calendar.DAY_OF_WEEK)-(c.get(Calendar.DAY_OF_WEEK)-1);
		return daysFromWeekStart;
	}
}