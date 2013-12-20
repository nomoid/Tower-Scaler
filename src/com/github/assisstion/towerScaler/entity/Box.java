package com.github.assisstion.towerScaler.entity;

public interface Box extends Boxable{
	boolean pointIn(double x, double y);
	boolean overlaps(Box b);
}
