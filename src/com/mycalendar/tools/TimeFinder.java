package com.mycalendar.tools;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.view.View;

import com.mycalendar.components.Event;

public class TimeFinder {
	
	private ArrayList<Event> existing;
	private ArrayList<Event> potential;
	private String sDate;
	private String sTime;
	private String eDate;
	private String eTime;
	
	public TimeFinder(ArrayList<Event> e, String sDate, String sTime, String eDate, String eTime){
		existing = e;
		this.sDate = sDate;
		this.sTime = sTime;
		this.eDate = eDate;
		this.eTime = eTime;
	}
	
	public int[] getDateToken(String tag){
		StringTokenizer tokenizer; 
		if(tag.equals("start"))
			tokenizer = new StringTokenizer(sDate, "/");
		else
			tokenizer = new StringTokenizer(eDate, "/");
		int[] dateToken = new int[3];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		dateToken[2] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}
	
	/**
	 * The string value of the time is splitted in the two components, which are stored in an array.
	 * @param tag which time has to be parse into the array
	 * @return int[]
	 */
	public int[] getTimeToken(String tag){
		StringTokenizer tokenizer;
		if(tag.equals("start"))
			tokenizer = new StringTokenizer(sTime, ":");
		else
			tokenizer = new StringTokenizer(eTime, ":");
		int[] dateToken = new int[2];
		dateToken[0] = Integer.parseInt(tokenizer.nextToken());
		dateToken[1] = Integer.parseInt(tokenizer.nextToken());
		return dateToken;
	}

}
