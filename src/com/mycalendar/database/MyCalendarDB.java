package com.mycalendar.database;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
			"CREATE TABLE IF NOT EXISTS events " +
			"(event_id INTEGER PRIMARY KEY,"+ 
			"event_name VARCHAR(40), " +
			"event_start_date DATE, " +
			"event_start_time TIME, " +
			"event_end_date DATE," +
			"event_end_time TIME," +
			"event_calendar_id INTEGER,"
			+ "event_all_day INTEGER DEFAULT 0,"
			+ "event_notes VARCHAR(255) DEFAULT NULL,"
			+ "UNIQUE(event_name, event_start_date, event_start_time, event_calendar_id));";
	
	public static final String CALENDAR_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS calendars " +
					"(calendar_id INTEGER PRIMARY KEY ,"
					+ "calendar_name VARCHAR(40), " +
					"calendar_color VARCHAR(15),"
					+ "calendar_email VARCHAR(70) DEFAULT NULL,"
					+ "UNIQUE(calendar_name, calendar_color, calendar_email));"; 
	
	public static final String SYNCH_CALENDAR_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS synch_calendars " +
					"(synch_calendar_id INTEGER AUTO INCREMENT,"
					+ "synch_calendar_name VARCHAR(40));"; 
	
	public static final String REMINDER_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS reminders " +
					"(reminder_id INTEGER,"
					+ "reminder_event_id INTEGER "
					+ "reminder_date_time VARCHAR(30),"
					+ "reminder_time_chosen VARCHAR(10));";  
	
	private Context ctx;

	/**
	 * It calls the upper constructor to create or open the app database.
	 * @param context the current context
	 */
	public MyCalendarDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		ctx = context;
	}

	/**
	 * When the database is created, it is called and executes the sql statement for the creation of the events table.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("DROP TABLE IF EXISTS events;");
//		db.execSQL("DROP TABLE IF EXISTS calendars;");
//		db.execSQL("DROP TABLE IF EXISTS synch_calendars;");
//		db.execSQL("DROP TABLE IF EXISTS reminders;");
		db.execSQL(EVENTS_TABLE_CREATE);
		db.execSQL(CALENDAR_TABLE_CREATE);
		db.execSQL(SYNCH_CALENDAR_TABLE_CREATE);
		db.execSQL(REMINDER_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onOpen(SQLiteDatabase db){
//		db.execSQL("DROP TABLE IF EXISTS events;");
//		db.execSQL("DROP TABLE IF EXISTS calendars;");
//		db.execSQL("DROP TABLE IF EXISTS synch_calendars;");
//		db.execSQL("DROP TABLE IF EXISTS reminders;");
		db.execSQL(EVENTS_TABLE_CREATE);
		db.execSQL(CALENDAR_TABLE_CREATE);
		db.execSQL(SYNCH_CALENDAR_TABLE_CREATE);
		db.execSQL(REMINDER_TABLE_CREATE);
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
		Toast.makeText(ctx, "Calendar ID: " + getCalendarByName(anEvent.getCalendar()).getID(), Toast.LENGTH_SHORT).show();
		ContentValues toInsert = new ContentValues();
		toInsert.put("event_name", anEvent.getName());
		toInsert.put("event_start_date", convertDateFromStringToDB(anEvent.getStartDate()));
		toInsert.put("event_end_date", convertDateFromStringToDB(anEvent.getEndDate()));
		toInsert.put("event_start_time", anEvent.getStartTime());
		toInsert.put("event_end_time", anEvent.getEndTime());
		toInsert.put("event_calendar_id", getCalendarByName(anEvent.getCalendar()).getID());
		toInsert.put("event_all_day", anEvent.getAllDayParsed());
		return this.getWritableDatabase().insert("events", null, toInsert);
	}
	
	public long updateEvent(Event update){
		ContentValues toInsert = new ContentValues();
		toInsert.put("event_name", update.getName());
		toInsert.put("event_start_date", convertDateFromStringToDB(update.getStartDate()));
		toInsert.put("event_end_date", convertDateFromStringToDB(update.getEndDate()));
		toInsert.put("event_start_time", update.getStartTime());
		toInsert.put("event_end_time", update.getEndTime());
		toInsert.put("event_calendar_id", getCalendarByName(update.getCalendar()).getID());
		toInsert.put("event_all_day", update.getAllDayParsed());
		String[] whereArgs = {String.valueOf(update.getId())};
		return this.getWritableDatabase().update("events", toInsert, "event_id=?", whereArgs);
	}
	
	public long removeEvent(int id){
		return this.getWritableDatabase().delete("events", "event_id=?", new String[] {String.valueOf(id)});		
	}
	
	public long addCalendar(AppCalendar aCalendar){
		ContentValues toInsert = new ContentValues();
		toInsert.put("calendar_name", aCalendar.getName());
		toInsert.put("calendar_color", aCalendar.getColor());
		return this.getWritableDatabase().insert("calendars", null, toInsert);
	}
	
	public AppCalendar[] getCompleteCalendarList(SQLiteDatabase db){
		String[] columns = {"calendar_name", "calendar_color"};
		Cursor result = db.query("calendars", columns, null, null, null, null, null);
		AppCalendar[] calendarList = new AppCalendar[result.getCount()];
		result.moveToFirst();
		for(int i = 0; i < result.getCount(); i++){
			if(!result.isAfterLast()){
				String name = result.getString(1);
				String color = result.getString(2);
				String email = result.getString(3); 
				AppCalendar aCalendar = new AppCalendar(name, color, email);
				calendarList[i] = aCalendar;
				result.moveToNext();
			}
		}
		return calendarList;
	} 
	
	public String[] getCalendarList(){
		String[] columns = {"calendar_name"};
		Cursor result = this.getReadableDatabase().query("calendars", columns, null, null, null, null, null);
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
	
	public AppCalendar getSingleCalendarByName(String calendarName){
		String[] values = {calendarName};
		Cursor result = this.getReadableDatabase().query("calendars", null, "calendar_name=?", values, null, null, null);
		result.moveToFirst();
		return new AppCalendar(result.getString(1), result.getString(2), result.getString(3));
	}
	
	public AppCalendar getCalendarByID(int id){
		String[] values = {String.valueOf(id)};
		Cursor result = this.getReadableDatabase().query("calendars", null, "ROWID=?", values, null, null, null);
		boolean check = result.moveToFirst();
		if(check){
			AppCalendar ac = new AppCalendar(result.getString(1), result.getString(2), result.getString(3));
			ac.setID(Integer.parseInt(result.getString(0)));
			return ac;
		}
		else
			return null;
	}
	
	public AppCalendar getCalendarByName(String name){
		String[] values = {name};
		Cursor result = this.getReadableDatabase().query("calendars", null, "calendar_name=?", values, null, null, null);
		result.moveToFirst();
		AppCalendar ac = new AppCalendar(result.getString(1), result.getString(2), result.getString(3));
		ac.setID(result.getInt(0));
		return ac;
	}
	
	public int removeCalendar(int calendarID){
		String[] selArgs = {String.valueOf(calendarID)};
		this.getWritableDatabase().delete("events", "event_calendar_id=?", selArgs);
		return this.getWritableDatabase().delete("calendars", "calendar_id=?", selArgs); 
	}
	
	public long updateCalendar(AppCalendar old, AppCalendar modified){
		ContentValues values = new ContentValues();
		values.put("calendar_name", modified.getName());
		values.put("calendar_color", modified.getColor());
		String[] whereArgs = {old.getName(), old.getColor()};
		return this.getWritableDatabase().update("calendar", values, "calendar_name=? AND calendar_color=?", whereArgs);
	}
	
	public String[] getEventNameList(){
		Cursor result = this.getReadableDatabase().query("events", null, null, null, null, null, "event_start_date");
		result.moveToFirst();
		Event[] listNormal = new Event[result.getCount()];  
		String[] list = new String[result.getCount()];
		for(int i=0; i<result.getCount(); i++){
			if(!result.isAfterLast()){
				Event toInsert = new Event(result.getString(1), 
						convertDateFromDBToString(result.getString(2)), 
						convertDateFromDBToString(result.getString(4)), 
						result.getString(3), 
						result.getString(5), 
						getCalendarByID(Integer.parseInt(result.getString(6))).getName());
				toInsert.setId(Integer.parseInt(result.getString(0)));
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
	
	public ArrayList<Event> getEventList(){
		Cursor result = this.getReadableDatabase().query("events", null, null, null, null, null, "event_start_date");
		ArrayList<Event> e = new ArrayList<Event>();
		boolean count = result.moveToFirst();
		int rows = result.getCount();
		for(int i = 0; count && (i < rows); i++){
			Event ev = new Event(result.getString(1), 
						convertDateFromDBToString(result.getString(2)), 
						convertDateFromDBToString(result.getString(4)), 
						result.getString(3), 
						result.getString(5), 
						getCalendarByID(Integer.parseInt(result.getString(6))).getName());
			ev.setId(Integer.parseInt(result.getString(0)));
			ev.setAllDay(result.getInt(7));
			e.add(ev);
			result.moveToNext();
		}
		
		return e;
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
		Cursor result = this.getReadableDatabase().query("events", 
				null, 
				"event_name=? AND event_start_date=? AND event_end_date=? AND event_start_time=? AND event_end_time=? AND event_calendar_id=?", 
				values, 
				null, 
				null, 
				null);
		result.moveToFirst();
		Event toReturn = new Event(result.getString(1), 
				convertDateFromDBToString(result.getString(2)), 
				convertDateFromDBToString(result.getString(4)), 
				result.getString(3), 
				result.getString(5), 
				getCalendarByID(result.getInt(6)).getName());
		toReturn.setId(Integer.parseInt(result.getString(0)));
		return toReturn;
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
	
	public long addReminder(int eventID, String dateTime, String before){
		ContentValues values = new ContentValues();
		values.put("reminder_event_id", eventID);
		values.put("reminder_date_time", dateTime);
		values.put("remider_time_chosen", before);
		return this.getWritableDatabase().insert("reminders", null, values);
	}
	
	public boolean checkEventUnique(String name, String date, String time, int calendar){
		String[] values = {name, date, time, String.valueOf(calendar)};
		String[] columns = {"event_name", "event_start_date", "event_start_time", "event_calendar_id"};
		Cursor result = getReadableDatabase().query("events", columns, "event_name=? AND event_start_date=? AND event_start_time=? AND event_calendar_id=?", values, null, null, null);
		return result.moveToFirst();
	}
	
	public boolean checkEvents(){
		Cursor result = this.getReadableDatabase().query("events", null, null, null, null, null, "event_start_date");
		return result.moveToFirst();
	}
	
	public ArrayList<Event> getEventsByDay(String date){
		String[] values = {date};
		Cursor result = this.getReadableDatabase().query("events", null, "event_start_date=?", values, null, null, "event_start_time");
		ArrayList<Event> list = new ArrayList<Event>();
		boolean check = result.moveToFirst();
		if(check){
			Event e = new Event(result.getString(1), 
				convertDateFromDBToString(result.getString(2)), 
				convertDateFromDBToString(result.getString(4)), 
				result.getString(3), 
				result.getString(5), 
				getCalendarByID(result.getInt(6)).getName());
			e.setId(Integer.parseInt(result.getString(0)));
			list.add(e);
		}
		return list;
	}
	
	public Event getEventByID(int id){
		Cursor result = getReadableDatabase().query("events", null, "ROWID=?", new String[] {String.valueOf(id)}, null, null, null);
		Toast.makeText(ctx, "Result: " + result.moveToFirst(), Toast.LENGTH_SHORT).show();
		if(result.moveToFirst()){
			Event e = new Event(result.getString(1), 
					convertDateFromDBToString(result.getString(2)), 
					convertDateFromDBToString(result.getString(4)), 
					result.getString(3), 
					result.getString(5), 
					getCalendarByID(result.getInt(6)).getName());
			e.setId(Integer.parseInt(result.getString(0)));
			return e;
		}
		else
		return null;
	}
}
