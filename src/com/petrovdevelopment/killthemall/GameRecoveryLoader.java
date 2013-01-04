package com.petrovdevelopment.killthemall;

import android.os.Bundle;

import com.petrovdevelopment.killthemall.game.NpcContainer;

/**
 * Recovers npc positions from previous state. 
 * Also used the GameLoader implementation to load the rest of the resources
 * @author andrey
 *
 */
public class GameRecoveryLoader extends GameLoader {

	Bundle mSavedInstanceState;
	
	public GameRecoveryLoader(GameView gameView, Bundle savedInstanceState) {
		super(gameView);
		mSavedInstanceState = savedInstanceState;
	}

	/**
	 * Recover NpcContainer from a previous state. 
	 */
	@Override
	public NpcContainer loadNpcContainer() {
		NpcContainer npcContainer = NpcContainer.create(mGameView);
		npcContainer.restoreState(mSavedInstanceState);
		return npcContainer;
	}
}
