package com.mycalendar.time_and_date;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.mycalendar.activity.EventEditor;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Toast;
import android.os.Bundle;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;

/**
 * This class create and manage the date picker dialog for an event.
 * @author Luca Bellettati
 *
 */
@TargetApi(11)
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private EventEditor parent;
	private Context context;
	private Calendar current;
	
	/**
	 * It creates the dialog according to the button pressed.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		//Checks if the initial value has been already change, according to a flag previously set.
		if(!parent.getDateAlreadySet() && !EventEditor.getIsModify()){
			Calendar c = Calendar.getInstance();
			return new DatePickerDialog(getActivity(), (OnDateSetListener) this, 
					c.get(Calendar.YEAR), 
					c.get(Calendar.MONTH), 
					c.get(Calendar.DAY_OF_MONTH));
		}
		else{
			
			//Checks which picker has to be created.
			if(this.getTag().equals("startDatePicker"))
				return new DatePickerDialog(getActivity(), (OnDateSetListener) this, 
						Array.getInt(parent.getDateToken("start"), 2), 
						Array.getInt(parent.getDateToken("start"), 1)-1, 
						Array.getInt(parent.getDateToken("start"), 0));
			else
				return new DatePickerDialog(getActivity(), (OnDateSetListener) this, 
						Array.getInt(parent.getDateToken("end"), 2), 
						Array.getInt(parent.getDateToken("end"), 1)-1, 
						Array.getInt(parent.getDateToken("end"), 0));
		}
	}
	
	/**
	 * It is called when the set button in the dialog is pressed. 
	 * The data received from the dialog are used to update the value of the date and of the button text. 
	 * If a date occurring before the current day is set a warning message is shown and no changes are performed.
	 */
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		
		//calendar with the values just set
		current = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		
		//comparison the verify if the values set are valid
		int result = parent.getCurrent().compareTo(current);
		if(result <= 0){
			parent.setDateAlreadySetFlag(true);
			if((this.getTag()).equals("startDatePicker")){
				parent.setDate(year, monthOfYear+1, dayOfMonth, "start");
				parent.setDate(year, monthOfYear+1, dayOfMonth, "end");
				parent.setDateButtonText(parent.getCurrentStartDate(), "start");
				parent.setDateButtonText(parent.getCurrentEndDate(), "end");
			}
			else {
				
				//Actions to update the date values
				parent.setDate(year, monthOfYear+1, dayOfMonth, "end");
				Calendar c1 = new GregorianCalendar(Array.getInt(parent.getDateToken("start"), 2), 
						Array.getInt(parent.getDateToken("start"), 1)-1, 
						Array.getInt(parent.getDateToken("start"), 0));
				Calendar c2 = new GregorianCalendar(Array.getInt(parent.getDateToken("end"), 2), 
						Array.getInt(parent.getDateToken("end"), 1)-1, 
						Array.getInt(parent.getDateToken("end"), 0));
				int compareResult = c1.compareTo(c2);
				switch (compareResult) {
				case -1: 
					parent.setDateButtonText(parent.getCurrentStartDate(), "start");
					parent.setDateButtonText(parent.getCurrentEndDate(), "end");
					break;
				case 0:
					break;				
				case 1:
					parent.setDate(year, monthOfYear+1, dayOfMonth, "start");
					parent.setDateButtonText(parent.getCurrentEndDate(), "start");
					parent.setDateButtonText(parent.getCurrentEndDate(), "end");
					break;
				}
			}
		}
		else if(result > 0)
			
			//Warning toast
			Toast.makeText(context, "No date before today can be set!", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * It sets some parents objects used during the dialog work flow.
	 * @param parent the current EventCreate instance where the dialog is used
	 * @param ctx the current context
	 */
	public void setParent(EventEditor parent, Context ctx){
		this.parent = parent;
		this.context =  ctx;
	}
	
	
}
