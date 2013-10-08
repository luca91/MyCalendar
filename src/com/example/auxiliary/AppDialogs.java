package com.example.auxiliary;

import com.example.mycalendar.AllCalendarList;
import com.example.mycalendar.AllEventsList;
import com.example.mycalendar.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

/**
 * It manages the creations of dialogs for the application.
 * @author Luca Bellettati
 *
 */
@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AppDialogs extends DialogFragment {
	
	private Context context;
	private int itemClicked;
	private AllCalendarList acl;
	private AllEventsList ael;
	AlertDialog.Builder builder;
	
	/**
	 * Construct an instance of this class.
	 * @param ctx the context in which the dialog will work.
	 * @param acl the instance of the AllCalendarList class associated
	 */
	@SuppressLint("ValidFragment")
	public AppDialogs(Context ctx, AllCalendarList acl){
		this.context = ctx;
		this.acl = acl;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.title_edit_dialog);
		builder.setItems(R.array.edit_actions, new DialogInterface.OnClickListener() {

			@SuppressLint("ValidFragment")
			public void onClick(DialogInterface dialog, int which) {
				itemClicked = which;
				Toast.makeText(context, "You clicked:  "+itemClicked, Toast.LENGTH_LONG).show();
				getParentACL().performClickedAction(getParentACL().getCurrentSelectedCalendar(), itemClicked);
        	}
        });
		builder.create();
	}
	
	public AppDialogs(Context ctx, AllEventsList ael){
		this.context = ctx;
		this.ael = ael;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.title_edit_dialog);
		builder.setItems(R.array.edit_actions, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				itemClicked = which;
				Toast.makeText(context, "You clicked:  "+itemClicked, Toast.LENGTH_LONG).show();
				getParentAEL().performClickedAction(getParentAEL().getCurrentSelectedEvent(), itemClicked);
        	}
        });
		builder.create();
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
	
	public AllCalendarList getParentACL(){
		return acl;
	}
	
	public AllEventsList getParentAEL(){
		return ael;
	}
}
