package com.github.assisstion.towerScaler.TSToolkit;

import org.newdawn.slick.gui.ComponentListener;

public interface TSCloseable{
	void close();

	void addCloseListener(ComponentListener listener);
}
