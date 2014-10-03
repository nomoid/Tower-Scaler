package com.github.assisstion.towerScaler;

import java.io.Serializable;

public class Score implements Serializable{

	private static final long serialVersionUID = -8027777200692759629L;
	public static final Character[] legalChars = {'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '-', '_'};

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
