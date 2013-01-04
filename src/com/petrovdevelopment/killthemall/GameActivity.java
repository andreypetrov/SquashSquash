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
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.petrovdevelopment.killthemall.game.World;

/**
 * The game activity.
 * It creates also the GameLoopThread and the game World.
 * In two cases  it calls the World - when the world is initialized and on touch events
 * In the first case the surface view is passed after it was  created, 
 * which allows the world to calculate the game element dimensions properly.
 * In the second case any touch event is passed to be handled by the world.
 * For any other purpose the World is accessed only by the GameLoopThread
 * 
 * @author andrey
 * 
 */
public class GameActivity extends Activity  implements Callback {
	private static final String ASSETS_FONT_LOCATION = "fonts/ObelixPro-cyr.ttf";

	private GameView mGameView;
	private World mWorld;
	private GameLoopThread mGameLoopThread;
	
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;
	private ImageView mExitButton;
	private ImageView mSoundButton;
	
	private Bundle mSavedInstanceState = null;
	private Handler mScoreHandler;
	
	private TextView mScoreTextTitleView;
	private TextView mScoreTextValueView;
	private TextView mTimeTextTitleView;
	private TextView mTimeTextValueView;
	
	private int mCurrentTime; //in seconds
	private int mCurrentScore;
	
	//TODO: put this to preferences file to enable/disable sound permanently between games
	private boolean hasSound = true; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		mGameView = (GameView) findViewById(R.id.game_view);
		mPlayButton = (ImageButton) findViewById(R.id.play);
		mPauseButton = (ImageButton) findViewById(R.id.pause);
		mExitButton = (ImageView) findViewById(R.id.exit);
		mSoundButton = (ImageView) findViewById(R.id.sound);
		
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
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		initializeHandler();
		mWorld = World.createWorld(mGameView, mSavedInstanceState, mScoreHandler);
		
		//initialize time and score 
		mCurrentTime = mWorld.getTime(); 
		mCurrentScore = mWorld.getScore(); 
		updateTimeAndScoreViews();
		
		mGameLoopThread = new GameLoopThread(mGameView, mWorld, this);

		// Start the actual game thread
		mGameLoopThread.setRunning(true);
		mGameLoopThread.start();
		Log.i(this.getClass().getSimpleName(), "mGameLooThread started " + mGameLoopThread);
	}

	
	/**
	 * Called when mGameView resizes
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);		
		System.out.println("mWidth: " + mGameView.getWidth());
		System.out.println("mHeght: " + mGameView.getHeight());
	}
	
	/**
	 * Stop the thread and release world resources;
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
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
	 * Create a handler that will be working on the application's main thread (the UI thread) 
	 * and update the score and time.
	 * The score and time updates will be coming in separate messages but via the same handler, 
	 * Assign the new values or keep the old if no new vales are coming in the message.
	 * TODO: add time manipulation
	 */
	private void initializeHandler() {
		// TODO Does it matter if this handler is static or no?
		mScoreHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {				
				//assign the new values or keep the old if no new vales are coming in the message
				int timeValue =  msg.getData().getInt(World.TIME, mCurrentTime);
				int scoreValue = msg.getData().getInt(World.SCORE, mCurrentScore);	
				
				mCurrentTime = timeValue;
				mCurrentScore = scoreValue;			
				updateTimeAndScoreViews();
			}
		};

	}

	private void updateTimeAndScoreViews() {
		mTimeTextValueView.setText(Integer.toString(mCurrentTime));
		mScoreTextValueView.setText(Integer.toString(mCurrentScore));
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
	 * Called when the Play ImageButton is clicked
	 */
	public void onClickPlay(View view) {
		view.setVisibility(View.GONE);
		mSoundButton.setVisibility(View.GONE);
		mExitButton.setVisibility(View.GONE);
		mPauseButton.setVisibility(View.VISIBLE);
		mWorld.resume();
	}

	/**
	 * Called when the Pause ImageButton is clicked
	 */
	public void onClickPause(View view) {
		view.setVisibility(View.GONE);
		mPlayButton.setVisibility(View.VISIBLE);
		mSoundButton.setVisibility(View.VISIBLE);
		mExitButton.setVisibility(View.VISIBLE);
		mWorld.pause();
	}

	/**
	 * Called when the Exit ImageButton is clicked
	 */
	public void onClickExit(View view) {
		finish();
	}
	
	/**
	 * Called when the Sound ImageButton is clicked. Toggles sound on/off
	 */
	public void onClickSound(View view) {
		//TODO: stop and start actual sound 
		hasSound = !hasSound;
		if(hasSound){
			mSoundButton.setImageResource(R.drawable.sound_on);
		} else {
			mSoundButton.setImageResource(R.drawable.sound_off);
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		mPauseButton.setVisibility(View.GONE);
		mPlayButton.setVisibility(View.VISIBLE);
		super.onResume();
		// Do not resume immediately the game. 
		// Instead resume only explicitly with the Play button
		// This guarantees better user experience.
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWorld.pause();
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
		//TODO: pass final score and information about the reason of the game's end
		Intent intent = new Intent(this, EndGameActivity.class);
		String gameEndReason = mWorld.getGameEndReason().toString();
		intent.putExtra(World.GAME_END_REASON, gameEndReason);
		startActivity(intent);
		finish();
	}


}
