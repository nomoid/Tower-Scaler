package com.github.assisstion.towerScaler.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

import com.github.assisstion.towerScaler.HighScoreTable;
import com.github.assisstion.towerScaler.Main;
import com.github.assisstion.towerScaler.Version;
import com.github.assisstion.towerScaler.TSToolkit.TSMenu;
import com.github.assisstion.towerScaler.TSToolkit.TSSingleContainerWindowMenu;
import com.github.assisstion.towerScaler.data.DataHelper;
import com.github.assisstion.towerScaler.data.SimpleEncryptionInputStream;
import com.github.assisstion.towerScaler.data.SimpleEncryptionOutputStream;
import com.github.assisstion.towerScaler.menu.GameOverMenu;
import com.github.assisstion.towerScaler.menu.HighScoreMenu;

public class MainEngine extends BasicGame implements Engine{

	protected String state = "main";
	protected Map<String, String> properties;
	protected MenuEngine me;
	protected GameEngine ge;
	protected Set<Engine> engines;
	protected Set<TSMenu> menus = new HashSet<TSMenu>();
	protected HighScoreMenu hsm;
	protected GameOverMenu gom;
	protected TSSingleContainerWindowMenu tsscwm;
	protected Set<KeyListener> keyListeners = new HashSet<KeyListener>();
	protected Properties preferences = new Properties();

	public MainEngine(){
		super("Tower Scaler" +
				(Main.class.getAnnotation(Version.class) == null ? ""
						: (" - Version " + Main.class.getAnnotation(
								Version.class).value())));
		ge = new GameEngine(this);
		me = new MenuEngine(this);
		properties = new HashMap<String, String>();
		engines = new HashSet<Engine>();
		Collections.addAll(engines, ge, me);
	}

	@Override
	public void render(GameContainer gc, Graphics g, int layer)
			throws SlickException{
		// Ignore layer
		render(gc, g);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		renderMain(gc, g);
		Set<Display> renderingDisplays = new HashSet<Display>();
		for(Engine e : engines){
			if(e.renderingStates() == null ||
					e.renderingStates().contains(getState())){
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
							if(Main.debug){
								e1.printStackTrace();
							}
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
							if(Main.debug){
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
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
		hsm = new HighScoreMenu(gc, (Main.getGameFrameWidth() / 4),
				(Main.getGameFrameHeight() / 4),
				(Main.getGameFrameWidth() * 3 / 4),
				(Main.getGameFrameHeight() * 3 / 4));
		hsm.init(gc);
		gom = new GameOverMenu(gc, (Main.getGameFrameWidth() / 4),
				(Main.getGameFrameHeight() / 4),
				(Main.getGameFrameWidth() * 3 / 4),
				(Main.getGameFrameHeight() * 3 / 4), ge);
		gom.init(gc);
		gom.getButton().addListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent source){
				updateHighScore();
				TSSingleContainerWindowMenu tsscwm = getWindowMenu();
				tsscwm.setComponent(getHighScoreMenu());
			}

		});
		tsscwm = new TSSingleContainerWindowMenu(gom, gom);
		tsscwm.addCloseListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent source){
				ge.paused = false;
				ge.reset();
				setState("game");
			}

		});
		tsscwm.init(gc);
		loadData();
		state = "menu";
		me.init(gc);
	}

	protected void loadData(){
		try{
			File hsFile = new File("data" + File.separator + "highscores.dat");
			new File(hsFile.getParent()).mkdirs();
			hsFile.createNewFile();
			List<? extends Object> list = DataHelper
					.readObjects(new BufferedInputStream(
							new SimpleEncryptionInputStream(
									new FileInputStream(hsFile), (byte) (255))));
			if(list == null){
				throw new IOException();
			}
			Object obj = list.get(0);
			if(obj instanceof HighScoreTable){
				HighScoreTable hst = (HighScoreTable) obj;
				hst.validateScores();
				hsm.setHighScores(hst);
			}
			else{
				throw new IOException();
			}
		}
		catch(FileNotFoundException e){
			if(Main.debug){
				e.printStackTrace();
			}
		}
		catch(IOException e){
			if(Main.debug){
				e.printStackTrace();
			}
		}
		try{
			File prefFile = new File("data" + File.separator +
					"preferences.properties");
			new File(prefFile.getParent()).mkdirs();
			if(prefFile.exists()){
				preferences.load(new FileInputStream(prefFile));
				ge.lastUsedName = preferences.getProperty("last-used-name", "");
			}
			else{
				prefFile.createNewFile();
			}
		}
		catch(IOException e){
			if(Main.debug){
				e.printStackTrace();
			}
		}
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
		for(KeyListener listener : keyListeners){
			listener.keyReleased(key, c);
		}
	}

	protected void updateHighScore(){
		if(ge.isLegit()){
			hsm.getHighScores().addScore(ge.getScore());
			try{
				File hsFile = new File("data" + File.separator +
						"highscores.dat");
				new File(hsFile.getParent()).mkdirs();
				hsFile.createNewFile();
				DataHelper.writeObjects(hsFile, new BufferedOutputStream(
						new SimpleEncryptionOutputStream(new FileOutputStream(
								hsFile), (byte) (255))), Collections
						.singletonList(hsm.getHighScores()));
			}
			catch(FileNotFoundException e){
				if(Main.debug){
					e.printStackTrace();
				}
			}
			catch(IOException e){
				if(Main.debug){
					e.printStackTrace();
				}
			}
			ge.lastUsedName = ge.getName();
			preferences.put("last-used-name", ge.lastUsedName);
			try{
				File prefFile = new File("data" + File.separator +
						"preferences.properties");
				new File(prefFile.getParent()).mkdirs();
				prefFile.createNewFile();
				preferences
						.store(new FileOutputStream(prefFile), "Preferences");
			}
			catch(IOException e){
				if(Main.debug){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyPressed(int key, char c){
		super.keyPressed(key, c);
		listen: {
			if(key == Input.KEY_ESCAPE){
				for(TSMenu menu : getMenus()){
					if(menu.isVisible()){
						menu.close();
						break listen;
					}
				}
				if(state.equals("game") || state.equals("game_over")){
					ge.paused = true;
					state = "menu";
					break listen;
				}
				else if(state.equals("menu")){
					if(Main.debug){
						System.out.println("Exiting system");
					}
					System.exit(0);
				}
			}
			if(key == Input.KEY_SPACE){
				if(state.equals("menu")){
					if(hasInputFocus()){
						ge.setArcadeMode(true);
						startGame();
					}
				}
				break listen;
			}
			if(key == Input.KEY_ENTER){
				if(state.equals("menu")){
					if(hasInputFocus()){
						ge.setArcadeMode(false);
						startGame();
					}
				}
				break listen;
			}
		}
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

	// Always returns null
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

	// Always returns null
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
		return hsm;
	}

	@Override
	public boolean hasInputFocus(){
		for(TSMenu menu : getMenus()){
			if(menu.isVisible() && menu.isStealingFocus()){
				return false;
			}
		}
		return true;
	}

	@Override
	public void setInputFocus(boolean focus){
		// Do nothing
	}

	public GameOverMenu getGameOverMenu(){
		return gom;
	}

	public TSSingleContainerWindowMenu getWindowMenu(){
		return tsscwm;
	}
}
