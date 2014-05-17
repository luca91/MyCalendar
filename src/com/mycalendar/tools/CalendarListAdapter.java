package com.mycalendar.tools;

import java.util.ArrayList;
import java.util.List;

import com.example.mycalendar.R;
import com.mycalendar.components.AppCalendar;
import com.mycalendar.components.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CalendarListAdapter extends ArrayAdapter<AppCalendar>{
	
	private ArrayList<AppCalendar> list;
	private Context c;

	public CalendarListAdapter(Context context, int resource,
			List<AppCalendar> objects) {
		super(context, resource, objects);
		 list = (ArrayList<AppCalendar>) objects;
		 c = context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public AppCalendar getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.calendar_item, parent, false);
		}
		TextView calendarName = (TextView) convertView.findViewById(R.id.cal_name);
		TextView calendarColor = (TextView) convertView.findViewById(R.id.cal_color);
		Button action = (Button) convertView.findViewById(R.id.import_button);
		calendarName.setText(getItem(position).getName());
		calendarColor.setText("");
		calendarColor.setBackgroundColor(AppCalendar.colorFromStringToInt(list.get(position).getColor()));
		if(getItem(position).getIsLocal())
			action.setText(R.string.text_export_calendar_button);
		else
			action.setText(R.string.text_import_calendar_button);
		action.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AppDialogs d = new AppDialogs(c);
				d.setMessage("Calendar successfully exported to your Google account.");
				d.setPositiveButton();
				d.createAndShowDialog();
			}
		});
		return convertView;
	}

}
