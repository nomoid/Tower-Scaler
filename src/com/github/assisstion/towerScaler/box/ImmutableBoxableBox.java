package com.github.assisstion.towerScaler.box;

import com.github.assisstion.TSToolkit.TSBoxable;

public class ImmutableBoxableBox implements Box{

	private TSBoxable b;

	public ImmutableBoxableBox(TSBoxable b){
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
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPos(MutableBoxable b){
		throw new UnsupportedOperationException();
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
