package com.github.assisstion.towerScaler.multiplayer;

import java.util.Map;

public interface Command{
	String getName();
	Map<String, String> getArguments();
}
