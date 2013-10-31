package com.example.time_and_date;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.example.mycalendar.EventEditor;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * This class create and manage the time picker dialog for an event.
 * @author Luca Bellettati
 *
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	private EventEditor parent;
	private Context context;
	private Calendar current;
	
	/**
	 * It creates the dialog according to the button pressed.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Dialog onCreateDialog(Bundle savedInstaceState){
		
		//Checks if the initial value has been already change, according to a flag previously set.
		if(!parent.getTimeAlreadySet() && !(EventEditor.getIsModify())){
			Calendar c = Calendar.getInstance();
			return new TimePickerDialog(getActivity(), this, 
					c.get(Calendar.HOUR_OF_DAY)+1, 0, true);
		}
		else {
			
			//Checks which picker has to be created.
			if(this.getTag().equals("startTimePicker"))	
				return new TimePickerDialog(getActivity(), this, 
						Array.getInt(parent.getTimeToken("start"), 0), 
						Array.getInt(parent.getTimeToken("start"), 1), 
						true);
			else
				return new TimePickerDialog(getActivity(), this, 
						Array.getInt(parent.getTimeToken("end"), 0), 
						Array.getInt(parent.getTimeToken("end"), 1), 
						true);
		}
	}

	/**
	 * It is called when the set button in the dialog is pressed. 
	 * The data received from the dialog are used to update the value of the time and of the button text. 
	 * If a time occuring before the current one is set a warning message is shown and no changes are performed.
	 */
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		//calendar with the values just set
		current = new GregorianCalendar(Array.getInt(parent.getDateToken("end"), 2), 
				Array.getInt(parent.getDateToken("end"), 1)-1, 
				Array.getInt(parent.getDateToken("end"), 0), 
				hourOfDay, 
				minute);
		int result = parent.getCurrent().compareTo(current);
		if (result <= 0){
			parent.setTimeAlreadySetFlag(true);
			if((this.getTag()).equals("startTimePicker")){
				parent.setTime(hourOfDay, minute, "start");
				parent.setTime(hourOfDay, minute, "end");
				parent.setTimeButtonText(parent.getCurrentStartTime(), "start");
				parent.setTimeButtonText(parent.getCurrentEndTime(), "end");
			}
			else{
				
				//Actions to update the date values
				parent.setTime(hourOfDay, minute, "end");
				Calendar c1 = new GregorianCalendar(Array.getInt(parent.getDateToken("start"), 2), 
						Array.getInt(parent.getDateToken("start"), 1)-1, 
						Array.getInt(parent.getDateToken("start"), 0), 
						Array.getInt(parent.getTimeToken("start"), 0),
						Array.getInt(parent.getTimeToken("start"), 1));
				Calendar c2 = new GregorianCalendar(Array.getInt(parent.getDateToken("end"), 2), 
						Array.getInt(parent.getDateToken("end"), 1)-1, 
						Array.getInt(parent.getDateToken("end"), 0),
						Array.getInt(parent.getTimeToken("end"), 0),
						Array.getInt(parent.getTimeToken("end"), 1));
				int compareResult = c1.compareTo(c2);
				switch (compareResult) {
				case -1:
					parent.setTimeButtonText(parent.getCurrentStartTime(), "start");
					parent.setTimeButtonText(parent.getCurrentEndTime(), "end");
					break;
				case 0:
					break;
				case 1:
					parent.setTime(hourOfDay, minute, "start");
					parent.setTimeButtonText(parent.getCurrentEndTime(), "start");
					parent.setTimeButtonText(parent.getCurrentEndTime(), "end");
					break;
				}
			}	
		}
		else if(result > 0)
			
			//Warning toast
			Toast.makeText(context, "No time before now can be set!", Toast.LENGTH_LONG).show();	
	}
	
	/**
	 * It sets some parents objects used during the dialog work flow.
	 * @param parent the current EventCreate instance where the dialog is used
	 * @param ctx the current context
	 */
	public void setParent(EventEditor parent, Context ctx){
		this.parent = parent;
		this.context = ctx;
	}


}
