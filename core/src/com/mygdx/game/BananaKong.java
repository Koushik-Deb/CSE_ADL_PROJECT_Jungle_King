package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.screens.CreditScreen;
import com.mygdx.game.screens.GameOverScreen;

import com.mygdx.game.screens.HowToPlayScreen;
import com.mygdx.game.screens.LoadingScreen;

import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.SettingsScreen;
import com.mygdx.game.screens.SplashScreen;
import com.mygdx.game.screens.Statistics;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BananaKong extends Game implements ApplicationListener, InputProcessor  {

	public SpriteBatch batch;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 800;
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 800;
	public static final float PPM = 100;
	public static final String TITLE = "Jungle King";

	public static final short GROUND_BIT = 1;
	public static final short KING_BIT = 2;
	public static final short OBSTACLE_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short Nothing_Bit = 16;
	public static final short NULL_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short BLANK_BIT = 256;
	public static final short BIRD_BIT = 512;

	//for the views and stage
	public OrthographicCamera camera;
	public Viewport screenPort;
	public static AssetManager manager;
	public AssetManager asset;

	//music
	public static boolean setMusic = true;
	public Music music;
	private MenuScreen ms;

	// Creating Preferences to save Data
	public static Preferences preferences;

	@Override
	public void create () {
		preferences = Gdx.app.getPreferences("JungleKing");
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);

		//load music and etc from the asset
		manager = new AssetManager();
		asset = new AssetManager();
		manager.load("audio/mp3.mp3", Music.class);
		manager.load("audio/Tiger6.mp3", Sound.class);
		manager.load("audio/yahoo.wav",Sound.class);
		manager.load("audio/punch.mp3",Sound.class);
		manager.finishLoading();

		PlayScreen.mainlives=3;
		PlayScreen.Lives = 5;
		PlayScreen.cont=0;

		//to set the view according to the screen where it is played
		screenPort = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void resize(int width, int height) {}

	//music settings
	public void playMusic()
	{
		music = manager.get("audio/mp3.mp3", Music.class);
		music.setLooping(true);
		music.play();
	}
	public void pauseMusic()
	{
		System.out.println("yoo2");
		if(music.isLooping()) {
			System.out.println("yoo");
				music.pause();
		}
		else playMusic();
	}


	//going into different screens
	public void gotoSplashScreen(){
		SplashScreen splashScreen = new SplashScreen(this);
		setScreen(splashScreen);
	}

	public void gotoMenuScreen(){
		//setting music and going to the main menu screen
		if(setMusic)
		{
			playMusic();
		}
		MenuScreen menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	public void gotoSettingsScreen(){
		if(setMusic)
			playMusic();
		SettingsScreen settingsScreen = new SettingsScreen(this);
		setScreen(settingsScreen);
	}

	public void gotoCreditScreen()
	{
		CreditScreen creditScreen = new CreditScreen(this);
		setScreen(creditScreen);
	}
	public void gotoHowToPlayScreen()
	{
		HowToPlayScreen howToPlayScreen = new HowToPlayScreen(this);
		setScreen(howToPlayScreen);
	}


	public void gotoPlayScreen(){
		PlayScreen playScreen = new PlayScreen(this);
		setScreen(playScreen);
	}

	public void gotoGameOverScreen()
	{
		GameOverScreen gameOverScreen = new GameOverScreen(this);
		setScreen(gameOverScreen);
	}


	//File Handling for saving Scores @author -- Rakib

	public  void setHighScore(int val) {
		if (!preferences.contains("highScore")) {
			System.out.println("Rakib");
			preferences.putInteger("highScore", 0);
		}

		if(val >= getHighScore()) {
			preferences.putInteger("highScore",val);
			preferences.flush();
		}
	}

	public  int getHighScore() {
		if (!preferences.contains("highScore")) {
			return 0;
		}
		return preferences.getInteger("highScore");
	}

	public void setMaxDistance(int val) {
		if (!preferences.contains("maxDistance")) {
			preferences.putInteger("maxDistance", 0);
		}

		if(val >= getMaxDistance()) {
			preferences.putInteger("maxDistance", val);
			preferences.flush();
		}
	}

	public int getMaxDistance() {
		return preferences.getInteger("maxDistance");
	}

	public void setMaxCoins(int val) {
		if (!preferences.contains("maxCoins")) {
			preferences.putInteger("maxCoins", 0);
		}

		if(val >= getMaxCoins()) {
			preferences.putInteger("maxCoins", val);
			preferences.flush();
		}
	}

	public int getMaxCoins() {
		return preferences.getInteger("maxCoins");
	}

	public void setTotalDistance (int val) {
		if (!preferences.contains("Distance")) {
			preferences.putInteger("Distance", 0);
		}

		else {
			preferences.putInteger("Distance", val + getTotalDistance());
			preferences.flush();
		}
	}

	public int getTotalDistance() {
		return preferences.getInteger("Distance");
	}

	public void setTotalCoins (int val) {
		if (!preferences.contains("coins")) {
			preferences.putInteger("coins", 0);
		}

		else {
			preferences.putInteger("coins", val + getTotalCoins());
			preferences.flush();
		}
	}

	public int getTotalCoins() {
		return preferences.getInteger("coins");
	}

	public void setTotalScore (int val) {
		if (!preferences.contains("totalScore")) {
			preferences.putInteger("totalScore", 0);
		}

		else {
			preferences.putInteger("totalScore", val + getTotalScore());
			preferences.flush();
		}
	}

	public int getTotalScore () {
		return preferences.getInteger("totalScore");
	}

	public void setGameCount () {

		{
			preferences.putInteger("gameCount", getGameCount() + 1);
			preferences.flush();
		}
	}

	public int getGameCount() {
		return preferences.getInteger("gameCount");
	}


	public void setHighScorer (String name) {
		preferences.putString("highScorer",name);
		preferences.flush();
	}

	public String getHighScorer () {
		return preferences.getString("highScorer");

	}


	@Override
	public void render () {
		super.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {

		super.dispose();
		asset.dispose();
	}


	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
