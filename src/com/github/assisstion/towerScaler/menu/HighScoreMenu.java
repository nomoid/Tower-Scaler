package com.github.assisstion.towerScaler.menu;

import java.util.List;

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
import com.github.assisstion.towerScaler.Constants;
import com.github.assisstion.towerScaler.Helper;
import com.github.assisstion.towerScaler.HighScoreTable;
import com.github.assisstion.towerScaler.Score;
import com.github.assisstion.towerScaler.TSToolkit.TSWindowMenu;

public class HighScoreMenu extends TSWindowMenu{

	protected HighScoreTable highScores = new HighScoreTable();
	protected int index = 0;

	public HighScoreMenu(GUIContext container, int x1, int y1, int x2, int y2){
		super(container, x1, y1, x2, y2, new Color(200, 200, 255), Color.black);
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
		super.init(gc);
		TSTextLabel label = new TSTextLabel(gc, getWidth() / 2,
				getHeight() / 8, " High Scores ");
		TSBoxLabel box = new TSBoxLabel(gc, label, Color.green, Color.black);
		addComponent(box);
		TSFocusTextButton buttonPrev = new TSFocusTextButton(gc, this,
				getWidth() / 4, getHeight() * 9 / 10, " Prev ",
				Helper.getDefaultFont(), Color.black,
				new Color(150, 150, 150), Color.black);
		buttonPrev.addListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent arg0){
				setIndex(getIndex() - 10);
			}

		});
		TSFocusTextButton buttonNext = new TSFocusTextButton(gc, this,
				getWidth() * 3 / 4, getHeight() * 9 / 10, " Next ",
				Helper.getDefaultFont(), Color.black,
				new Color(150, 150, 150), Color.black);
		addComponent(buttonPrev);
		buttonNext.addListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent arg0){
				setIndex(getIndex() + 10);
			}

		});
		addComponent(buttonNext);
	}

	@Override
	public void renderContainer(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		super.renderContainer(gc, g, x, y);
		List<Score> scores = highScores.getScores(index, Constants.scoresDisplayed + index);
		int i = 0;
		for(Score score : scores){
			TSTextLabel name = new TSTextLabel(gc, getWidth() * 1 / 5,
					getHeight() * (i + 4) / 16, i + index + 1 + ". " +
							score.getName(), Helper.getDefaultFont(),
							Color.black, -1, 0);
			TSTextLabel number = new TSTextLabel(gc, getWidth() * 4 / 5,
					getHeight() * (i + 4) / 16, String.valueOf(Math.round(score
							.getScore() * 100) / 100.0),
							Helper.getDefaultFont(), Color.black, 1, 0);
			name.render(gc, g, getContainerX() + x, getContainerY() + y);
			number.render(gc, g, getContainerX() + x, getContainerY() + y);
			i++;
		}
	}

	public void setIndex(int index){
		int size = highScores.getScores().size() - 1;
		if(index > size - Constants.scoresDisplayed){
			index = size -
					size % Constants.scoresDisplayed;
		}
		if(index < 0){
			index = 0;
		}
		this.index = index;
	}

	public int getIndex(){
		return index;
	}

	public HighScoreTable getHighScores(){
		return highScores;
	}

	public void setHighScores(HighScoreTable highScores){
		this.highScores = highScores;
	}
}
