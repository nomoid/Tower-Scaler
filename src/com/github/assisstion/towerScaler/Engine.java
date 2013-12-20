package com.github.assisstion.towerScaler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.github.assisstion.towerScaler.entity.Box;
import com.github.assisstion.towerScaler.entity.BoxImpl;
import com.github.assisstion.towerScaler.entity.CollisionEntity;
import com.github.assisstion.towerScaler.entity.Entity;
import com.github.assisstion.towerScaler.entity.GravitationalEntity;
import com.github.assisstion.towerScaler.entity.PlatformBlock;
import com.github.assisstion.towerScaler.entity.Player;

public class Engine extends BasicGame{
	
	public Set<Entity> entities;
	public Set<CollisionEntity> collisionObjects;
	public Set<GravitationalEntity> collidables;
	public Map<GravitationalEntity, Box> safeBoxes;
	public double gameX;
	public double gameY;
	public double nextUpdateX;
	public double nextUpdateY;
	public Player player;
	public boolean paused;
	protected boolean aboveBlock;
	protected boolean leftOfBlock;
	protected boolean belowBlock;
	protected boolean rightOfBlock;
	protected int blockCounter;
	protected CollisionEntity nextTo;

	public Engine(){
		super("Tower Scaler" + 
				(Main.class.getAnnotation(Version.class) == null ? "" : 
				(" - Version " + Main.class.getAnnotation(Version.class).value())));
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		g.setBackground(new Color(150, 150, 255));
		for(Entity e : entities){
			g.drawImage(e.getImage(), (float) (e.getX1() - gameX), (float) (e.getY1() - gameY));
		}
		g.drawString("Score: " + Helper.round(-gameY, 2), 10, 30);
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
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
		gameX = player.getX1() - 480;
		gameY = 0;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException{
		if(paused){
			if(gc.getInput().isKeyDown(Input.KEY_R)){
				paused = false;
			}
			else{
				return;
			}
		}
		else{
			if(gc.getInput().isKeyDown(Input.KEY_P)){
				paused = true;
				return;
			}
		}
		gameX = player.getX1() - 480;
		gameY -= -(gameY / 1000) + 1;
		if(gameY <= nextUpdateY){
			PlatformBlock pb0 = new PlatformBlock(nextUpdateX, gameY - 100);
			//nextUpdateX = -nextUpdateX + 250;
			nextUpdateX = nextUpdateX + ((Math.random() - 0.5) * 200);
			nextUpdateY -= 100;
			entities.add(pb0);
			collisionObjects.add(pb0);
		}
		blockCounter++;
		aboveBlock = false;
		belowBlock = false;
		leftOfBlock = false;
		rightOfBlock = false;
		safeBoxes.clear();
		Input input = gc.getInput();
		preCollisionCheck();
		inputCheck(input, delta);
		postCollisionCheck();
		if(player.getY1() - gameY < 0){
			player.setY(gameY - 1);
			player.setYVelocity(0);
		}
		if(player.getY1() - gameY > 640 + player.getHeight()){
			System.out.println("Game Over! Score: " + Helper.round(-gameY, 2));
			System.exit(0);
		}
	}
	
	protected void preCollisionCheck(){
		boolean xYes = true;
		boolean yYes = true;
		for(GravitationalEntity ge : collidables){
			for(CollisionEntity ce : collisionObjects){
				if(!ge.overlaps(ce)){
					//Down collision test
					boolean b1 = false;
					BoxImpl boxDown = new BoxImpl(ge.getBox());
					boxDown.setPos(boxDown.getX1(), boxDown.getX2(), boxDown.getY1(), boxDown.getY2() + 1);
					if(boxDown.overlaps(ce)){
						ge.setY(ce.getY1() - ge.getHeight() - 1);	
						ge.setYVelocity(0);
						if(GravitationalEntity.yGravity >= 0){
							yYes = false;
						}
						aboveBlock = true;
						nextTo = ce;
						blockCounter = 0;
					}
					else{
						b1 = true;
					}
					//Up collision test
					boolean b2 = false;
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
					else{
						b2 = true;
					}
					//Left collision test
					boolean b3 = false;
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
					else{
						b3 = true;
					}
					//Right collision test
					boolean b4 = false;
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
					else{
						b4 = true;
					}
					BoxImpl boxA;
					if(b1 && b3){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1() - 1, boxA.getX2(), boxA.getY1(), boxA.getY2() + 1);
						if(boxA.overlaps(ce)){
							if((-GravitationalEntity.xGravity) > (GravitationalEntity.yGravity)){
								ge.incrementX(-1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity >= 0){
									yYes = false;
								}
								aboveBlock = true;
								blockCounter = 0;
							}
							else if((-GravitationalEntity.xGravity) < (GravitationalEntity.yGravity)){
								ge.incrementY(1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity <= 0){
									xYes = false;
								}
								rightOfBlock = true;
							}
							nextTo = ce;
							b1 = false;
							b2 = false;
							b3 = false;
							b4 = false;
						}
					}
					if(b1 && b4){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1(), boxA.getX2() + 1, boxA.getY1(), boxA.getY2() + 1);
						if(boxA.overlaps(ce)){
							if((GravitationalEntity.xGravity) > (GravitationalEntity.yGravity)){
								ge.incrementX(1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity >= 0){
									yYes = false;
								}
								aboveBlock = true;
								blockCounter = 0;
							}
							else if((GravitationalEntity.xGravity) < (GravitationalEntity.yGravity)){
								ge.incrementY(1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity >= 0){
									xYes = false;
								}
								leftOfBlock = true;
							}
							nextTo = ce;
							b1 = false;
							b2 = false;
							b3 = false;
							b4 = false;
						}
					}
					if(b2 && b4){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1(), boxA.getX2() + 1, boxA.getY1() - 1, boxA.getY2());
						if(boxA.overlaps(ce)){
							if((GravitationalEntity.xGravity) > (-GravitationalEntity.yGravity)){
								ge.incrementX(1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity <= 0){
									yYes = false;
								}
								belowBlock = true;
							}
							else if((GravitationalEntity.xGravity) < (-GravitationalEntity.yGravity)){
								ge.incrementY(-1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity >= 0){
									xYes = false;
								}
								leftOfBlock = true;
							}
							nextTo = ce;
							b1 = false;
							b2 = false;
							b3 = false;
							b4 = false;
						}
					}
					if(b2 && b3){
						boxA = new BoxImpl(ge.getBox());
						boxA.setPos(boxA.getX1() - 1, boxA.getX2(), boxA.getY1()-1, boxA.getY2());
						if(boxA.overlaps(ce)){
							if((-GravitationalEntity.xGravity) > (-GravitationalEntity.yGravity)){
								ge.incrementX(-1);
								ge.setYVelocity(0);
								if(GravitationalEntity.yGravity <= 0){
									yYes = false;
								}
								belowBlock = true;
							}
							else if((-GravitationalEntity.xGravity) < (-GravitationalEntity.yGravity)){
								ge.incrementY(-1);
								ge.setXVelocity(0);
								if(GravitationalEntity.xGravity <= 0){
									xYes = false;
								}
								rightOfBlock = true;
							}
							nextTo = ce;
							b1 = false;
							b2 = false;
							b3 = false;
							b4 = false;
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
	
	protected void inputCheck(Input input, int delta){
		if(input.isKeyDown(Input.KEY_SPACE)){
			System.out.println("Break!");
			paused = true;
		}
		if(input.isKeyDown(Input.KEY_UP)){
			if(aboveBlock){
				player.setYVelocity(-1.44);
			}
		}
		if(input.isKeyDown(Input.KEY_DOWN)){
			player.incrementYVelocity(0.12);
		}
		if(input.isKeyDown(Input.KEY_LEFT)){
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
		if(input.isKeyDown(Input.KEY_RIGHT)){
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
		player.incrementX(0.3 * player.getXVelocity() * delta);
		player.incrementY(0.3 * player.getYVelocity() * delta);
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
							System.out.println("Strange");
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
}
