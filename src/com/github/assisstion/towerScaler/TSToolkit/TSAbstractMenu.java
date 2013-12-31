package com.github.assisstion.towerScaler.TSToolkit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSMultiContainer;

public abstract class TSAbstractMenu extends TSMultiContainer implements TSMenu{
	
	public TSAbstractMenu(GUIContext container){
		this(container, 0, 0);
	}
	
	public TSAbstractMenu(GUIContext container, int x, int y){
		this(container, x, y, x, y);
	}
	
	public TSAbstractMenu(GUIContext container, int x1, int y1, int x2, int y2){
		super(container, x1, y1, x2, y2);
	}

	protected boolean visible;

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		render(gc, g, 0);
	}

	@Override
	public boolean hasFocus(){
		return isVisible();
	}

	@Override
	public boolean isVisible(){
		return visible;
	}

	@Override
	public void setVisible(boolean visible){
		this.visible = visible;
	}

}
