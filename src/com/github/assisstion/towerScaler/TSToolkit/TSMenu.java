package com.github.assisstion.towerScaler.TSToolkit;

import com.github.assisstion.TSToolkit.TSContainer;
import com.github.assisstion.towerScaler.engine.LayeredDisplay;

public interface TSMenu extends LayeredDisplay, TSContainer, TSCloseable{
	boolean isVisible();
	void setVisible(boolean visible);
	boolean isStealingFocus();
}
