package com.github.assisstion.towerScaler;

import java.io.Serializable;

public class Score implements Serializable{
	
	private static final long serialVersionUID = -8027777200692759629L;
	
	protected double score;
	protected String name;
	protected long hash;
	
	protected Score(){
		
	}
	
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
