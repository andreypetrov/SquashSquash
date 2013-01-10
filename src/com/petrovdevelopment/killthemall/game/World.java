package com.petrovdevelopment.killthemall.game;

import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.petrovdevelopment.killthemall.GameLoader;
import com.petrovdevelopment.killthemall.GameRecoveryLoader;
import com.petrovdevelopment.killthemall.GameView;

/**
 * Game World, holding all other elements.
 * There should be one game world for now. 
 * The class is not static to insure it is properly destroyed with the 
 * Activity and the GameLoopThread destroyed. 
 * TODO: extract time tracking in a separate TimeManager class
 * @author andrey
 * 
 */
public class World implements GameElement {
	// KEYS used in the bundle in saveState() and in the Handler's messages
	public static final String NPCS = "npcs";
	public static final String SCORE = "score";
	public static final String TIME = "time";
	public static final String TIME_MILLIS = "time_millis";
	public static final String TIME_PASSED = "time_passed";
	public static final String GAME_END_REASON = "game_end_reason";
	
	public static final String DEATH_EFFECTS = "deathEffects"; // Ignore for now
	public static final String BIRTH_EFFECTS = "birthEffects"; // Ignore for now

	public static final Long GAME_DURATION_MILISECONDS = 15000l;
	public static final int GAME_DURATION_SECONDS = 15;
	public static final int INITIAL_SCORE = 0;

	private static final int ALIEN_KILL_POINTS = 10; // plus 10 points for killing an alien
	private static final int HUMAN_KILL_POINTS = -5; // minus 5 points for killing a human
	

	// TODO check if those initializations are ok, since they are not resetting on a new game
	private GameState mGameState = GameState.PAUSED;
	private GameEndReason mGameEndReason = GameEndReason.NONE;

	
	
	private Difficulty mDifficulty = Difficulty.MEDIUM; // Ignore for now

	public enum GameState {
		RUNNING, PAUSED, END
	}

	public enum GameEndReason {
		NONE, TIME_IS_UP, ALL_ALIENS_DEAD, ALL_HUMANS_DEAD;
	}
	
	public enum Difficulty {
		EASY, MEDIUM, HARD
	}

	private Handler mScoreHandler;

	private int mScore;
	private int mTimeLeftInSeconds;
	private long mTimeLeftInMilliseconds;
	private long mCurrentTimeInMilliseconds;

	private Background mBackground;

	private NpcContainer mNpcContainer;
	private DeathEffectContainer mDeathEffectContainer;
	
	/**
	 * Static factory method returning a new World instance.
	 * There may be unlimited new words
	 * @param gameView
	 * @param savedInstanceState
	 * @param scoreHandler
	 * @return
	 */
	public static World createWorld(GameView gameView, Bundle savedInstanceState, Handler scoreHandler) {
		return new World(gameView, savedInstanceState, scoreHandler);
	}
	
	private World(GameView gameView, Bundle savedInstanceState, Handler scoreHandler) {
		mScoreHandler = scoreHandler;
		GameLoader gameLoader;
		if (savedInstanceState == null) {
			mScore = INITIAL_SCORE;
			mTimeLeftInSeconds = GAME_DURATION_SECONDS;
			mTimeLeftInMilliseconds = GAME_DURATION_MILISECONDS;
			gameLoader = new GameLoader(gameView);
		} else {
			mScore = savedInstanceState.getInt(SCORE);
			mTimeLeftInSeconds = savedInstanceState.getInt(TIME);
			mTimeLeftInMilliseconds = savedInstanceState.getLong(TIME_MILLIS);
			gameLoader = new GameRecoveryLoader(gameView, savedInstanceState);
		}
		mBackground = gameLoader.loadBackground();
		mNpcContainer = gameLoader.loadNpcContainer();
		mDeathEffectContainer = gameLoader.loadDeathEffectContainer();
	}


	/**
	 * Update all game elements. Consider update methods execution dependencies. Currently there are none.
	 */
	@Override
	public void update() {
		if (getGameState() == GameState.RUNNING) {
			updateTime();
			mBackground.update();
			mDeathEffectContainer.update();
			mNpcContainer.update();
		}
	}

	/**
	 * Render all game elements. Order of rendering affects the order on the Z axis. First things rendered are on the back.
	 * Last things rendered are on the front.
	 */
	@Override
	public void render(Canvas canvas) {
		mBackground.render(canvas);
		mDeathEffectContainer.render(canvas);
		mNpcContainer.render(canvas);
	}

	/**
	 * On touch remove top touched npc and create a death effect on the touched spot. Trigger GameState.END if the last Alien or Human
	 * was killed 
	 * @param touchX
	 * @param touchY
	 */
	public void onTouchEvent(float touchX, float touchY) {
		if (mGameState == GameState.RUNNING) {
			Npc touchedNpc = mNpcContainer.removeTopTouchedNpc(touchX, touchY);
			if (touchedNpc != null) {
				boolean isAlien = touchedNpc.getNpcType().isAlien();
				updateScore(isAlien);
				// Create a new death effect at the location of the killed npc
				mDeathEffectContainer.createDeathEffect(touchedNpc.getCenterX(), touchedNpc.getCenterY(), isAlien);
			}
		}
		
		// Check for game end condition
		Log.i(this.getClass().getSimpleName(), "Aliens count: " + mNpcContainer.getAlienCount());
		if (mNpcContainer.getAlienCount() <= 0) {
			end(GameEndReason.ALL_ALIENS_DEAD);
		} else if (mNpcContainer.getHumanCount() <= 0) {
			end(GameEndReason.ALL_HUMANS_DEAD);
		}

	}

	private void end(GameEndReason gameEndReason) {
		setGameState(GameState.END);
		setGameEndReason(gameEndReason);
	}

	private void updateTime() {
		long lastUpdateTime = mCurrentTimeInMilliseconds;
		mCurrentTimeInMilliseconds = System.currentTimeMillis();
		long timeElapsed = mCurrentTimeInMilliseconds - lastUpdateTime;
		mTimeLeftInMilliseconds = mTimeLeftInMilliseconds - timeElapsed;
		if (mTimeLeftInMilliseconds <= 0) {
			end(GameEndReason.TIME_IS_UP);
		}

		int newTimeLeftInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(mTimeLeftInMilliseconds);
		if (mTimeLeftInSeconds > newTimeLeftInSeconds) {
			mTimeLeftInSeconds = newTimeLeftInSeconds;
			updateTimeView();
		}
	}

	/**
	 * Updates the time view on the UI thread.
	 */
	private void updateTimeView() {
		Message message = mScoreHandler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putInt(TIME, mTimeLeftInSeconds);
		message.setData(bundle);
		mScoreHandler.sendMessage(message);
	}

	/**
	 * Calculates the game score, based on the type of the killed Npc
	 * 
	 * @param isAlien
	 */
	private void updateScore(boolean isAlien) {
		// Update the score
		if (isAlien) {
			mScore += ALIEN_KILL_POINTS;
		} else {
			mScore += HUMAN_KILL_POINTS; 
		}
		updateScoreView();
	}

	/**
	 * updates the score view on the UI thread. TODO: remove initialization from here. It is expensive to create every time a
	 * new bundle. Better update the existing one
	 */
	private void updateScoreView() {
		// Send a message to the UI thread to update the Score View
		Message message = mScoreHandler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putInt(SCORE, mScore);
		message.setData(bundle);
		mScoreHandler.sendMessage(message);
	}

	/**
	 * Save state of Score and NPCs. Ignore the death effects since they are too short to matter
	 * 
	 * @param outState
	 */
	public void saveState(Bundle outState) {
		mNpcContainer.saveState(outState);
		outState.putInt(SCORE, mScore);
		outState.putInt(TIME, mTimeLeftInSeconds);
		outState.putLong(TIME_MILLIS, mTimeLeftInMilliseconds);
	}

	public void resume() {
		setGameState(GameState.RUNNING);
		// set the time of the current restart, based on it you will be calculating the elapsed time
		mCurrentTimeInMilliseconds = System.currentTimeMillis();
	}

	public void pause() {
		setGameState(GameState.PAUSED);
	}

	//
	// GETTERS AND SETTERS
	//

	public Difficulty getDifficulty() {
		return mDifficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		mDifficulty = difficulty;
	}

	public GameState getGameState() {
		return mGameState;
	}

	private void setGameState(GameState gameState) {
		mGameState = gameState;
	}

	private void setGameEndReason(GameEndReason gameEndReason) {
		mGameEndReason = gameEndReason;
	}

	public GameEndReason getGameEndReason() {
		return mGameEndReason;
	}

	public int getScore() {
		return mScore;
	}

	/**
	 * Time left in seconds
	 * @return
	 */
	public int getTime() {
		return mTimeLeftInSeconds;
	}
	
	/**
	 * Time left in milliseconds
	 * @return
	 */
	public long getTimeInMilliseconds() {
		return mTimeLeftInMilliseconds;
	}
	
	/**
	 * Time passed since the beginning of the game
	 * @return
	 */
	public int getTimePassed() {
		return World.GAME_DURATION_SECONDS - mTimeLeftInSeconds;
	}

}
