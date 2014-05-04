package com.mycalendar.components;

public abstract class AppItem {

	private String name;

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}
}