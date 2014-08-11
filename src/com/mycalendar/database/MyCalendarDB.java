package com.mycalendar.database;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.components.Reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CalendarContract;
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
			"event_name TEXT, " +
			"event_start_date TEXT, " +
			"event_start_time TEXT, " +
			"event_end_date TEXT," +
			"event_end_time TEXT," +
			"event_calendar_id INTEGER,"
			+ "event_all_day INTEGER DEFAULT 0,"
			+ "event_parent_id INTEGER,"
			+ "event_flexibility TEXT DEFAULT None,"
			+ "event_flexibility_range INTEGER DEFAULT 0,"
			+ "event_repetition_id INTEGER DEFAULT 1,"
			+ "event_reminder_id INTEGER DEFAULT 0,"
			+ "event_notes TEXT DEFAULT NULL,"
			+ "UNIQUE(event_name, event_start_date, event_start_time, event_calendar_id));";
	
	public static final String CALENDAR_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS calendars " +
					"(calendar_id INTEGER PRIMARY KEY ,"
					+ "calendar_name TEXT, " +
					"calendar_color TEXT,"
					+ "UNIQUE(calendar_name));";
	
	public static final String REMINDER_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS reminders " +
					"(reminder_id INTEGER PRIMARY KEY,"
					+ "reminder_event_id INTEGER, "
//					+ "reminder_event_id_uri INTEGER DEFAULT 0,"
					+ "reminder_time_chosen INTEGER);";
//					+ "reminder_id_uri INTEGER DEFAULT 0);";
	
	public static final String[] EVENT_PROJECTION = new String[] {
		CalendarContract.Calendars._ID,                           // 0
		CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
		CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
		CalendarContract.Calendars.NAME,							 // 3
		CalendarContract.Calendars.OWNER_ACCOUNT,                 // 4
		CalendarContract.Calendars.CALENDAR_COLOR				 // 5
	};
	
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
//		db.execSQL("DROP TABLE IF EXISTS reminders;");
		db.execSQL(EVENTS_TABLE_CREATE);
		db.execSQL(CALENDAR_TABLE_CREATE);
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
//		db.execSQL("DROP TABLE IF EXISTS reminders;");
		db.execSQL(EVENTS_TABLE_CREATE);
		db.execSQL(CALENDAR_TABLE_CREATE);
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
		toInsert.put("event_all_day", anEvent.getAllDay());
		toInsert.put("event_flexibility", anEvent.getFlexibility());
		toInsert.put("event_flexibility_range", anEvent.getFlexibilityRange());
		toInsert.put("event_repetition_id", anEvent.getRepetition());
		toInsert.put("event_notes", anEvent.getNotes());
		return this.getWritableDatabase().insert("events", null, toInsert);
	}
	
	public int updateEvent(Event update){
		ContentValues toInsert = new ContentValues();
		toInsert.put("event_name", update.getName());
		toInsert.put("event_start_date", convertDateFromStringToDB(update.getStartDate()));
		toInsert.put("event_end_date", convertDateFromStringToDB(update.getEndDate()));
		toInsert.put("event_start_time", update.getStartTime());
		toInsert.put("event_end_time", update.getEndTime());
		toInsert.put("event_calendar_id", getCalendarByName(update.getCalendar()).getID());
		toInsert.put("event_all_day", update.getAllDay());
		toInsert.put("event_flexibility", update.getFlexibility());
		toInsert.put("event_flexibility_range", update.getFlexibilityRange());
		toInsert.put("event_repetition_id", update.getRepetition());
		toInsert.put("event_notes", update.getNotes());
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
	
	public ArrayList<AppCalendar> getCompleteCalendarList(){
		Cursor result = this.getReadableDatabase().query("calendars", null, null, null, null, null, null);
		ArrayList<AppCalendar> calendarList = new ArrayList<AppCalendar>();
		Toast.makeText(ctx, "Calendar count: " + result.getCount(), Toast.LENGTH_SHORT).show();
		result.moveToFirst();
		int rows = result.getCount();
		boolean count = result.moveToFirst();
		for(int i = 0; count && (i < rows); i++){
			String name = result.getString(1);
			String color = result.getString(2);
			AppCalendar aCalendar = new AppCalendar(name, color);
			aCalendar.setID(result.getInt(0));;
			calendarList.add(aCalendar);
			result.moveToNext();
		}
		return calendarList;
	} 
	
	public ArrayList<String> getCalendarList(){
		String[] columns = {"calendar_name"};
		Cursor result = this.getReadableDatabase().query("calendars", columns, null, null, null, null, null);
		ArrayList<String> list = new ArrayList<String>();
		result.moveToFirst();
		for(int i = 0; i < result.getCount(); i++){
			if(!(result.isAfterLast())){
				list.add(result.getString(0));
				result.moveToNext();
			}
		}
		return list;
	}
	
	public AppCalendar getSingleCalendarByID(int id){
		String[] values = {String.valueOf(id)};
		Cursor result = this.getReadableDatabase().query("calendars", null, "calendar_id=?", values, null, null, null);
		result.moveToFirst();
		AppCalendar toReturn = new AppCalendar(result.getString(1), result.getString(2)); 
		toReturn.setID(result.getInt(0));
		return toReturn; 
	}
	
	public AppCalendar getSingleCalendarByName(String name){
		String[] values = {name};
		Cursor result = this.getReadableDatabase().query("calendars", null, "calendar_name=?", values, null, null, null);
		result.moveToFirst();
		AppCalendar toReturn = new AppCalendar(result.getString(1), result.getString(2)); 
		toReturn.setID(result.getInt(0));
		return toReturn; 
	}
	
	public AppCalendar getCalendarByID(int id){
		String[] values = {String.valueOf(id)};
		Cursor result = this.getReadableDatabase().query("calendars", null, "ROWID=?", values, null, null, null);
		boolean check = result.moveToFirst();
		if(check){
			AppCalendar ac = new AppCalendar(result.getString(1), result.getString(2));
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
		AppCalendar ac = new AppCalendar(result.getString(1), result.getString(2));
		ac.setID(result.getInt(0));
		return ac;
	}
	
	public int removeCalendar(int calendarID){
		String[] selArgs = {String.valueOf(calendarID)};
		this.getWritableDatabase().delete("events", "event_calendar_id=?", selArgs);
		return this.getWritableDatabase().delete("calendars", "calendar_id=?", selArgs); 
	}
	
	public long updateCalendar(int id, AppCalendar modified){
		ContentValues values = new ContentValues();
		values.put("calendar_name", modified.getName());
		values.put("calendar_color", modified.getColor());
		String[] whereArgs = {String.valueOf(id)};
		return this.getWritableDatabase().update("calendars", values, "calendar_id=?", whereArgs);
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
	
	public ArrayList<String> getReminderList(){
		Cursor result = this.getReadableDatabase().query("reminders", null, null, null, null, null, null);
		ArrayList<String> e = new ArrayList<String>();
		boolean count = result.moveToFirst();
		int rows = result.getCount();
		for(int i = 0; count && (i < rows); i++){
			String r = "Reminder " + result.getInt(0) + " for event " + getEventByID(result.getInt(1)).getName();
			e.add(r);
			result.moveToNext();
		}
		return e;
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
		String year = tz.nextToken();
		String month = tz.nextToken();
		String day = tz.nextToken();
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
	
	public long addReminder(Reminder aReminder){
		ContentValues values = new ContentValues();
		values.put("reminder_event_id", aReminder.getEventID());
		values.put("reminder_time_chosen", aReminder.getRemTimChosen());
		return this.getWritableDatabase().insert("reminders", null, values);
	}
	
	public long removeReminder(int eventID){
		String[] selArgs = {String.valueOf(eventID)};
		return this.getWritableDatabase().delete("reminders", "reminder_event_id=?", selArgs); 
	}
	
	public long updateReminder(Reminder update){
		ContentValues values = new ContentValues();
//		values.put("reminder_date_time", update.getDateTime());
		values.put("reminder_time_chosen", update.getRemTimChosen());
		String[] whereArgs = {String.valueOf(update.getEventID())};
		return this.getWritableDatabase().update("reminders", values, "reminder_event_id=?", whereArgs);
	}
	
	public Reminder getReminderByEventID(int id){
		String[] values = {String.valueOf(id)};
		Cursor result = getReadableDatabase().query("reminders", null, "reminder_event_id = ?", values, null, null, null);
		result.moveToFirst();
		if(result.getCount() == 1){
//			Reminder rem = new Reminder(result.getString(2), Integer.parseInt(result.getString(1)));
//			rem.setRemTimeChosen(result.getInt(3));
//			return rem;
		}
		return null;
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
	
	public ArrayList<Event> getEventsByDay(int day, int month, int year){
		String[] values = {convertDateFromStringToDB(formatDate(day, month, year)), convertDateFromStringToDB(formatDate(day, month, year))};
		Cursor result = this.getReadableDatabase().query("events", null, "event_start_date = ? OR event_end_date = ?", values, null, null, "event_start_time");
		ArrayList<Event> list = new ArrayList<Event>();
		boolean check = result.moveToFirst();
		int rows = result.getCount();
		for(int i = 0; check && (i < rows); i++){
			Event e = new Event(result.getString(1), 
				convertDateFromDBToString(result.getString(2)), 
				convertDateFromDBToString(result.getString(4)), 
				result.getString(3), 
				result.getString(5), 
				getCalendarByID(result.getInt(6)).getName());
			e.setId(Integer.parseInt(result.getString(0)));
			e.setAllDay(result.getInt(7));
			list.add(e);
			result.moveToNext();
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
			e.setAllDay(result.getInt(7));
			e.setFlexibility(result.getString(9));
			e.setFlexibilityRange(result.getInt(10));
			e.setRepetition(result.getInt(11));
			e.setNotes(result.getString(13));
			return e;
		}
		else
		return null;
	}
	
	public ArrayList<Event> getEventsFromDay(int day, int month, int year, int hours, int minutes){
		Calendar c = new GregorianCalendar(year, month, day, hours, minutes);
		Cursor result = this.getReadableDatabase().query("events", null, null, null, null, null, "event_start_date, event_start_time");
		ArrayList<Event> list = new ArrayList<Event>();
		boolean check = result.moveToFirst();
		int rows = result.getCount();
		for(int i = 0; check && (i < rows); i++){
			Event e = new Event(result.getString(1), 
				convertDateFromDBToString(result.getString(2)), 
				convertDateFromDBToString(result.getString(4)), 
				result.getString(3), 
				result.getString(5), 
				getCalendarByID(result.getInt(6)).getName());
			e.setId(Integer.parseInt(result.getString(0)));
			e.setAllDay(result.getInt(7));
			int[] date = e.getDateToken("start");
			int[] time = e.getTimeToken("start");
			Calendar actual = new GregorianCalendar(date[2], date[1], date[0], time[0], time[1]);
			if(actual.compareTo(c) >= 0)
				list.add(e);
			result.moveToNext();
		}
		return list;
	}
	
	public ArrayList<Event> getEventsFromTime(int day, int month, int year, int hours, int minutes){
		Calendar c = new GregorianCalendar(year, month, day, hours, minutes);
		String date = convertDateFromStringToDB(formatDate(day, month+1, year));
		String[] values = {date, date};
		Cursor result = this.getReadableDatabase().query("events", null, "event_start_date = ? OR event_end_date = ?", values, null, null, "event_start_date, event_start_time");
		ArrayList<Event> list = new ArrayList<Event>();
		boolean check = result.moveToFirst();
		int rows = result.getCount();
		for(int i = 0; check && i < rows; i++){
			Event e = new Event(result.getString(1), 
				convertDateFromDBToString(result.getString(2)), 
				convertDateFromDBToString(result.getString(4)), 
				result.getString(3), 
				result.getString(5), 
				getCalendarByID(result.getInt(6)).getName());
			e.setId(Integer.parseInt(result.getString(0)));
			e.setAllDay(result.getInt(7));
			int[] dateS = e.getDateToken("start");
			int[] timeS = e.getTimeToken("start");
			Calendar actualS = new GregorianCalendar(dateS[2], dateS[1]-1, dateS[0], timeS[0], timeS[1]);
			int[] dateE = e.getDateToken("end");
			int[] timeE = e.getTimeToken("end");
			Calendar actualE = new GregorianCalendar(dateE[2], dateE[1]-1, dateE[0], timeE[0], timeE[1]);
			Calendar end = new GregorianCalendar(year, month, day, hours+1, 0);
			if((actualS.compareTo(c) >= 0 && actualS.compareTo(end) < 0) || (actualE.compareTo(c) > 0 && actualE.compareTo(end) <= 0))
				list.add(e);
			result.moveToNext();
		}
		return list;
	}
	
	public String formatDate(int d, int m, int y){
		String date;
		int day;
		int month;
		int year;
		day = d;
		month = m;
		year = y;
		if(day<10 && month>=10){
			date = "0"+day+"/"+(month)+"/"+year;
		 }
		 else if(day<10 && month<10){
			 date = "0"+day+"/"+"0"+month+"/"+year;
		 }
		 else if(day>=10 && month<10){
			 date = day+"/"+"0"+month+"/"+year;
		 }
		 else{
			 date = day+"/"+month+"/"+year;
		 }
		return date;
	}
	
	public String formatTime(int h, int m){
		String time;
		int hours; 
		int minutes;
		hours = h;
		minutes = m;
		if(hours<10 && minutes>=10){
			time = "0"+hours+":"+minutes;
		}
		else if(hours >= 10 && minutes < 10){
			time = hours+":"+"0"+minutes;
		}
		else if(hours < 10 && minutes < 10){
			time = "0"+hours+":"+"0"+minutes;
		}
		else if(hours == 0 && minutes < 10){
			time = "00"+":"+"0"+minutes;
		}
		else if(hours == 0 && minutes >= 10){
			time = "00"+":"+"0"+minutes;
		}
		else if(hours < 10 && minutes == 10){
			time = "0"+hours+":"+"00";
		}
		else if(hours >= 0 && minutes >= 10){
			time = hours+":"+"00";
		}
		else if(hours == 0 && minutes == 0){
			time = "00"+":"+"00";
		}
		else{
			time = hours+":"+minutes;
		}
		return time;
	}
}
