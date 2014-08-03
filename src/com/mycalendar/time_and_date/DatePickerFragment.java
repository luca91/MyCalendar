package com.mycalendar.time_and_date;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.mycalendar.activity.EventEditor;
import com.mycalendar.tools.TimeButtonManager;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.os.Bundle;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;

/**
 * This class create and manage the date picker dialog for an event.
 * @author Luca Bellettati
 *
 */
@TargetApi(11)
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private TimeButtonManager manager;
	private Calendar current;
	
	/**
	 * It creates the dialog according to the button pressed.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		current = Calendar.getInstance();
		
		//Checks if the initial value has been already change, according to a flag previously set.
		if(!manager.getDateAlreadySet() && !EventEditor.getIsModify()){
			return new DatePickerDialog(getActivity(), (OnDateSetListener) this, 
					current.get(Calendar.YEAR), 
					current.get(Calendar.MONTH), 
					current.get(Calendar.DAY_OF_MONTH));
		}
		else{
			
			//Checks which picker has to be created.
			if(this.getTag().equals("startDatePicker"))
				return new DatePickerDialog(getActivity(), (OnDateSetListener) this, 
						Array.getInt(manager.getDateToken("start"), 2), 
						Array.getInt(manager.getDateToken("start"), 1)-1, 
						Array.getInt(manager.getDateToken("start"), 0));
			else
				return new DatePickerDialog(getActivity(), (OnDateSetListener) this, 
						Array.getInt(manager.getDateToken("end"), 2), 
						Array.getInt(manager.getDateToken("end"), 1)-1, 
						Array.getInt(manager.getDateToken("end"), 0));
		}
	}
	
	/**
	 * It is called when the set button in the dialog is pressed. 
	 * The data received from the dialog are used to update the value of the date and of the button text. 
	 * If a date occurring before the current day is set a warning message is shown and no changes are performed.
	 */
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		
//		current = Calendar.getInstance();
		
//		manager.setCurrentCalendar(current);
//		Calendar set;
//		boolean result = false;
		manager.setDateAlreadySetFlag(true);
		if(this.getTag().equals("startDatePicker")){
//			set = new GregorianCalendar(year, monthOfYear, dayOfMonth,manager.getHours("start"), manager.getMinutes("start"));
			Calendar current = Calendar.getInstance();
			String currentDate = current.get(Calendar.DAY_OF_MONTH) + "/" + (current.get(Calendar.MONTH)+1) + "/" + current.get(Calendar.YEAR);
			if(currentDate.equals(manager.getCurrentStartDate()) && manager.getHours("start") == 0 && manager.getMinutes("start") == 0){
				Calendar start = new GregorianCalendar(year, monthOfYear, dayOfMonth, current.get(Calendar.HOUR), 0);
				start.add(Calendar.HOUR_OF_DAY, +1);
				manager.setStartCalendar(start);
			}
			else
				manager.setStartCalendar(new GregorianCalendar(year, monthOfYear, dayOfMonth, manager.getHours("start"), manager.getMinutes("start")));
			manager.setDateButtonText("start");
//			result = manager.checkDateStartValidity();
		}
		
		else{
//			set = new GregorianCalendar(year, monthOfYear, dayOfMonth,manager.getHours("end"), manager.getMinutes("end"));
			manager.setEndCalendar(new GregorianCalendar(year, monthOfYear, dayOfMonth,manager.getHours("end"), manager.getMinutes("end")));
			manager.setDateButtonText("end");
//			result = manager.checkDateEndValidity();
		}
//		
//		if(result){
//			manager.setDateAlreadySetFlag(true);
//			
//			//start date picker
//			if((this.getTag()).equals("startDatePicker")){
//				manager.setStartDateValues();
//				manager.setDateButtonText("start");
//				
//				if(manager.checkCalendarPrecedence() > 0){
//					manager.setEndCalendar(new GregorianCalendar(year, monthOfYear, dayOfMonth, manager.getHours("end"), manager.getMinutes("end")));
//					manager.setEndDateValues();
//					manager.setDateButtonText("end");
//				}
//			}
//			
//			//end date picker
//			else {
//				
//				//Actions to update the date values
//				manager.setEndDateValues();
//				int compareResult = manager.checkCalendarPrecedence();
//
//				//start date is after currently set end date
//				if(compareResult > 0){
//					manager.setStartCalendar(set);
//					manager.setStartDateValues();
//					manager.setEndDateValues();
//					manager.setDateButtonText("start");
//				}
//				manager.setDateButtonText("end");
//			}
//		}
//		else {
//			
//			//Warning toast
//			Toast.makeText(context, "No date before today can be set!", Toast.LENGTH_SHORT).show();
//		}
 	}
	
	public void setButtonManager(TimeButtonManager manager){
		this.manager = manager;
	}
}
