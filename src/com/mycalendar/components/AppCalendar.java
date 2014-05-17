package com.mycalendar.components;

import java.util.UUID;

import android.graphics.Color;

/**
 * This class creates and manages a calendar instance-
 * @author Luca Bellettati
 *
 */
public class AppCalendar{
	
	/**
	 * Constant for the name field.
	 */
	public static final String CAL_NAME = "com.example.aux.C_NAME";
	
	/**
	 * Constant for the color field.
	 */
	public static final String CAL_COLOR = "com.example.aux.C_COLOR";
	public static final String CAL_EMAIL = "com.example.aux.C_EMAIL";
	private String name;
	private String color;
	private int id;
	private int serverID;
	private boolean isSync;
	private boolean isLocal;
	
	/**
	 * 
	 * @param name the calendar name
	 * @param color the color associated to this calendar
	 */
	public AppCalendar(String name, String color){
		this.name = name;
		this.color = color;
		isLocal = true;
	}
	
	/**
	 * It gets the name of this calendar:
	 * @return String
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * It gets the color associated to this calendar.
	 * @return String
	 */
	public String getColor(){
		return this.color;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setColor(String color){
		this.color = color;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
	public void setIsSync(boolean sync){
		isSync = sync;
	}
	
	public boolean getIsSync(){
		return isSync;
	}
	
	public void setIsLocal(boolean isLocal){
		this.isLocal = isLocal;
	}
	
	public boolean getIsLocal(){
		return isLocal;
	}
	
	public void setServerID(int id){
		serverID = id;
	}
	
	public int getServerID(){
		return serverID;
	}
	
	/**
	 * It return the associated constant value of this calendar color.
	 * @param color the string denoting the color of this calendar
	 * @return int
	 */
	public static int colorFromStringToInt(String color){
		if(color.equals("Black"))
			return Color.BLACK;
		if(color.equals("Blue"))
			return Color.BLUE;
		if(color.equals("Cyan"))
			return Color.CYAN;
		if(color.equals("Dark Grey"))
			return Color.DKGRAY;
		if(color.equals("Gray"))
			return Color.GRAY;
		if(color.equals("Green"))
			return Color.GREEN;
		if(color.equals("Light Gray"))
			return Color.LTGRAY;
		if(color.equals("Magenta"))
			return Color.MAGENTA;
		if(color.equals("Red"))
			return Color.RED;
		if(color.equals("White"))
			return Color.WHITE;
		if(color.equals("Yellow"))
			return Color.YELLOW;
		return -1;
	}
	
	public static int parseBooleanToInt(boolean b){
		if(b)
			return 1;
		else 
			return 0;
	}
	
	public static String parseHexStringColor(String c){
		int i = Integer.parseInt(c, 16);
		return String.valueOf(i);
	}
}
