package com.mycalendar.tools;

import com.example.mycalendar.R;
import com.mycalendar.activity.ItemList;
import com.mycalendar.activity.MainActivity;
import com.mycalendar.database.MyCalendarDB;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * It manages the creations of dialogs for the application.
 * @author Luca Bellettati
 *
 */
@SuppressLint("ValidFragment")
public class AppDialogs extends DialogFragment implements DialogInterface.OnClickListener {
	
	private Context context;
	private int itemClicked;
	private ItemList itm;
	AlertDialog.Builder builder;
	
	/**
	 * Construct an instance of this class.
	 * @param ctx the context in which the dialog will work.
	 * @param acl the instance of the AllCalendarList class associated
	 */
	@SuppressLint("ValidFragment")
	public AppDialogs(Context ctx, ItemList itm){
		this.context = ctx;
		this.itm = itm;
		builder = new AlertDialog.Builder(context);
	}
	
	public AppDialogs(Context ctx){
		this.context = ctx;
		builder = new AlertDialog.Builder(context);
	}
	
	/**
	 * It create and show a dialog.
	 */
	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState){	
		return builder.show();
	}	
	
	/**
	 * It returns the value of the item clicked.
	 * @return int
	 */
	public int getItemClicked(){
		return this.itemClicked;
	}
	
	public void setMessage(String message){
//		this.message = message;
		builder.setMessage(message);
	}
	
	public void setTitle(String title){
//		this.title = title;
		builder.setTitle(title);
	}
	
	public void createAndShowDialog(){
		builder.create();
		builder.show();
	}
	
	public void setNegativeButton(){
		builder.setNegativeButton(R.string.negative_button, this);
	}
	
	public void setPositiveButton(){
		builder.setPositiveButton(R.string.positive_button, this);
	}
	
	public void setEditItems(){
		builder.setItems(R.array.edit_actions, this);
	}
	
	public void noCalendarDialog(String message, final Intent intent, final Activity a){
		builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.title_no_calendar_dialog);
		builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "You clicked:  "+which, Toast.LENGTH_LONG).show();
				a.startActivity(intent);
        	}
        });
		builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "You clicked:  "+which, Toast.LENGTH_LONG).show();
        	}
        });
		builder.create();
		builder.show();
	}
	
	public ItemList getItemList(){
		return this.itm;
	}
	
	public void alreadyExistingEventAlert(){
		builder = new AlertDialog.Builder(context);
		builder.setTitle("Attention!");
		builder.setMessage("There is already an event with this name on this day at this time. Please choose another one.");
		builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "You clicked:  "+which, Toast.LENGTH_LONG).show();
        	}
        });
		builder.create();
		builder.show();
	}
	
	public void emptyEventNameAlert(){
		builder = new AlertDialog.Builder(context);
		builder.setTitle("No event name!");
		builder.setMessage("Please insert a name for the event.");
		builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "You clicked:  "+which, Toast.LENGTH_LONG).show();
        	}
        });
		builder.create();
		builder.show();
	}
	
	public void wrongDateAlert(){
		builder = new AlertDialog.Builder(context);
		builder.setTitle("Wrong date!");
		builder.setMessage("The selected date are not valid. Please edit your choice.");
		builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "You clicked:  "+which, Toast.LENGTH_LONG).show();
        	}
        });
		builder.create();
		builder.show();
	}
	
	public void confirmDelete(final int eventID){
		builder = new AlertDialog.Builder(context);
		builder.setTitle("Warning!");
		builder.setMessage("Are you sure you want to delete this event?");
//		setPositiveButton();
		builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MyCalendarDB db = MainActivity.getAppDB();
				db.removeEvent(eventID);
				db.removeReminder(eventID);
			}
		});
		setNegativeButton();
		builder.create();
		builder.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		itemClicked = which;
		Toast.makeText(context, "You clicked dialog: " + itemClicked, Toast.LENGTH_LONG).show();
	}

}
