package com.github.assisstion.towerScaler.entity;

public interface Boxable{
	double getX1();
	double getX2();
	double getY1();
	double getY2();
	void setPos(double x1, double x2, double y1, double y2);
	void setPos(Boxable b);
}
