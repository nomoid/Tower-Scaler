package com.github.assisstion.towerScaler.multiplayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.newdawn.slick.GameContainer;

import com.github.assisstion.towerScaler.engine.GameEngine;
import com.github.assisstion.towerScaler.engine.MainEngine;
import com.github.assisstion.towerScaler.entity.Opponent;
import com.github.assisstion.towerScaler.entity.Player;

public class MultiplayerGameEngine extends GameEngine{

	protected MultiplayerProcessor processor;
	protected boolean ready = false;
	protected GameContainer gc;
	protected Opponent opponent;
	protected Random metaRandom = new Random();
	protected int nextSeed = metaRandom.nextInt();
	protected Object seedWaiter = new Object();
	protected boolean hasSeed = false;

	@Override
	public void initRandom(){
		random = new Random(nextSeed);
	}

	@Override
	public void endInit(GameContainer gc){
		opponent = new Opponent(player.getX1(), player.getX2(), playerTexture());
		entities.add(opponent);
		collisionObjects.add(opponent);
		allowCheats = false;
		allowPause = false;
	}

	@Override
	protected void gameOverReset(){
		//Do nothing
	}

	@Override
	protected void gameOver(GameContainer gc, boolean calledByOthers){
		if(!calledByOthers){
			Command gameOver = Commands.make("gameOver");
			try{
				processor.output(gameOver, false);
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.gameOver(gc, calledByOthers);
	}

	@Override
	protected void endUpdate(){
		super.endUpdate();
		Map<String, String> map = new HashMap<String, String>();
		map.put("x", String.valueOf(player.getX1()));
		map.put("y", String.valueOf(player.getY1()));
		Command updateXY = Commands.make("updateXY", map);
		try{
			processor.output(updateXY, false);
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void initPlayer(String playerLocation){
		int x = hasSeed ? 200 : 0;
		int y = 500;
		player = new Player(playerLocation, x, y);
		entities.add(player);
		collidables.add(player);
	}

	@Override
	protected void initStartingBlocks(String blockLocation){
		addBlock(blockLocation, 50, 0);
		addBlock(blockLocation, 0, 100);
		addBlock(blockLocation, 50, 200);
		addBlock(blockLocation, 0, 300);
		addBlock(blockLocation, 50, 400);
		addBlock(blockLocation, 0, 500);
		addBlock(blockLocation, 50, 600);
		addBlock(blockLocation, 150, 0);
		addBlock(blockLocation, 200, 100);
		addBlock(blockLocation, 150, 200);
		addBlock(blockLocation, 200, 300);
		addBlock(blockLocation, 150, 400);
		addBlock(blockLocation, 200, 500);
		addBlock(blockLocation, 150, 600);
	}

	public MultiplayerGameEngine(MainEngine parent){
		super(parent);
		setArcadeMode(true);
	}

	public void addProcessor(MultiplayerProcessor processor){
		this.processor = processor;
	}

	@Override
	public void update(GameContainer gc, int delta){
		if(ready){
			super.update(gc, delta);
		}
	}

	@Override
	public void init(GameContainer gc){
		this.gc = gc;
		if(ready){
			super.init(gc);
		}
	}

	public void ready(boolean b){
		new Thread(new Readier(b)).start();;
	}

	protected class Readier implements Runnable{

		protected boolean b;

		protected Readier(boolean b){
			this.b = b;
		}

		@Override
		public void run(){
			if(!b){
				synchronized(seedWaiter){
					while(!hasSeed){
						try{
							seedWaiter.wait();
						}
						catch(InterruptedException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			ready = true;
		}
	}

	public void process(MultiplayerProcessor processor, Command command){
		// TODO Auto-generated method stub
		String name = command.getName();
		Map<String, String> map = command.getArguments();
		if(name.equalsIgnoreCase("")){
			throw new IllegalArgumentException();
		}
		/*
		else if(name.equalsIgnoreCase("print")){
			String value = map.get("value");
			System.out.println("Printed: " + value);
		}
		else if(name.equalsIgnoreCase("echo-send")){
			System.out.println("echo-send");
			processor.input(Commands.make("echo-recieve").toString());
		}
		else if(name.equalsIgnoreCase("echo-recieve")){
			System.out.println("echo-recieve");
		}
		 */
		else if(name.equalsIgnoreCase("updateXY")){
			double x = Double.parseDouble(map.get("x"));
			double y = Double.parseDouble(map.get("y"));
			if(opponent != null){
				opponent.setLocation(x, y);
			}
		}
		else if(name.equalsIgnoreCase("gameOver")){
			gameOver(gc, true);
		}
		else if(name.equalsIgnoreCase("resetMP")){
			nextSeed = Integer.parseInt(map.get("seed"));
			getParent().resetMP(true);
		}
		else if(name.equalsIgnoreCase("deliverSeed")){
			System.out.println("delivered");
			nextSeed = Integer.parseInt(map.get("seed"));
			hasSeed = true;
			if(player != null){
				player.setLocation(200, 500);
			}
			synchronized(seedWaiter){
				seedWaiter.notifyAll();
			}
		}
		else if(name.equalsIgnoreCase("disconnect")){
			paused = true;
			getParent().setForegroundEngine(getParent().getMenuEngine());
		}
		else{
			System.out.println("Unknown command: " + name);
		}
	}

	@Override
	public boolean isMultiplayer(){
		return true;
	}

	public void callForReset(){
		Map<String, String> map = new HashMap<String, String>();
		nextSeed = metaRandom.nextInt();
		map.put("seed", String.valueOf(nextSeed));
		Command gameOver = Commands.make("resetMP", map);
		try{
			processor.output(gameOver, false);
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnect(){
		if(processor != null){
			try{
				Command gameOver = Commands.make("disconnect");
				try{
					processor.output(gameOver, false);
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
