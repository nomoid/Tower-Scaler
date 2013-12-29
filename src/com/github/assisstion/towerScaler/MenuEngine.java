package com.github.assisstion.towerScaler;

import java.awt.Font;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

import com.github.assisstion.TSToolkit.TSFocusTextButton;
import com.github.assisstion.towerScaler.TSToolkit.TSMenu;

public class MenuEngine extends AbstractEngine{
	
	protected MainEngine engine;
	protected TSFocusTextButton startButton;
	protected TSFocusTextButton highScoreButton;
	
	public MenuEngine(MainEngine e){
		engine = e;
	}
	
	protected MenuEngine(){
		
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		render(gc, g, 0);
	}

	@Override
	public void render(GameContainer gc, Graphics g, int layer)
			throws SlickException{
		switch(layer){
			case 0:
				render0(gc, g);
				break;
			case 1:
				render1(gc, g);
				break;
		}
	}
	
	protected void render0(GameContainer gc, Graphics g) throws SlickException{
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
		startButton.render(gc, g);
		highScoreButton.render(gc, g);
		/*
		String text = "Start Game";
		g.setColor(new Color(150, 150, 255));
		g.fillRect(((Main.getGameFrameWidth() - ttf.getWidth(text)) / 2) - 2, 200 - 2, ttf.getWidth(text) + 4, ttf.getHeight(text) + 4);
		g.setColor(Color.black);
		g.drawRect(((Main.getGameFrameWidth() - ttf.getWidth(text)) / 2) - 2, 200 - 2, ttf.getWidth(text) + 4, ttf.getHeight(text) + 4);
		g.drawString(text, ((Main.getGameFrameWidth() - ttf.getWidth(text)) / 2), 200);
		*/
	}
	
	protected void render1(GameContainer gc, Graphics g){
		String shs = getEngineProperty("show_highscores");
		if(shs != null && shs.equals("true")){
			
		}
		/*
		g.setColor(new Color(0, 0, 255));
		g.fillRect(0, 0, 400, 400);
		*/
	}

	@Override
	public void init(GameContainer gc){
		startButton = new TSFocusTextButton(gc, this, Main.getGameFrameWidth() / 2, 200,
				"Start Game", new TrueTypeFont(Helper.getDefaultFont(), true), 
				Color.black, new Color(150, 150, 255), Color.black);
		startButton.addListener(new ComponentListener(){
			@Override
			public void componentActivated(AbstractComponent source){
				getParent().startGame();
			}
		});
		highScoreButton = new TSFocusTextButton(gc, this, Main.getGameFrameWidth() / 2, 240,
				"Highscores", new TrueTypeFont(Helper.getDefaultFont(), true), 
				Color.black, new Color(255, 150, 150), Color.black);
		highScoreButton.addListener(new ComponentListener(){
			@Override
			public void componentActivated(AbstractComponent source){
				HighScoreMenu hsm = getParent().getHighScoreMenu();
				hsm.setVisible(true);
				addMenu(hsm);
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
	public MainEngine getParent(){
		return engine;
	}

	@Override
	public Set<Integer> renderableLayers(){
		Set<Integer> layers = new HashSet<Integer>();
		Collections.addAll(layers, 0, 1);
		return layers;
	}
	
	@Override
	public Set<String> renderingStates(){
		return Collections.singleton("menu");
	}

	@Override
	public boolean hasFocus(){
		for(TSMenu menu : getMenus()){
			if(menu.isVisible()){
				return false;
			}
		}
		return getState().equals("menu");
	}
}
