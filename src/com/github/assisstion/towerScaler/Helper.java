package com.github.assisstion.towerScaler;

public final class Helper{
	private Helper(){
		
	}
	
	public static double round(double d, int digits){
		return Math.round(d * Math.pow(10, digits)) / Math.pow(10, digits);
	}
}
