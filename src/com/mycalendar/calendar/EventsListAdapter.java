package com.mycalendar.calendar;

import java.util.ArrayList;

import com.example.mycalendar.R;
import com.mycalendar.components.Event;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventsListAdapter extends ArrayAdapter<Event> {
	
	private Context c;
	private ArrayList<Event> list;
	
	public EventsListAdapter(Context ctx, ArrayList<Event> eventsList2){
		super(ctx, R.layout.event_item, eventsList2);
		c = ctx;
		list = eventsList2;
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
		return convertView;
	}
}
