package com.github.assisstion.towerScaler.TSToolkit;

import com.github.assisstion.TSToolkit.TSMouseFocusable;
import com.github.assisstion.towerScaler.LayeredDisplay;

public interface TSMenu extends LayeredDisplay, TSMouseFocusable{
	boolean isVisible();
	void setVisible(boolean visible);
}
