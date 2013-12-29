package com.github.assisstion.TSToolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.towerScaler.Helper;

public class TSTextLabel extends TSComponent{
	
	protected Font font;
	protected String text;
	protected Color textColor;
	protected int alignX;
	protected int alignY;
	
	public TSTextLabel(GUIContext container, String text){
		this(container, 0, 0, text);
	}
	
	public TSTextLabel(GUIContext container, int x, int y, String text){
		this(container, x, y, text, new TrueTypeFont(Helper.getDefaultFont(), true));
	}
	
	public TSTextLabel(GUIContext container, int x, int y, String text, Font font){
		this(container, x, y, text, font, Color.black);
	}
	
	public TSTextLabel(GUIContext container, int x, int y, String text, 
			Font font, Color textColor){
		this(container, x, y, text, font, textColor, 0, 0);
	}
	
	public TSTextLabel(GUIContext container, int x, int y, String text, 
			Font font, Color textColor, int alignX, int alignY){
		super(container, x, y, 0, 0);
		this.text = text;
		this.font = font;
		this.textColor = textColor;
		this.alignX = alignX;
		this.alignY = alignY;
	}

	@Override
	public void render(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();
		g.setColor(getTextColor());
		g.setFont(getFont());
		g.drawString(getText(), getTextX() + x, getTextY() + y);
		g.setColor(tempColor);
		g.setFont(tempFont);
	}
	
	@Override
	public int getHeight(){
		return getTextHeight();
	}
	

	public int getTextHeight(){
		return getFont().getHeight(getText());
	}
	
	@Override
	public int getWidth(){
		return getTextWidth();
	}
	
	public int getTextWidth(){
		return getFont().getWidth(getText());
	}
	
	@Override
	public double getX1(){
		return getTextX();
	}

	@Override
	public double getX2(){
		return getTextX() + getWidth();
	}
	
	public int getTextX(){
		switch(getAlignX()){
			case -1:
				return getRealX();
			case 0:
				return getRealX() - (getTextWidth() / 2);
			case 1:
				return getRealX() - getTextWidth();
			default:
				return getRealX();
		}
	}
	
	public int getRealX(){
		return (int) super.getX1();
	}
	
	@Override
	public double getY1(){
		return getTextY();
	}

	@Override
	public double getY2(){
		return getTextY() + getHeight();
	}
	
	public int getTextY(){
		switch(getAlignY()){
			case -1:
				return getRealY();
			case 0:
				return getRealY() - (getTextHeight() / 2);
			case 1:
				return getRealY() - getTextHeight();
			default:
				return getRealY();
		}
	}
	
	public int getRealY(){
		return (int) super.getY1();
	}
	
	public Font getFont(){
		return font;
	}
	
	public String getText(){
		return text;
	}
	
	public int getAlignX(){
		return alignX;
	}
	
	public int getAlignY(){
		return alignY;
	}
	
	public Color getTextColor(){
		return textColor;
	}
}
