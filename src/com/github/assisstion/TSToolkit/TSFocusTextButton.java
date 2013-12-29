package com.github.assisstion.TSToolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.towerScaler.Helper;

public class TSFocusTextButton extends TSFocusButton{
	
	public static final int ALIGN_LEFT = -1;
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_UP = -1;
	public static final int ALIGN_DOWN = 1;
	
	protected String text;
	protected Color textColor;
	protected Font font;
	protected int borderPadding;
	protected int alignX;
	protected int alignY;

	public TSFocusTextButton(GUIContext container, TSMouseFocusable parent, String text){
		this(container, parent, 0, 0, text);
	}
	
	public TSFocusTextButton(GUIContext container, TSMouseFocusable parent, int x, int y, String text){
		this(container, parent, x, y, text, new TrueTypeFont(Helper.getDefaultFont(), true));
	}
	
	public TSFocusTextButton(GUIContext container, TSMouseFocusable parent, int x, int y, String text, Font font){
		this(container, parent, x, y, text, font, Color.black, Color.white, Color.black);
	}

	public TSFocusTextButton(GUIContext container, TSMouseFocusable parent, int x, int y, String text,
			Font font, Color textColor, Color boxFillColor, Color boxBorderColor){
		this(container, parent, x, y, text, font, textColor, boxFillColor, boxBorderColor, 2, 0, 0);
	}
	
	public TSFocusTextButton(GUIContext container, TSMouseFocusable parent, int x, int y, String text,
			Font font, Color textColor, Color boxFillColor, Color boxBorderColor, 
			int padding, int alignX, int alignY){
		super(container, parent, x, y, 0, 0, boxFillColor, boxBorderColor);
		this.text = text;
		this.font = font;
		this.textColor = textColor;
		this.borderPadding = padding;
		this.alignX = alignX;
		this.alignY = alignY;
	}

	@Override
	public void render(GameContainer container, Graphics g, int x, int y) throws SlickException{
		super.render(container, g, x, y);
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
		return getTextHeight() + getBorderPadding() * 2;
	}
	

	public int getTextHeight(){
		return getFont().getHeight(getText());
	}
	
	@Override
	public int getWidth(){
		return getTextWidth() + getBorderPadding() * 2;
	}
	
	public int getTextWidth(){
		return getFont().getWidth(getText());
	}
	
	@Override
	public double getX1(){
		return getTextX() - getBorderPadding();
	}

	@Override
	public double getX2(){
		return getTextX() - getBorderPadding() + getWidth();
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
		return getTextY() - getBorderPadding();
	}

	@Override
	public double getY2(){
		return getTextY() - getBorderPadding() + getHeight();
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
	
	public int getBorderPadding(){
		return borderPadding;
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
