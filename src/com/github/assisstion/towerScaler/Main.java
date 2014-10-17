package com.github.assisstion.towerScaler;

import java.io.File;

import org.newdawn.slick.AppGameContainer;

import com.github.assisstion.towerScaler.engine.MainEngine;
import com.github.assisstion.towerScaler.media.AudioHelper;

/*
 * Platformer (Tower Scaler)
 * By Markus Feng (assisstion)
 *
 * Using the Slick2D Engine
 * http://slick.ninjacave.com/
 * (See License at EOF)
 *
 * Version History:
 *
 * Pre-Alpha 0.3.0.1 (2014-10-16)
 *   Added opponent block sprite
 *   Added multiplayer block bumping
 *
 * Pre-Alpha 0.3.0.0 (2014-10-15)
 *   Added multiplayer
 *   Added multiplayer processor
 *   Added multiplayer command system
 *   Added dependency on SocketRelay API 1.1.4
 *
 * Pre-Alpha 0.2.3.0 (2014-10-11)
 *   Added Stats menu
 *   Added Stats storage system
 *
 * Pre-Alpha 0.2.2.4 (2014-10-10)
 *   Changed state-based system to "Active Engine" based system
 *
 * Pre-Alpha 0.2.2.3 (2014-10-06)
 *   Changed compiler compliance to 1.7
 *   Added fullscreen support
 *   Minor keyboard fix
 *
 * Pre-Alpha 0.2.2.2 (2014-10-05)
 *   Added options menu
 *   Semantic changes
 *   Minor sound fix
 *
 * Pre-Alpha 0.2.2.1 (2014-10-03)
 *   Stores unlimited high scores
 *     Added previous and next button to highscore menu
 *   Made boxed labels wider
 *
 * Pre-Alpha 0.2.2.0 (2014-10-03)
 *   Formatted Code
 *   Changed arcade mode player block color
 *   Added a maximum limit to scrolling speed
 *     Currently at 3200+ distance
 *   Added constants to separate class
 *
 * Pre-Alpha 0.2.1.0 (2014-02-18)
 *   Split game into regular and arcade mode
 *     Arcade mode is the old mode
 *     Regular mode is a new mode
 *   Regular mode changes
 *     Added new sprites
 *     Disabled side scrolling
 *     Changed block generating algorithm
 *
 * Pre-Alpha 0.2.0.1 (2014-01-12)
 *   Minor mouse focus fix
 *
 * Pre-Alpha 0.2.0.0 (2014-01-11)
 *   Github launch
 *   Added license
 *   Added audio system
 *     Currently using song: Revolution by LunacyEcho
 *   Added game over menu
 *   Improved performance
 *   Sorted files
 *     Moved files to individual packages
 *   Code cleanup
 *   Other changes
 *     High score checking
 *     Last used name saving
 *     TSComponent parent hierarchy
 *   Other minor fixes
 *
 *
 * Pre-Alpha 0.1.3.1 (2013-12-31)
 *   Added high score saving
 *     Added file output helper classes
 *     Saves using system serialization
 *   Added score hashing
 *   Other minor fixes
 *
 * Pre-Alpha 0.1.3.0 (2013-12-29)
 *   Added TSToolkit system
 *   Added menu system
 *   Added high score menu
 *   Added high score system
 *   Sorted files
 *     Renamed files
 *     Moved files to TSToolkit package
 *   Other minor fixes
 *
 * Pre-Alpha 0.1.2.2 (2013-12-27)
 *   Added layering system
 *   Added engine property system
 *   Sorted files
 *     Renamed engine related files
 *   Other minor fixes
 *
 * Pre-Alpha 0.1.2.1 (2013-12-25)
 *   Added option of restarting and exiting to menu in pausing and after game over
 *   Other minor fixes
 *
 * Pre-Alpha 0.1.2.0 (2013-12-25)
 *   Added menu
 *   Sorted files
 *     Renamed files
 *     Engine moved to separate files
 *     Box files moved to separate package
 *
 * Pre-Alpha 0.1.1.2 (2013-12-23)
 *   Changed player jump height and gravity
 *   Added noClip
 *
 * Pre-Alpha 0.1.1.1 (2013-12-20)
 *   Ready for export
 *   Added game_over state
 *
 * Pre-Alpha 0.1.1.0 (2013-12-20)
 *   Added scroll disabling
 *   Fixed pausing
 *   Changed level generator
 *   Added cleanup
 *   Added more rounding
 *   Collision fixes
 *
 * Pre-Alpha 0.1.0.2 (2013-12-20)
 *   Reorganized Engine class
 *
 * Pre-Alpha 0.1.0.1 (2013-12-20)
 *   Minor rounding fix
 *
 * Pre-Alpha 0.1.0.0 (2013-12-20)
 *   Created git repository
 *   Created project
 *   Created game using Slick2D engine
 *   Completed entity hierarchy
 *   Completed collision engine
 *
 */

@Version(value = "Pre-Alpha 0.3.0.1", lastUpdate = "2014-10-16")
public final class Main{

	private Main(){
		// Not to be instantiated
	}

	private static int gameFrameWidth = 960;
	private static int gameFrameHeight = 640;
	public static boolean debug = false;
	private static final boolean fullScreen = false;

	public static void main(String[] args){
		try{
			AppGameContainer app = new AppGameContainer(new MainEngine());
			if(fullScreen){
				gameFrameWidth = app.getScreenWidth();
				gameFrameHeight = app.getScreenHeight();
			}
			app.setDisplayMode(gameFrameWidth, gameFrameHeight, fullScreen);
			app.setMinimumLogicUpdateInterval(20);
			app.setMaximumLogicUpdateInterval(20);
			app.setTargetFrameRate(50);
			app.setAlwaysRender(true);
			app.setUpdateOnlyWhenVisible(false);
			AudioHelper.loopSound("resources" + File.separator +
					"Revolution by LunacyEcho.wav");
			if(debug){
				System.out.println("App started!");
			}
			app.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static int getGameFrameWidth(){
		return gameFrameWidth;
	}

	public static int getGameFrameHeight(){
		return gameFrameHeight;
	}

	/*
	 * License All parts of this software, unless otherwise noted, is licensed
	 * under the MIT license.
	 *
	 * See <http://opensource.org/licenses/MIT> for more details.
	 *
	 * The MIT License (MIT)
	 *
	 * Copyright (c) 2013-2014 Markus Feng
	 *
	 * Permission is hereby granted, free of charge, to any person obtaining a
	 * copy of this software and associated documentation files (the
	 * "Software"), to deal in the Software without restriction, including
	 * without limitation the rights to use, copy, modify, merge, publish,
	 * distribute, sublicense, and/or sell copies of the Software, and to permit
	 * persons to whom the Software is furnished to do so, subject to the
	 * following conditions:
	 *
	 * The above copyright notice and this permission notice shall be included
	 * in all copies or substantial portions of the Software.
	 *
	 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
	 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
	 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
	 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
	 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
	 * USE OR OTHER DEALINGS IN THE SOFTWARE.
	 */

	/*
	 * Slick2D license
	 *
	 * License Copyright (c) 2013, Slick2D All rights reserved.
	 *
	 * Redistribution and use in source and binary forms, with or without
	 * modification, are permitted provided that the following conditions are
	 * met:
	 *
	 * - Redistributions of source code must retain the above copyright notice,
	 * this list of conditions and the following disclaimer.
	 *
	 * - Redistributions in binary form must reproduce the above copyright
	 * notice, this list of conditions and the following disclaimer in the
	 * documentation and/or other materials provided with the distribution.
	 *
	 * - Neither the name of the Slick2D nor the names of its contributors may
	 * be used to endorse or promote products derived from this software without
	 * specific prior written permission.
	 *
	 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
	 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
	 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
	 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
	 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
	 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
	 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
	 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
	 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
	 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	 */
}
