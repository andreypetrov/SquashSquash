package com.petrovdevelopment.squashsquash.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;

import com.petrovdevelopment.squashsquash.GameView;

/**
 * Container of npcs TODO: test with non-synchronized list
 * 
 * @author andrey
 * 
 */
public class NpcContainer implements GameElement {
	private GameView mGameView;
	private List<Npc> mNpcs;
	private int mDemonCount;
	private int mHumanCount;

	public static NpcContainer create(GameView gameView) {
		return new NpcContainer(gameView);
	}

	private NpcContainer(GameView gameView) {
		mDemonCount = 0;
		mHumanCount = 0;
		mGameView = gameView;
		mNpcs = Collections.synchronizedList(new ArrayList<Npc>());
	}

	public void createNpc(NpcType npcType) {
		Npc npc;
		if (npcType.isEnemy()) {
			npc = new EnemyNpc(mGameView, npcType);
		} else {
			npc = new FriendNpc(mGameView, npcType);
		}
		add(npc);
	}

	@Override
	public void update() {
		synchronized (mNpcs) {
			if (mNpcs != null) {
				for (Npc npc : mNpcs) {
					npc.update();
				}
			}
		}
	}

	@Override
	public void render(Canvas canvas) {
		synchronized (mNpcs) {
			if (mNpcs != null) {
				for (Npc npc : mNpcs) {
					npc.render(canvas);
				}
			}
		}
	}

	/**
	 * Discard the whole list of Npcs. TODO Remove? Probably not needed
	 */
	public void removeAll() {
		synchronized (mNpcs) {
			mNpcs = Collections.synchronizedList(new ArrayList<Npc>());
			mDemonCount = 0;
			mHumanCount = 0;
		}
	}

	public int getDemonCount() {
		return mDemonCount;
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
		synchronized (mNpcs) {
			boolean npcWasRemoved = mNpcs.remove(npc);
			// verify if actually an npc was removed
			if (npcWasRemoved) {
				// adjust counters
				if (npc.getNpcType().isEnemy()) {
					mDemonCount--;
				} else {
					mHumanCount--;
				}
			}
			return npcWasRemoved;
		}
	}

	private boolean add(Npc npc) {
		synchronized (mNpcs) {
			mNpcs.add(npc);
			if (npc.getNpcType().isEnemy()) {
				mDemonCount++;
			} else {
				mHumanCount++;
			}
			return true;
		}
	}

	public void saveState(Bundle outState) {
		// FIXME here mNpcs.size() returns 0,
		// if i try to save immediately, without pressing "Play" button
		// is it because i access from the UI thread without sync?
		synchronized (mNpcs) {
			Parcelable[] npcs = new Parcelable[mNpcs.size()];
			for (int i = 0; i < mNpcs.size(); i++) {
				npcs[i] = mNpcs.get(i);
			}
			outState.putParcelableArray(World.NPCS, npcs);
		}
	}

	public void restoreState(Bundle inState) {
		Parcelable[] npcsArray = inState.getParcelableArray(World.NPCS);
		for (int i = 0; i < npcsArray.length; i++) {
			Random random = new Random();
			Npc npc = (Npc) npcsArray[i];

			// Make sure the X and Y of the sprites are inside the parent view
			// FIXME: there is an issue because of the continuing rendering on change configuration
			// this makes the View to be restored with sprites out of the edges.
			// To fix it, rendering need to stop before saving the instance?
			if (npc.getX() > mGameView.getWidth() - npc.getWidth()) {
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
		System.out.println("RemoveTop");
		Npc touchedNpc = getTopTouched(touchX, touchY);
		if (touchedNpc != null) {
			remove(touchedNpc);
			return touchedNpc;
		} else {
			return null;
		}
	}

	/**
	 * Checks list elements in reverse order to find the last (top most) NPC that was touched. Returns null if no Npc was
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
