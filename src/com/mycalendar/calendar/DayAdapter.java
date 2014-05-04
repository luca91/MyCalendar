package com.mycalendar.calendar;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.example.mycalendar.R;
import com.mycalendar.components.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DayAdapter {

	private ArrayList<TextView> hours;
	private ArrayList<TextView> eventsObjects;
	private ScrollView eventsObjectContainer;
	private RelativeLayout gridContainer;
	private LinearLayout hoursContainerDay;
	private RelativeLayout eventsContainer;
	private Context ctx;
	
	public DayAdapter(Context c, LinearLayout hoursContainer) {
		ctx = c;
		hoursContainerDay = hoursContainer;
	}
	
	public void setHours(){
		hoursContainerDay.setPadding(0, 0, 1, 1);
		for(int i = 0; i <= 23; i++){
			TextView tv = new TextView(ctx);
			if(i < 10)
				tv.setText("0" + i);
			else
				tv.setText(i);
			
			hours.add(tv);
		}
	}
	
	public RelativeLayout getGridContainer(){
		return gridContainer;
	}
	
	public void setEventsContainer(Event...events){
		
	}
	
	public void setGridContainer(){
		gridContainer.addView(eventsContainer);
	}
		

}
