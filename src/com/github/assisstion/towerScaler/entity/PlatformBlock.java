package com.github.assisstion.towerScaler.entity;

import com.github.assisstion.towerScaler.Helper;

public class PlatformBlock extends CollisionEntity{
	public PlatformBlock(double x, double y){
		super(x, y, Helper.javaroot + "resources/block.png");
	}
}
