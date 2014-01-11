package com.github.assisstion.towerScaler.entity;

import java.io.File;

public class PlatformBlock extends CollisionEntity{
	public PlatformBlock(double x, double y){
		super(x, y, "resources" + File.separator + "block.png");
	}
}
