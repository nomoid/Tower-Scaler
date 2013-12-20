package com.github.assisstion.towerScaler.entity;

public class BoxableBox implements Box{
	
	private Boxable b;
	
	public BoxableBox(Boxable b){
		this.b = b;
	}

	@Override
	public double getX1(){
		return b.getX1();
	}

	@Override
	public double getX2(){
		return b.getX2();
	}

	@Override
	public double getY1(){
		return b.getY1();
	}

	@Override
	public double getY2(){
		return b.getY2();
	}

	@Override
	public void setPos(double x1, double x2, double y1, double y2){
		b.setPos(x1, x2, y1, y2);
	}
	
	@Override
	public void setPos(Boxable b){
		this.b.setPos(b);
	}

	@Override
	public boolean pointIn(double x, double y){
		return new BoxImpl(this).pointIn(x, y);
	}

	@Override
	public boolean overlaps(Box b){
		return new BoxImpl(this).overlaps(b);
	}

}
