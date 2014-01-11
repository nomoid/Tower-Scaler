package com.github.assisstion.towerScaler.menu;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSBoxLabel;
import com.github.assisstion.TSToolkit.TSTextLabel;
import com.github.assisstion.towerScaler.Helper;
import com.github.assisstion.towerScaler.HighScoreTable;
import com.github.assisstion.towerScaler.Score;
import com.github.assisstion.towerScaler.TSToolkit.TSWindowMenu;

public class HighScoreMenu extends TSWindowMenu{
	
	protected HighScoreTable highScores = new HighScoreTable();

	public HighScoreMenu(GUIContext container, int x1, int y1, int x2,
			int y2){
		super(container, x1, y1, x2, y2, new Color(200, 200, 255), Color.black);
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException{
		super.init(gc);
		TSTextLabel label = new TSTextLabel(gc, getWidth() / 2, getHeight() / 8, "High Scores");
		TSBoxLabel box = new TSBoxLabel(gc, label, Color.green, Color.black);
		addComponent(box);
	}

	@Override
	public void renderContainer(GameContainer gc, Graphics g, int x, int y) throws SlickException{
		super.renderContainer(gc, g, x, y);
		LinkedList<Score> scores = highScores.getScores();
		int i = 0;
		for(Score score : scores){
			TSTextLabel name = new TSTextLabel(gc, getWidth() * 1 / 5, 
					getHeight() * (i + 4) / 16, (i + 1) + ". " + score.getName(), 
					Helper.getDefaultFont(), Color.black, -1, 0);
			TSTextLabel number = new TSTextLabel(gc, getWidth() * 4 / 5, 
					getHeight() * (i + 4) / 16, String.valueOf(Math.round(score.getScore() * 100) / 100.0),
					Helper.getDefaultFont(), Color.black, 1, 0);
			name.render(gc, g, getContainerX() + x, getContainerY() + y);
			number.render(gc, g, getContainerX() + x, getContainerY() + y);
			i++;
		}
	}
	
	public HighScoreTable getHighScores(){
		return highScores;
	}
	
	public void setHighScores(HighScoreTable highScores){
		this.highScores = highScores;
	}
}
