package com.petrovdevelopment.killthemall;

import android.os.Bundle;

import com.petrovdevelopment.killthemall.game.GameLoader;
import com.petrovdevelopment.killthemall.game.NpcContainer;

/**
 * Recovers npc positions from previous state. 
 * Also used the GameLoader implementation to load the rest of the resources
 * @author andrey
 *
 */
public class GameRecoveryLoader extends GameLoader {

	Bundle mSavedInstanceState;
	GameView mGameView;
	
	public GameRecoveryLoader(GameView gameView, Bundle savedInstanceState) {
		super(gameView);
		mSavedInstanceState = savedInstanceState;
		mGameView = gameView;
	}

	/**
	 * Recover npcs from a previous state. 
	 * If they are out of the parent view bounds, put them back inside.
	 */
	@Override
	public NpcContainer loadNpcContainer() {
		NpcContainer npcContainer = new NpcContainer(mGameView);
		npcContainer.restoreState(mSavedInstanceState);
		return npcContainer;
	}
}
