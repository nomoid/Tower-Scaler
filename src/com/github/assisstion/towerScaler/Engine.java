package com.github.assisstion.towerScaler;

import java.util.Map;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Engine{
	void render(GameContainer gc, Graphics g, int layer) throws Exception;
	void render(GameContainer gc, Graphics g) throws Exception;
	void init(GameContainer gc) throws Exception;
	void update(GameContainer gc, int delta) throws Exception;
	void setState(String state);
	void setEngineProperties(Map<String, String> properties);
	void setEngineProperty(String key, String value);
	String getState();
	Map<String, String> getEngineProperties();
	String getEngineProperty(String key);
	Engine getParent();
	Set<Integer> renderableLayers();
	Set<String> renderingStates();
}
