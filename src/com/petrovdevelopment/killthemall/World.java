package com.petrovdevelopment.killthemall;

import java.util.List;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

/**
 * Game World, holding all other elements. Singleton.
 * @author andrey
 * 
 */
public class World implements GameElement {
	//Parcelable KEYS used in the bundle in saveState()
	public static final String NPCS = "npcs";
	public static final String SCORE = "score";
	public static final String DEATH_EFFECTS = "deathEffects"; //Ignore for now
	public static final String BIRTH_EFFECTS = "birthEffects"; //Ignore for now
	
	
	public static World mInstance;

	private GameState mGameState = GameState.PAUSED;
	private Difficulty mDifficulty = Difficulty.MEDIUM; // Ignore for now
	
	public enum GameState {
		RUNNING, PAUSED, READY, WIN, LOSS
	}

	public enum Difficulty {
		EASY, MEDIUM, HARD
	}

	private Handler mScoreHandler;
	
	private int mScore;
	private Background mBackground;
	private List<Npc> mNpcs;

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
		if (savedInstanceState == null)  {
			mScore = 0;
			gameLoader = new GameLoader(gameView);
		} else {
			mScore = savedInstanceState.getInt(SCORE);
			gameLoader = new GameRecoveryLoader(gameView, savedInstanceState);
		}
		mBackground = gameLoader.loadBackground();
		mNpcs = gameLoader.loadNpcs();
		gameLoader.loadDeathEffect();
	}	
	
	/**
	 * Update all game elements. Consider update methods execution dependencies. Currently there are none.
	 */
	@Override
	public void update() {
		mBackground.update();
		DeathEffect.updateAll();
		for (Npc npc : mNpcs) {
			npc.update();
		}
	}

	/**
	 * Render all game elements. Order of rendering affects the order on the Z axis. First things rendered are on the back.
	 * Last things rendered are on the front.
	 */
	@Override
	public void render(Canvas canvas) {
		mBackground.render(canvas);
		DeathEffect.renderAll(canvas);

		for (Npc npc : mNpcs) {
			npc.render(canvas);
		}
	}

	/**
	 * On touch remove top touched npc and create a death effect on the touched spot TODO rework this to put the death effect
	 * in the middle of the npc sprite, not on the touch coordinates
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
					npc.touch(); //does nothing currently
					
					// no need this class to keep track of the created deathEffects.
					// the class DeathEffects itself does that
					boolean isAlien = npc.getNpcType().isAlien();	
					
					updateScoreView(isAlien);
					
					//TODO: send score update to the UI thread.
					Log.i(this.getClass().getSimpleName(), "Score: " + mScore);
					DeathEffect.create(touchX, touchY, isAlien);
					//remove the Npc from the list (kill it)
					mNpcs.remove(i);
					
					// kill only the top most NPC, i.e. only the last one by not checking the rest of the list
					break;
				}
			}
		}
	}

	/**
	 * update the score on the main UI (not sure if this is not anyways true)
	 * TODO: remove initialization from here because it is expensive to create everytime a new bundle.
	 * Better update the existing one
	 * @param isAlien
	 */
	private void updateScoreView(boolean isAlien) {
		if (isAlien) {
			mScore+=10; //10 points for killing an alien
		} else {
			mScore-=5; //-5 points for killing a human
		}
		Message message = mScoreHandler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putInt(SCORE, mScore);
		message.setData(bundle);
		mScoreHandler.sendMessage(message);
	}
	

	/**
	 * Save state of Score and NPCs. Ignore the death effects since they are too short to matter 
	 * @param outState
	 */
	public void saveState(Bundle outState) {
		
		Parcelable[] npcs = new Parcelable[mNpcs.size()];
		for (int i = 0; i < mNpcs.size(); i++) {
			npcs[i] = mNpcs.get(i);
		}
		outState.putParcelableArray(NPCS, npcs);
		outState.putInt(SCORE, mScore);
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

	public void setGameState(GameState gameState) {
		mGameState = gameState;
	}
	
	public int getScore() {
		return mScore;
	}


}
