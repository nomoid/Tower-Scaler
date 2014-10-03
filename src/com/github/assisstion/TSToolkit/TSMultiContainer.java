package com.github.assisstion.TSToolkit;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public abstract class TSMultiContainer extends TSComponent implements
		TSContainer{

	protected List<TSComponent> components;

	public TSMultiContainer(GUIContext container){
		this(container, 0, 0);
	}

	public TSMultiContainer(GUIContext container, int x, int y){
		this(container, x, y, x, y);
	}

	public TSMultiContainer(GUIContext container, int x1, int y1, int x2, int y2){
		super(container, x1, y1, x2, y2);
		components = new LinkedList<TSComponent>();
	}

	@Override
	public int getContainerX(){
		return getX();
	}

	@Override
	public int getContainerY(){
		return getY();
	}

	protected void addComponent(TSComponent component){
		components.add(component);
		component.setParent(this);
	}

	protected void removeComponent(TSComponent component){
		component.setParent(null);
		components.remove(component);
	}

	protected List<TSComponent> getComponents(){
		return components;
	}

	protected void setComponents(List<TSComponent> components){
		this.components = components;
		for(TSComponent component : components){
			component.setParent(this);
		}
	}

	@Override
	public final void render(GameContainer container, Graphics g, int x, int y)
			throws SlickException{
		renderContainer(container, g, x, y);
		for(TSComponent component : components){
			component.render(container, g, getContainerX() + x,
					getContainerY() + y);
		}
	}

	@Override
	public abstract void renderContainer(GameContainer container, Graphics g,
			int x, int y) throws SlickException;
}
