package com.mycalendar.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.mycalendar.activity.EventsView;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.database.MyCalendarDB;

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
			int color = AppCalendar.colorFromStringToInt(db.getSingleCalendarByName(cursor.getString(5)).getColor());
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
	
	public static String parseWeekDayToString(int day){
		switch(day){
		case 1:
			return "Sun";
		case 2:
			return "Mon";
		case 3: 
			return "Tue";
		case 4: 
			return "Wed";
		case 5:
			return "Thu";
		case 6:
			return "Fri";
		case 7:
			return "Sat";
		}
		return "";
	}
	
	public static String parseMonthToString(int month){
		switch(month){
		case 0:
			return "Jan";
		case 1:
			return "Feb";
		case 2:
			return "Mar";
		case 3: 
			return "Apr";
		case 4: 
			return "May";
		case 5:
			return "Jun";
		case 6:
			return "Jul";
		case 7:
			return "Aug";
		case 8:
			return "Sep";
		case 9:
			return "Oct";
		case 10:
			return "Nov";
		case 11:
			return "Dec";
		}
		return "";
	}
}


