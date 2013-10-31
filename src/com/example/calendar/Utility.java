package com.example.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.components.AppCalendar;
import com.example.database.MyCalendarDB;
import com.example.mycalendar.CalendarViewDay;
import com.example.mycalendar.CalendarViewMonth;
import com.example.mycalendar.CalendarViewWeek;
import com.example.mycalendar.CalendarViewYear;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;

public class Utility {
	public static ArrayList<String> nameOfEvent = new ArrayList<String>();
	public static ArrayList<String> startDates = new ArrayList<String>();
	public static ArrayList<String> endDates = new ArrayList<String>();
	public static ArrayList<String> descriptions = new ArrayList<String>();
	public static ArrayList<Integer> colors = new ArrayList<Integer>();

	public static ArrayList<String> readCalendarEvent(MyCalendarDB db) {
		Cursor cursor = 
				db.getReadableDatabase().query("event", new String[] { "event_name", "event_date_start", "event_time_start",
								"event_date_end", "event_time_end", "event_calendar", "event_id" }, null,
						null, null, null, "event_date_start");
		cursor.moveToFirst();
		// fetching calendars name
		String CNames[] = new String[cursor.getCount()];

		// fetching calendars id
		nameOfEvent.clear();
		startDates.clear();
		endDates.clear();
		descriptions.clear();
		colors.clear();
		
		for (int i = 0; i < CNames.length; i++) {

			nameOfEvent.add(cursor.getString(0));
//			startDates.add(getDate(Long.parseLong(cursor.getString(3))));
//			endDates.add(getDate(Long.parseLong(cursor.getString(4))));
			startDates.add(cursor.getString(1));
			endDates.add(cursor.getString(3));
			descriptions.add(cursor.getString(6));
			CNames[i] = cursor.getString(0);
			int color = AppCalendar.colorFromStringToInt(db.getSingleCalendar(cursor.getString(5)).getColor());
			colors.add(color);
			cursor.moveToNext();

		}
		return nameOfEvent;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
	
	public static void changeView(View v, Context ctx, Activity from, String selectedView, String actualView){
		Intent intent;
		if(!selectedView.equals(actualView)){
			if(selectedView.equals("Day")){
				intent = new Intent(ctx, CalendarViewDay.class);
				from.startActivity(intent);
			}
			if(selectedView.equals("Week")){
				intent = new Intent(ctx, CalendarViewWeek.class);
				from.startActivity(intent);
			}
			if(selectedView.equals("Month")){
				intent = new Intent(ctx, CalendarViewMonth.class);
				from.startActivity(intent);
			}
			if(selectedView.equals("Year")){
				intent = new Intent(ctx, CalendarViewYear.class);
				from.startActivity(intent);
			}
		}
	}
}


