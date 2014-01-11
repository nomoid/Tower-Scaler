package com.github.assisstion.towerScaler.media;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.github.assisstion.towerScaler.Main;

public final class AudioHelper{
	
	//Guarantee only one sound is playing at a time
	public static ReentrantLock audioLock = new ReentrantLock();
	public static Condition looperCondition = audioLock.newCondition();
	private static AudioLooper looper;
	private static ThreadGroup soundStreamerThreads = new ThreadGroup("SoundStreamers");
	
	private AudioHelper(){
		//Not to be instantiated
	}
	
	public static void loopSound(String location) {
		if (looper == null || !looper.on) {
			looper = new AudioLooper(location);
			new Thread(looper, "SoundLooper-" + looper.hashCode()).start();
		}
	}

	public static class AudioLooper implements Runnable, Looper {

		private String location;
		private boolean on = true;
		private boolean ready = true;
		private boolean paused = false;
		private Object pauseLock = new Object();
		private Object onLock = new Object();
		private Object readyLock = new Object();

		public AudioLooper(String location) {
			this.location = location;
			ResourceManager.addAudioPlayer(this);
		}

		@Override
		public void run() {
			try {
				while (on) {
					if (ready) {
						audioLock.lock();
						try{
							while (paused) {
								looperCondition.await();
							}
							streamSound(location, this);
							if(Main.debug){
								System.out.println("Audio Loop");
							}
							ready = false;
						}
						finally{
							audioLock.unlock();
						}
					}
					else{
						synchronized(readyLock){
							readyLock.wait();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ResourceManager.removeAudioPlayer(this);
			}
		}

		public void stop() {
			synchronized (onLock) {
				on = false;
			}
		}

		@Override
		public boolean isOn() {
			synchronized (onLock) {
				return on;
			}
		}

		@Override
		public void ready() {
			synchronized(readyLock){
				readyLock.notify();
			}
			ready = true;
		}

		@Override
		public boolean isPaused() {
			synchronized (pauseLock) {
				return paused;
			}
		}

		@Override
		public void setPaused(boolean paused) {
			boolean tempPaused;
			synchronized (pauseLock) {
				tempPaused = this.paused;
				this.paused = paused;
			}
			if (tempPaused && !paused) {
				audioLock.lock();
				try{
					looperCondition.notify();
				}
				finally{
					audioLock.unlock();
				}
			}
			this.paused = paused;
		}

		@Override
		public int compareTo(AudioPlayable ap) {
			return new Integer(hashCode()).compareTo(ap.hashCode());
		}
	}
	
	//Plays a sound controlled by the Looper
	public static void streamSound(String location, Looper looper){
		SoundStreamer ss = new SoundStreamer(location, looper);
		new Thread(soundStreamerThreads, ss, "SoundStreamer-"+ ss.hashCode()).start();
	}
	
	//Plays a sound once, without Looper control
	public static void streamSound(String location){
		SoundStreamer ss = new SoundStreamer(location);
		new Thread(soundStreamerThreads, ss, "SoundStreamer-"+ ss.hashCode()).start();
	}
	
	private static class SoundStreamer implements Runnable{
		private String location;
		private Looper looper;
		
		public SoundStreamer(String location, Looper looper){
			this.location = location;
			this.looper = looper;
		}
		
		public SoundStreamer(String location){
			this.location = location;
			this.looper = new Looper(){
				private boolean paused;
				
				{
					ResourceManager.addAudioPlayer(this);
				}
				
				@Override
				public void ready(){
					ResourceManager.removeAudioPlayer(this);
				}

				@Override
				public boolean isOn(){
					return true;
				}

				@Override
				public void setPaused(boolean paused){
					this.paused = paused;
				}

				@Override
				public boolean isPaused(){
					return paused;
				}

				@Override
				public int compareTo(AudioPlayable ap){
					return new Integer(hashCode()).compareTo(ap.hashCode());
				}
			};
		}
		
		@Override
		public void run(){
			audioLock.lock();
			try{
				try{
					SourceDataLine sdl = null;
				    try {
				    	int bufferSize = 4096;
				    	AudioFormat format = AudioSystem.getAudioFileFormat(new File(location)).getFormat();
						sdl = AudioSystem.getSourceDataLine(format);
						sdl.open(format, bufferSize);
				        BufferedInputStream bis = new BufferedInputStream(AudioSystem.getAudioInputStream(new File(location)));
				        byte[] buffer = new byte[bufferSize];
				        int b = 0;
				        sdl.start();
				        while((b = bis.read(buffer)) >= 0 && looper.isOn()){
				        	while(looper.isPaused()){
				        		looperCondition.await();
				        	}
					        sdl.write(buffer, 0, b);
				        }
				        sdl.drain();
				        sdl.stop();
				    } 
					catch(IOException e){
						e.printStackTrace();
					}
					catch(UnsupportedAudioFileException e){
						e.printStackTrace();
					}
					catch(LineUnavailableException e){
						e.printStackTrace();
					}
				    finally{
				    	if(sdl != null){
				    		sdl.close();
				    	}
				    }
				    looper.ready();
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
			finally{
				audioLock.unlock();
			}
		}
	}
}
