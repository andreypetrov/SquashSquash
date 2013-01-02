package com.petrovdevelopment.killthemall;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;

import com.petrovdevelopment.killthemall.World.GameState;

/**
 * Game loop thread. TODO: stop the thread before exiting the application TODO: forbid switching screen orientation to
 * landscape/portrait
 * 
 * @author andrey
 * 
 */
public class GameLoopThread extends Thread {
	private GameActivity mGameActivity;
	private GameView mGameView;
	private World mWorld;
	
	
	private volatile boolean running = false;
	private static final long FPS = 25;
	private static final long TICKS_PS = 1000 / FPS;

	public GameLoopThread(GameView gameView, World world, GameActivity gameActivity) {
		mGameView = gameView;
		mWorld = world;
		mGameActivity = gameActivity;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	//TODO add one rendering before the loop and after the initial setup
	@Override
	public void run() {	
		Log.i(this.getClass().getSimpleName(), mWorld.getGameState().toString());
		
		while (running) {
			long startTime;
			long sleepTime;

			
			Canvas canvas = null;
			startTime = System.currentTimeMillis();

			// Update and render should be synchronized to happen after each other!
			try {
				canvas = mGameView.getHolder().lockCanvas();
				// lockCanvas will return null in the last run, when we are destroying the view
				if (canvas != null) {
					synchronized (mGameView.getHolder()) {	
						//TODO: maybe add one initial update and draw before the while loop to have a proper starting state
						mWorld.update();
						mWorld.render(canvas);
					}
				}
				
			} finally {
				if (canvas != null) {
					mGameView.getHolder().unlockCanvasAndPost(canvas);
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
			
			
			
			//Finish the game 
			if(mWorld.getGameState() == GameState.END) {
				mGameActivity.onGameEnd();
			}
		}
	}


	public void saveState(Bundle outState) {
		// TODO Auto-generated method stub

	}

	public void restoreState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

}
