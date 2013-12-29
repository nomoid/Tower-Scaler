package com.github.assisstion.towerScaler;

import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface LayeredDisplay extends Display{
	void render(GameContainer gc, Graphics g, int layer) throws Exception;
	Set<Integer> renderableLayers();
}
