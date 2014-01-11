package com.github.assisstion.TSToolkit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public abstract class TSSingleContainer extends TSComponent implements TSContainer{
	protected TSComponent component;

	public TSSingleContainer(GUIContext container){
		this(container, 0, 0);
	}
	
	public TSSingleContainer(GUIContext container, int x, int y){
		this(container, x, y, null);
	}
	
	public TSSingleContainer(GUIContext container, int x, int y, TSComponent component){
		this(container, x, y, x, y, null);
	}
	
	public TSSingleContainer(GUIContext container, int x1, int y1, int x2, int y2, TSComponent component){
		super(container, x1, y1, x2, y2);
		this.component = component;
	}
	
	@Override
	public int getContainerX(){
		return getX();
	}
	
	@Override
	public int getContainerY(){
		return getY();
	}

	protected void setComponent(TSComponent component){
		this.component = component;
		component.setParent(this);
	}
	
	protected void removeComponent(){
		if(component != null){
			component.setParent(null);
		}
		this.component = null;
	}
	
	protected TSComponent getComponent(){
		return component;
	}

	@Override
	public final void render(GameContainer container, Graphics g, int x, int y) throws SlickException{
		renderContainer(container, g, x, y);
		component.render(container, g, getContainerX() + x, getContainerY() + y);
	}
	
	

}
