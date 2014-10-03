package com.github.assisstion.towerScaler.box;

import com.github.assisstion.towerScaler.Helper;

public class BoxImpl implements Box{

	public static int rounding = 6;

	protected double xMin;
	protected double xMax;
	protected double yMin;
	protected double yMax;

	public BoxImpl(MutableBoxable b){
		setPos(b.getX1(), b.getX2(), b.getY1(), b.getY2());
	}

	public BoxImpl(double x1, double x2, double y1, double y2){
		setPos(x1, x2, y1, y2);
	}

	@Override
	public boolean pointIn(double x, double y){
		x = round(x);
		y = round(y);
		if(x >= this.xMin && x <= this.xMax && y >= this.yMin && y <= this.yMax){
			return true;
		}
		return false;
	}

	@Override
	public boolean overlaps(Box b){
		if(this.pointIn(b.getX1(), b.getY1()) ||
				this.pointIn(b.getX1(), b.getY2()) ||
				this.pointIn(b.getX2(), b.getY1()) ||
				this.pointIn(b.getX2(), b.getY2()) ||
				b.pointIn(this.getX1(), this.getY1()) ||
				b.pointIn(this.getX1(), this.getY2()) ||
				b.pointIn(this.getX2(), this.getY1()) ||
				b.pointIn(this.getX2(), this.getY2())){
			return true;
		}
		return false;
	}

	public void setX1(double x1){
		this.xMin = round(x1);
	}

	public void setX2(double x2){
		this.xMax = round(x2);
	}

	public void setY1(double y1){
		this.yMin = round(y1);
	}

	public void setY2(double y2){
		this.yMax = round(y2);
	}

	@Override
	public void setPos(double x1, double x2, double y1, double y2){
		setX1(x1);
		setX2(x2);
		setY1(y1);
		setY2(y2);
	}

	@Override
	public void setPos(MutableBoxable b){
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
		char[] x = {(char) this.xMax, (char) this.xMin, (char) this.yMax,
				(char) this.yMin};
		String s = String.copyValueOf(x);
		return s.hashCode();
	}

	protected double round(double x){
		if(rounding == 0){
			return x;
		}
		else{
			return Helper.round(x, rounding);
		}
	}
}
