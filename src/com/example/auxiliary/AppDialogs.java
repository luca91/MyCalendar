package com.example.auxiliary;

import com.example.mycalendar.ItemList;
import com.example.mycalendar.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
		builder.setTitle(R.string.title_edit_dialog);
		builder.setItems(R.array.edit_actions, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				itemClicked = which;
				Toast.makeText(context, "You clicked:  "+itemClicked, Toast.LENGTH_LONG).show();
				getItemList().performClickedAction(getItemList().getItemName(), itemClicked);
        	}
        });
		builder.create();
	}
	
	public AppDialogs(Context ctx){
		this.context = ctx;
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
	
	public void noEventDialog(String message, final Intent intent){
		builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.title_no_calendar_dialog);
		builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "You clicked:  "+which, Toast.LENGTH_LONG).show();
				startActivity(intent);
        	}
        });
		builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "You clicked:  "+which, Toast.LENGTH_LONG).show();
        	}
        });
		builder.create();
	}
	
	public ItemList getItemList(){
		return this.itm;
	}
}
