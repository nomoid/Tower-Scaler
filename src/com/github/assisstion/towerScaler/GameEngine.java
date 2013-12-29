package com.github.assisstion.towerScaler;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.github.assisstion.towerScaler.box.Box;
import com.github.assisstion.towerScaler.box.BoxImpl;
import com.github.assisstion.towerScaler.entity.CollisionEntity;
import com.github.assisstion.towerScaler.entity.Entity;
import com.github.assisstion.towerScaler.entity.GravitationalEntity;
import com.github.assisstion.towerScaler.entity.PlatformBlock;
import com.github.assisstion.towerScaler.entity.Player;

public class GameEngine extends AbstractEngine{
	
	//Set to false to reset
	protected boolean initialized;
	protected MainEngine engine;
	protected Set<Entity> entities;
	protected Set<CollisionEntity> collisionObjects;
	protected Set<GravitationalEntity> collidables;
	protected Map<GravitationalEntity, Box> safeBoxes;
	protected double gameX;
	protected double gameY;
	protected double nextUpdateX;
	protected double nextUpdateY;
	protected Player player;
	//Whether gravity affects player or not
	protected boolean noClip;
	protected boolean paused;
	//Whether automatic game scrolling is enabled or not
	protected boolean scrollingEnabled;
	//Number of jumps available, resets on touching block top
	protected int jumpCounter;
	protected boolean aboveBlock;
	protected boolean leftOfBlock;
	protected boolean belowBlock;
	protected boolean rightOfBlock;
	//Number of frames since last touching block
	protected int blockCounter;
	protected String name;
	protected long hash;
	protected boolean legit;
	
	public GameEngine(MainEngine parent){
		engine = parent;
		parent.addKeyListener(this);
	}
	
	protected GameEngine(){
		
	}
	
	@Override
	public void init(GameContainer gc){
		initialized = true;
		entities = new HashSet<Entity>();
		collisionObjects = new HashSet<CollisionEntity>();
		collidables = new HashSet<GravitationalEntity>();
		safeBoxes = new HashMap<GravitationalEntity, Box>();
		player = new Player();
		entities.add(player);
		collidables.add(player);
		PlatformBlock pb0 = new PlatformBlock(150, 0);
		entities.add(pb0);
		collisionObjects.add(pb0);
		PlatformBlock pbn = new PlatformBlock(100, 100);
		entities.add(pbn);
		collisionObjects.add(pbn);
		PlatformBlock pb1 = new PlatformBlock(150, 200);
		entities.add(pb1);
		collisionObjects.add(pb1);
		PlatformBlock pb2 = new PlatformBlock(100, 300);
		entities.add(pb2);
		collisionObjects.add(pb2);
		PlatformBlock pb3 = new PlatformBlock(150, 400);
		entities.add(pb3);
		collisionObjects.add(pb3);
		PlatformBlock pb4 = new PlatformBlock(100, 500);
		entities.add(pb4);
		collisionObjects.add(pb4);
		PlatformBlock pb5 = new PlatformBlock(150, 600);
		entities.add(pb5);
		collisionObjects.add(pb5);
		nextUpdateY = 0;
		nextUpdateX = 100;
		gameX = player.getX1() - (Main.getGameFrameWidth() / 2);
		gameY = 0;
		paused = false;
		scrollingEnabled = true;
		blockCounter = 0;
		aboveBlock = false;
		belowBlock = false;
		leftOfBlock = false;
		rightOfBlock = false;
		noClip = false;
		jumpCounter = 2;
		name = "";
		legit = true;
	}
	
	@Override
	public void render(GameContainer gc, Graphics g, int layer){
		//Ignore layer
		render(gc, g);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g){
		g.setBackground(new Color(150, 150, 255));
		//g.setBackground(new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
		Input input = gc.getInput();
		int x = input.getMouseX();
		int y = input.getMouseY();
		for(Entity e : entities){
			g.drawImage(e.getImage(), (float) (e.getX1() - gameX), (float) (e.getY1() - gameY));
			if(Main.debug){
				if(e instanceof CollisionEntity){
					if(new BoxImpl((CollisionEntity)e).pointIn(x + gameX, y + gameY)){
						g.drawString(e.getX1() + ", " + e.getY1() + ", " + e.getX2() + ", " + e.getY2(), 10, 110);
					}
				}
			}
		}
		g.setColor(Color.white);
		g.drawString("Score: " + Helper.round(-gameY, 2), 10, 30);
		if(Main.debug){
			if(!getState().equals("game_over")){
				g.drawString("(" + aboveBlock + ", " + belowBlock + ", " + leftOfBlock + ", " + rightOfBlock + ")", 10, 90);
			}
		}
		if(paused){
			g.drawString("Game paused!", 10, 50);
		}
		if(paused || getState().equals("game_over")){
			g.drawString("(Press space to return to menu, press enter to restart)", 10, 70);
			g.drawString("Name: " + name, 10, 90);
		}
		if(getState().equals("game_over")){
			//g.setBackground(new Color(150, 150, 255));
			g.drawString("Game Over!", 10, 50);
			//g.drawString("Score: " + Helper.round(-gameY, 2), 10, 30);
		}
	}
	
	@Override
	public void update(GameContainer gc, int delta){
		Input input = gc.getInput();
		if(getState().equals("game_over")){
			gameOverUpdate(input, delta);
			return;
		}
		if(!pauseCheck(input)){
			return;
		}
		upkeepCheck(input);
		preCollisionCheck();
		inputCheck(input, delta);
		gameMovementCheck();
		postCollisionCheck();
		cleanupCheck();
	}
	
	protected void gameOverUpdate(Input input, int delta){
		
	}
	
	

	protected boolean pauseCheck(Input input){
		if(paused){
			if(input.isKeyDown(Input.KEY_R)){
				paused = false;
			}
			else{
				return false;
			}
		}
		else{
			if(input.isKeyDown(Input.KEY_P)){
				paused = true;
				return false;
			}
		}
		return true;
	}
	
	protected void upkeepCheck(Input input){
		gameX = player.getX1() - (Main.getGameFrameWidth() / 2);
		if(input.isKeyDown(Input.KEY_L)){
			scrollingEnabled = false;
			legit = false;
		}
		if(input.isKeyDown(Input.KEY_O)){
			scrollingEnabled = true;
		}
		if(scrollingEnabled){
			gameY -= -(gameY / 1000) + 1;
		}
		if(gameY <= nextUpdateY){
			Set<PlatformBlock> addToEntities = new HashSet<PlatformBlock>();
			for(int n = 0; n < 10; n++){
				PlatformBlock pb0 = new PlatformBlock(nextUpdateX, gameY - 100);
				/*if(Math.random() * 2 < 1){
					for(int i = 1; i < 10; i++){
						PlatformBlock pb1 = new PlatformBlock(nextUpdateX - pb0.getHeight() * i, gameY - 100);
						boolean overlap = false;
						for(Entity e : addToEntities){
							if(new BoxImpl(e).overlaps(pb0)){
								overlap = true;
							}
						}
						if(!overlap){
							addToEntities.add(pb0);
							entities.add(pb0);
							collisionObjects.add(pb0);
						}
						else{
							break;
						}
						if(Math.random() * (i + 2) >= 1){
							break;
						}
					}
				}*/
				nextUpdateX = nextUpdateX + ((Math.random() - 0.5) * 200);
				boolean overlap = false;
				for(Entity e : addToEntities){
					if(new BoxImpl(e).overlaps(pb0)){
						overlap = true;
					}
				}
				if(!overlap){
					addToEntities.add(pb0);
					entities.add(pb0);
					collisionObjects.add(pb0);
				}
				else{
					n--;
				}
				if(Math.random() * (n + 1) >= 1){
					break;
				}
			}
			//nextUpdateX = -nextUpdateX + 250;
			nextUpdateX = nextUpdateX + ((Math.random() - 0.5) * 200);
			nextUpdateY -= 100;
		}
		blockCounter++;
		aboveBlock = false;
		belowBlock = false;
		leftOfBlock = false;
		rightOfBlock = false;
		safeBoxes.clear();
	}
	
	protected void preCollisionCheck(){
		boolean xYes = true;
		boolean yYes = true;
		for(GravitationalEntity ge : collidables){
			for(CollisionEntity ce : collisionObjects){
				if(!ge.overlaps(ce)){
					//Down collision test
					BoxImpl boxDown = new BoxImpl(ge.getBox());
					boxDown.setPos(boxDown.getX1(), boxDown.getX2(), boxDown.getY1(), boxDown.getY2() + 1);
					if(boxDown.overlaps(ce)){
						ge.setY(ce.getY1() - ge.getHeight() - 1);	
						ge.setYVelocity(0);
						if(GravitationalEntity.yGravity >= 0){
							yYes = false;
						}
						aboveBlock = true;
						blockCounter = 0;
					}
					//Up collision test
					BoxImpl boxUp = new BoxImpl(ge.getBox());
					boxUp.setPos(boxUp.getX1(), boxUp.getX2(), boxUp.getY1() - 1, boxUp.getY2());
					if(boxUp.overlaps(ce)){
						ge.setY(ce.getY1() + ce.getHeight() + 1);	
						ge.setYVelocity(0);
						if(GravitationalEntity.yGravity <= 0){
							yYes = false;
						}
						belowBlock = true;
					}
					//Left collision test
					BoxImpl boxLeft = new BoxImpl(ge.getBox());
					boxLeft.setPos(boxLeft.getX1() - 1, boxLeft.getX2(), boxLeft.getY1(), boxLeft.getY2());
					if(boxLeft.overlaps(ce)){
						ge.setX(ce.getX1() + ce.getWidth() + 1);
						ge.setXVelocity(0);
						if(GravitationalEntity.xGravity <= 0){
							xYes = false;
						}
						rightOfBlock = true;
					}
					//Right collision test
					BoxImpl boxRight = new BoxImpl(ge.getBox());
					boxRight.setPos(boxRight.getX1(), boxRight.getX2() + 1, boxRight.getY1(), boxRight.getY2());
					if(boxRight.overlaps(ce)){
						ge.setX(ce.getX1() - ge.getWidth() - 1);
						ge.setXVelocity(0);
						if(GravitationalEntity.xGravity >= 0){
							xYes = false;
						}
						leftOfBlock = true;
					}
					BoxImpl boxA;
					if((!aboveBlock) && (!rightOfBlock)){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1() - 1, boxA.getX2(), boxA.getY1(), boxA.getY2() + 1);
						if(boxA.overlaps(ce)){
							if((-GravitationalEntity.xGravity) > (GravitationalEntity.yGravity)){
								ge.incrementX(-1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity >= 0){
									yYes = false;
								}
								blockCounter = 0;
							}
							else if((-GravitationalEntity.xGravity) < (GravitationalEntity.yGravity)){
								ge.incrementY(1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity <= 0){
									xYes = false;
								}
							}
						}
					}
					if((!aboveBlock) && (!leftOfBlock)){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1(), boxA.getX2() + 1, boxA.getY1(), boxA.getY2() + 1);
						if(boxA.overlaps(ce)){
							if((GravitationalEntity.xGravity) > (GravitationalEntity.yGravity)){
								ge.incrementX(1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity >= 0){
									yYes = false;
								}
								blockCounter = 0;
							}
							else if((GravitationalEntity.xGravity) < (GravitationalEntity.yGravity)){
								ge.incrementY(1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity >= 0){
									xYes = false;
								}
							}
						}
					}
					if((!belowBlock) && (!leftOfBlock)){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1(), boxA.getX2() + 1, boxA.getY1() - 1, boxA.getY2());
						if(boxA.overlaps(ce)){
							if((GravitationalEntity.xGravity) > (-GravitationalEntity.yGravity)){
								ge.incrementX(1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity <= 0){
									yYes = false;
								}
							}
							else if((GravitationalEntity.xGravity) < (-GravitationalEntity.yGravity)){
								ge.incrementY(-1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity >= 0){
									xYes = false;
								}
							}
						}
					}
					if((!belowBlock) && (!rightOfBlock)){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1() - 1, boxA.getX2(), boxA.getY1()-1, boxA.getY2());
						if(boxA.overlaps(ce)){
							if((-GravitationalEntity.xGravity) > (-GravitationalEntity.yGravity)){
								ge.incrementX(-1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity <= 0){
									yYes = false;
								}
							}
							else if((-GravitationalEntity.xGravity) < (-GravitationalEntity.yGravity)){
								ge.incrementY(-1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity <= 0){
									xYes = false;
								}
							}
						}
					}
				}
				else{
					ge.setY(ce.getY1() - ge.getHeight() - 1);	
					ge.setYVelocity(0);
					xYes = false;
					yYes = false;
				}
			}
			safeBoxes.put(ge, new BoxImpl(ge));
			ge.updateGravity(xYes, yYes);
		}
		
	}
	
	/*
	 * Input Map:
	 * Q: 
	 * W: move up
	 * E: 
	 * R: resume game
	 * T: 
	 * Y: 
	 * U: 
	 * I: 
	 * O: resume scrolling
	 * P: pause game
	 * A: move left
	 * S: move down
	 * D: move right
	 * F: 
	 * J: 
	 * K: 
	 * L: pause scrolling
	 * Z: enable debug mode
	 * X: disable debug mode
	 * C: 
	 * V: 
	 * B: break
	 * N: noClip on
	 * M: noClip off
	 * Space: exit to menu (after game over or while paused)
	 * Enter: restart game (after game over or while paused)
	 * Escape: exit to menu from anywhere, shut down from menu
	 * Up: move up
	 * Left: move left
	 * Down: move down
	 * Right: move right
	 */
	protected void inputCheck(Input input, int delta){
		if(input.isKeyDown(Input.KEY_Z)){
			Main.debug = true;
			legit = false;
		}
		if(input.isKeyDown(Input.KEY_X)){
			Main.debug = false;
		}
		if(input.isKeyDown(Input.KEY_N)){
			noClip = true;
			legit = false;
		}
		if(input.isKeyDown(Input.KEY_M)){
			noClip = false;
		}
		if(input.isKeyDown(Input.KEY_B)){
			if(Main.debug){
				System.out.println("Break!");
				paused = true;
			}
		}
		if(aboveBlock){
			//Double jumping
			jumpCounter = 2;
		}
		if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)){
			if(noClip){
				player.incrementY(-0.2 * delta);
			}
		}
		if(input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_W)){
			if(!noClip){
				if(jumpCounter > 0){
					jumpCounter--;
					if(aboveBlock){
						player.setYVelocity(-2);
					}
					//Midair Jumping
					else{
						player.setYVelocity(-1.5);
					}
				}
			}
		}
		if(input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)){
			if(noClip){
				player.incrementY(0.2 * delta);
			}
			else{
				player.incrementYVelocity(0.12);
			}
		}
		if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)){
			if(!rightOfBlock){
				double movement = (0.3 - 0.012 * blockCounter) * delta;
				if(movement > 0.2 * delta){
					player.incrementX(-movement);
				}
				else{
					player.incrementX(-0.2 * delta);
				}
			}
		}
		if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){
			if(!leftOfBlock){
				double movement = (0.3 - 0.012 * blockCounter) * delta;
				if(movement > 0.2 * delta){
					player.incrementX(movement);
				}
				else{
					player.incrementX(0.2 * delta);
				}
			}
		}
		if(!noClip){
			player.incrementX(0.3 * player.getXVelocity() * delta);
			player.incrementY(0.3 * player.getYVelocity() * delta);
		}
	}
	
	protected void gameMovementCheck(){
		if(player.getY1() - gameY < 0){
			player.setY(gameY + 1);
			player.setYVelocity(0);
		}
		if(player.getY1() - gameY > Main.getGameFrameHeight() + player.getHeight()){
			if(Main.debug){
				System.out.println("Game Over! Score: " + Helper.round(-gameY, 2));
			}
			
			setState("game_over");
			initialized = false;
		}
	}
	
	protected void postCollisionCheck(){
		for(GravitationalEntity ge : collidables){
			Box safeBox = safeBoxes.get(ge);
			double maxY = Math.abs(ge.getY1() - safeBox.getY1());
			double maxX = Math.abs(ge.getX1() - safeBox.getX1());
			double maxDistance = Math.sqrt((maxX * maxX) + (maxY * maxY));
			double minDistance = maxDistance;
			double minX = maxX;
			double minY = maxY;
			double xMultiplier = -new Double(safeBox.getX1()).compareTo(ge.getX1());
			double yMultiplier = -new Double(safeBox.getY1()).compareTo(ge.getY1());
			double slope = Math.abs(maxY / maxX);
			//true for xN, false for yN
			boolean calcN = false;
			boolean xN = false;
			for(CollisionEntity ce : collisionObjects){
				if(ge.overlaps(ce)){
					//true for xN, false for yN
					boolean tempCalcN = false;
					boolean tempXN = false;
					double xMovement;
					 //Sector LX
					if(safeBox.getX2() < ce.getX1()){
						xMovement = Helper.round(ce.getX1() - safeBox.getX2(), ce.getRoundingDigits());
					}
					//Sector RX
					else if(safeBox.getX1() > ce.getX2()){
						xMovement = Helper.round(safeBox.getX1() - ce.getX2(), ce.getRoundingDigits());
					}
					//Sector MX
					else{
						xMovement = -1;
					}
					double yMovement;
					//Sector XD
					if(safeBox.getY2() < ce.getY1()){
						yMovement = Helper.round(ce.getY1() - safeBox.getY2(), ce.getRoundingDigits());
					}
					//Sector XU
					else if(safeBox.getY1() > ce.getY2()){
						yMovement = Helper.round(safeBox.getY1() - ce.getY2(), ce.getRoundingDigits());
					}
					//Sector XM
					else{
						yMovement = -1;
					}
					if(xMovement > 1){
						xMovement--;
					}
					else if(xMovement >= 0){
						xMovement = 0;
					}
					if(yMovement > 1){
						yMovement--;
					}
					else if(yMovement >= 0){
						yMovement = 0;
					}
					double distance;
					if(yMovement == 0){
						if(xMovement == 0){
							distance = 0;
						}
						else{
							distance = xMovement;
						}
					}
					else if(maxX == 0){
						distance = yMovement;
					}
					else{
						if(xMovement <= 0 && yMovement <= 0){
							distance = 0;
						}
						else {
							if(yMovement <= 0){
								yMovement = slope * xMovement;
								tempCalcN = true;
								tempXN = false;
							}
							else if(xMovement <= 0){
								xMovement = yMovement / slope;
								tempCalcN = true;
								tempXN = true;
							}
							else{
								if(slope * xMovement <= yMovement){
									yMovement = slope * xMovement;
									tempCalcN = true;
									tempXN = false;
								}
								else{
									xMovement = yMovement / slope;
									tempCalcN = true;
									tempXN = true;
								}
							}
							distance = Math.sqrt((xMovement * xMovement) + (yMovement * yMovement));
						}
					}
					if(distance < minDistance){
						minDistance = distance;
						if(xMovement < 0){
							xMovement = 0;
						}
						if(yMovement < 0){
							yMovement = 0;
						}
						minX = xMovement;
						minY = yMovement;
						if((minX > 0 && minX < 0.000001) || (minY > 0 && minY < 0.000001)){
							if(Main.debug){
								System.out.println("Strange");
							}
						}
						calcN = tempCalcN;
						xN = tempXN;
					}
				}
			}
			if(calcN){
				Box smallSafeBox = new BoxImpl(ge);
				//System.out.println("Active!");
				if(xN){
					double a = maxX - minX;
					double smallMaxDistance = (a * maxDistance) / (a + maxDistance);
					double smallMinDistance = smallMaxDistance;
					for(CollisionEntity ce : collisionObjects){
						double smallXMovement = -1;
						if(ge.overlaps(ce)){
							if(smallSafeBox.getX2() < ce.getX1()){
								smallXMovement = ce.getX1() - smallSafeBox.getX2();
							}
							else if(smallSafeBox.getX1() > ce.getX2()){
								smallXMovement = smallSafeBox.getX1() - ce.getX2();
							}
						}
						if(smallXMovement > 1){
							smallXMovement--;
						}
						else if(smallXMovement >= 0){
							smallXMovement = 0;
						}
						if(smallXMovement >= 0 && smallXMovement < smallMinDistance){
							smallMinDistance = smallXMovement;
						}
					}
					minX += smallMinDistance;
				}
				else{
					double a = maxY - minY;
					double smallMaxDistance = (a * maxDistance) / (a + maxDistance);
					double smallMinDistance = smallMaxDistance;
					for(CollisionEntity ce : collisionObjects){
						double smallYMovement = -1;
						if(ge.overlaps(ce)){
							if(smallSafeBox.getY2() < ce.getY1()){
								smallYMovement = Helper.round(ce.getY1() - smallSafeBox.getY2(), ce.getRoundingDigits());
							}
							else if(smallSafeBox.getY1() > ce.getY2()){
								smallYMovement = Helper.round(smallSafeBox.getY1() - ce.getY2(), ce.getRoundingDigits());
							}
						}
						if(smallYMovement > 1){
							smallYMovement--;
						}
						else if(smallYMovement >= 0){
							smallYMovement = 0;
						}
						if(smallYMovement >= 0 && smallYMovement < smallMinDistance){
							smallMinDistance = smallYMovement;
						}
					}
					minY += smallMinDistance;
				}
			}
			/*
			System.out.println("omx: " + maxX);
			System.out.println("omy: " + maxY);
			System.out.println("nmx: " + minX);
			System.out.println("nmy: " + minY);
			*/
			ge.setY(safeBox.getY1() + (minY * yMultiplier)); 
			ge.setX(safeBox.getX1() + (minX * xMultiplier));
			safeBox.setPos(ge);
		}
	}
	
	
	protected void cleanupCheck(){
		Set<Entity> markedForRemoval = new HashSet<Entity>();
		for(Entity e : entities){
			if(e.getY1() - gameY > Main.getGameFrameHeight() + e.getHeight()){
				markedForRemoval.add(e);
			}
		}
		for(Entity e : markedForRemoval){
			entities.remove(e);
			if(collisionObjects.contains(e)){
				collisionObjects.remove(e);
			}
			if(collidables.contains(e)){
				collidables.remove(e);
			}
		}
	}

	@Override
	public MainEngine getParent(){
		return engine;
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	public void reset(){
		initialized = false;
	}

	@Override
	public Set<Integer> renderableLayers(){
		return Collections.singleton(0);
	}

	@Override
	public Set<String> renderingStates(){
		Set<String> states = new HashSet<String>();
		Collections.addAll(states, "game", "game_over");
		return states;
	}

	//Always returns false
	@Override
	public boolean hasFocus(){
		return false;
	}
	
	@Override
	public void keyPressed(int key, char c){
		if(key == Input.KEY_ESCAPE){
			return;
		}
		if(getState().equals("game_over")){
			if(key == Input.KEY_SPACE || key == Input.KEY_ENTER){
				return;
			}
			if(key == Input.KEY_BACK || key == Input.KEY_DELETE){
				if(name.length() > 0){
					name = name.substring(0, name.length() - 1);
				}
			}
			else{
				name += c;
			}
		}
	}
	
	public Score getScore(){
		return new Score(-gameY, name, hash);
	}
	
	public boolean isLegit(){
		return legit;
	}
}
