package com.github.assisstion.towerScaler.entity;

import java.io.File;

public class Player extends GravitationalEntity{
	public Player(String sprite){
		super(100, 500, "resources" + File.separator + sprite);
	}

	public Player(String sprite, int x, int y){
		super(x, y, "resources" + File.separator + sprite);
	}
}
