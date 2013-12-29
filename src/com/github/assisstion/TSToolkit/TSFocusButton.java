package com.github.assisstion.TSToolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.towerScaler.box.BoxHelper;


public class TSFocusButton extends TSComponent{
	
	protected Color boxFillColor;
	protected Color boxBorderColor;
	protected TSMouseFocusable parent;

	public TSFocusButton(GUIContext container, TSMouseFocusable parent){
		this(container, parent, 0, 0);
	}
	
	public TSFocusButton(GUIContext container, TSMouseFocusable parent, int x, int y){
		this(container, parent, x, y, x, y);
	}
	
	public TSFocusButton(GUIContext container, TSMouseFocusable parent, int x1, int y1, int x2, int y2){
		this(container, parent, x1, y1, x2, y2, Color.white, Color.black);
	}

	public TSFocusButton(GUIContext container, TSMouseFocusable parent, int x1, int y1, int x2, int y2, Color boxFillColor, Color boxBorderColor){
		super(container, x1, y1, x2, y2);
		this.boxFillColor = boxFillColor;
		this.boxBorderColor = boxBorderColor;
		this.parent = parent;
	}
	
	@Override
	public void mouseReleased(int button, int x, int y){
		if(getParent().hasFocus()){
			if(BoxHelper.pointIn(this, x, y)){
				notifyListeners();
			}
		}
	}
	
	public Color getBoxFillColor(){
		return boxFillColor;
	}
	
	public Color getBoxBorderColor(){
		return boxBorderColor;
	}
	
	public TSMouseFocusable getParent(){
		return parent;
	}
	
	public void setParent(TSMouseFocusable parent){
		this.parent = parent;
	}

	@Override
	public void render(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();
		g.setColor(getBoxFillColor());
		g.fillRect(getX() + x, getY() + y, getWidth(), getHeight());
		g.setColor(getBoxBorderColor());
		g.drawRect(getX() + x, getY() + y, getWidth(), getHeight());
		g.setColor(tempColor);
		g.setFont(tempFont);
	}
}
