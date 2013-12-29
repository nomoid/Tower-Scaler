package com.github.assisstion.TSToolkit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.towerScaler.box.BoxImpl;
import com.github.assisstion.towerScaler.box.MutableBoxable;

public abstract class TSComponent extends AbstractComponent implements TSRenderableObject{
	
	protected MutableBoxable location;

	public TSComponent(GUIContext container){
		this(container, 0, 0);
	}
	
	public TSComponent(GUIContext container, int x, int y){
		this(container, x, y, x, y);
	}
	
	public TSComponent(GUIContext container, int x1, int y1, int x2, int y2){
		super(container);
		location = new BoxImpl(x1, x2, y1, y2);
	}


	@Override
	public int getHeight(){
		return (int)(location.getY2() - location.getY1());
	}

	@Override
	public int getWidth(){
		return (int)(location.getX2() - location.getX1());
	}

	@Override
	public int getX(){
		return (int)(getX1());
	}
	
	@Override
	public double getX1(){
		return location.getX1();
	}
	
	@Override
	public double getX2(){
		return location.getX2();
	}

	@Override
	public int getY(){
		return (int)(getY1());
	}
	
	@Override
	public double getY1(){
		return location.getY1();
	}
	
	@Override
	public double getY2(){
		return location.getY2();
	}

	@Override
	public void setLocation(int x, int y){
		if(location != null){
			location.setPos(x, x + location.getX2() - location.getX1(), y, y + location.getY2() - location.getX1());
		}
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException{
		render((GameContainer) container, g, 0, 0);
	}
	
	@Override
	public abstract void render(GameContainer gc, Graphics g, int x, int y) throws SlickException;
}
