package com.petrovdevelopment.squashsquash;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.petrovdevelopment.squashsquash.fragments.ConfirmDialog;
import com.petrovdevelopment.squashsquash.game.World;
import com.petrovdevelopment.squashsquash.game.World.TouchedElement;
import com.petrovdevelopment.squashsquash.sound.MediaClientActivity;
import com.petrovdevelopment.squashsquash.sound.SoundEffectsManager.SoundEffect;
import com.petrovdevelopment.squashsquash.utils.TextManager;
import com.petrovdevelopment.squashsquash.utils.U;

/**
 * The game activity. It creates also the GameLoopThread and the game World. In two cases it calls the World - when the world
 * is initialized and on touch events In the first case the surface view is passed after it was created, which allows the
 * world to calculate the game element dimensions. In the second case any touch event is passed to be handled by the world.
 * For any other purpose the World is accessed only by the GameLoopThread
 * 
 * @author andrey
 * 
 */
public class GameActivity extends MediaClientActivity implements Callback {

	private GameView mGameView;
	private World mWorld;
	private GameLoopThread mGameLoopThread;

	private ImageButton mPlayButton;
	private ImageButton mPauseButton;
	private ImageView mExitButton;

	private Bundle mSavedInstanceState = null;
	private Handler mScoreHandler;

	private TextView mScoreValueTextView;
	private TextView mTimeValueTextView;

	private int mCurrentTime; // in seconds
	private int mCurrentScore;

	private boolean mGameOver = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout(R.layout.activity_game);

		mGameView = (GameView) findViewById(R.id.game_view);
		mPlayButton = (ImageButton) findViewById(R.id.play);
		mPauseButton = (ImageButton) findViewById(R.id.pause);
		mExitButton = (ImageView) findViewById(R.id.exit);
		mScoreValueTextView = (TextView) findViewById(R.id.scoreValue);
		mTimeValueTextView = (TextView) findViewById(R.id.timeValue);


		mSavedInstanceState = savedInstanceState;

		// Setting up the music and the sound button is required to be able to properly initialize it in the onStart();
		initializeMediaButtons((ImageView) findViewById(R.id.sfx), (ImageView) findViewById(R.id.music));
		
		
		// For Debugging only:
		if (savedInstanceState == null) {
			// we were just launched: set up a new game

		} else {
			// we are being restored: resume a previous game
		}
	}

	/**
	 * Set the layout and its text views with the proper font
	 * 
	 * @param gameEndReason
	 */
	private void setLayout(int layoutId) {
		View layout = getLayoutInflater().inflate(layoutId, null);
		Typeface customFont = ((MainApplication) getApplication()).getTextManager().getCustomFont();
		U.setCustomFont(layout, customFont, TextManager.FONT_SIZE);
		// Set the activity's layout
		setContentView(layout);
	}

	/**
	 * Called when mGameView has already been loaded so its size is known
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		initializeHandler();
		mWorld = World.createWorld(mGameView, mSavedInstanceState, mScoreHandler);

		// initialize time and score
		mCurrentTime = mWorld.getTime();
		mCurrentScore = mWorld.getScore();
		updateTimeAndScoreViews();

		mGameLoopThread = new GameLoopThread(mGameView, mWorld, this);

		// Start the game thread
		mGameLoopThread.setRunning(true);
		mGameLoopThread.start();
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
	}

	/**
	 * Create a handler that will be working on the application's main thread (the UI thread) and update the score and time.
	 * The score and time updates will be coming in separate messages but via the same handler, Assign the new values or keep
	 * the old if no new vales are coming in the message. TODO: add time manipulation
	 */
	private void initializeHandler() {
		// TODO Does it matter if this handler is static or no?
		mScoreHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				// assign the new values or keep the old if no new vales are coming in the message
				mCurrentTime = msg.getData().getInt(World.TIME, mCurrentTime);
				mCurrentScore = msg.getData().getInt(World.SCORE, mCurrentScore);
				updateTimeAndScoreViews();
			}
		};

	}

	private void updateTimeAndScoreViews() {
		mScoreValueTextView.setText(Integer.toString(mCurrentScore));
		((MainApplication) getApplication()).getTextManager().setTimeLeft(mTimeValueTextView, mCurrentTime);
	}

	/**
	 * Method called by the underlying GameView
	 * 
	 * @param touchX
	 * @param touchY
	 */
	public void onGameViewTouchEvent(float touchX, float touchY) {
		TouchedElement touchedElement = mWorld.onTouchEvent(touchX, touchY);
		switch (touchedElement) {
		case DEMON:
			// make demon sound
			getMediaService().playSound(SoundEffect.DEATH_DEMON);
			break;
		case HUMAN:
			// make human sound
			getMediaService().playSound(SoundEffect.DEATH_HUMAN);
			break;
		case NONE:
			// do nothing
			break;
		default: // ignore
		}
	}

	/**
	 * Called when the Play ImageButton is clicked
	 */
	public void onClickPlay(View view) {
		view.setVisibility(View.GONE);
		setMusicButtonInvisible();
		setSfxButtonInvisible();

		mExitButton.setVisibility(View.GONE);
		mPauseButton.setVisibility(View.VISIBLE);
		mWorld.resume();
	}

	/**
	 * Called when the Pause ImageButton is clicked
	 */
	public void onClickPause(View view) {
		mWorld.pause();
		view.setVisibility(View.GONE);
		mPlayButton.setVisibility(View.VISIBLE);
		setMusicButtonVisible();
		setSfxButtonVisible();
		mExitButton.setVisibility(View.VISIBLE);
	}

	/**
	 * Called when the Exit ImageButton is clicked
	 */
	public void onClickExit(View view) {
		showConfirmDialog();
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// // Do not resume immediately the game.
	// // Instead resume only explicitly with the Play button
	// // This guarantees better user experience.
	// }

	@Override
	protected void onPause() {
		super.onPause();
		// Simulate pause click if the game is not over.
		// If the game is over, the activity will soon be destroyed, so no need to show pause state buttons
		if (!mGameOver) {
			mPauseButton.performClick();
		} else {
			// if this is not called there will be a memory leak.
			// TODO Should check my world class to figure out why?
			mWorld.pause();
		}
	}

	/**
	 * Notification that something is about to happen, to give the Activity a chance to save state. TODO: persist score, time,
	 * music, etc on storage?
	 * 
	 * @param outState
	 *            a Bundle into which this Activity should save its state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// just have the View's thread save its state into our Bundle
		super.onSaveInstanceState(outState);
		mWorld.saveState(outState);
	}

	/**
	 * Finish the current activity and start the End Game activity. This method get executed on the GameLoop thread.
	 */
	public void onGameEnd() {
		mGameOver = true;
		Intent intent = new Intent(this, EndActivity.class);
		String gameEndReason = mWorld.getGameEndReason().toString();
		int score = mWorld.getScore();
		int time = mWorld.getTime();
		int timePassed = mWorld.getTimePassed();

		intent.putExtra(World.GAME_END_REASON, gameEndReason);
		intent.putExtra(World.SCORE, score);
		intent.putExtra(World.TIME, time);
		intent.putExtra(World.TIME_PASSED, timePassed);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mPauseButton.performClick(); // simulate pause press
			showConfirmDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Show a dialog asking the user to confirm if they want to leave the game
	 */
	private void showConfirmDialog() {
		ConfirmDialog confirmDialog = new ConfirmDialog();
		confirmDialog.show(getFragmentManager(), MainApplication.DIALOG);
	}
}
