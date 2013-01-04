package com.petrovdevelopment.killthemall.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * A singleton variation, with controlled number of instances, Need to be preloaded with initialize(). Uses only one bmp for
 * every DeathEffect instance
 * 
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
