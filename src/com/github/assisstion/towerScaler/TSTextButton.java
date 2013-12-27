package com.github.assisstion.towerScaler;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.towerScaler.box.BoxHelper;


public class TSTextButton extends TSComponent{
	
	public static final int ALIGN_LEFT = -1;
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_UP = -1;
	public static final int ALIGN_DOWN = 1;
	
	protected String text;
	protected Color textColor;
	protected Color boxFillColor;
	protected Color boxBorderColor;
	protected Font font;
	protected int borderPadding;
	protected int alignX;
	protected int alignY;
	protected Engine parent;
	protected String onState;
	protected String onPropertyKey;
	protected String onPropertyValue;

	public TSTextButton(GUIContext container, Engine parent, String onState, String text){
		this(container, parent, onState, 0, 0, text);
	}
	
	public TSTextButton(GUIContext container, Engine parent, String onState, int x, int y, String text){
		this(container, parent, onState, x, y, text, new TrueTypeFont(Helper.getDefaultFont(), true));
	}
	
	public TSTextButton(GUIContext container, Engine parent, String onState, int x, int y, String text, Font font){
		this(container, parent, onState, x, y, text, font, Color.black, Color.white, Color.black);
	}

	public TSTextButton(GUIContext container, Engine parent, String onState, int x, int y, String text,
			Font font, Color textColor, Color boxFillColor, Color boxBorderColor){
		this(container, parent, onState, x, y, text, font, textColor, boxFillColor, boxBorderColor, 2, 0, 0);
	}
	
	public TSTextButton(GUIContext container, Engine parent, String onState, int x, int y, String text,
			Font font, Color textColor, Color boxFillColor, Color boxBorderColor, 
			int padding, int alignX, int alignY){
		super(container, x, y);
		this.text = text;
		this.font = font;
		this.textColor = textColor;
		this.boxFillColor = boxFillColor;
		this.boxBorderColor = boxBorderColor;
		this.borderPadding = padding;
		this.alignX = alignX;
		this.alignY = alignY;
		this.parent = parent;
		this.onState = onState;
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException{
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();
		g.setColor(getBoxFillColor());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		g.setColor(getBoxBorderColor());
		g.drawRect(getX(), getY(), getWidth(), getHeight());
		g.setColor(getTextColor());
		g.setFont(getFont());
		g.drawString(getText(), getTextX(), getTextY());
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
	
	@Override
	public void mouseReleased(int button, int x, int y){
		if(getEngineParent() == null || ((getOnState() == null || 
				getEngineParent().getState().equals(getOnState())) &&
				(getOnPropertyKey() == null || getOnPropertyValue() == null ||
				getEngineParent().getEngineProperty(getOnPropertyKey())
				.equals(getOnPropertyValue())))){
			if(BoxHelper.pointIn(this, x, y)){
				notifyListeners();
			}
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
	
	public Color getBoxFillColor(){
		return boxFillColor;
	}
	
	public Color getBoxBorderColor(){
		return boxBorderColor;
	}
	
	public Color getTextColor(){
		return textColor;
	}
	
	public Engine getEngineParent(){
		return parent;
	}
	
	public void setEngineParent(Engine parent){
		this.parent = parent;
	}
	
	public void setOnState(String state){
		onState = state;
	}
	
	public String getOnState(){
		return onState;
	}
	
	public void putOnProperty(String key, String value){
		onPropertyKey = key;
		onPropertyValue = value;
	}
	
	public String getOnPropertyKey(){
		return onPropertyKey;
	}
	
	public String getOnPropertyValue(){
		return onPropertyValue;
	}
}
