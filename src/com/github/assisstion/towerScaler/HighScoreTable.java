package com.github.assisstion.towerScaler;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HighScoreTable implements Serializable{

	private static final long serialVersionUID = -4837857492582903032L;

	protected int maxScores = 10;
	protected LinkedList<Score> scores = new LinkedList<Score>();

	public void addScore(Score score){
		if(score.getName() == null || score.getName().equals("")){
			return;
		}
		boolean add = false;
		int index = 0;
		for(Score oldScore : scores){
			if(score.getScore() > oldScore.getScore()){
				add = true;
				break;
			}
			index++;
			/*if(index >= maxScores){
				break;
			}*/
		}
		//if(index < maxScores){
		add = true;
		//}
		if(add){
			scores.add(index, score);
		}
		/*while(scores.size() > maxScores){
			scores.removeLast();
		}*/
	}

	public List<Score> getScores(){
		return scores;
	}

	public List<Score> getScores(int start, int end){
		if(start > scores.size() - 1){
			start = scores.size() - 1;
		}
		if(end > scores.size()){
			end = scores.size();
		}
		return scores.subList(start, end);
	}

	public void validateScores(){
		LinkedList<Score> toBeRemoved = new LinkedList<Score>();
		for(Score score : scores){
			if(score.name.length() > 20){
				toBeRemoved.add(score);
				continue;
			}
			for(char c : score.name.toCharArray()){
				Set<Character> allChars = new HashSet<Character>();
				Collections.addAll(allChars, Score.legalChars);
				if(!allChars.contains(c)){
					toBeRemoved.add(score);
					break;
				}
			}
		}
		for(Score score : toBeRemoved){
			scores.remove(score);
		}
		toBeRemoved.clear();
	}
}
