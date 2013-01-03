package com.petrovdevelopment.killthemall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.petrovdevelopment.killthemall.game.Npc;
import com.petrovdevelopment.killthemall.game.World;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

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
	public List<Npc> loadNpcs() {
		List<Npc> npcsList = new ArrayList<Npc>();
		
		Log.i(this.getClass().getSimpleName(), "mGameView.width:" + mGameView.getWidth());
		Log.i(this.getClass().getSimpleName(), "mGameView.height:" + mGameView.getHeight());
		
		
		Parcelable[] npcsArray = mSavedInstanceState.getParcelableArray(World.NPCS);
		for (int i = 0; i<npcsArray.length; i++) {
			//mX = random.nextInt(mParentView.getWidth() - mWidth);
			//mY = random.nextInt(mParentView.getHeight() - mHeight);
			
			Random random = new Random();
			Npc npc = (Npc) npcsArray[i];
			
			//Make sure the X and Y of the sprites are inside the parent view
			//FIXME: there is an issue because of the continuing rendering on change configuration
			//this makes the View to be restored with sprites out of the edges. 
			//To fix it, rendering need to stop before saving the instance?
			if (npc.getX()> mGameView.getWidth() - npc.getWidth()) {
				System.out.println(npc.getNpcType() + "has X:" + npc.getX() + ", which is out of Width bound " + mGameView.getWidth());
				npc.setX(random.nextInt(mGameView.getWidth() - npc.getWidth()));
			}
			if (npc.getY() > mGameView.getHeight() - npc.getHeight()) {
				
				System.out.println(npc.getNpcType() + "has Y:"+ npc.getY()+", which is out of Height bound " + mGameView.getHeight());
				npc.setY(random.nextInt(mGameView.getHeight() - npc.getHeight()));
			}
			
			npc.setParentViewAndInitialize(mGameView);
			npcsList.add(npc);
			
		}
		return npcsList;
	}
}
