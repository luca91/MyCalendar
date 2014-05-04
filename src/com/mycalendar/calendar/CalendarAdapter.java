package com.mycalendar.calendar;

import java.util.ArrayList;

import com.example.mycalendar.R;
import com.mycalendar.activity.EventsView;
import com.mycalendar.components.Event;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarAdapter extends ArrayAdapter<Event> {
	
	private Context c;
	private ArrayList<Event> list;
	
	public CalendarAdapter(Context ctx, ArrayList<Event> l){
		super(ctx, 0, l);
		c = ctx;
		list = l;
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
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.event_item, parent, false);
		}
		TextView nameAndCalendar = (TextView) convertView.findViewById(R.id.nameAndCalendarItem);
		TextView dateAndTime = (TextView) convertView.findViewById(R.id.dateAndTimeItem);
		nameAndCalendar.setText(toPut.getName() + " (" + toPut.getCalendar() + ")");
		if(toPut.getStartDate().equals(toPut.getEndDate()))
			dateAndTime.setText(toPut.getStartDate() + ", " + toPut.getStartTime() + "-" + toPut.getEndTime());
		else
			dateAndTime.setText(toPut.getStartDate() + ", " + toPut.getStartTime() + "\n"
					+ toPut.getEndDate() + ", " + toPut.getStartTime());
		TextView id = (TextView) convertView.findViewById(R.id.hiddenEventID);
		id.setText((String.valueOf(toPut.getId())));
		return convertView;
	}
}
