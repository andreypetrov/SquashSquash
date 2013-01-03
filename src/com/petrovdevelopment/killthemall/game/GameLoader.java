package com.petrovdevelopment.killthemall.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.petrovdevelopment.killthemall.Background;
import com.petrovdevelopment.killthemall.GameView;
import com.petrovdevelopment.killthemall.R;

/**
 * Class responsible for loading of all resources.
 * Bitmaps, sounds, etc.
 * TODO: add progress bar while loading?
 * @author andrey
 *
 */
public class GameLoader {
	private GameView mGameView;
	public GameLoader(GameView gameView) {
		mGameView = gameView;
	}

	public Background loadBackground(){
		return new Background(mGameView.getResources(), R.drawable.back, mGameView.getWidth(), mGameView.getHeight());
	}

	public NpcContainer loadNpcContainer() {
		NpcContainer npcContainer = new NpcContainer(mGameView);
		//Create one NPC from every possible type
		for (NpcType npcType : NpcType.values()) {
			npcContainer.createNpc(npcType);
		}
		return npcContainer;
	}

	public DeathEffectContainer loadDeathEffectContainer() {
		Bitmap bitmapBloodAlien = BitmapFactory.decodeResource(mGameView.getResources(), R.drawable.blood_alien);
		Bitmap bitmapBloodHuman = BitmapFactory.decodeResource(mGameView.getResources(), R.drawable.blood_human);
		DeathEffectContainer deathEffectContainer = DeathEffectContainer.getInstance();
		deathEffectContainer.initialize(mGameView, bitmapBloodAlien, bitmapBloodHuman);	
		return deathEffectContainer;
	}

	public int loadScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long loadTimeLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

}
