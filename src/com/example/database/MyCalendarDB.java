package com.example.database;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

import com.example.components.AppCalendar;
import com.example.components.Event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class manage the connections and the data retrieval/updates with the app database.
 * @author Luca Bellettati
 *
 */
public class MyCalendarDB extends SQLiteOpenHelper {
	
	/**
	 * Constant fiels for the database name.
	 */
	private static final String DB_NAME = "mycalendar";
	
	/**
	 * Constant field for the database version.
	 */
	private static final int DB_VERSION = 2;
	
	/**
	 * Constant string for the creation of the events table.
	 */
	public static final String EVENTS_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS event " +
			"(event_name VARCHAR(40), " +
			"event_date_start DATE, " +
			"event_time_start TIME, " +
			"event_date_end DATE," +
			"event_time_end TIME," +
			"event_calendar VARCHAR(30));";
	public static final String CALENDAR_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS calendar " +
					"(calendar_name VARCHAR(40), " +
					"calendar_color VARCHAR(15));"; 

	/**
	 * It calls the upper constructor to create or open the app database.
	 * @param context the current context
	 */
	public MyCalendarDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * When the database is created, it is called and executes the sql statement for the creation of the events table.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("DROP TABLE IF EXISTS event;");
//		db.execSQL("DROP TABLE IF EXISTS calendar;");
		db.execSQL(EVENTS_TABLE_CREATE);
		db.execSQL(CALENDAR_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onOpen(SQLiteDatabase db){
//		db.execSQL("DROP TABLE IF EXISTS event;");
//		db.execSQL("DROP TABLE IF EXISTS calendar;");
		db.execSQL(EVENTS_TABLE_CREATE);
		db.execSQL(CALENDAR_TABLE_CREATE);
	}
	
	/**
	 * It adds an event to the events table.
	 * @param name event name
	 * @param startDate event start date
	 * @param endDate event end date
	 * @param startTime event start time
	 * @param endTime event end time
	 * @param calendar event calendar
	 * @param db the database
	 */
	public long addEvent(Event anEvent){
		ContentValues toInsert = new ContentValues();
		toInsert.put("event_name", anEvent.getName());
		toInsert.put("event_date_start", convertDateFromStringToDB(anEvent.getStartDate()));
		toInsert.put("event_date_end", convertDateFromStringToDB(anEvent.getEndDate()));
		toInsert.put("event_time_start", anEvent.getStartTime());
		toInsert.put("event_time_end", anEvent.getEndTime());
		toInsert.put("event_calendar", anEvent.getCalendar());
		return this.getWritableDatabase().insert("event", null, toInsert);
	}
	
	public long addCalendar(AppCalendar aCalendar){
		ContentValues toInsert = new ContentValues();
		toInsert.put("calendar_name", aCalendar.getName());
		toInsert.put("calendar_color", aCalendar.getColor());
		return this.getWritableDatabase().insert("calendar", null, toInsert);
	}
	
	public AppCalendar[] getCompleteCalendarList(SQLiteDatabase db){
		String[] columns = {"calendar_name", "calendar_color"};
		Cursor result = db.query("calendar", columns, null, null, null, null, null);
		AppCalendar[] calendarList = new AppCalendar[result.getCount()];
		result.moveToFirst();
		for(int i = 0; i < result.getCount(); i++){
			if(!result.isAfterLast()){
				String name = result.getString(0);
				String color = result.getString(1);
				AppCalendar aCalendar = new AppCalendar(name, color);
				calendarList[i] = aCalendar;
				result.moveToNext();
			}
		}
		return calendarList;
	} 
	
	public String[] getCalendarList(){
		String[] columns = {"calendar_name"};
		Cursor result = this.getReadableDatabase().query("calendar", columns, null, null, null, null, null);
		String[] calendarList = new String[result.getCount()];
		result.moveToFirst();
		for(int i = 0; i < result.getCount(); i++){
			if(!(result.isAfterLast())){
				calendarList[i] = result.getString(0);
				result.moveToNext();
			}
		}
		return calendarList;
	}
	
	public AppCalendar getSingleCalendar(String calendarName){
		String[] values = {calendarName};
		Cursor result = this.getReadableDatabase().query("calendar", null, "calendar_name=?", values, null, null, null);
		result.moveToFirst();
		return new AppCalendar(result.getString(0), result.getString(1));
	}
	
	public int removeCalendar(String calendarName){
		AppCalendar toDelete = getSingleCalendar(calendarName);
		String[] queryArgs = {toDelete.getName(), toDelete.getColor()};
		String[] columns = {"event_name"};
		String[] selArgs = {calendarName};
		Cursor eventList = this.getWritableDatabase().query("event", columns, "event_calendar=?", selArgs, null, null, null);
		for(int i = 0; i < eventList.getCount(); i++){
			this.getWritableDatabase().delete("event", "event_calendar=?", selArgs);
		}
		return this.getWritableDatabase().delete("calendar", "calendar_name=? AND calendar_color=?", queryArgs); 
	}
	
	public long updateCalendar(AppCalendar old, AppCalendar modified){
		ContentValues values = new ContentValues();
		values.put("calendar_name", modified.getName());
		values.put("calendar_color", modified.getColor());
		String[] whereArgs = {old.getName(), old.getColor()};
		return this.getWritableDatabase().update("calendar", values, "calendar_name=? AND calendar_color=?", whereArgs);
	}
	
	public String[] getEventList(){
		Cursor result = this.getReadableDatabase().query("event", null, null, null, null, null, "event_date_start");
		result.moveToFirst();
		Event[] listNormal = new Event[result.getCount()];  
		String[] list = new String[result.getCount()];
		for(int i=0; i<result.getCount(); i++){
			if(!result.isAfterLast()){
				Event toInsert = new Event(result.getString(0), 
						convertDateFromDBToString(result.getString(1)), 
						convertDateFromDBToString(result.getString(3)), 
						result.getString(2), 
						result.getString(4), 
						result.getString(5));
				listNormal[i] = toInsert;
				result.moveToNext();
			}
		}
//		listNormal = sort(listNormal);
		for(int i=0; i<result.getCount(); i++){
			list[i] = Array.get(listNormal, i).toString();
		}
		return list;
	}
	
	public Event[] sort(Event[] toSort){
		Arrays.sort(toSort, new Comparator<Event>() {
			
			public int compare(Event t1, Event t2){
				if(Array.getInt(t1.getDateToken("start"), 0) > Array.getInt(t2.getDateToken("start"), 0))
					return 1;
				else if(Array.getInt(t1.getDateToken("start"), 0) < Array.getInt(t2.getDateToken("start"), 0))
					return -1;
				else if(Array.getInt(t1.getDateToken("start"), 0) == Array.getInt(t2.getDateToken("start"), 0)){
					if(Array.getInt(t1.getDateToken("start"), 1) > Array.getInt(t2.getDateToken("start"), 1))
						return 1;
					else if(Array.getInt(t1.getDateToken("start"), 1) < Array.getInt(t2.getDateToken("start"), 1))
						return -1;
					else if(Array.getInt(t1.getDateToken("start"), 1) == Array.getInt(t2.getDateToken("start"), 1)){
						if(Array.getInt(t1.getDateToken("start"), 2) > Array.getInt(t2.getDateToken("start"), 2))
							return 1;
						else if(Array.getInt(t1.getDateToken("start"), 2) < Array.getInt(t2.getDateToken("start"), 2))
							return -1;
						else if(Array.getInt(t1.getDateToken("start"), 2) == Array.getInt(t2.getDateToken("start"), 2)){
							if(Array.getInt(t1.getTimeToken("start"), 0) > Array.getInt(t2.getDateToken("start"), 1))
								return 1;
							else if(Array.getInt(t1.getTimeToken("start"), 0) < Array.getInt(t2.getTimeToken("start"), 0))
								return -1;
							else if(Array.getInt(t1.getTimeToken("start"), 0) == Array.getInt(t2.getTimeToken("start"), 0)){
								if(Array.getInt(t1.getTimeToken("start"), 1) > Array.getInt(t2.getTimeToken("start"), 1))
									return 1;
								else if(Array.getInt(t1.getTimeToken("start"), 1) < Array.getInt(t2.getTimeToken("start"), 1))
									return -1;
								else if(Array.getInt(t1.getTimeToken("start"), 1) == Array.getInt(t2.getTimeToken("start"), 0))
									return 0;
							}
						}
					}
				}
				return 2;
			}
		});
		
		return toSort;
	} 
	
	public Event getSingleEvent(Event toSearch){
		String[] values = {toSearch.getName(), 
				convertDateFromStringToDB(toSearch.getStartDate()), 
				convertDateFromStringToDB(toSearch.getEndDate()), 
				toSearch.getStartTime(), 
				toSearch.getEndTime(), 
				toSearch.getCalendar()};
		Cursor result = this.getReadableDatabase().query("event", 
				null, 
				"event_name=? AND event_date_start=? AND event_date_end=? AND event_time_start=? AND event_time_end=? AND event_calendar=?", 
				values, 
				null, 
				null, 
				null);
		result.moveToFirst();
		return new Event(result.getString(0), 
				convertDateFromDBToString(result.getString(1)),
				convertDateFromDBToString(result.getString(2)),
				result.getString(3),
				result.getString(4),
				result.getString(5));
	}
	
	public String convertDateFromDBToString(String inputDate){
		String outputDate = "";
		StringTokenizer tz = new StringTokenizer(inputDate, "-");
		String day = tz.nextToken();
		String month = tz.nextToken();
		String year = tz.nextToken();
		outputDate = day+"/"+month+"/"+year;
		return outputDate;
	}
	
	public String convertDateFromStringToDB(String inputDate){
		String outputDate = "";
		StringTokenizer tz = new StringTokenizer(inputDate, "/");
		String day = tz.nextToken();
		String month = tz.nextToken();
		String year = tz.nextToken();
		outputDate = year+"-"+month+"-"+day;
		return outputDate;
	}
}
