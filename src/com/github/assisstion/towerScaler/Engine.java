package com.github.assisstion.towerScaler;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Engine extends BasicGame implements AbstractEngine{
	
	public String state = "main";
	protected MenuEngine me;
	protected GameEngine ge;

	public Engine(){
		super("Tower Scaler" + 
				(Main.class.getAnnotation(Version.class) == null ? "" : 
				(" - Version " + Main.class.getAnnotation(Version.class).value())));
		ge = new GameEngine(this);
		me = new MenuEngine(this);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		if(Main.debug){
			if(!gc.isShowingFPS()){
				gc.setShowFPS(true);
			}
		}
		else{
			if(gc.isShowingFPS()){
				gc.setShowFPS(false);
			}
		}
		if(state.equals("menu")){
			me.render(gc, g);
		}
		else if(state.equals("game") || state.equals("game_over")){
			ge.render(gc, g);
			if(state.equals("game_over")){
				//g.setBackground(new Color(150, 150, 255));
				g.drawString("Game Over!", 10, 50);
				//g.drawString("Score: " + Helper.round(-gameY, 2), 10, 30);
			}
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
		state = "menu";
		me.init(gc);
		//ge.init(gc);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException{
		if(state.equals("menu")){
			me.update(gc, delta);
		}
		if(state.equals("game")){
			if(!ge.isInitialized()){
				ge.init(gc);
			}
			ge.update(gc, delta);
		}
	}
	
	@Override
	public void keyReleased(int key, char c){
		super.keyPressed(key, c);
		if(key == Input.KEY_ESCAPE){
			if(state.equals("game") || state.equals("game_over")){
				ge.paused = false;
				state = "menu";
			}
			else if(state.equals("menu")){
				if(Main.debug){
					System.out.println("Exiting system");
				}
				System.exit(0);
			}
		}
		if(ge.paused == true || state.equals("game_over")){
			if(key == Input.KEY_SPACE){
				ge.paused = false;
				state = "menu";
			}
			if(key == Input.KEY_ENTER){
				ge.paused = false;
				ge.reset();
				state = "game";
			}
		}
	}
	

	public void startGame(){
		ge.reset();
		state = "game";
	}

	@Override
	public void setState(String state){
		this.state = state;
	}

	@Override
	public String getState(){
		return state;
	}

	//Always returns null
	@Override
	public AbstractEngine getParent(){
		return null;
	}
}
