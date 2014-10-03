package com.github.assisstion.towerScaler.engine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public interface Display{
	void render(GameContainer gc, Graphics g) throws SlickException;

	void init(GameContainer gc) throws SlickException;

	void update(GameContainer gc, int delta) throws SlickException;
}
