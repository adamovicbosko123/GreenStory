package greenstory.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import greenstory.game.screens.MainMenuScreen;


public class GreenStory extends Game {public static final float SCENE_WIDTH = 12f;
	public static final float SCENE_HEIGHT = 8.8f;

	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short WIFE_BIT = 4;
	public static final short ELEVATOR_GROUND_BIT = 8;
	public static final short PLAYER_FEET_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public static final short LEFT_GRAB_BIT = 64;
	public static final short ATTACK_BIT = 128;
	public static final short ELEVATOR_SENSOR_BIT = 256;
	public static final short DEATH_BIT = 512;
	public static final short PLAYER_SENSOR = 1024;
	// public static final short FIREBALL_BIT = 2048;
	public static final short RIGHT_GRAB_BIT = 4096;
	public static final short BARREL_BIT = 8192;
	public static final short WALL_BIT = 16384;


	public SpriteBatch batch;
	public static GreenStory mainGame;
	public static final float PPM = 50f;

	public GreenStory() {
		mainGame = this;
	}

	public Color mainColor;

	@Override
	public void create() {
		batch = new SpriteBatch();
		mainGame = this;
		mainColor = batch.getColor();
		setScreen(new MainMenuScreen());
	}

	@Override
	public void render() {
		super.render();
	}

	public void dispose() {
		super.dispose();
	}

}
