package com.mycalendar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.mycalendar.R;
import com.mycalendar.calendar.Utility;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	private LinearLayout weekDayGrid;
	private LinearLayout daysContainer;
	ArrayAdapter<CharSequence> adapterViewTypes;
	private MyCalendarDB db;
	private RelativeLayout eventsObjectContainer;
	private static final int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 29}; //the last number represent the days of February for leap years
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		db = MainActivity.getAppDB();
		calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		setInitialState(actualView);
	}
	
	public void setInitialState(String type){
		switch (type){
		case "Day":
			setContentView(R.layout.activity_calendar_view_day);
			calendar = Calendar.getInstance();
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
			calendar = Calendar.getInstance();
			selectViewType = (Spinner) findViewById(R.id.viewTypeWeek);
			selectedTimePeriod = (TextView) findViewById(R.id.selectedWeek);
			hours = (LinearLayout) findViewById(R.id.hoursContainerWeek);
			weekDayGrid = (LinearLayout) findViewById(R.id.backgroundAsGridWeek);
			daysContainer = (LinearLayout) findViewById(R.id.weekDaysContainer);
			setWeekLayoutObjects();
			break;
		case "Month":
			setContentView(R.layout.activity_calendar_view_month);
			calendar = Calendar.getInstance();
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
		setGridItems(null);
	}
	
	public void setWeekLayoutObjects(){
		setHours();
		Calendar c = (Calendar) calendar.clone();
		int today = c.get(Calendar.DAY_OF_WEEK);
		int dayToSub = (today-2);
		c.add(Calendar.DAY_OF_YEAR, -dayToSub);
		setWeek(c);
		setDaysOfWeek(c, 60, 74);
		setGridItems(c);
		
	}
	
	public void setMonthLayoutObjects(){
		setMonth();
		Calendar month = (Calendar) calendar.clone();
		int startDay = (month.get(Calendar.DAY_OF_MONTH));
		month.add(Calendar.DAY_OF_YEAR, -startDay);
		int startDayFirstWeekOfMonth = (month.get(Calendar.DAY_OF_WEEK)-1);
		month.add(Calendar.DAY_OF_YEAR, -startDayFirstWeekOfMonth);
		Toast.makeText(this, "Day of year: " + month.get(Calendar.DAY_OF_YEAR), Toast.LENGTH_LONG).show();
		setGridItems(month);
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
	
	public void setWeek(Calendar c) {
		Calendar week = (Calendar) c.clone();
		week.add(Calendar.DAY_OF_YEAR, 6);
		selectedTimePeriod.setText(c.get(Calendar.DAY_OF_MONTH) + " " + Utility.parseMonthToString(c.get(Calendar.MONTH)) + "-" + week.get(Calendar.DAY_OF_MONTH) + " " + Utility.parseMonthToString(week.get(Calendar.MONTH)));
	}
	
	public void setMonth(){
		selectedTimePeriod.setText(Utility.parseMonthToString(calendar.get(Calendar.MONTH)) + ", " + calendar.get(Calendar.YEAR));
	}
	
	public void setDaysOfWeek(Calendar weekBegin, int emptySpace, int spaceLeft){
		Calendar week = (Calendar) weekBegin.clone();
		int firstDayOfMonth = -1;
		int lastDayOfMonth = -1;
		for(int i = 0; i <= 7; i++){
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams lp = null;
			if(i == 0){
				lp = new LinearLayout.LayoutParams(new LayoutParams(emptySpace, LayoutParams.MATCH_PARENT));
				tv.setBackgroundColor(Color.GRAY);
			}
			else{
				week.add(Calendar.DAY_OF_YEAR, 1);
				lp = new LinearLayout.LayoutParams(new LayoutParams(getWeekGridItemWidth(spaceLeft), LayoutParams.MATCH_PARENT));
				tv.setText(Utility.parseWeekDayToString(week.get(Calendar.DAY_OF_WEEK)) + " " + week.get(Calendar.DAY_OF_MONTH));
				Calendar aux = Calendar.getInstance();
				firstDayOfMonth = (aux.get(Calendar.DAY_OF_YEAR)-aux.get(Calendar.DAY_OF_MONTH)+1);
				lastDayOfMonth = firstDayOfMonth + (monthDays[aux.get(Calendar.MONTH)]-1);
				if(week.get(Calendar.DAY_OF_YEAR) < firstDayOfMonth || week.get(Calendar.DAY_OF_YEAR) > lastDayOfMonth)
					tv.setBackgroundColor(Color.WHITE);
				else
					tv.setBackgroundColor(Color.GRAY);
				tv.setTextSize(12f);
			}
			lp.setMargins(0, 0, 2, 2);
			tv.setLayoutParams(lp);
			tv.setGravity(Gravity.CENTER);
			if(week.get(Calendar.DAY_OF_YEAR) < firstDayOfMonth)
				tv.setTextColor(Color.GRAY);
			else
				tv.setTextColor(Color.WHITE);
			daysContainer.addView(tv);
		}
	}
	
	public void setGridItems(Calendar c){
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
			Calendar month = (Calendar) c.clone();
			int weekCount = getWeekCountPerMonth(month);	
			for(int i = 0; i < weekCount-1; i++){
				daysContainer = new LinearLayout(this);
				setDaysOfWeek(month, 0, 14);
				LinearLayout week = new LinearLayout(this);
				week.setBackgroundColor(Color.LTGRAY);
				for(int j = 0; j < 7; j++){
					TextView day = new TextView(this);
					day.setBackgroundColor(Color.WHITE);
					week.addView(day, weekDayLayoutParams);
				}
				month.add(Calendar.DAY_OF_YEAR, 7);
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
	public int getWeekCountPerMonth(Calendar month){
		Calendar c = (Calendar) month.clone();
		int weekNumberOfMonthStart = c.get(Calendar.WEEK_OF_YEAR);
		int lastDayOfCurrentMonth = monthDays[c.get(Calendar.MONTH)+1];
		c.add(Calendar.DAY_OF_YEAR,  lastDayOfCurrentMonth);
		int weekNumberOfMonthEnd = c.get(Calendar.WEEK_OF_YEAR);
		return weekNumberOfMonthEnd - weekNumberOfMonthStart + 1;
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
	
	@Override
	public void onBackPressed(){
		Intent toHome = new Intent(this, MainActivity.class);
		startActivity(toHome);
	}
}
