package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

/**
 * TODO: use darker icons for the ActionBar TODO: add overflow icon to the ActionBar TODO: In this branch build the Activities
 * and menus and all extra things around the maing game TODO: Add scoring, begin and end activities TODO: Pause it onPause and
 * stop it onStop! etc. Use LunarLander to get the ideas from
 * 
 * @author andrey
 * 
 */
public class GameActivity extends Activity {

	private GameLoopThread mGameLoopThread;
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();

		setContentView(R.layout.activity_main);
		GameView gameView = (GameView) findViewById(R.id.game_view);
		mGameLoopThread = gameView.getGameLoopThread();
		mPlayButton = (ImageButton) findViewById(R.id.play);
		mPauseButton = (ImageButton) findViewById(R.id.pause);

		if (savedInstanceState == null) {
			// we were just launched: set up a new game
			Log.w(this.getClass().getName(), "SIS is null");
		} else {
			// we are being restored: resume a previous game
			mGameLoopThread.restoreState(savedInstanceState);
			Log.w(this.getClass().getName(), "SIS is nonnull");
		}
	}

	/**
	 * Called when the Play ImageButton is clicked
	 */
	public void onClickPlay(View view) {
		view.setVisibility(View.GONE);
		mPauseButton.setVisibility(View.VISIBLE);
		mGameLoopThread.doResume();
	}

	/**
	 * Called when the Pause ImageButton is clicked
	 */
	public void onClickPause(View view) {
		view.setVisibility(View.GONE);
		mPlayButton.setVisibility(View.VISIBLE);
		mGameLoopThread.doPause();
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
		//Do not resume immediately the game. Instead only explicitly with the Play button
		//This guarantees better user experience.
		
		//mGameLoopThread.doResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPauseButton.setVisibility(View.GONE);
		mPlayButton.setVisibility(View.VISIBLE);
		mGameLoopThread.doPause();
	}

	/**
	 * Notification that something is about to happen, to give the Activity a chance to save state.
	 * 
	 * @param outState
	 *            a Bundle into which this Activity should save its state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// just have the View's thread save its state into our Bundle
		super.onSaveInstanceState(outState);
		mGameLoopThread.saveState(outState);
		Log.w(this.getClass().getName(), "SIS called");
	}
}
