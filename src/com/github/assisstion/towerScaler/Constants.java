package com.github.assisstion.towerScaler;

public final class Constants{
	public static final double maxXVelocity = 0;
	public static final double maxYVelocity = 8;

	public static final double yGravityConst = 0.08; // 0.08 0.16
	public static final double xGravityConst = 0;
	public static double yGravity = yGravityConst;
	public static double xGravity = xGravityConst;
	public static final double maxSpeedDelta = 0.3;
	public static final double minSpeedDelta = 0.2;
	public static final double jumpConst = 2; // 2 2.82
	public static double jump = jumpConst;
	public static double jumpMidairConst = 1.5; // 1.5 2.12
	public static double jumpMidair = jumpMidairConst;
	public static final double downSpeedIncrease = 0.12;
	public static final double airSlowdown = 0.012;
	public static final double scrollSpeedFactor = 1000;
	public static final double scrollMax = 4.2;

	private Constants(){

	}
}
