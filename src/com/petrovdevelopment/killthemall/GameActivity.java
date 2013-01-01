package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * TODO: add ETC1 compression to the images if using OpenGl TODO: In this branch build the Activities and menus and all extra
 * things around the maing game TODO: Add scoring, begin and end activities TODO: fix the score, time, exit, (pause/resume)
 * bar TODO: play button should be in the middle after pause is pressed It is important to recompile the activity if its
 * layout xml has changed
 * 
 * @author andrey
 * 
 */
public class GameActivity extends Activity {
	private static final String ASSETS_FONT_LOCATION = "fonts/ObelixPro-cyr.ttf";

	private GameView mGameView;
	private World mWorld;
	private GameLoopThread mGameLoopThread;
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;
	private Bundle mSavedInstanceState = null;
	private Handler mScoreHandler;
	private TextView mScoreTextTitleView;
	private TextView mScoreTextValueView;

	private TextView mTimeTextTitleView;
	private TextView mTimeTextValueView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		mGameView = (GameView) findViewById(R.id.game_view);
		mPlayButton = (ImageButton) findViewById(R.id.play);
		mPauseButton = (ImageButton) findViewById(R.id.pause);
		mWorld = World.getInstance();
		mSavedInstanceState = savedInstanceState;

		initializeTextFields();

		// For Debugging only:
		if (savedInstanceState == null) {
			// we were just launched: set up a new game
			Log.i(this.getClass().getSimpleName(), "SIS is null");

		} else {
			// we are being restored: resume a previous game
			Log.i(this.getClass().getSimpleName(), "SIS is nonnull");
		}
	}

	/**
	 * Get text fields references and use custom font for the fields
	 */
	private void initializeTextFields() {
		mTimeTextTitleView = (TextView) findViewById(R.id.timeTitle);
		mTimeTextValueView = (TextView) findViewById(R.id.timeValue);
		mScoreTextTitleView = (TextView) findViewById(R.id.scoreTitle);
		mScoreTextValueView = (TextView) findViewById(R.id.scoreValue);

		Typeface font = Typeface.createFromAsset(getAssets(), ASSETS_FONT_LOCATION);
		mTimeTextTitleView.setTypeface(font);
		mTimeTextValueView.setTypeface(font);
		mScoreTextTitleView.setTypeface(font);
		mScoreTextValueView.setTypeface(font);

	}

	/**
	 * Called when mGameView has already been loaded so its size is known
	 */
	public void onGameViewSurfaceCreated() {
		initializeHandler();
		mWorld.initialize(mGameView, mSavedInstanceState, mScoreHandler);
		mGameLoopThread = new GameLoopThread(mGameView, mWorld, this);

		// Start the actual game thread
		mGameLoopThread.setRunning(true);
		mGameLoopThread.start();
		Log.i(this.getClass().getSimpleName(), "mGameLooThread started " + mGameLoopThread);
	}

	/**
	 * Create a handler that will be working on the application's main thread (the UI thread) and update the score and time
	 * TODO: add time manipulation
	 */
	private void initializeHandler() {
		// TODO Does it matter if this handler is static or no?
		mScoreHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				mScoreTextValueView.setText(Integer.toString(msg.getData().getInt(World.SCORE)));
			}
		};

	}

	/**
	 * Method called by the underlying GameView
	 * 
	 * @param touchX
	 * @param touchY
	 */
	public void onGameViewTouchEvent(float touchX, float touchY) {
		mWorld.onTouchEvent(touchX, touchY);
	}

	/**
	 * Stop the thread and release world resources;
	 */
	public void onGameViewSurfaceDestroyed() {
		boolean retry = true;
		mGameLoopThread.setRunning(false);
		while (retry) {
			try {
				mGameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		mWorld.onDestroy();
		Log.i(this.getClass().getSimpleName(), "Surface destroyed");

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
		// Do not resume immediately the game. 
		// Instead resume only explicitly with the Play button
		// This guarantees better user experience.
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPauseButton.setVisibility(View.GONE);
		mPlayButton.setVisibility(View.VISIBLE);
		mGameLoopThread.doPause();
	}

	/**
	 * Notification that something is about to happen, to give the Activity a chance to save state. TODO: persist score on
	 * storage?
	 * 
	 * @param outState
	 *            a Bundle into which this Activity should save its state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// just have the View's thread save its state into our Bundle
		super.onSaveInstanceState(outState);
		mWorld.saveState(outState);
		Log.i(this.getClass().getSimpleName(), "SIS called");
	}

	/**
	 * Finish the current activity and start the End Game activity. This method executes on the GameLoop thread.
	 */
	public void onGameEnd() {
		Intent intent = new Intent(this, EndGameActivity.class);
		startActivity(intent);
		finish();
	}
}
