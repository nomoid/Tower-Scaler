package com.github.assisstion.towerScaler.entity;

import org.newdawn.slick.Image;

public class CollisionEntity extends Entity implements Box{
	
	protected Box box;
	
	public CollisionEntity(){
		box = new BoxableBox(this);
	}
	
	public CollisionEntity(double x, double y, Image image){
		super(x, y, image);
		box = new BoxableBox(this);
	}
	
	public CollisionEntity(double x, double y, String src){
		super(x, y, src);
		box = new BoxableBox(this);
	}

	@Override
	public boolean pointIn(double x, double y){
		return box.pointIn(x, y);
	}

	@Override
	public boolean overlaps(Box b){
		return box.overlaps(b);
	}

	public Box getBox(){
		return box;
	}

	@Override
	public void setPos(Boxable b){
		box.setPos(b);
	}
}
