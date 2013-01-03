package com.petrovdevelopment.killthemall.game;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.petrovdevelopment.killthemall.Background;
import com.petrovdevelopment.killthemall.GameLoader;
import com.petrovdevelopment.killthemall.GameRecoveryLoader;
import com.petrovdevelopment.killthemall.GameView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

/**
 * Game World, holding all other elements. Singleton. TODO: handle properly score, alienCount, humanCount, etc. in
 * onSaveInstanceState
 * Have in mind that the GameWorld instance is not guaranteed to be destroyed when the activity is, 
 * because it is static, so all initialization should happen in the initialize() method
 * @author andrey
 * 
 */
public class World implements GameElement {
	//KEYS used in the bundle in saveState() and in the Handler's messages
	public static final String NPCS = "npcs";
	public static final String SCORE = "score";
	public static final String TIME = "time";
	public static final String TIME_MILLIS = "time_millis";
		
	public static final String DEATH_EFFECTS = "deathEffects"; // Ignore for now
	public static final String BIRTH_EFFECTS = "birthEffects"; // Ignore for now

	public static final Long GAME_DURATION_MILISECONDS = 15000l;
	public static final int GAME_DURATION_SECONDS = 15;
	public static final int INITIAL_SCORE = 0;
	
	public static World mInstance;

	//TODO check if those initializations are ok, since they are not resetting on a new game
	private GameState mGameState = GameState.PAUSED;
	private GameEndReason mGameEndReason = GameEndReason.NONE;
	
	private Difficulty mDifficulty = Difficulty.MEDIUM; // Ignore for now
	
	
	public enum GameState {
		RUNNING, PAUSED, END
	}
	
	public enum GameEndReason {
		NONE, TIME_IS_UP, ALL_ALIENS_DEAD, ALL_HUMEN_DEAD
	}
	
	public enum Difficulty {
		EASY, MEDIUM, HARD
	}

	
	
	
	private Handler mScoreHandler;

	private int mScore;
	private int mAlienCount;
	private int mHumanCount;
	private int mTimeLeftInSeconds;
	private long mTimeLeftInMilliseconds;
	private long mCurrentTimeInMilliseconds;

	private Background mBackground;
	private List<Npc> mNpcs;
	private DeathEffectContainer mDeathEffectContainer;

	private World() {
	}

	public static World getInstance() {
		if (mInstance == null) {
			mInstance = new World();
		}
		return mInstance;
	}

	public void initialize(GameView gameView, Bundle savedInstanceState, Handler scoreHandler) {
		mScoreHandler = scoreHandler;
		GameLoader gameLoader;
		if (savedInstanceState == null) {
			mScore = INITIAL_SCORE;
			mTimeLeftInSeconds = GAME_DURATION_SECONDS;
			mTimeLeftInMilliseconds = GAME_DURATION_MILISECONDS;			
			gameLoader = new GameLoader(gameView);
		} else {
			mScore = savedInstanceState.getInt(SCORE);
			mTimeLeftInSeconds =  savedInstanceState.getInt(TIME);
			mTimeLeftInMilliseconds = savedInstanceState.getLong(TIME_MILLIS);	
			gameLoader = new GameRecoveryLoader(gameView, savedInstanceState);
		}
		mBackground = gameLoader.loadBackground();
		mNpcs = gameLoader.loadNpcs();
		mDeathEffectContainer = gameLoader.loadDeathEffectContainer();

		// mScore = gameLoader.loadScore();
		// mTimeLeftInMilliseconds = gameLoader.loadTimeLeft();

		setAlienAndHumanCount();

	}

	private void setAlienAndHumanCount() {
		mAlienCount = 0;
		mHumanCount = 0;
		for (Npc npc : mNpcs) {
			if (npc.getNpcType().isAlien()) {
				mAlienCount++;
			} else {
				mHumanCount++;
			}
		}
	}

	/**
	 * Update all game elements. Consider update methods execution dependencies. Currently there are none.
	 */
	@Override
	public void update() {
		if (getGameState() == GameState.RUNNING) {
			updateTime();
			mBackground.update();
			mDeathEffectContainer.updateAll();
			for (Npc npc : mNpcs) {
				npc.update();
			}
		}
	}

	/**
	 * Render all game elements. Order of rendering affects the order on the Z axis. First things rendered are on the back.
	 * Last things rendered are on the front.
	 */
	@Override
	public void render(Canvas canvas) {
		mBackground.render(canvas);
		mDeathEffectContainer.renderAll(canvas);
		for (Npc npc : mNpcs) {
			npc.render(canvas);
		}
	}

	/**
	 * On touch remove top touched npc and create a death effect on the touched spot. Trigger GameState.END if the last Alien
	 * was killed TODO rework this to put the death effectin the middle of the npc sprite, not on the touch coordinates
	 * 
	 * @param touchX
	 * @param touchY
	 */
	public void onTouchEvent(float touchX, float touchY) {
		if (mGameState == GameState.RUNNING) {
			// TODO: rework this with using Iterator for safe removal
			for (int i = mNpcs.size() - 1; i >= 0; i--) {
				Npc npc = mNpcs.get(i);
				if (npc.isTouched(touchX, touchY)) {
					npc.touch(); // does nothing currently

					// no need this class to keep track of the created deathEffects.
					// the class DeathEffects itself does that
					boolean isAlien = npc.getNpcType().isAlien();
					updateScore(isAlien);
					mDeathEffectContainer.createDeathEffect(touchX, touchY, isAlien);
					// remove the Npc from the list (kill it)
					mNpcs.remove(i);
					// kill only the top most NPC, i.e. only the last one by not checking the rest of the list
					break;
				}
			}
			if (mAlienCount <= 0) {
				end(GameEndReason.ALL_ALIENS_DEAD);
			} else if (mHumanCount <= 0) {
				end(GameEndReason.ALL_HUMEN_DEAD);
			}
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
	 * Calculates the game score
	 * 
	 * @param isAlien
	 */
	private void updateScore(boolean isAlien) {
		// Update the score
		if (isAlien) {
			mScore += 10; // plus 10 points for killing an alien
			mAlienCount--;
		} else {
			mScore -= 5; // minus 5 points for killing a human
			mHumanCount--;
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

		Parcelable[] npcs = new Parcelable[mNpcs.size()];
		for (int i = 0; i < mNpcs.size(); i++) {
			npcs[i] = mNpcs.get(i);
		}
		outState.putParcelableArray(NPCS, npcs);
		outState.putInt(SCORE, mScore);
		outState.putInt(TIME, mTimeLeftInSeconds);
		outState.putLong(TIME_MILLIS, mTimeLeftInMilliseconds);
	}

	/**
	 * Make sure to empty the death effects list
	 */
	public void onDestroy() {
		mDeathEffectContainer.removeAll();
	}

	public void resume() {
		setGameState(GameState.RUNNING);
		// set the time of the last restart, based on it you will be calculating the elapsed time
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
	

	public int getTime() {
		return mTimeLeftInSeconds;
	}

	public long getTimeInMilliseconds() {
		return mTimeLeftInMilliseconds;
	}
}
