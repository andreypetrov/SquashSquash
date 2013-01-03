package com.petrovdevelopment.killthemall;

import java.util.ArrayList;
import java.util.List;

import com.petrovdevelopment.killthemall.game.DeathEffectContainer;
import com.petrovdevelopment.killthemall.game.Npc;
import com.petrovdevelopment.killthemall.game.NpcType;

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
		npcs.add(createNpc(NpcType.BAD1));
		npcs.add(createNpc(NpcType.BAD2));
		npcs.add(createNpc(NpcType.BAD3));
		npcs.add(createNpc(NpcType.BAD4));
		npcs.add(createNpc(NpcType.BAD5));
		npcs.add(createNpc(NpcType.BAD6));
		npcs.add(createNpc(NpcType.GOOD1));
		npcs.add(createNpc(NpcType.GOOD2));
		npcs.add(createNpc(NpcType.GOOD3));
		npcs.add(createNpc(NpcType.GOOD4));
		npcs.add(createNpc(NpcType.GOOD5));
		npcs.add(createNpc(NpcType.GOOD6));
		return npcs;
	}
	
	/**
	 * Helper method to create a single Npc
	 * @param resourceId
	 * @return
	 */
	private Npc createNpc(NpcType npcType) {
		Npc npc = new Npc(mGameView, npcType);
		return npc;
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
