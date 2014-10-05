package com.github.assisstion.towerScaler.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSBoxLabel;
import com.github.assisstion.TSToolkit.TSFocusTextButton;
import com.github.assisstion.TSToolkit.TSTextLabel;
import com.github.assisstion.towerScaler.Helper;
import com.github.assisstion.towerScaler.TSToolkit.TSWindowMenu;
import com.github.assisstion.towerScaler.engine.GameEngine;

public class OptionsMenu extends TSWindowMenu{

	protected GameEngine ge;
	protected TSFocusTextButton tsftb;

	public OptionsMenu(GUIContext container, int x1, int y1, int x2, int y2, GameEngine ge){
		super(container, x1, y1, x2, y2, new Color(200, 200, 255), Color.black);
		this.ge = ge;
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
		super.init(gc);
		TSTextLabel label = new TSTextLabel(gc, getWidth() / 2,
				getHeight() / 8, " Options ");
		TSBoxLabel box = new TSBoxLabel(gc, label, Color.green, Color.black);
		tsftb = new TSFocusTextButton(gc, this, getWidth() * 3 / 4,
				getHeight() / 4, " Toggle Sound ", Helper.getDefaultFont(),
				Color.cyan, Color.black, Color.black);
		tsftb.addListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent arg0){
				ge.setSoundOn(!ge.isSoundOn());
			}

		});
		addComponent(box);
		addComponent(tsftb);
	}

	@Override
	public void renderContainer(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		super.renderContainer(gc, g, x, y);
		TSTextLabel name = new TSTextLabel(gc, getWidth() / 8, getHeight() / 4,
				"Sound:     " + (ge.isSoundOn() ? "Off" : "On"), Helper.getDefaultFont(),
				Color.black, -1, 0);
		name.render(gc, g, getContainerX() + x, getContainerY() + y);
	}
}
