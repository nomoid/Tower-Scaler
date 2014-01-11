package com.github.assisstion.towerScaler.media;

public interface AudioPlayable extends Comparable<AudioPlayable>{
	void setPaused(boolean paused);
	boolean isPaused();
}
