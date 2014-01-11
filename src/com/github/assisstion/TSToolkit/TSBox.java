package com.github.assisstion.TSToolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class TSBox extends TSComponent implements TSColoredBox{
	
	protected Color fillColor;
	protected Color borderColor;

	protected TSBox(GUIContext container, Color fillColor, Color borderColor){
		this(container, 0, 0, fillColor, borderColor);
	}
	
	protected TSBox(GUIContext container, int x, int y, Color fillColor, Color borderColor){
		this(container, x, y, 0, 0, fillColor, borderColor);
	}
	
	public TSBox(GUIContext container, int x1, int y1, int x2, int y2, Color fillColor, Color borderColor){
		super(container, x1, y1, x2, y2);
		this.fillColor = fillColor;
		this.borderColor = borderColor;
	}

	public Color getFillColor(){
		return fillColor;
	}
	
	public Color getBorderColor(){
		return borderColor;
	}

	@Override
	public void render(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		Color color = g.getColor();
		g.setColor(getFillColor());
		g.fillRect(getX() + x, getY() + y, getWidth(), getHeight());
		g.setColor(getBorderColor());
		g.drawRect(getX() + x, getY() + y, getWidth(), getHeight());
		g.setColor(color);
	}
}
