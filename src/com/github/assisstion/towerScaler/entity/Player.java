package com.github.assisstion.towerScaler.entity;

import java.io.File;

public class Player extends GravitationalEntity{
	public Player(){
		super(100, 500, "resources" + File.separator + "block.png");
	}
}
