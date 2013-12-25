package com.github.assisstion.towerScaler;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

public class MenuEngine implements AbstractEngine{
	
	protected Engine engine;
	protected TSTextButton button;
	
	public MenuEngine(Engine e){
		engine = e;
	}
	
	protected MenuEngine(){
		
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		g.setBackground(new Color(75, 255, 75));
		Font font = new Font("Calibri", Font.PLAIN, 40);
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		g.setFont(ttf);
		g.setColor(Color.black);
		String title = "Tower Scaler";
		g.drawString(title, ((Main.getGameFrameWidth() - ttf.getWidth(title)) / 2), 100);
		Font fontSmall = new Font("Calibri", Font.PLAIN, 20);
		TrueTypeFont ttfs = new TrueTypeFont(fontSmall, true);
		g.setFont(ttfs);
		String author = "by assisstion";
		g.drawString(author, ((Main.getGameFrameWidth() - ttfs.getWidth(author)) / 2), 150);
		button.render(gc, g);
		/*
		String text = "Start Game";
		g.setColor(new Color(150, 150, 255));
		g.fillRect(((Main.getGameFrameWidth() - ttf.getWidth(text)) / 2) - 2, 200 - 2, ttf.getWidth(text) + 4, ttf.getHeight(text) + 4);
		g.setColor(Color.black);
		g.drawRect(((Main.getGameFrameWidth() - ttf.getWidth(text)) / 2) - 2, 200 - 2, ttf.getWidth(text) + 4, ttf.getHeight(text) + 4);
		g.drawString(text, ((Main.getGameFrameWidth() - ttf.getWidth(text)) / 2), 200);
		*/
	}

	@Override
	public void init(GameContainer gc){
		button = new TSTextButton(gc, Main.getGameFrameWidth() / 2, 200,
				"Start Game", new TrueTypeFont(Helper.getDefaultFont(), true), 
				Color.black, new Color(150, 150, 255), Color.black);
		button.addListener(new ComponentListener(){
			@Override
			public void componentActivated(AbstractComponent source){
				getParent().startGame();
			}
		});
		/*
		System.out.println(button.getX());
		System.out.println(button.getY());
		System.out.println(button.getWidth());
		System.out.println(button.getHeight());
		*/
	}

	@Override
	public void update(GameContainer gc, int delta){
		//Do nothing
	}

	@Override
	public String getState(){
		return getParent().getState();
	}
	
	@Override
	public void setState(String state){
		getParent().setState(state);
	}

	@Override
	public Engine getParent(){
		return engine;
	}
}
