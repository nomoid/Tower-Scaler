package com.github.assisstion.towerScaler.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.github.assisstion.towerScaler.Helper;

public class Entity implements Comparable<Entity>, Boxable{
	
	private static final int ROUNDING_DIGITS = 6;
	
	private static long idCounter = 0;
	private static Object idLock = new Object();
	
	protected double xLocation;
	protected double yLocation;
	protected long id;
	protected Image image;
	
	public long getID(){
		return id;
	}
	
	public Entity(){
		synchronized(idLock){
			id = idCounter++;
		}
	}
	
	public Entity(double x, double y, Image image){
		this();
		this.xLocation = x;
		this.yLocation = y;
		round();
		this.image = image;
	}
	public Entity(double x, double y, String src){
		this();
		this.xLocation = x;
		this.yLocation = y;
		round();
		try{
			this.image = new Image(src);
		}
		catch(SlickException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean equals(Object a){
		Entity e = null;
		if(a instanceof Entity){
			e = (Entity) a;
			if(e.getID() == this.getID()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return (int) id;
	}

	@Override
	public int compareTo(Entity e) {
		return new Long(getID()).compareTo(e.getID());
	}
	
	public Image getImage(){
		return image;
	}

	public double getX1(){
		return xLocation;
	}

	public double getX2(){
		return Helper.round(xLocation + image.getWidth(), 8);
	}

	public double getY1(){
		return yLocation;
	}

	public double getY2(){
		return Helper.round(yLocation + image.getHeight(), 8);
	}
	
	public double getWidth(){
		return image.getWidth();
	}
	
	public double getHeight(){
		return image.getHeight();
	}
	
	public void setX(double x){
		xLocation = x;
		round();
	}
	
	public void setY(double y){
		yLocation = y;
		round();
	}
	
	public void setLocation(double x, double y){
		xLocation = x;
		yLocation = y;
		round();
	}
	
	/**
	 * @return new X value
	 */
	public double incrementX(double i){
		xLocation += i;
		round();
		return xLocation;
	}
	
	/**
	 * @return new Y value
	 */
	public double incrementY(double j){
		yLocation += j;
		round();
		return yLocation;
	}
	
	public void incrementLocation(double i, double j){
		xLocation += i;
		yLocation += j;
		round();
	}

	/**
	 * 
	 * @param x1
	 * @param x2 ignored
	 * @param y1
	 * @param y2 ignored
	 */
	public void setPos(double x1, double x2, double y1, double y2){
		this.xLocation = x1;
		this.yLocation = y1;
		round();
	}
	
	public int getRoundingDigits(){
		return ROUNDING_DIGITS;
	}
	
	protected void round(){
		if(getRoundingDigits() == 0){
			return;
		}
		xLocation = Helper.round(xLocation, getRoundingDigits());
		yLocation = Helper.round(yLocation, getRoundingDigits());
	}
	
	@Override
	public void setPos(Boxable b){
		setPos(b.getX1(), b.getX2(), b.getY1(), b.getY2());
	}
}
