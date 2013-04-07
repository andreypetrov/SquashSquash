package com.petrovdevelopment.squashsquash;

import android.content.Context;
import android.os.Bundle;

import com.petrovdevelopment.squashsquash.game.NpcContainer;

/**
 * Recovers npc positions from previous state. 
 * Also used the GameLoader implementation to load the rest of the resources
 * @author andrey
 *
 */
public class GameRecoveryLoader extends GameLoader {

	Bundle mSavedInstanceState;
	
	public GameRecoveryLoader(Context context, GameView gameView, Bundle savedInstanceState) {
		super(context, gameView);
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
