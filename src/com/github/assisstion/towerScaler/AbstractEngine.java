package com.github.assisstion.towerScaler;

import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class AbstractEngine implements Engine{
	
	@Override
	public void render(GameContainer gc, Graphics g) throws Exception{
		render(gc, g, 0);
	}

	@Override
	public void setState(String state){
		getParent().setState(state);
	}

	@Override
	public void setEngineProperties(Map<String, String> properties){
		getParent().setEngineProperties(properties);
	}

	@Override
	public void setEngineProperty(String key, String value){
		getParent().setEngineProperty(key, value);
	}

	@Override
	public String getState(){
		return getParent().getState();
	}

	@Override
	public Map<String, String> getEngineProperties(){
		return getParent().getEngineProperties();
	}

	@Override
	public String getEngineProperty(String key){
		return getParent().getEngineProperty(key);
	}
}
