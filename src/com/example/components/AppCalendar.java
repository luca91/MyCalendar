package com.example.components;

import java.util.UUID;

import android.graphics.Color;

/**
 * This class creates and manages a calendar instance-
 * @author Luca Bellettati
 *
 */
public class AppCalendar extends AppItem{
	
	/**
	 * Constant for the name field.
	 */
	public static final String C_NAME = "com.example.aux.C_NAME";
	
	/**
	 * Constant for the color field.
	 */
	public static final String C_COLOR = "com.example.aux.C_COLOR";
	private String name;
	private String color;
	private String id;
	private UUID calendar_id;
	
	/**
	 * 
	 * @param name the calendar name
	 * @param color the color associated to this calendar
	 */
	public AppCalendar(String name, String color){
		this.name = name;
		this.color = color;
		id = getUUIDCalendar();
		super.setName(name);
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
	
	public String getUUIDCalendar(){
		calendar_id = UUID.randomUUID();
		return calendar_id.toString();
	}
	
	public String getId(){
		return this.id;
	}

}
