package com.mycalendar.time_and_date;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.mycalendar.activity.EventEditor;
import com.mycalendar.tools.TimeButtonManager;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

/**
 * This class create and manage the time picker dialog for an event.
 * @author Luca Bellettati
 *
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	private TimeButtonManager manager;
	private Calendar current;
	
	/**
	 * It creates the dialog according to the button pressed.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Dialog onCreateDialog(Bundle savedInstaceState){
		
		current = Calendar.getInstance();
		
		//Checks if the initial value has been already change, according to a flag previously set.
		if(!manager.getTimeAlreadySet() && !(EventEditor.getIsModify()) && !EventEditor.getIsFromFinder()){
			if(this.getTag().equals("startTimePicker")){
				return new TimePickerDialog(getActivity(), this, 
						current.get(Calendar.HOUR_OF_DAY)+1, 0, true);
			}
			else{
				return new TimePickerDialog(getActivity(), this, 
						current.get(Calendar.HOUR_OF_DAY)+2, 0, true);
			}
		}
		else {
			
			//Checks which picker has to be created.
			if(this.getTag().equals("startTimePicker"))	
				return new TimePickerDialog(getActivity(), this, 
						Array.getInt(manager.getTimeToken("start"), 0), 
						Array.getInt(manager.getTimeToken("start"), 1), 
						true);
			else
				return new TimePickerDialog(getActivity(), this, 
						Array.getInt(manager.getTimeToken("end"), 0), 
						Array.getInt(manager.getTimeToken("end"), 1), 
						true);
		}
	}

	/**
	 * It is called when the set button in the dialog is pressed. 
	 * The data received from the dialog are used to update the value of the time and of the button text. 
	 * If a time occuring before the current one is set a warning message is shown and no changes are performed.
	 */
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
//		current = Calendar.getInstance();
//		manager.setCurrentCalendar(current);
//		Calendar set;
//		boolean result = false;
		manager.setTimeAlreadySetFlag(true);
		if((this.getTag()).equals("startTimePicker")){
//			set = new GregorianCalendar(manager.getYear("start"), manager.getMonth("start")-1, manager.getDay("start"), hourOfDay, minute);
			manager.setStartCalendar(new GregorianCalendar(manager.getYear("start"), manager.getMonth("start")-1, manager.getDay("start"), hourOfDay, minute));
			manager.setTimeButtonText("start");
//			result = manager.checkDateStartValidity();
		}
		
		else{
//			set = new GregorianCalendar(manager.getYear("end"), manager.getMonth("end")-1, manager.getDay("end"), hourOfDay, minute);
			manager.setEndCalendar(new GregorianCalendar(manager.getYear("end"), manager.getMonth("end")-1, manager.getDay("end"), hourOfDay, minute));
			manager.setTimeButtonText("end");
//			manager.updateTimeAndDate("end");
//			result = manager.checkDateEndValidity();
		}

//		if (result){
//			manager.setTimeAlreadySetFlag(true);
//			
//			//start time picker
//			if((this.getTag()).equals("startTimePicker")){
//				manager.setStartTimeValues();
//				manager.setTimeButtonText("start");
//				
//				if(manager.checkCalendarPrecedence() > 0){ //start date&time are after the currently set end date&time
//					manager.setEndCalendar(new GregorianCalendar(manager.getYear("end"), manager.getMonth("end"), manager.getDay("end"), hourOfDay, minute));
//					manager.setEndTimeValues();
//					manager.setTimeButtonText("end");
//				}
			}
			
			//end time picker
//			else{
//				
//				//Actions to update the date values
//				manager.setEndTimeValues();
//				int compareResult = manager.checkCalendarPrecedence();
//				
//				//start time is after currently set end time
//				if(compareResult > 0){
//					manager.setStartCalendar(set);
//					manager.setStartTimeValues();
//					manager.setEndTimeValues();
//					manager.setTimeButtonText("start");
//				}
//				manager.setTimeButtonText("end");
//			}	
//		}
//		else
//			
//			//Warning toast
//			Toast.makeText(context, "No time before now can be set!", Toast.LENGTH_LONG).show();	
//	}
	
	public void setButtonManager(TimeButtonManager manager){
		this.manager = manager;
	}
}
