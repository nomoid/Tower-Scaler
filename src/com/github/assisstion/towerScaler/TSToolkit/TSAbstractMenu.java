package com.github.assisstion.towerScaler.TSToolkit;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSMultiContainer;

public abstract class TSAbstractMenu extends TSMultiContainer implements TSMenu{
	
	protected boolean visible;
	protected Set<ComponentListener> closeListeners = new HashSet<ComponentListener>();
	
	public TSAbstractMenu(GUIContext container){
		this(container, 0, 0);
	}
	
	public TSAbstractMenu(GUIContext container, int x, int y){
		this(container, x, y, x, y);
	}
	
	public TSAbstractMenu(GUIContext container, int x1, int y1, int x2, int y2){
		super(container, x1, y1, x2, y2);
		focus = false;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		render(gc, g, 0);
	}
	
	@Override
	public void close(){
		setVisible(false);
		for(ComponentListener listener : closeListeners){
			listener.componentActivated(this);
		}
	}
	
	@Override
	public void addCloseListener(ComponentListener listener){
		closeListeners.add(listener);
	}

	protected void removeCloseListener(ComponentListener listener){
		closeListeners.remove(listener);
	}
	
	protected Set<ComponentListener> getCloseListeners(){
		return closeListeners;
	}
	
	@Override
	public boolean isVisible(){
		return visible;
	}

	@Override
	public void setVisible(boolean visible){
		this.visible = visible;
		setInputFocus(visible);
	}
	
	@Override
	public boolean isStealingFocus(){
		return hasInputFocus();
	}

}
