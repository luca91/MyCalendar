package com.example.calendar;

import java.util.GregorianCalendar;

import com.example.mycalendar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DayAdapter extends ArrayAdapter<String> {

	private Context context;
	private String[] items;
	private ImageView previous;
	private ImageView next;
	private TextView day;
	
	public DayAdapter(Context context, int resource, String[] objects, GregorianCalendar calendar) {
		super(context, resource, objects);
		this.context = context;
		items = objects;
	}
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = LayoutInflater.from(context);
		View hourView = inflater.inflate(R.layout.day_item, parent,false);
		TextView tv = (TextView) hourView.findViewById(R.id.hour);
		tv.setText(items[position]);
		return hourView;
	}

	

}
