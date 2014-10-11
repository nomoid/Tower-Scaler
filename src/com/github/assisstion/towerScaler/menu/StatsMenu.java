package com.github.assisstion.towerScaler.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSBoxLabel;
import com.github.assisstion.TSToolkit.TSFocusTextButton;
import com.github.assisstion.TSToolkit.TSTextLabel;
import com.github.assisstion.towerScaler.Helper;
import com.github.assisstion.towerScaler.Score;
import com.github.assisstion.towerScaler.StatsHolder;
import com.github.assisstion.towerScaler.TSToolkit.TSWindowMenu;
import com.github.assisstion.towerScaler.engine.GameEngine;

public class StatsMenu extends TSWindowMenu{

	protected StatsHolder stats = new StatsHolder();
	protected GameEngine ge;
	protected TSFocusTextButton tsftb;
	protected String name = "";

	public StatsMenu(GUIContext container, int x1, int y1, int x2, int y2,
			GameEngine ge){
		super(container, x1, y1, x2, y2, new Color(200, 200, 255), Color.black);
		this.ge = ge;
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
		super.init(gc);
		TSTextLabel label = new TSTextLabel(gc, getWidth() / 2,
				getHeight() / 8, " Stats ");
		TSBoxLabel box = new TSBoxLabel(gc, label, Color.green, Color.black);
		addComponent(box);
	}

	@Override
	public void renderContainer(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		super.renderContainer(gc, g, x, y);
		TSTextLabel nameLabel = new TSTextLabel(gc, getWidth() / 8, 80,
				"Name:     " + name, Helper.getDefaultFont(),
				Color.black, -1, 0);
		nameLabel.render(gc, g, getContainerX() + x, getContainerY() + y);

		if(stats.getStats().containsKey(name)){
			Map<String, String> player = stats.getPlayerStats(name);
			int i = 3;
			for(Map.Entry<String, String> entry : player.entrySet()){
				String s = statToString(entry.getKey(), entry.getValue());
				if(s == null){
					continue;
				}
				TSTextLabel entryLabel = new TSTextLabel(gc, getWidth() / 8, i * 40,
						s, Helper.getDefaultFont(),
						Color.black, -1, 0);
				entryLabel.render(gc, g, getContainerX() + x, getContainerY() + y);
				i++;
			}
		}
	}

	private String statToString(String key, String value){
		if(key.equalsIgnoreCase("total-runs")){
			return "Total Runs: " + value;
		}
		if(key.equalsIgnoreCase("total-score")){
			return null;
		}
		if(key.equalsIgnoreCase("average-score")){
			return "Average Score: " + value;
		}
		if(key.equalsIgnoreCase("total-time")){
			return "Time Played: " + Long.parseLong(value) / 1000.0 +
					" seconds";
		}
		return key + ": " + value;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException{
		super.update(gc, delta);
	}

	public GameEngine getGameEngine(){
		return ge;
	}

	public void setGameEngine(GameEngine ge){
		this.ge = ge;
	}

	public void pushChar(int key, char c){
		if(key == Input.KEY_BACK || key == Input.KEY_DELETE){
			if(name.length() > 0){
				name = name.substring(0, name.length() - 1);
			}
		}
		else{
			Set<Character> allChars = new HashSet<Character>();
			Collections.addAll(allChars, Score.legalChars);
			if(allChars.contains(c)){
				name += c;
			}
		}
	}

	public StatsHolder getStatsHolder(){
		return stats;
	}

	public void setStatsHolder(StatsHolder stats){
		this.stats = stats;
	}

	public void updateFromGame(){
		StatsHolder stats = 	getStatsHolder();
		double totalScore = 0;
		int totalRuns = 0;
		Map<String, Double> playerTotals = new HashMap<String, Double>();
		Map<String, Integer> playerRuns = new HashMap<String, Integer>();
		List<Score> scores = ge.getParent().getHighScoreMenu().getHighScores().getScores();
		for(Score score : scores){
			String name = score.getName();
			if(playerTotals.containsKey(name)){
				playerTotals.put(name, playerTotals.get(name) + score.getScore());
			}
			else{
				playerTotals.put(name, score.getScore());
			}
			if(playerRuns.containsKey(name)){
				playerRuns.put(name, playerRuns.get(name) + 1);
			}
			else{
				playerRuns.put(name, 1);
			}
			totalScore += score.getScore();
			totalRuns++;
		}
		Map<String, String> global = stats.getPlayerStats("");
		global.put("total-runs", String.valueOf(totalRuns));
		global.put("total-score", String.valueOf(totalScore));
		if(totalRuns > 0){
			global.put("average-score", String.valueOf(totalScore / totalRuns));
		}
		for(Map.Entry<String, Double> entry : playerTotals.entrySet()){
			String name = entry.getKey();
			Map<String, String> ps = stats.getPlayerStats(name);
			ps.put("total-score", String.valueOf(entry.getValue()));
			if(playerRuns.containsKey(name)){
				ps.put("average-score", String.valueOf(entry.getValue() /
						playerRuns.get(name)));
			}
		}
		for(Map.Entry<String, Integer> entry : playerRuns.entrySet()){
			stats.getPlayerStats(entry.getKey()).put("total-runs",
					String.valueOf(entry.getValue()));
		}
		Map<String, String> player = stats.getPlayerStats(ge.getName());
		try{
			if(player.containsKey("total-time")){
				player.put("total-time", String.valueOf(Long.parseLong(
						player.get("total-time")) + ge.getLastGameTime()));
			}
			else{
				player.put("total-time", String.valueOf(ge.getLastGameTime()));
			}
		}
		catch(NumberFormatException e){
			e.printStackTrace();
			player.put("total-time", "0");
		}
		long total = 0;
		global.put("total-time", "0");
		for(Map.Entry<String, Map<String, String>> entry : stats.getStats().entrySet()){
			if(entry.getValue().containsKey("total-time")){
				total += Long.parseLong(entry.getValue().get("total-time"));
			}
		}
		global.put("total-time", String.valueOf(total));
	}
}
