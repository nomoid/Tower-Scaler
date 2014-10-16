package com.github.assisstion.towerScaler.multiplayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.assisstion.towerScaler.engine.MainEngine;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;
import com.markusfeng.SocketRelay.C.SocketProcessorAbstract;

public class MultiplayerProcessor extends SocketProcessorAbstract<String> implements SocketProcessorGenerator<MultiplayerProcessor>{

	protected MainEngine engine;
	protected boolean serverd;

	public MultiplayerProcessor(MainEngine ge, boolean serverD){
		engine = ge;
		serverd = serverD;
	}

	/*public MultiplayerProcessor(MainEngine mainEngine, boolean b){
		this(mainEngine);
		start(false);
		serverd = false;
	}*/

	@Override
	public void attachHandler(SocketHandler<String> handler){
		super.attachHandler(handler);
		if(serverd){
			MultiplayerGameEngine en = engine.getMultiplayerGameEngine();
			Map<String, String> map = new HashMap<String, String>();
			en.nextSeed = en.metaRandom.nextInt();
			map.put("seed", String.valueOf(en.nextSeed));
			Command gameOver = Commands.make("deliverSeed", map);
			try{
				outputToHandler(handler, gameOver, false);
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void input(String in){
		Command command = Commands.parseCommand(in);
		engine.getMultiplayerGameEngine().process(this, command);
	}

	public void output(Command out, boolean blocking) throws IOException{
		output(out.toString(), blocking);
	}

	public void outputToHandler(SocketHandler<String> handler, Command out, boolean blocking) throws IOException{
		outputToHandler(handler, out.toString(), blocking);
	}

	@Override
	public void close() throws IOException{
		// TODO Auto-generated method stub
	}

	@Override
	public MultiplayerProcessor get(){
		start(true);
		return this;
	}

	public void start(boolean b){
		engine.startMultiplayer();
		engine.getMultiplayerGameEngine().ready(b);
	}

}
