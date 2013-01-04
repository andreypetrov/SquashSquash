package com.petrovdevelopment.killthemall.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * A death effect used when an NPC is dying for a short time.
 * It is an independent entity from the Npc. The World is synchronizing them
 * @author andrey
 * 
 */
public class DeathEffect implements GameElement {
	// TODO: change the life to be in seconds, independent of the number of game ticks
	private static int INITIAL_LIFE = 15; // number of game ticks
	
	//
	// Object variables
	//
	private final int mX;
	private final int mY;
	private final boolean mIsAlien;
	
	private int mLife = INITIAL_LIFE;
	private Bitmap mBitmap;
	
	public DeathEffect(int x, int y, boolean isAlien, Bitmap bitmap) {
		mIsAlien = isAlien;
		mBitmap = bitmap;
		mX = x;
		mY = y;
	}

	public int getLife() {
		return mLife;
	}
	
	private int getX() {
		return mX;
	}

	private int getY() {
		return mY;
	}

	public boolean isAlien() {
		return mIsAlien;
	}
	
	private void decrementLife() {
		mLife--;
	}

	@Override
	public void update() {
		decrementLife();	
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawBitmap(mBitmap, getX(), getY(), null);
	}

	
}
