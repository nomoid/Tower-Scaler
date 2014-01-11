package com.github.assisstion.towerScaler.engine;

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
import com.github.assisstion.towerScaler.Helper;
import com.github.assisstion.towerScaler.Main;
import com.github.assisstion.towerScaler.menu.HighScoreMenu;

public class MenuEngine extends AbstractEngine{
	
	protected MainEngine engine;
	protected TSFocusTextButton startButton;
	protected TSFocusTextButton highScoreButton;
	protected TrueTypeFont titleFont;
	protected TrueTypeFont subtitleFont;
	
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
		g.setFont(titleFont);
		g.setColor(Color.black);
		String title = "Tower Scaler";
		g.drawString(title, ((Main.getGameFrameWidth() - titleFont.getWidth(title)) / 2), 100);
		g.setFont(subtitleFont);
		String author = "by assisstion";
		g.drawString(author, ((Main.getGameFrameWidth() - subtitleFont.getWidth(author)) / 2), 150);
		startButton.render(gc, g);
		highScoreButton.render(gc, g);
	}
	
	protected void render1(GameContainer gc, Graphics g){
		/* UNUSED
		 * Do nothing (yet)
		 */
	}

	@Override
	public void init(GameContainer gc){
		titleFont = new TrueTypeFont(new Font("Calibri", Font.PLAIN, 40), true);
		subtitleFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 20), true);
		startButton = new TSFocusTextButton(gc, this, Main.getGameFrameWidth() / 2, 200,
				"Start Game", Helper.getDefaultFont(), 
				Color.black, new Color(150, 150, 255), Color.black);
		startButton.addListener(new ComponentListener(){
			@Override
			public void componentActivated(AbstractComponent source){
				getParent().startGame();
			}
		});
		highScoreButton = new TSFocusTextButton(gc, this, Main.getGameFrameWidth() / 2, 240,
				"Highscores", Helper.getDefaultFont(), 
				Color.black, new Color(255, 150, 150), Color.black);
		highScoreButton.addListener(new ComponentListener(){
			@Override
			public void componentActivated(AbstractComponent source){
				HighScoreMenu hsm = getParent().getHighScoreMenu();
				hsm.setVisible(true);
				addMenu(hsm);
			}
		});
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
	public boolean hasInputFocus(){
		return getParent().hasInputFocus();
	}
	
	@Override
	public void setInputFocus(boolean focus){
		//Do nothing
	}
}
