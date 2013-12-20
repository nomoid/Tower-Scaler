package com.github.assisstion.towerScaler;

import java.io.File;

public final class Helper{
	
	public static String javaroot;
	
	static{
		javaroot = System.getProperty("MF_javaroot");
		if(javaroot == null){
			javaroot = "";
		}
		else{
			javaroot += File.separator;
		}
	}
	
	private Helper(){
		
	}
	
	public static double round(double d, int digits){
		return Math.round(d * Math.pow(10, digits)) / Math.pow(10, digits);
	}
}
