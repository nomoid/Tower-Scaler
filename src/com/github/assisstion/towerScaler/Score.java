package com.github.assisstion.towerScaler;

public class Score{
	
	protected double score;
	protected String name;
	protected long hash;
	
	public Score(double score, String name, long hash){
		this.score = score;
		this.name = name;
		this.hash = hash;
	}
	
	public long getHash(){
		return hash;
	}
	
	public String getName(){
		return name;
	}
	
	public double getScore(){
		return score;
	}
}
