package com.github.assisstion.towerScaler.box;


public final class BoxHelper{

	private BoxHelper(){
		
	}
	
	public static boolean overLaps(Boxable a, Boxable b){
		return new ImmutableBoxableBox(a).overlaps(new ImmutableBoxableBox(b));
	}
	
	public static boolean pointIn(Boxable a, int x, int y){
		return new ImmutableBoxableBox(a).pointIn(x, y);
	}
}
