package com.github.assisstion.towerScaler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StatsHolder implements Serializable{

	private static final long serialVersionUID = -5246697853937347646L;

	protected Map<String, Map<String, String>> stats;

	public StatsHolder(){
		stats = new HashMap<String, Map<String, String>>();
	}

	public StatsHolder(Map<String, Map<String, String>> stats){
		this.stats = stats;
	}

	public Map<String, Map<String, String>> getStats(){
		return stats;
	}

	public Map<String, String> getPlayerStats(String name){
		if(stats.containsKey(name)){
			return stats.get(name);
		}
		else{
			Map<String, String> player = new HashMap<String, String>();
			stats.put(name, player);
			return player;
		}
	}
}
