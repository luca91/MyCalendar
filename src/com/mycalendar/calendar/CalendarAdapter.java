package com.mycalendar.calendar;

import java.util.ArrayList;

import com.example.mycalendar.R;
import com.mycalendar.activity.EventEditor;
import com.mycalendar.activity.EventsView;
import com.mycalendar.activity.MainActivity;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;
import com.mycalendar.database.MyCalendarDB;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarAdapter extends ArrayAdapter<Event> {
	
	private Context c;
	private ArrayList<Event> list;
	private boolean isFinder;
	
	public CalendarAdapter(Context ctx, ArrayList<Event> l, boolean f){
		super(ctx, 0, l);
		c = ctx;
		list = l;
		isFinder = f;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Event getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Event toPut = (Event) list.get(position);
		MyCalendarDB db = MainActivity.getAppDB();
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.event_item, parent, false);
		}
		TextView nameAndCalendar = (TextView) convertView.findViewById(R.id.nameAndCalendarItem);
		TextView dateAndTime = (TextView) convertView.findViewById(R.id.dateAndTimeItem);
		TextView calColor = (TextView) convertView.findViewById(R.id.calColorEventList);
		calColor.setBackgroundColor(AppCalendar.colorFromStringToInt(db.getCalendarByName(toPut.getCalendar()).getColor()));
		nameAndCalendar.setText(list.get(position).getName());
		if(toPut.getStartDate().equals(toPut.getEndDate()))
			if(toPut.getStartTime().equals("00:00") && toPut.getEndTime().equals("00:00"))
				dateAndTime.setText(toPut.getStartDate() + ", " + "All day");
			else
				dateAndTime.setText(toPut.getStartDate() + ", " + toPut.getStartTime() + "-" + toPut.getEndTime());
		else
			dateAndTime.setText(toPut.getStartDate() + ", " + toPut.getStartTime() + "\n"
					+ toPut.getEndDate() + ", " + toPut.getStartTime());
		TextView id = (TextView) convertView.findViewById(R.id.hiddenEventID);
		id.setText((String.valueOf(toPut.getId())));
		Button toHide = (Button) convertView.findViewById(R.id.choose_event_found);
		if(!isFinder)
			toHide.setVisibility(View.INVISIBLE);
		else{
			toHide.setVisibility(View.VISIBLE);
			
		}
		return convertView;
	}
	
	public void setEventsList(ArrayList<Event> list){
		this.list = list;
	}
}
