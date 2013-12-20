package com.github.assisstion.towerScaler; 

import org.newdawn.slick.AppGameContainer;

/*
 * Platformer (Tower Scaler)
 * By Markus Feng (assisstion)
 * 
 * Using the Slick2D Engine
 * (See License at EOF)
 * 
 * Version History:
 * 
 * Pre-Alpha 0.1.0.0 (2013-12-20)
 *   Created git repository
 *   Created project 
 *   Created game using Slick2D engine
 *   Completed entity hierarchy
 *   Completed collision engine
 * 
 */

@Version(value = "Pre-Alpha 0.1.0.0", lastUpdate = "2013-12-20")
public class Main{
	
	private static final int gameFrameWidth = 960;
	private static final int gameFrameHeight = 640;
	
	public static void main(String[] args){
		try{
			AppGameContainer app = new AppGameContainer(new Engine());
			app.setDisplayMode(gameFrameWidth, gameFrameHeight, false);
			//app.setShowFPS(false);
			app.setMinimumLogicUpdateInterval(20);
			app.setMaximumLogicUpdateInterval(20);
			app.setTargetFrameRate(50);
			app.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Slick2D license
	 * 
	 * License
	 * Copyright (c) 2013, Slick2D
	 * All rights reserved.
	 *
	 * Redistribution and use in source and binary forms, with or without modification, 
	 * are permitted provided that the following conditions are met:
	 *
	 * - Redistributions of source code must retain the above copyright notice, 
	 * this list of conditions and the following disclaimer.
	 * 
	 * - Redistributions in binary form must reproduce the above copyright notice, 
	 * this list of conditions and the following disclaimer in the documentation and/or 
	 * other materials provided with the distribution.
	 * 
	 * - Neither the name of the Slick2D nor the names of its contributors may be used 
	 * to endorse or promote products derived from this software without specific prior 
	 * written permission.
	 * 
	 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ÒAS ISÓ AND 
	 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
	 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
	 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
	 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
	 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
	 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
	 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
	 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
	 * OF THE POSSIBILITY OF SUCH DAMAGE.
	 */
}
