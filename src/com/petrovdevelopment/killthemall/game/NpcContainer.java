package com.petrovdevelopment.killthemall.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;

import com.petrovdevelopment.killthemall.GameView;

/**
 * Container of npcs
 * TODO: test with non-synchronized list
 * @author andrey
 * 
 */
public class NpcContainer implements GameElement {
	private GameView mGameView;
	private List<Npc> mNpcs;
	private int mAlienCount;
	private int mHumanCount;

	public NpcContainer(GameView gameView) {
		mAlienCount = 0;
		mHumanCount = 0;
		mGameView = gameView;
		mNpcs = Collections.synchronizedList(new ArrayList<Npc>());
	}

	public void createNpc(NpcType npcType) {
		// Lazy initialize mNpcs. Maybe remove this.
		if (mNpcs == null) {
			mNpcs = Collections.synchronizedList(new ArrayList<Npc>());
		}
		Npc npc = new Npc(mGameView, npcType);
		add(npc);
	}

	@Override
	public void update() {
		if (mNpcs != null) {
			synchronized (mNpcs) {
				for (Npc npc : mNpcs) {
					npc.update();
				}
			}
		}
	}

	@Override
	public void render(Canvas canvas) {
		if (mNpcs != null) {
			synchronized (mNpcs) {
				for (Npc npc : mNpcs) {
					npc.render(canvas);
				}
			}
		}
	}

	/**
	 * discard the whole list of death effects on game's end
	 */
	public void removeAll() {
		mNpcs = Collections.synchronizedList(new ArrayList<Npc>());
		mAlienCount = 0;
		mHumanCount = 0;
	}

	public int getAlienCount() {
		return mAlienCount;
	}

	public int getHumanCount() {
		return mHumanCount;
	}

	/**
	 * Remove the Npc from the container (effectively kill it)
	 * 
	 * @param npc
	 * @return
	 */
	private boolean remove(Npc npc) {
		boolean npcWasRemoved = mNpcs.remove(npc);
		// verify if actually an npc was removed
		if (npcWasRemoved) {
			// adjust counters
			if (npc.getNpcType().isAlien()) {
				mAlienCount--;
			} else {
				mHumanCount--;
			}
		}
		return npcWasRemoved;
	}

	private boolean add(Npc npc) {
		mNpcs.add(npc);
		if (npc.getNpcType().isAlien()) {
			mAlienCount++;
		} else {
			mAlienCount--;
		}
		return true;
	}

	public void saveState(Bundle outState) {
		Parcelable[] npcs = new Parcelable[mNpcs.size()];
		for (int i = 0; i < mNpcs.size(); i++) {
			npcs[i] = mNpcs.get(i);
		}
		outState.putParcelableArray(World.NPCS, npcs);
	}

	public void restoreState(Bundle inState) {
		Parcelable[] npcsArray = inState.getParcelableArray(World.NPCS);
		for (int i = 0; i<npcsArray.length; i++) {
			Random random = new Random();
			Npc npc = (Npc) npcsArray[i];
			
			//Make sure the X and Y of the sprites are inside the parent view
			//FIXME: there is an issue because of the continuing rendering on change configuration
			//this makes the View to be restored with sprites out of the edges. 
			//To fix it, rendering need to stop before saving the instance?
			if (npc.getX()> mGameView.getWidth() - npc.getWidth()) {
				npc.setX(random.nextInt(mGameView.getWidth() - npc.getWidth()));
			}
			if (npc.getY() > mGameView.getHeight() - npc.getHeight()) {
				npc.setY(random.nextInt(mGameView.getHeight() - npc.getHeight()));
			}		
			npc.setParentViewAndInitialize(mGameView);
			add(npc);
		}
	}
	
	
	
	
	/**
	 * Removes the top most (on Z coordinate) NPC and returns it. If no Npc was touched, returns null
	 * 
	 * @param touchX
	 * @param touchY
	 * @return The Npc removed from the container
	 */
	public Npc removeTopTouchedNpc(float touchX, float touchY) {
		Npc touchedNpc = getTopTouched(touchX, touchY);
		if (touchedNpc != null) {
			remove(touchedNpc);
			return touchedNpc;
		} else {
			return null;
		}
	}

	/**
	 * Check list elements in reverse order to find the last (top most) NPC that was touched. Return null if no npc was
	 * touched.
	 * 
	 * @param touchX
	 * @param touchY
	 * @return
	 */
	private Npc getTopTouched(float touchX, float touchY) {
		synchronized (mNpcs) {
			for (int i = mNpcs.size() - 1; i >= 0; i--) {
				Npc npc = mNpcs.get(i);
				if (npc.isTouched(touchX, touchY)) {
					return npc;
				}
			}
		}
		return null;
	}

}