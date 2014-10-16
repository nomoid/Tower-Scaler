package com.github.assisstion.towerScaler.multiplayer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import com.github.assisstion.TSToolkit.TSBoxLabel;
import com.github.assisstion.TSToolkit.TSFocusTextButton;
import com.github.assisstion.TSToolkit.TSTextLabel;
import com.github.assisstion.towerScaler.Helper;
import com.github.assisstion.towerScaler.TSToolkit.TSWindowMenu;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketServer;

public class MultiplayerMenu extends TSWindowMenu{

	protected MultiplayerGameEngine ge;
	protected TSFocusTextButton ctsftb;
	protected String hostName = "";
	private TSFocusTextButton stsftb;

	private TSTextLabel label;
	private TSTextLabel label2;
	private TSTextLabel label3;

	private TSBoxLabel box;
	private TSBoxLabel box2;
	private TSBoxLabel box3;

	private boolean serverConnection;

	public MultiplayerMenu(GUIContext container, int x1, int y1, int x2, int y2,
			MultiplayerGameEngine ge){
		super(container, x1, y1, x2, y2, new Color(200, 200, 255), Color.black);
		this.ge = ge;
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
		super.init(gc);
		label = new TSTextLabel(gc, getWidth() / 2,
				getHeight() / 8, " Multiplayer ");
		label2 = new TSTextLabel(gc, getWidth() / 2,
				getHeight() / 4, " Server ");
		label3 = new TSTextLabel(gc, getWidth() / 2,
				getHeight() * 5 / 8, " Client ");
		box = new TSBoxLabel(gc, label, Color.green, Color.black);
		box2 = new TSBoxLabel(gc, label2, Color.yellow, Color.black);
		box3 = new TSBoxLabel(gc, label3, Color.yellow, Color.black);
		ctsftb = new TSFocusTextButton(gc, this, getWidth() / 2,
				getHeight() * 7 / 8, " Connect ", Helper.getDefaultFont(),
				Color.cyan, Color.black, Color.black);
		stsftb = new TSFocusTextButton(gc, this, getWidth() / 2,
				getHeight() / 2, " Host ", Helper.getDefaultFont(),
				Color.cyan, Color.black, Color.black);
		reset();
	}

	@Override
	public void renderContainer(GameContainer gc, Graphics g, int x, int y)
			throws SlickException{
		super.renderContainer(gc, g, x, y);
		if(!serverConnection){
			TSTextLabel name = new TSTextLabel(gc, getWidth() / 8, getHeight() * 3 / 4,
					"Host:     " + hostName, Helper.getDefaultFont(),
					Color.black, -1, 0);
			name.render(gc, g, getContainerX() + x, getContainerY() + y);
		}
		else{
			TSTextLabel wait = new TSTextLabel(gc, getWidth() / 8, getHeight() / 2,
					"Waiting for client... ", Helper.getDefaultFont(),
					Color.black, -1, 0);
			wait.render(gc, g, getContainerX() + x, getContainerY() + y);
		}
		TSTextLabel port = new TSTextLabel(gc, getWidth() / 8, getHeight() * 3 / 8,
				"Port:     " + hostName, Helper.getDefaultFont(),
				Color.black, -1, 0);
		port.render(gc, g, getContainerX() + x, getContainerY() + y);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException{
		super.update(gc, delta);
	}

	public MultiplayerGameEngine getGameEngine(){
		return ge;
	}

	public void setGameEngine(MultiplayerGameEngine ge){
		this.ge = ge;
	}

	public TSFocusTextButton getServerButton(){
		return stsftb;
	}

	public TSFocusTextButton getClientButton(){
		return ctsftb;
	}

	public void pushChar(int key, char c){
		if(key == Input.KEY_BACK || key == Input.KEY_DELETE){
			if(hostName.length() > 0){
				hostName = hostName.substring(0, hostName.length() - 1);
			}
		}
		else{
			if(c > 32 && c < 127){
				hostName += c;
			}
		}
	}

	public String getField(){
		return hostName;
	}

	public void serverWindow(SocketServer<SocketHandler<String>> server){
		serverConnection = true;
		removeComponent(box3);
		removeComponent(ctsftb);
		removeComponent(stsftb);
	}

	public void reset(){
		clearComponents();
		serverConnection = false;
		addComponent(box);
		addComponent(box2);
		addComponent(box3);
		addComponent(ctsftb);
		addComponent(stsftb);
	}
}
