package com.github.assisstion.TSToolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class TSBoxLabel extends TSSingleContainer{
	
	protected int borderPadding;
	protected Color boxFillColor;
	protected Color boxBorderColor;
	
	public TSBoxLabel(GUIContext container){
		this(container, null);
	}

	public TSBoxLabel(GUIContext container, TSComponent item){
		this(container, item, Color.white, Color.black);
	}
	
	public TSBoxLabel(GUIContext container, TSComponent item, 
			Color boxFillColor, Color boxBorderColor){
		this(container, item, boxFillColor, boxBorderColor, 2);
	}
	
	public TSBoxLabel(GUIContext container, TSComponent item, 
			Color boxFillColor, Color boxBorderColor, int borderPadding){
		super(container, 0, 0, 0, 0, item);
		this.borderPadding = borderPadding;
		this.boxFillColor = boxFillColor;
		this.boxBorderColor = boxBorderColor;
	}
	
	@Override
	public int getContainerX(){
		return 0;
	}
	
	@Override
	public int getContainerY(){
		return 0;
	}
	
	@Override
	public int getX(){
		return (int) getX1();
	}
	
	@Override
	public int getY(){
		return (int) getY1();
	}
	
	@Override
	public int getWidth(){
		return (int) (getX2() - getX1());
	}
	
	@Override
	public int getHeight(){
		return (int) (getY2() - getY1());
	}
	
	@Override
	public double getX1(){
		return component.getX1() - borderPadding;
	}
	
	@Override
	public double getX2(){
		return component.getX2() + borderPadding;
	}
	
	@Override
	public double getY1(){
		return component.getY1() - borderPadding;
	}
	
	@Override
	public double getY2(){
		return component.getY2() + borderPadding;
	}
	
	public Color getBoxFillColor(){
		return boxFillColor;
	}
	
	public Color getBoxBorderColor(){
		return boxBorderColor;
	}
	
	@Override
	public void renderContainer(GameContainer container, Graphics g, int x,
			int y) throws SlickException{
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
