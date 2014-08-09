package com.mycalendar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.mycalendar.R;
import com.mycalendar.calendar.Utility;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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

public class EventsView extends Activity implements OnItemSelectedListener, OnClickListener, OnLongClickListener{
	
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
	private static final int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //the last number represent the days of February for leap years
	private Calendar actualTime = null;
	private ArrayList<Event> viewDialogItems;
	private int[] eventsByHourCount = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
	private int[] eventsByDayCount;
	private int[] eventsContainersIDs = {R.id.hour_0, 
			R.id.hour_1,
			R.id.hour_2,
			R.id.hour_3, 
			R.id.hour_4, 
			R.id.hour_5, 
			R.id.hour_6, 
			R.id.hour_7, 
			R.id.hour_8, 
			R.id.hour_9, 
			R.id.hour_10, 
			R.id.hour_11, 
			R.id.hour_12, 
			R.id.hour_13, 
			R.id.hour_14, 
			R.id.hour_15, 
			R.id.hour_16, 
			R.id.hour_17, 
			R.id.hour_18, 
			R.id.hour_19, 
			R.id.hour_20, 
			R.id.hour_21, 
			R.id.hour_22,
			R.id.hour_23};
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		db = MainActivity.getAppDB();
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		actualTime = Calendar.getInstance();
//		actualTime.setFirstDayOfWeek(Calendar.MONDAY);
		setInitialState(actualView);
	}
	
	public void setInitialState(String type){
		switch (type){
		case "Day":
			setContentView(R.layout.activity_calendar_view_day);
			actualTime = MainActivity.actualCalendar;
			selectViewType = (Spinner) findViewById(R.id.viewTypeDay);
			selectedTimePeriod = (TextView) findViewById(R.id.selectedDay);
			hours = (LinearLayout) findViewById(R.id.hoursContainerDay);
			weekDayGrid = (LinearLayout) findViewById(R.id.backgroundAsGridDay);
			eventsObjectContainer = (RelativeLayout) findViewById(R.id.eventsContainerDay);
			list = db.getEventsByDay(actualTime.get(Calendar.DAY_OF_MONTH) + "/" + (actualTime.get(Calendar.MONTH)+1)+"/"+actualTime.get(Calendar.YEAR));
			setDayLayoutObjects();
			setEventsByHourCount();
			setEventByDay();
			break;
		case "Week":
			setContentView(R.layout.activity_calendar_view_week);
			actualTime = MainActivity.actualCalendar;
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
			actualTime = MainActivity.actualCalendar;
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
		selectViewType.setSelection(getActualViewIndex(), false);
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
			setInitialState(viewT);
		}
	}
	
	public void setDayLayoutObjects(){
		setDay();
//		setHours();
		setGridItems();
	}
	
	public void setWeekLayoutObjects(){
		weekDayGrid.removeAllViews();
		daysContainer.removeAllViews();
//		hours.removeAllViews();
//		setHours();
//		Calendar c = (Calendar) actualTime.clone();
		int today = actualTime.get(Calendar.DAY_OF_WEEK);
		int dayToSub = (today-2);
		actualTime.add(Calendar.DAY_OF_YEAR, -dayToSub);
		setWeek(actualTime);
		setDaysOfWeek(actualTime, 60, 74);
		setGridItems();
		
	}
	
	public void setMonthLayoutObjects(){
		int startDay;
		if(actualTime.get(Calendar.DAY_OF_MONTH) > 1){
			startDay = (actualTime.get(Calendar.DAY_OF_MONTH));
			actualTime.add(Calendar.DAY_OF_MONTH, -(startDay-1));
		}
		setMonth();
		Toast.makeText(this, "Day of month: " + actualTime.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
		Toast.makeText(this, "Month: " + actualTime.get(Calendar.MONTH), Toast.LENGTH_SHORT).show();
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
		if(actualTime == null)
			actualTime = (Calendar) calendar.clone();
		selectedTimePeriod.setText(Utility.parseWeekDayToString(actualTime.get(Calendar.DAY_OF_WEEK)) + ", " 
		+ actualTime.get(Calendar.DAY_OF_MONTH) + " "
		+ Utility.parseMonthToString(actualTime.get(Calendar.MONTH)) + " " 
		+ actualTime.get(Calendar.YEAR));
	}
	
	public void setWeek(Calendar c) {
		Calendar week = (Calendar) c.clone();
		week.add(Calendar.DAY_OF_YEAR, 6);
		selectedTimePeriod.setText(c.get(Calendar.DAY_OF_MONTH) + " " + Utility.parseMonthToString(c.get(Calendar.MONTH)) + "-" + week.get(Calendar.DAY_OF_MONTH) + " " + Utility.parseMonthToString(week.get(Calendar.MONTH)));
	}
	
	public void setMonth(){
		selectedTimePeriod.setText(Utility.parseMonthToString(actualTime.get(Calendar.MONTH)) + ", " + actualTime.get(Calendar.YEAR));
	}
	
	public void setDaysOfWeek(Calendar weekBegin, int emptySpace, int spaceLeft){
		Calendar week = (Calendar) weekBegin.clone();
		int firstDayOfMonth = -1;
		int lastDayOfMonth = -1;
		firstDayOfMonth = actualTime.get(Calendar.DAY_OF_YEAR);
		lastDayOfMonth = firstDayOfMonth + (monthDays[actualTime.get(Calendar.MONTH)]-1);
		for(int i = 0; i <= 7; i++){
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams lp = null;
			if(i == 0){
				lp = new LinearLayout.LayoutParams(new LayoutParams(emptySpace, LayoutParams.MATCH_PARENT));
				tv.setBackgroundColor(Color.GRAY);
			}
			else{
				lp = new LinearLayout.LayoutParams(new LayoutParams(getWeekGridItemWidth(spaceLeft), LayoutParams.MATCH_PARENT));
				tv.setText(Utility.parseWeekDayToString(week.get(Calendar.DAY_OF_WEEK)) + " " + week.get(Calendar.DAY_OF_MONTH));
				if(week.get(Calendar.DAY_OF_YEAR) < firstDayOfMonth || week.get(Calendar.DAY_OF_YEAR) > lastDayOfMonth)
					tv.setBackgroundColor(Color.WHITE);
				else
					tv.setBackgroundColor(Color.GRAY);
				tv.setTextSize(12f);
				if(week.get(Calendar.DAY_OF_YEAR) < firstDayOfMonth || week.get(Calendar.DAY_OF_YEAR) > lastDayOfMonth)
					tv.setTextColor(Color.GRAY);
				else
					tv.setTextColor(Color.WHITE);
				week.add(Calendar.DAY_OF_YEAR, 1);
			}
			lp.setMargins(0, 0, 2, 2);
			tv.setLayoutParams(lp);
			tv.setGravity(Gravity.CENTER);
			daysContainer.addView(tv);
		}
	}
	
	public void setGridItems(){
		switch(actualView){
		case "Day":
//			weekDayGrid.setBackgroundColor(Color.LTGRAY);
//			LinearLayout.LayoutParams glp = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
//			glp.setMargins(0, 0, 2, 2);
//			for(int i = 0; i < 24; i++){
//				Button dayHour = new Button(this);
//				dayHour.setBackgroundColor(Color.WHITE);
//				dayHour.setClickable(true);
//				weekDayGrid.addView(dayHour, glp);
//			}
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
					dayHour.setOnClickListener(this);
					dayHour.setBackground(getResources().getDrawable(R.drawable.button_state_day));
					dayHour.setTag(String.valueOf(i) + String.valueOf(j+1));
					weekHour.addView(dayHour, dayHourLayoutParams);
				}
				weekDayGrid.addView(weekHour, hourLayoutParams);
			}
			break;
		case "Month":
			weekDayGrid.setBackgroundColor(Color.LTGRAY);
			if(weekDayGrid.getChildCount() != 0)
				weekDayGrid.removeAllViews();
			LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 200));
			LinearLayout.LayoutParams weekDayLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(getWeekGridItemWidth(14), 200));
			LinearLayout.LayoutParams daysLayoutParams = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 40));
			weekLayoutParams.setMargins(0, 0, 2, 2);
			weekDayLayoutParams.setMargins(2, 0, 0, 0);
			Calendar month = (Calendar) actualTime.clone();
			if(month.get(Calendar.DAY_OF_WEEK) != 2){
				int startDayFirstWeekOfMonth = -1;
				if(month.get(Calendar.DAY_OF_WEEK) == 1)
					month.add(Calendar.DAY_OF_YEAR, +1);
				else{
					startDayFirstWeekOfMonth = (actualTime.get(Calendar.DAY_OF_WEEK)-2);
					month.add(Calendar.DAY_OF_YEAR, -startDayFirstWeekOfMonth);
				}
			}
			
			int weekCount = getWeekCountPerMonth();
			for(int i = 0; i <= weekCount; i++){
				daysContainer = new LinearLayout(this);
				setDaysOfWeek(month, 0, 14);
				LinearLayout week = new LinearLayout(this);
				week.setBackgroundColor(Color.LTGRAY);
				for(int j = 0; j < 7; j++){
					TextView day = new TextView(this);
					day.setBackgroundColor(Color.WHITE);
					day.setOnClickListener(this);
					day.setOnLongClickListener(this);
					day.setBackground(getResources().getDrawable(R.drawable.button_state_day));
					day.setTag(String.valueOf(i+1) + String.valueOf(j+1));
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
		
	}
	
	public void setEventsShapes(){
		
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
		 switch(actualView){
		 case "Week":
			 onWeekHourClick(v);
			 break;
		 case "Month":
			Intent toEditor = new Intent(this, EventEditor.class);
			String tag = (String) v.getTag();
			int dayOfWeek = actualTime.get(Calendar.DAY_OF_WEEK);
			int hourIdx = Integer.parseInt(tag.substring(0,1));
			int index = Integer.parseInt(tag.substring(1,2));
			Toast.makeText(this, "Day selected before: " + actualTime.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
			actualTime.add(Calendar.DAY_OF_MONTH, -(dayOfWeek-1));
			Toast.makeText(this, "Day selected: " + actualTime.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
			actualTime.add(Calendar.DAY_OF_MONTH, (((hourIdx-1)*7)+index));
			toEditor.putExtra(Event.S_DATE, new int[] {actualTime.get(Calendar.DAY_OF_MONTH), (actualTime.get(Calendar.MONTH)), actualTime.get(Calendar.YEAR)});
			toEditor.putExtra(Event.S_TIME, String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1)); 
			startActivity(toEditor);
			break;
		 }
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
		Calendar c = (Calendar) actualTime.clone();
		int weekNumberOfMonthStart = c.get(Calendar.WEEK_OF_YEAR);
		int lastDayOfCurrentMonth = monthDays[c.get(Calendar.MONTH)];
		c.add(Calendar.DAY_OF_YEAR,  (lastDayOfCurrentMonth-1));
		int weekNumberOfMonthEnd = c.get(Calendar.WEEK_OF_YEAR);
		if(c.get(Calendar.MONTH) == 11)
			return 52 - weekNumberOfMonthStart + 1;
		return weekNumberOfMonthEnd - weekNumberOfMonthStart;
	}
	
	public int getLastDayOfMonth(int month){
		switch(month){
		case 1: case 3: case 5: case 7: case 8: case 10: case 12:
			return 31;
		case 2:
			if(((GregorianCalendar) actualTime).isLeapYear(actualTime.get(Calendar.YEAR)))
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
	
	public void onPrevDayClick(View v){
		actualTime.add(Calendar.DAY_OF_YEAR, -1);
		list = db.getEventsByDay(actualTime.get(Calendar.DAY_OF_MONTH) + "/" + (actualTime.get(Calendar.MONTH)+1)+"/"+actualTime.get(Calendar.YEAR));
		setEventsByHourCount();
		setEventByDay();
		setDay();
	}
	
	public void onNextDayClick(View v){
		actualTime.add(Calendar.DAY_OF_YEAR, 1);
		list = db.getEventsByDay(actualTime.get(Calendar.DAY_OF_MONTH) + "/" + (actualTime.get(Calendar.MONTH)+1)+"/"+actualTime.get(Calendar.YEAR));
		setEventsByHourCount();
		setEventByDay();
		setDay();
	}
	
	public void onPrevWeekClick(View v){
		actualTime.add(Calendar.DAY_OF_YEAR, -7);
		setWeekLayoutObjects();
	}
	
	public void onNextWeekClick(View v){
		actualTime.add(Calendar.DAY_OF_YEAR, 7);
		setWeekLayoutObjects();
	}
	
	public void onPrevMonthClick(View v){
		actualTime.add(Calendar.MONTH, -1);
		setMonthLayoutObjects();
	}
	
	public void onNextMonthClick(View v){
		int daysToAdd = getLastDayOfMonth(actualTime.get(Calendar.MONTH)+1);
		actualTime.add(Calendar.DAY_OF_YEAR, daysToAdd);
		setMonthLayoutObjects();
	}
	
	public int getActualViewIndex(){
		switch(actualView){
		case "Day":
			return 0;
		case "Week":
			return 1;
		case "Month":
			return 2;
		}
		return -1;
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
	
	public void onHourClick(View v){
		Intent toEditor = new Intent(this, EventEditor.class);
		int[] sDate = {actualTime.get(Calendar.DAY_OF_MONTH), actualTime.get(Calendar.MONTH), actualTime.get(Calendar.YEAR)}; 
		toEditor.putExtra(Event.S_DATE, sDate);
		toEditor.putExtra(Event.S_TIME, (String) v.getTag());
		startActivity(toEditor);
	}
	
	public void onWeekHourClick(View v){
		Intent toEditor = new Intent(this, EventEditor.class);
		String tag = (String) v.getTag();
		Calendar c = (Calendar) actualTime.clone();
		int hourIdx = Integer.parseInt(tag.substring(0,1));
		int index = Integer.parseInt(tag.substring(1,2));
		c.add(Calendar.DAY_OF_MONTH, +(index-1));
		toEditor.putExtra(Event.S_DATE, new int[] {c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.MONTH)), c.get(Calendar.YEAR)});
		toEditor.putExtra(Event.S_TIME, String.valueOf(hourIdx)); 
		startActivity(toEditor);
	}
	
	public void onMonthDayClick(View v){
		actualView = "Day";
		String tag = (String) v.getTag();
		setContentView(R.layout.activity_calendar_view_day);
		int dayOfWeek = actualTime.get(Calendar.DAY_OF_WEEK);
		int hourIdx = Integer.parseInt(tag.substring(0,1));
		int index = Integer.parseInt(tag.substring(1,2));
		Toast.makeText(this, "Day selected before: " + actualTime.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
		actualTime.add(Calendar.DAY_OF_MONTH, -(dayOfWeek-1));
		Toast.makeText(this, "Day selected: " + actualTime.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
		actualTime.add(Calendar.DAY_OF_MONTH, (((hourIdx-1)*7)+index));
		setInitialState(actualView);
	}
	
	/**
	 * It sets the number of events for each hour as button text.
	 */
	public void setEventByDay(){
		for(int i = 0; i < 24; i++){
			Button b = (Button) findViewById(eventsContainersIDs[i]);
			b.setOnLongClickListener(this);
			if(eventsByHourCount[i] > 0){
				b.setText(eventsByHourCount[i] + " events");
				b.setBackgroundColor(Color.LTGRAY);
			}
		}
	}
	
	/**
	 * It counts the event of that day for each hour.
	 */
	public void setEventsByHourCount(){
		for(Event e: list.toArray(new Event[]{})){
			int[] time = e.getTimeToken("start");
			Toast.makeText(this, "TIME: " + time[0], Toast.LENGTH_SHORT).show();
			eventsByHourCount[time[0]]++; 
		}
	}

	@Override
	public boolean onLongClick(View arg0) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final Context ctx = this;
		switch(actualView){
		case "Day":
			builder.setTitle("Your events for today");
			int hour = Integer.parseInt((String) arg0.getTag());
			viewDialogItems = db.getEventsFromTime(actualTime.get(Calendar.DAY_OF_MONTH), actualTime.get(Calendar.MONTH)+1, actualTime.get(Calendar.YEAR), hour, 0);
			ArrayList<String> items = new ArrayList<String>();
			for(Event ev: viewDialogItems.toArray(new Event[]{})){
				String s = ev.getName();
				items.add(s);
			}
			if(viewDialogItems.size() > 0)
				builder.setItems(items.toArray(new String[]{}), new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toShow = new Intent(ctx, EventShow.class);
						toShow.putExtra(Event.ID, viewDialogItems.get(which).getId());
						ctx.startActivity(toShow);
					}
				});
			else
				builder.setMessage("No events.");
			builder.create();
			builder.show();
			break;
		case "Month":
			onMonthDayClick(arg0);
		}
		return false;
	}
	
	public void setEventsByMonth(){
		eventsByDayCount = new int[monthDays[actualTime.get(Calendar.MONTH)]];
		for(int i = 0; i < eventsByDayCount.length; i++){
			list = db.getEventsByDay(i + "/" + (actualTime.get(Calendar.MONTH)+1)+"/"+actualTime.get(Calendar.YEAR));
			
		}
	}
}
