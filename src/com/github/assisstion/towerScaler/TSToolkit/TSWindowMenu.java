package com.github.assisstion.towerScaler.TSToolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSFocusButton;

public class TSWindowMenu extends TSBoxMenu{
	
	protected TSFocusButton closeButton;
	protected TSWindowMenu instance = this;

	public TSWindowMenu(GUIContext container, Color boxFillColor, Color boxBorderColor){
		this(container, 0, 0, boxFillColor, boxBorderColor);
	}
	
	public TSWindowMenu(GUIContext container, int x, int y, Color boxFillColor, Color boxBorderColor){
		this(container, x, y, x, y, boxFillColor, boxBorderColor);
	}
	
	public TSWindowMenu(GUIContext container, int x1, int y1, int x2, int y2, Color boxFillColor, Color boxBorderColor){
		super(container, x1, y1, x2, y2, boxFillColor, boxBorderColor);
	}
	
	@Override
	public void renderContainer(GameContainer gc, Graphics g, int x, int y) throws SlickException{
		super.renderContainer(gc, g, x, y);
		closeButton.render(gc, g, x, y);
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException{
		super.init(gc);
		closeButton = new TSFocusButton(gc, this, getX(), getY(), getX() + 16, getY() + 16,
				new Color(255, 0, 0, 127), Color.black);
		closeButton.addListener(new ComponentListener(){
			@Override
			public void componentActivated(AbstractComponent source){
				instance.close();
			}
		});
	}
}
