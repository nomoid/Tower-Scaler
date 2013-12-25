package com.github.assisstion.towerScaler.box;


public interface MutableBoxable extends Boxable{
	void setPos(double x1, double x2, double y1, double y2);
	void setPos(MutableBoxable b);
}
