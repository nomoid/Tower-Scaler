package com.github.assisstion.TSToolkit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public interface TSContainer extends TSRenderableObject{
	void renderContainer(GameContainer container, Graphics g, int x, int y)
			throws SlickException;

	int getContainerX();

	int getContainerY();
}
