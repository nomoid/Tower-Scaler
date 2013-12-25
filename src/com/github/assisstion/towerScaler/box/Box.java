package com.github.assisstion.towerScaler.box;


public interface Box extends MutableBoxable{
	boolean pointIn(double x, double y);
	boolean overlaps(Box b);
}
