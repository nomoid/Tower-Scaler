package com.github.assisstion.towerScaler.TSToolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSColoredBox;
import com.github.assisstion.TSToolkit.TSComponent;

public class TSSingleContainerWindowMenu extends TSWindowMenu{
	
	protected TSComponent component;
	protected TSSingleContainerWindowMenu instance;
	protected boolean previousInputFocus;
	
	public TSSingleContainerWindowMenu(TSColoredBox tscb, TSComponent component){
		this(component.getContext(), component.getX(), component.getY(), 
				tscb.getFillColor(), tscb.getBorderColor(), component);
	}

	public TSSingleContainerWindowMenu(GUIContext container,
			Color boxFillColor, Color boxBorderColor){
		this(container, 0, 0, boxFillColor, boxBorderColor);
	}
	
	public TSSingleContainerWindowMenu(GUIContext container, int x, int y, Color boxFillColor, Color boxBorderColor){
		this(container, x, y, boxFillColor, boxBorderColor, null);
	}
	
	public TSSingleContainerWindowMenu(GUIContext container, int x, int y, 
			Color boxFillColor, Color boxBorderColor, TSComponent component){
		super(container, x, y, 0, 0, boxFillColor, boxBorderColor);
		setComponent(component);
	}
	
	@Override
	public int getWidth(){
		if(component == null){
			return 0;
		}
		return component.getWidth();
	}
	
	@Override
	public int getHeight(){
		if(component == null){
			return 0;
		}
		return component.getHeight();
	}
	
	@Override
	public int getContainerX(){
		return 0;
	}
	
	@Override
	public int getContainerY(){
		return 0;
	}
	
	@Override
	public int getRealX(){
		return 0;
	}
	
	@Override
	public int getRealY(){
		return 0;
	}
	
	public void setComponent(TSComponent component){
		removeComponent();
		this.component = component;
		if(component != null){
			if(component instanceof TSCloseable){
				((TSCloseable) component).addCloseListener(new CloseListener(component));
			}
		}
		previousInputFocus = component.hasInputFocus();
		component.setInputFocus(hasInputFocus());
		component.setParent(this);
	}

	public void removeComponent(){
		if(component != null){
			component.setParent(null);
			component.setInputFocus(previousInputFocus);
		}
		previousInputFocus = false;
		this.component = null;
	}
	
	public TSComponent getComponent(){
		return component;
	}

	@Override
	public void renderContainer(GameContainer container, Graphics g, int x, int y) throws SlickException{
		super.renderContainer(container, g, x, y);
		if(component != null){
			component.render(container, g, getContainerX() + x, getContainerY() + y);
		}
	}
	
	@Override
	public void setVisible(boolean visible){
		super.setVisible(visible);
		if(component != null && component instanceof TSMenu){
			((TSMenu) component).setVisible(visible);
		}
	}
	
	@Override
	public void setInputFocus(boolean focus){
		super.setInputFocus(focus);
		if(component != null){
			component.setInputFocus(focus);
		}
	}

	protected class CloseListener implements ComponentListener{

		protected TSComponent listenerComponent;
		
		public CloseListener(TSComponent component){
			this.listenerComponent = component;
		}
		
		@Override
		public void componentActivated(AbstractComponent source){
			if(isVisible()){
				if(component != null && component.equals(listenerComponent)){
					close();
				}
			}
		}

	}

}
