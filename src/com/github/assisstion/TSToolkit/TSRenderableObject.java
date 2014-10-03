package com.github.assisstion.TSToolkit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public interface TSRenderableObject extends TSObject{
	void render(GUIContext container, Graphics g) throws SlickException;

	void render(GameContainer container, Graphics g, int x, int y)
			throws SlickException;
}
