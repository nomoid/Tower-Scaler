package com.github.assisstion.towerScaler.TSToolkit;

import java.util.Collections;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSBox;
import com.github.assisstion.TSToolkit.TSColoredBox;

public class TSBoxMenu extends TSAbstractMenu implements TSColoredBox{

	public TSBoxMenu(GUIContext container, Color boxFillColor,
			Color boxBorderColor){
		this(container, 0, 0, boxFillColor, boxBorderColor);
	}

	public TSBoxMenu(GUIContext container, int x, int y, Color boxFillColor,
			Color boxBorderColor){
		this(container, x, y, x, y, boxFillColor, boxBorderColor);
	}

	public TSBoxMenu(GUIContext container, int x1, int y1, int x2, int y2,
			Color boxFillColor, Color boxBorderColor){
		super(container, x1, y1, x2, y2);
		this.boxFillColor = boxFillColor;
		this.boxBorderColor = boxBorderColor;
	}

	protected TSBox box;
	protected Color boxFillColor;
	protected Color boxBorderColor;
	protected boolean initialized;
	protected boolean visible;

	// Subclasses should call super.render()
	@Override
	public void render(GameContainer gc, Graphics g, int layer)
			throws SlickException{
		render(gc, g);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		render(gc, g, 0, 0);
	}

	// Subclasses should call super.init()
	@Override
	public void init(GameContainer gc) throws SlickException{
		initialized = true;
		box = new TSBox(gc, getX(), getY(), getX() + getWidth(), getY() +
				getHeight(), getFillColor(), getBorderColor());
	}

	@Override
	public Color getFillColor(){
		return boxFillColor;
	}

	@Override
	public Color getBorderColor(){
		return boxBorderColor;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException{

	}

	@Override
	public Set<Integer> renderableLayers(){
		return Collections.singleton(1);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException{
		render((GameContainer) container, g, 0, 0);
	}

	@Override
	public void renderContainer(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		if(!initialized){
			init((GameContainer) gc);
		}
		box.render(gc, g);
	}
}
