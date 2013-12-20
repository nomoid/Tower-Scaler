package com.github.assisstion.towerScaler.entity;

import com.github.assisstion.towerScaler.Helper;

public class Player extends GravitationalEntity{
	public Player(){
		super(100, 500, Helper.javaroot + "resources/block.png");
	}
}
