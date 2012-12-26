package com.petrovdevelopment.killthemall;

import java.util.List;
import android.graphics.Canvas;

/**
 * Game World, holding all other elements. Singleton.
 * 
 * @author andrey
 * 
 */
public class World implements GameElement {
	public static World mInstance;

	private GameState mGameState;	
	private Difficulty mDifficulty; //Ignore for now

	public enum GameState {
		RUNNING, PAUSE, READY, WIN, LOSS
	}

	public enum Difficulty {
		EASY, MEDIUM, HARD
	}

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

	public void initialize(GameLoader gameLoader) {
		mBackground = gameLoader.loadBackground();
		mNpcs = gameLoader.loadNpcs();
		gameLoader.loadDeathEffect();
		setGameState(GameState.PAUSE);
		setDifficulty(Difficulty.MEDIUM);
	}

	/**
	 * Update all game elements. Consider update dependencies. Currently there are none.
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
	 * On touch remove top touched npc and create a death effect on the touched spot
	 * TODO rework this to put the death effect in the middle of the npc sprite, not on the touch coordinates
	 * @param touchX
	 * @param touchY
	 */
	public void onTouchEvent(float touchX, float touchY) {
		// TODO: rework this with using Iterator for safe removal
		for (int i = mNpcs.size() - 1; i >= 0; i--) {
			Npc npc = mNpcs.get(i);
			if (npc.isTouched(touchX, touchY)) {
				npc.touch();
				mNpcs.remove(i);
				// no need this class to keep track of the created deathEffects.
				// the class DeathEffects itself does that
				DeathEffect.create(touchX, touchY);

				// kill only the last (top most) NPC, i.e. only the last one
				break;
			}
		}
	}

	//
	// GETTERS AND SETTERS
	//

	public Difficulty getDifficulty() {
		return mDifficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.mDifficulty = difficulty;
	}

	public GameState getGameState() {
		return mGameState;
	}

	public void setGameState(GameState gameState) {
		this.mGameState = gameState;
	}

}
