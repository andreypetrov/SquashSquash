package com.petrovdevelopment.killthemall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * A singleton variation, with controlled number of instances, Need to be preloaded with initialize(). Uses only one bmp for
 * every DeathEffect instance
 * 
 * @author andrey
 * 
 */
public class DeathEffect {
	// TODO: change the life to be in seconds, independent of the number of game ticks
	private static int INITIAL_LIFE = 15; // number of game ticks
	
	//
	// Object variables
	//
	private final int mX;
	private final int mY;
	private final boolean mIsAlien;
	
	private int mLife = INITIAL_LIFE;
	
	public DeathEffect(int x, int y, boolean isAlien) {
		mIsAlien = isAlien;
		mX = x;
		mY = y;
	}

	public int getLife() {
		return mLife;
	}
	
	public int getX() {
		return mX;
	}

	public int getY() {
		return mY;
	}

	public boolean isAlien() {
		return mIsAlien;
	}
	
	public void decrementLife() {
		mLife--;
	}

}
