package com.github.assisstion.towerScaler.engine;

import java.util.Map;
import java.util.Set;

import com.github.assisstion.TSToolkit.TSMouseFocusable;
import com.github.assisstion.towerScaler.TSToolkit.TSMenu;

public interface Engine extends LayeredDisplay, TSMouseFocusable{

	void setEngineProperties(Map<String, String> properties);

	void setEngineProperty(String key, String value);

	Map<String, String> getEngineProperties();

	String getEngineProperty(String key);

	Engine getParent();

	void addMenu(TSMenu menu);

	void removeMenu(TSMenu menu);

	Set<TSMenu> getMenus();

	void setMenus(Set<TSMenu> menus);

	boolean isActive();

	boolean toBeRendered();

	boolean isInitialized();
}
