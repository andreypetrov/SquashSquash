package com.petrovdevelopment.killthemall;

import com.petrovdevelopment.killthemall.World.GameState;

import android.graphics.Canvas;
import android.os.Bundle;

/**
 * Game loop thread. TODO: stop the thread before exiting the application TODO: forbid switching screen orientation to
 * landscape/portrait TODO: transfer all game state tracking in a separate singleton GameWorld, or something like that
 * landscape
 * 
 * @author andrey
 * 
 */
public class GameLoopThread extends Thread {
	private GameView gameView;
	private GameLoader mGameLoader;
	private World mWorld;
	
	
	private volatile boolean running = false;
	private static final long FPS = 25;
	private static final long TICKS_PS = 1000 / FPS;

	private GameState mGameState;


	public GameLoopThread(GameView gameView) {
		this.gameView = gameView;
		mGameLoader = new GameLoader(gameView);
		mWorld = World.getInstance();
		
		setGameState(GameState.PAUSE);
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		System.out.println(mGameState);
		
		while (running) {
			long startTime;
			long sleepTime;

			Canvas canvas = null;
			startTime = System.currentTimeMillis();

			// TODO create method render, wrapper of the onDraw
			// Update and onDraw should be synchronized to happen after each other!!!

			try {
				canvas = gameView.getHolder().lockCanvas();
				// lockCanvas will return null in the last run, when we are destroying the view
				if (canvas != null) {
					synchronized (gameView.getHolder()) {	
						//TODO: there is some error if i put onDraw in the if statement. Even worse if i put more things in it.
						//It has something to do with the non-instaneous pause of the animation.
						//Probably the Npc class is bugged somehow
						//TODO: maybe add one initial update and draw before the while loop to have a proper starting state
						if (getGameState() == GameState.RUNNING) {
							gameView.update();
							
						}	
						gameView.onDraw(canvas);
					}
				}
			} finally {
				if (canvas != null) {
					gameView.getHolder().unlockCanvasAndPost(canvas);
				}
			}

			sleepTime = TICKS_PS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0) { // slow down if it is too fast
					sleep(sleepTime);
				}
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	public void doResume() {
		setGameState(GameState.RUNNING);
	}

	public void doPause() {
		setGameState(GameState.PAUSE);
	}

	public void saveState(Bundle outState) {
		// TODO Auto-generated method stub

	}

	public void setGameState(GameState gameState) {
		mGameState = gameState;
	}

	public GameState getGameState() {
		return mGameState;
	}

	public void restoreState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

}
