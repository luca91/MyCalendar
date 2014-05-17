package com.mycalendar.activity;

import java.io.IOException;

import com.example.mycalendar.R;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.CalendarRequest;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class ExportCalendar extends Activity {
	
	private static final String AUTHORITY = "com.mycalendar.android.datasync.provider";
	private static final String ACCOUNT_TYPE = "gmail.com";
	private static final String ACCOUNT = "lucabelles";
	private Account account;
	private ContentResolver resolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_calendar);
		setupActionBar();
		account = createSyncAccount(this);
		resolver = getContentResolver();
		ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
	}
	
	public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account fakeAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
      
        if (accountManager.addAccountExplicitly(fakeAccount, null, null)) {
           
        } else {
           
        }
        return fakeAccount;
    }
	
	static Uri asSyncAdapter(Uri uri, String account, String accountType) {
	    return uri.buildUpon()
	        .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
	        .appendQueryParameter(Calendars.ACCOUNT_NAME, account)
	        .appendQueryParameter(Calendars.ACCOUNT_TYPE, accountType).build();
	 }
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.export_calendar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addCalendarOnGoogleAccount() throws IOException{
		Calendar calendar = new Calendar();
		calendar.setSummary("calendarSummary");
		calendar.setTimeZone("Europe/Rome");
		
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		
//		 GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(
//			        response.accessToken, httpTransport, jsonFactory, clientId, clientSecret,
//			        response.refreshToken);
		
		com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar(new NetHttpTransport(), jsonFactory, null);

		com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();

		System.out.println(createdCalendar.getId());
	}

}
