package com.petrovdevelopment.killthemall;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

	public List<Npc> loadNpcs() {
		List<Npc> npcs = new ArrayList<Npc>();
		npcs.add(createNpc(R.drawable.bad1));
		npcs.add(createNpc(R.drawable.bad2));
		npcs.add(createNpc(R.drawable.bad3));
		npcs.add(createNpc(R.drawable.bad4));
		npcs.add(createNpc(R.drawable.bad5));
		npcs.add(createNpc(R.drawable.bad6));
		npcs.add(createNpc(R.drawable.good1));
		npcs.add(createNpc(R.drawable.good2));
		npcs.add(createNpc(R.drawable.good3));
		npcs.add(createNpc(R.drawable.good4));
		npcs.add(createNpc(R.drawable.good5));
		npcs.add(createNpc(R.drawable.good6));
		return npcs;
	}
	
	/**
	 * Helper method to create a single Npc
	 * @param resourceId
	 * @return
	 */
	private Npc createNpc(int resourceId) {
		Bitmap bitmap = BitmapFactory.decodeResource(mGameView.getResources(), resourceId);
		return new Npc(mGameView, bitmap);
	}

	public void loadDeathEffect() {
		Bitmap bitmapBlood = BitmapFactory.decodeResource(mGameView.getResources(), R.drawable.blood1);
		DeathEffect.initialize(mGameView, bitmapBlood);
	}
}
