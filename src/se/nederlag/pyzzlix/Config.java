package se.nederlag.pyzzlix;

public final class Config {
	
	public static final int DEFAULT_GRAVITY_DELAY = 5;

	public static final int VOLUME_STEPS = 10;

	public static final int BOARD_WIDTH = 10;
	public static final int BOARD_HEIGHT = 13;

	public static final int NUM_BLOCKS_FOR_LEVEL = 40;

	public static final int MAX_PAUSE_TIME_SEC = 5;
	public static final int MIN_BLOCKS_FOR_CIRCLE_SCORE = 5;
	public static final int POINTS_PER_LEVEL_FOR_CIRCLE_SCORE = 10;
	public static final int POINTS_PER_LEVEL_FOR_BLOCK_SCORE = 10;

	public static final double PERCENTAGE_TIME_GIVEN_PER_BLOCK = 1/30.0;

	public static final double PAUSE_TIME_PER_BLOCK = 0.25;

	public static final int HOURGLASS_DEFAULT = 2000;
	
	private Config() {		
	}
}
