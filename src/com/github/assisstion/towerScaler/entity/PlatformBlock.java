package com.github.assisstion.towerScaler.entity;

import java.io.File;

public class PlatformBlock extends CollisionEntity{
	public PlatformBlock(double x, double y, String location){
		super(x, y, "resources" + File.separator + location);
	}
}
