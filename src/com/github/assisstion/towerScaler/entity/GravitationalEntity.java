package com.github.assisstion.towerScaler.entity;

import org.newdawn.slick.Image;

import com.github.assisstion.towerScaler.Helper;

public class GravitationalEntity extends CollisionEntity{
	
	//xVelocity: - = going left, + = going right
	//yVelocity: - = going up, + = going down
	protected double xVelocity;
	protected double yVelocity;
	
	public static double xGravity = 0;
	public static double yGravity = 0.08;
	public static double maxXVelocity = 0;
	public static double maxYVelocity = 8;
	
	
	public GravitationalEntity(){
		
	}
	
	public GravitationalEntity(double x, double y, Image image){
		super(x, y, image);
	}
	
	public GravitationalEntity(double x, double y, String src){
		super(x, y, src);
	}
	
	public void updateGravity(boolean xYes, boolean yYes){
		if(xYes && (getXVelocity() <= maxXVelocity)){
			incrementXVelocity(xGravity);
		}
		if(yYes && (getYVelocity() <= maxYVelocity)){
			incrementYVelocity(yGravity);
		}
	}
	
	public double getXVelocity(){
		return xVelocity;
	}
	
	public double getYVelocity(){
		return yVelocity;
	}
	
	public void setXVelocity(double x){
		xVelocity = x;
		round();
	}
	
	public void setYVelocity(double y){
		yVelocity = y;
		round();
	}
	
	public void setVelocity(double x, double y){
		xVelocity = x;
		yVelocity = y;
		round();
	}
	
	public double incrementXVelocity(double x){
		xVelocity += x;
		round();
		return xVelocity;
	}
	
	public double incrementYVelocity(double y){
		yVelocity += y;
		round();
		return yVelocity;
	}
	
	public void incrementVelocity(double x, double y){
		xVelocity += x;
		yVelocity += y;
		round();
	}
	
	protected void round(){
		if(getRoundingDigits() == 0){
			return;
		}
		super.round();
		xVelocity = Helper.round(xVelocity, getRoundingDigits());
		yVelocity = Helper.round(yVelocity, getRoundingDigits());
	}
}
