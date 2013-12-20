package com.github.assisstion.towerScaler.entity;

public class BoxImpl implements Box{
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	
	public BoxImpl(Boxable b){
		this.xMin = b.getX1();
		this.xMax = b.getX2();
		this.yMin = b.getY1();
		this.yMax = b.getY2();
	}
	
	public BoxImpl(double x1,double x2,double y1,double y2){
		xMin = x1;
		xMax = x2;
		yMin = y1;
		yMax = y2;
	}
	
	@Override
	public boolean pointIn(double x, double y){
		if(x >= this.xMin && x <= this.xMax && y >= this.yMin && y <= this.yMax){
			return true;
		}
		return false;
	}

	@Override
	public boolean overlaps(Box b){
		if(this.pointIn(b.getX1(), b.getY1()) || this.pointIn(b.getX1(), b.getY2()) || this.pointIn(b.getX2(), b.getY1()) || this.pointIn(b.getX2(), b.getY2())
				|| b.pointIn(this.getX1(), this.getY1()) || b.pointIn(this.getX1(), this.getY2()) || b.pointIn(this.getX2(), this.getY1()) || b.pointIn(this.getX2(), this.getY2())){
			return true;
		}
		return false;
	}

	@Override
	public void setPos(double x1,double x2,double y1,double y2){
		xMin = x1;
		xMax = x2;
		yMin = y1;
		yMax = y2;
	}
	
	@Override
	public void setPos(Boxable b){
		setPos(b.getX1(), b.getX2(), b.getY1(), b.getY2());
	}

	@Override
	public double getX1(){
		return xMin;
	}
	

	@Override
	public double getX2(){
		return xMax;
	}
	
	@Override
	public double getY1(){
		return yMin;
	}
	
	@Override
	public double getY2(){
		return yMax;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof BoxImpl){
			BoxImpl b = (BoxImpl) o;
			if(b.hashCode() == this.hashCode()){
				return true;
			}
		}
		return false;
		
	}
	
	@Override
	public int hashCode(){
		char[] x = {(char) this.xMax, (char) this.xMin, (char) this.yMax, (char) this.yMin};
		String s = String.copyValueOf(x);
		return s.hashCode();
	}
}
