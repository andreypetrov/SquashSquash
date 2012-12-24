package com.petrovdevelopment.killthemall;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * TODO: add overflow icon to the ActionBar
 * TODO: In this branch build the Activities and menus and all extra things around the maing game
 * TODO: Add scoring, begin and end activities
 * TODO: Pause it onPause and stop it onStop! etc. Use LunarLander to get the ideas from
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
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//load GameView
		mGameView = new GameView(this);
		setContentView(mGameView);
		//mGameLoopThread = new GameLoopThread(mGameView);
	
	}
	
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
