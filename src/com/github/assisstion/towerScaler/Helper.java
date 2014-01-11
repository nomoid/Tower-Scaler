package com.github.assisstion.towerScaler;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public final class Helper{
	
	private static final TrueTypeFont defaultFont = new TrueTypeFont(new Font("Calibri", Font.PLAIN, 20), true);
	
	private Helper(){
		//Not to be instantiated
	}
	
	public static double round(double d, int digits){
		return Math.round(d * Math.pow(10, digits)) / Math.pow(10, digits);
	}
	
	public static TrueTypeFont getDefaultFont(){
		return defaultFont;
	}
	
	public static Color getDefaultForegroundColor(){
		return Color.black;
	}
	
	public static Color getDefaultBackgroundColor(){
		return Color.white;
	}
}
