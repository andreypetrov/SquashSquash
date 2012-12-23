package com.petrovdevelopment.killthemall;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

/**
 * Add scoring, begin and end activities
 * TODO: put the gameLoopThread to be started by the activity not by the gameView
 * Pause it onPause and stop it onStop! etc.
 * @author andrey
 *
 */
public class MainActivity extends Activity {
	
	//private GameLoopThread mGameLoopThread;
	private GameView mGameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//load GameView
		mGameView = new GameView(this);
		setContentView(mGameView);
		//mGameLoopThread = new GameLoopThread(mGameView);
	
	}
	
//	public void startGameLoop() {
//		System.out.println("Game loop started");
//		mGameLoopThread.setRunning(true);
//		mGameLoopThread.start();	
//	}
	
	//gameLoopThread.setRunning(true);
	//gameLoopThread.start();
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
}
