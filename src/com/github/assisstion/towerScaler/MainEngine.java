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
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

import com.github.assisstion.towerScaler.TSToolkit.TSMenu;

public class MainEngine extends BasicGame implements Engine{
	
	protected String state = "main";
	protected Map<String, String> properties;
	protected MenuEngine me;
	protected GameEngine ge;
	protected Set<Engine> engines;
	protected Set<TSMenu> menus = new HashSet<TSMenu>();
	protected HighScoreMenu wm;
	protected Set<KeyListener> keyListeners = new HashSet<KeyListener>();

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
		Set<Display> renderingDisplays = new HashSet<Display>();
		for(Engine e : engines){
			if(e.renderingStates() == null || e.renderingStates().contains(getState())){
				renderingDisplays.add(e);
			}
		}
		for(TSMenu m : menus){
			if(m.isVisible()){
				renderingDisplays.add(m);
			}
		}
		Set<Integer> layers = new TreeSet<Integer>();
		for(Display d : renderingDisplays){
			if(d instanceof LayeredDisplay){
				LayeredDisplay ld = (LayeredDisplay) d;
				layers.addAll(ld.renderableLayers());
			}
		}
		boolean passed = false;
		for(int i : layers){
			for(Display d : renderingDisplays){
				if(d instanceof LayeredDisplay){
					LayeredDisplay ld = (LayeredDisplay) d;
					if(ld.renderableLayers().contains(i)){
						try{
							ld.render(gc, g, i);
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
				else{
					if(!passed && i >= 0){
						passed = true;
						try{
							d.render(gc, g);
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
		wm = new HighScoreMenu(gc, (Main.getGameFrameWidth() / 4), (Main.getGameFrameHeight() / 4),
				(Main.getGameFrameWidth() * 3 / 4), (Main.getGameFrameHeight() * 3 / 4));
		state = "menu";
		me.init(gc);
		//ge.init(gc);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException{
		if(state.equals("menu")){
			me.update(gc, delta);
		}
		if(state.equals("game") || state.equals("game_over")){
			if(state.equals("game") && !ge.isInitialized()){
				ge.init(gc);
			}
			ge.update(gc, delta);
		}
	}
	
	@Override
	public void keyReleased(int key, char c){
		super.keyReleased(key, c);
		listen: {
			if(key == Input.KEY_ESCAPE){
				if(state.equals("game") || state.equals("game_over")){
					ge.paused = false;
					state = "menu";
					break listen;
				}
				else if(state.equals("menu")){
					for(TSMenu menu : getMenus()){
						if(menu.isVisible()){
							menu.setVisible(false);
							break listen;
						}
					}
					if(Main.debug){
						System.out.println("Exiting system");
					}
					System.exit(0);
				}
			}
			if(ge.paused == true || state.equals("game_over")){
				if(key == Input.KEY_SPACE){
					ge.paused = false;
					if(ge.isLegit()){
						wm.getHighScores().addScore(ge.getScore());
					}
					state = "menu";
					break listen;
				}
				if(key == Input.KEY_ENTER){
					ge.paused = false;
					if(ge.isLegit()){
						wm.getHighScores().addScore(ge.getScore());
					}
					ge.reset();
					state = "game";
					break listen;
				}
			}
		}
		for(KeyListener listener : keyListeners){
			listener.keyReleased(key, c);
		}
	}
	
	@Override
	public void keyPressed(int key, char c){
		super.keyPressed(key, c);
		for(KeyListener listener : keyListeners){
			listener.keyPressed(key, c);
		}
	}
	
	@Override
	public void inputEnded(){
		super.inputEnded();
		for(KeyListener listener : keyListeners){
			listener.inputEnded();
		}
	}
	
	@Override
	public void inputStarted(){
		super.inputStarted();
		for(KeyListener listener : keyListeners){
			listener.inputStarted();
		}
	}
	
	@Override
	public void setInput(Input input){
		super.setInput(input);
		for(KeyListener listener : keyListeners){
			listener.setInput(input);
		}
	}
	
	public void addKeyListener(KeyListener listener){
		keyListeners.add(listener);
	}
	
	public void removeKeyListener(KeyListener listener){
		keyListeners.remove(listener);
	}
	
	public Set<KeyListener> getKeyListeners(){
		return keyListeners;
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

	@Override
	public void addMenu(TSMenu menu){
		menus.add(menu);
	}

	@Override
	public void removeMenu(TSMenu menu){
		menus.remove(menu);
	}

	@Override
	public Set<TSMenu> getMenus(){
		return menus;
	}

	@Override
	public void setMenus(Set<TSMenu> menus){
		this.menus = menus;
	}

	public HighScoreMenu getHighScoreMenu(){
		return wm;
	}

	@Override
	public boolean hasFocus(){
		return false;
	}
}
