package com.petrovdevelopment.squashsquash;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.petrovdevelopment.squashsquash.R;
import com.petrovdevelopment.squashsquash.game.Background;
import com.petrovdevelopment.squashsquash.game.DeathEffectContainer;
import com.petrovdevelopment.squashsquash.game.NpcContainer;
import com.petrovdevelopment.squashsquash.game.NpcType;

/**
 * Class responsible for loading of all resources.
 * Bitmaps, sounds, etc.
 * TODO: add progress bar while loading?
 * @author andrey
 *
 */
public class GameLoader {
	protected GameView mGameView;
	public GameLoader(GameView gameView) {
		mGameView = gameView;
	}

	public Background loadBackground(){
		return new Background(mGameView.getResources(), R.drawable.wall_bg, mGameView.getWidth(), mGameView.getHeight());
	}

	public NpcContainer loadNpcContainer() {
		NpcContainer npcContainer = NpcContainer.create(mGameView);
		//Create one NPC from every possible type
		for (NpcType npcType : NpcType.values()) {
			npcContainer.createNpc(npcType);
		}
		Log.i(this.getClass().getSimpleName(), "loadNpcContainer() called");
		return npcContainer;
	}

	public DeathEffectContainer loadDeathEffectContainer() {
		Bitmap bitmapBloodAlien = BitmapFactory.decodeResource(mGameView.getResources(), R.drawable.blood_alien);
		Bitmap bitmapBloodHuman = BitmapFactory.decodeResource(mGameView.getResources(), R.drawable.blood_human);
		DeathEffectContainer deathEffectContainer = DeathEffectContainer.create(mGameView, bitmapBloodAlien, bitmapBloodHuman);
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