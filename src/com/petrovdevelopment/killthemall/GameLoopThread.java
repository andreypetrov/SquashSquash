package com.petrovdevelopment.killthemall;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {
	private GameView gameView;
	private boolean running = false;
	private static final long FPS = 25;
	private static final long TICKS_PS = 1000/FPS;
	
	public GameLoopThread(GameView gameView){
		this.gameView = gameView;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public void run() {
		while(running) {
			long startTime;
			
			long sleepTime;
			
			Canvas canvas = null;
			startTime = System.currentTimeMillis();
			
			//TODO create method render, wrapper of the onDraw
			//Update and onDraw should be synchronized to happen after each other!!!
			
			//First update
			gameView.update();
			
			//Then draw
			try {
				canvas = gameView.getHolder().lockCanvas();
				synchronized (gameView.getHolder()) {
					gameView.onDraw(canvas);
				}
			} finally {
				if(canvas != null) {
					gameView.getHolder().unlockCanvasAndPost(canvas);
				}
			}
			
			sleepTime = TICKS_PS -(System.currentTimeMillis()-startTime);
			try {
				if (sleepTime>0) { //slow down if it is too fast
					sleep(sleepTime);
				}
			} catch (Exception e) {
				//do nothing
			}
		}
	}
	
}
