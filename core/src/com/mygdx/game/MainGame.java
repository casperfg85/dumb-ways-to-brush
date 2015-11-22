package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;

import javax.swing.text.html.Option;

public class MainGame extends Game {
	SpriteBatch batch;
	Texture img;

	public static final int nullScreenID = -1;
	public static final int splashScreenID = 1;
	public static final int mainMenuScreenID = 2;
	public static final int mainGameScreenID = 3;
	public static final int optionsScreenID = 4;
    public static final int endGameScreenID = 5;

	private SplashScreen splashScreen;
	private MainMenuScreen mainMenuScreen;
	private MainGameScreen mainGameScreen;
	private OptionsScreen optionsScreen;
    private EndGameScreen endGameScreen;

    private int lastGameScore = 0;

	private HashMap<Integer,GameScreen> screenHashMap = new HashMap<Integer, GameScreen>();

	@Override
	public void create () {
		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		mainGameScreen = new MainGameScreen(this);
		optionsScreen = new OptionsScreen(this);
        endGameScreen = new EndGameScreen(this);

		screenHashMap.put(splashScreenID,splashScreen);
		screenHashMap.put(mainMenuScreenID,mainMenuScreen);
		screenHashMap.put(mainGameScreenID,mainGameScreen);
		screenHashMap.put(optionsScreenID, optionsScreen);
        screenHashMap.put(endGameScreenID, endGameScreen);

		start();
	}

	public void start() {
		setGameScreen(splashScreenID, nullScreenID);
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				easeOut(splashScreenID,mainMenuScreenID,1.0f);
			}
		},2.0f);

	}
	/*
	@Override
	public void render () {


	}
	*/

    public void resetGameData(){
        lastGameScore = -1;
    }

    public int getLastGameScore() {
        return lastGameScore;
    }

    public void setLastGameScore(int score){
        lastGameScore = score;
    }

	public void setGameScreen(int gameScreenIDToShow, int gameScreenIDToDispose){
		if(screenHashMap.containsKey(gameScreenIDToShow)){
			setScreen(screenHashMap.get(gameScreenIDToShow));
			if(gameScreenIDToDispose != -1 && screenHashMap.containsKey(gameScreenIDToDispose)){
				screenHashMap.get(gameScreenIDToDispose).dispose();
			}
		} else {
			System.out.println("[ERROR] : SCREEN ID NOT FOUND !");
		}

	}

	public void easeOut(final int fromGSID, final int toGSID, float duration){
		float interval = 1.0f/Gdx.graphics.getFramesPerSecond();
		int nTimes = (int)(duration/interval);
		final float alphaDelta = 1.0f/nTimes;
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				screenHashMap.get(fromGSID).setScreenAlpha(screenHashMap.get(fromGSID).getScreenAlpha() - alphaDelta);
			}
		}, 0.0f, interval, nTimes);
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				setGameScreen(toGSID, fromGSID);
			}
		}, duration);
	}

	public void exit(){
		Gdx.app.exit();
	}
}
