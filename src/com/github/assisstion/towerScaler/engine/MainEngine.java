package com.github.assisstion.towerScaler.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
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
import java.util.concurrent.CopyOnWriteArraySet;

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
import com.github.assisstion.towerScaler.StatsHolder;
import com.github.assisstion.towerScaler.Version;
import com.github.assisstion.towerScaler.TSToolkit.TSMenu;
import com.github.assisstion.towerScaler.TSToolkit.TSSingleContainerWindowMenu;
import com.github.assisstion.towerScaler.data.DataHelper;
import com.github.assisstion.towerScaler.data.SimpleEncryptionInputStream;
import com.github.assisstion.towerScaler.data.SimpleEncryptionOutputStream;
import com.github.assisstion.towerScaler.menu.GameOverMenu;
import com.github.assisstion.towerScaler.menu.HighScoreMenu;
import com.github.assisstion.towerScaler.menu.OptionsMenu;
import com.github.assisstion.towerScaler.menu.StatsMenu;
import com.github.assisstion.towerScaler.multiplayer.MultiplayerGameEngine;
import com.github.assisstion.towerScaler.multiplayer.MultiplayerMenu;
import com.github.assisstion.towerScaler.multiplayer.MultiplayerProcessor;
import com.markusfeng.SocketRelay.A.SocketClient;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketServer;
import com.markusfeng.SocketRelay.C.SocketHelper;

public class MainEngine extends BasicGame implements Engine{

	protected static final int DEFAULT_PORT = 50131;
	protected Map<String, String> properties;
	protected MenuEngine me;
	protected GameEngine ge;
	protected Set<Engine> engines;
	protected Set<Engine> backgroundEngines;
	protected Engine foregroundEngine;
	protected Set<TSMenu> menus = new HashSet<TSMenu>();
	protected HighScoreMenu hsm;
	protected GameOverMenu gom;
	protected TSSingleContainerWindowMenu tsscwm;
	protected Set<KeyListener> keyListeners = new HashSet<KeyListener>();
	protected Properties preferences = new Properties();
	protected OptionsMenu opm;
	protected StatsMenu stm;
	protected MultiplayerMenu mpm;
	protected MultiplayerGameEngine mge;
	private boolean lastGameMP;
	protected Set<Closeable> closeables;

	public MainEngine(){
		super("Tower Scaler" +
				(Main.class.getAnnotation(Version.class) == null ? ""
						: " - Version " + Main.class.getAnnotation(
								Version.class).value()));
		closeables = new HashSet<Closeable>();
		ge = new GameEngine(this);
		me = new MenuEngine(this);
		mge = new MultiplayerGameEngine(this);
		properties = new HashMap<String, String>();
		engines = new HashSet<Engine>();
		backgroundEngines = new CopyOnWriteArraySet<Engine>();
		setForegroundEngine(this);
		Collections.addAll(engines, ge, me, mge);
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
			if(e.toBeRendered()){
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
		hsm = new HighScoreMenu(gc, Main.getGameFrameWidth() / 4,
				Main.getGameFrameHeight() / 4,
				Main.getGameFrameWidth() * 3 / 4,
				Main.getGameFrameHeight() * 3 / 4);
		hsm.init(gc);
		gom = new GameOverMenu(gc, Main.getGameFrameWidth() / 4,
				Main.getGameFrameHeight() / 4,
				Main.getGameFrameWidth() * 3 / 4,
				Main.getGameFrameHeight() * 3 / 4, ge);
		gom.init(gc);
		opm = new OptionsMenu(gc, Main.getGameFrameWidth() / 4,
				Main.getGameFrameHeight() / 4,
				Main.getGameFrameWidth() * 3 / 4,
				Main.getGameFrameHeight() * 3 / 4, ge);
		opm.init(gc);
		stm = new StatsMenu(gc, Main.getGameFrameWidth() / 4,
				Main.getGameFrameHeight() / 4,
				Main.getGameFrameWidth() * 3 / 4,
				Main.getGameFrameHeight() * 3 / 4, ge);
		stm.init(gc);
		mpm = new MultiplayerMenu(gc, Main.getGameFrameWidth() / 4,
				Main.getGameFrameHeight() / 4,
				Main.getGameFrameWidth() * 3 / 4,
				Main.getGameFrameHeight() * 3 / 4, mge);
		mpm.init(gc);
		mpm.getServerButton().addListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent source){

				MultiplayerProcessor mp = new MultiplayerProcessor(MainEngine.this, true);
				try{
					String host = mpm.getField();
					int port = host == "" ? DEFAULT_PORT : Integer.parseInt(mpm.getField());
					SocketServer<SocketHandler<String>> server =
							SocketHelper.getStringServer(port, mp);
					closeables.add(server);
					server.open();
					mpm.serverWindow(server);
				}
				catch(NumberFormatException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mge.addProcessor(mp);
			}

		});
		mpm.getClientButton().addListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent source){

				MultiplayerProcessor mp = new MultiplayerProcessor(MainEngine.this, false);
				try{
					String[] field = mpm.getField().split(":");
					String host = field.length < 1 ? "localhost" : field[0];
					int port = field.length < 2 ? DEFAULT_PORT : Integer.parseInt(field[1]);
					SocketClient<SocketHandler<String>> client =
							SocketHelper.getStringClient(host, port, mp);
					closeables.add(client);
					client.open();
					mp.start(false);
					mge.addProcessor(mp);
				}
				catch(NumberFormatException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		gom.getButton().addListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent source){
				updateHighScore();
				updateStats();
				TSSingleContainerWindowMenu tsscwm = getWindowMenu();
				tsscwm.setComponent(getHighScoreMenu());
			}

		});
		tsscwm = new TSSingleContainerWindowMenu(gom, gom);
		tsscwm.addCloseListener(new ComponentListener(){

			@Override
			public void componentActivated(AbstractComponent source){
				if(lastGameMP){
					resetMP(false);
				}
				else{
					ge.paused = false;
					ge.reset();
					setForegroundEngine(ge);
				}
			}

		});
		tsscwm.init(gc);
		loadData();
		setForegroundEngine(me);
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
									new FileInputStream(hsFile), (byte) 255)));
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
			File stFile = new File("data" + File.separator + "stats.dat");
			new File(stFile.getParent()).mkdirs();
			stFile.createNewFile();
			List<? extends Object> stList = DataHelper
					.readObjects(new BufferedInputStream(
							new SimpleEncryptionInputStream(
									new FileInputStream(stFile), (byte) 255)));
			if(stList == null){
				throw new IOException();
			}
			Object stObj = stList.get(0);
			if(stObj instanceof StatsHolder){
				StatsHolder hst = (StatsHolder) stObj;
				stm.setStatsHolder(hst);
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
		for(Engine e : engines){
			if(e.isActive()){
				if(!e.isInitialized()){
					e.init(gc);
				}
				e.update(gc, delta);
			}
		}
	}

	@Override
	public void keyReleased(int key, char c){
		super.keyReleased(key, c);
		for(KeyListener listener : keyListeners){
			listener.keyReleased(key, c);
		}
	}

	protected void updateStats(){
		try{
			stm.updateFromGame();
			File stFile = new File("data" + File.separator +
					"stats.dat");
			new File(stFile.getParent()).mkdirs();
			stFile.createNewFile();
			DataHelper.writeObjects(stFile, new BufferedOutputStream(
					new SimpleEncryptionOutputStream(new FileOutputStream(
							stFile), (byte) 255)), Collections
							.singletonList(stm.getStatsHolder()));
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
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
								hsFile), (byte) 255)), Collections
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
				if(ge.isActive()){
					ge.paused = true;
					setForegroundEngine(me);
					break listen;
				}
				else if(mge.isActive()){
					mge.paused = true;
					setForegroundEngine(me);
					break listen;
				}
				else if(me.isActive()){
					if(Main.debug){
						System.out.println("Exiting system");
					}
					System.exit(0);
				}
			}
			if(key == Input.KEY_SPACE){
				if(me.isActive()){
					if(hasInputFocus()){
						ge.setArcadeMode(true);
						startGame();
					}
				}
				break listen;
			}
			if(key == Input.KEY_ENTER){
				if(me.isActive()){
					if(hasInputFocus()){
						ge.setArcadeMode(false);
						startGame();
					}
				}
				break listen;
			}
			if(getHighScoreMenu().hasInputFocus()){
				if(key == Input.KEY_RIGHT){
					getHighScoreMenu().setIndex(
							getHighScoreMenu().getIndex() + 10);
					break listen;
				}
				if(key == Input.KEY_LEFT){
					getHighScoreMenu().setIndex(
							getHighScoreMenu().getIndex() - 10);
					break listen;
				}
			}
			if(getStatsMenu().hasInputFocus()){
				getStatsMenu().pushChar(key, c);
				break listen;
			}
			if(getMultiplayerMenu().hasInputFocus()){
				getMultiplayerMenu().pushChar(key, c);
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
		lastGameMP = false;
		ge.reset();
		setForegroundEngine(ge);
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

	//Mutable
	public Set<Engine> getBackgroundEngines(){
		return backgroundEngines;
	}

	public Engine getForegroundEngine(){
		return foregroundEngine;
	}

	public void setForegroundEngine(Engine fe){
		foregroundEngine = fe;
		if(!(fe instanceof MultiplayerGameEngine)){
			try{
				mge.disconnect();
				close();
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	public OptionsMenu getOptionsMenu(){
		return opm;
	}

	public StatsMenu getStatsMenu(){
		return stm;
	}

	//Unmodifiable set
	public Set<Engine> getActiveEngines(){
		Set<Engine> set = new HashSet<Engine>(backgroundEngines);
		set.add(foregroundEngine);
		return Collections.unmodifiableSet(set);
	}

	//Always returns true
	@Override
	public boolean isActive(){
		return true;
	}

	//Always returns true
	@Override
	public boolean toBeRendered(){
		return true;
	}

	public MenuEngine getMenuEngine(){
		return me;
	}

	public GameEngine getGameEngine(){
		return ge;
	}

	@Override
	public boolean isInitialized(){
		return true;
	}

	public MultiplayerMenu getMultiplayerMenu(){
		return mpm;
	}

	public MultiplayerGameEngine getMultiplayerGameEngine(){
		return mge;
	}

	public void startMultiplayer(){
		lastGameMP = true;
		mge.reset();
		mpm.setVisible(false);
		setForegroundEngine(mge);
	}

	public void resetMP(boolean calledByOthers){
		mge.paused = false;
		mge.reset();
		if(!calledByOthers){
			mge.callForReset();
		}
		else{
			getWindowMenu().setVisible(false);
		}
		setForegroundEngine(mge);
	}

	public void close() throws IOException{
		for(Closeable closeable : closeables){
			closeable.close();
		}
	}

	@Override
	public boolean closeRequested(){
		try{
			mge.disconnect();
			close();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return true;
		}
	}
}
