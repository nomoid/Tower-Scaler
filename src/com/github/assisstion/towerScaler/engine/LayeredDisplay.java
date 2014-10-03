package com.github.assisstion.towerScaler.engine;

import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public interface LayeredDisplay extends Display{
	void render(GameContainer gc, Graphics g, int layer) throws SlickException;

	Set<Integer> renderableLayers();
}
