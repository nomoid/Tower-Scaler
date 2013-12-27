package com.github.assisstion.towerScaler;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MainEngine extends BasicGame implements Engine{
	
	protected String state = "main";
	protected Map<String, String> properties;
	protected MenuEngine me;
	protected GameEngine ge;
	protected Set<Engine> engines;

	public MainEngine(){
		super("Tower Scaler" + 
				(Main.class.getAnnotation(Version.class) == null ? "" : 
				(" - Version " + Main.class.getAnnotation(Version.class).value())));
		ge = new GameEngine(this);
		me = new MenuEngine(this);
		properties = new HashMap<String, String>();
		engines = new HashSet<Engine>();
		Collections.addAll(engines, ge, me);
	}

	@Override
	public void render(GameContainer gc, Graphics g, int layer) throws SlickException{
		//Ignore layer
		render(gc, g);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		renderMain(gc, g);
		Set<Engine> renderingEngines = new HashSet<Engine>();
		for(Engine e : engines){
			if(e.renderingStates() == null || e.renderingStates().contains(getState())){
				renderingEngines.add(e);
			}
		}
		Set<Integer> layers = new TreeSet<Integer>();
		for(Engine e : renderingEngines){
			layers.addAll(e.renderableLayers());
		}
		for(int i : layers){
			for(Engine e : renderingEngines){
				if(e.renderableLayers().contains(i)){
					try{
						e.render(gc, g, i);
					}
					catch(SlickException s){
						throw s;
					}
					catch(Exception e1){
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		/*
		if(state.equals("menu")){
			me.render(gc, g);
		}
		else if(state.equals("game") || state.equals("game_over")){
			ge.render(gc, g);
		}
		*/
	}
	
	public void renderMain(GameContainer gc, Graphics g){
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
	public Engine getParent(){
		return null;
	}

	@Override
	public void setEngineProperties(Map<String, String> properties){
		this.properties = properties;
	}

	@Override
	public void setEngineProperty(String key, String value){
		properties.put(key, value);
	}

	@Override
	public Map<String, String> getEngineProperties(){
		return properties;
	}

	@Override
	public String getEngineProperty(String key){
		return properties.get(key);
	}

	@Override
	public Set<Integer> renderableLayers(){
		return Collections.singleton(0);
	}

	//Always returns null
	@Override
	public Set<String> renderingStates(){
		return null;
	}
}
