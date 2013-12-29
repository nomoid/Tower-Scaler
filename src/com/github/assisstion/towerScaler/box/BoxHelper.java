package com.github.assisstion.towerScaler.box;

import com.github.assisstion.TSToolkit.TSBoxable;


public final class BoxHelper{

	private BoxHelper(){
		
	}
	
	public static boolean overLaps(TSBoxable a, TSBoxable b){
		return new ImmutableBoxableBox(a).overlaps(new ImmutableBoxableBox(b));
	}
	
	public static boolean pointIn(TSBoxable a, int x, int y){
		return new ImmutableBoxableBox(a).pointIn(x, y);
	}
}
