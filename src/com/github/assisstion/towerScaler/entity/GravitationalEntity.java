package com.github.assisstion.towerScaler.entity;

import org.newdawn.slick.Image;

import com.github.assisstion.towerScaler.Constants;
import com.github.assisstion.towerScaler.Helper;

public class GravitationalEntity extends CollisionEntity{

	// xVelocity: - = going left, + = going right
	// yVelocity: - = going up, + = going down
	protected double xVelocity;
	protected double yVelocity;

	public GravitationalEntity(){

	}

	public GravitationalEntity(double x, double y, Image image){
		super(x, y, image);
	}

	public GravitationalEntity(double x, double y, String src){
		super(x, y, src);
	}

	public void updateGravity(boolean xYes, boolean yYes){
		if(xYes && getXVelocity() <= Constants.maxXVelocity){
			incrementXVelocity(Constants.xGravity);
		}
		if(yYes && getYVelocity() <= Constants.maxYVelocity){
			incrementYVelocity(Constants.yGravity);
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

	@Override
	protected void round(){
		if(getRoundingDigits() == 0){
			return;
		}
		super.round();
		xVelocity = Helper.round(xVelocity, getRoundingDigits());
		yVelocity = Helper.round(yVelocity, getRoundingDigits());
	}
}
