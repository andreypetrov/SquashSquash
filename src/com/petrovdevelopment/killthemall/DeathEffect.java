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
public class DeathEffect implements GameElement {
	//
	// Class variables
	//
	// TODO: change the life to be in seconds, independent of the number of game ticks
	private static int INITIAL_LIFE = 15; // number of game ticks
	private static GameView mGameView;

	private static Bitmap mBitmapAlien;
	private static Bitmap mBitmapHuman;

	private static int mBitmapWidth;
	private static int mBitmapHeight;
	// Keep static synchronized count of all the variables of this class
	private static List<DeathEffect> deathEffects;

	//
	// Object variables
	//
	private final int mX;
	private final int mY;
	private int mLife = INITIAL_LIFE;
	private final boolean mIsAlien;

	/**
	 * Initialize the class variables Images of human and alien should have the same size
	 * 
	 * @param gameView
	 * @param bitmap
	 */
	public static void initialize(GameView gameView, Bitmap bitmapAlien, Bitmap bitmapHuman) {
		mGameView = gameView;
		mBitmapAlien = bitmapAlien;
		mBitmapHuman = bitmapHuman;

		mBitmapWidth = bitmapAlien.getWidth();
		mBitmapHeight = bitmapAlien.getHeight();
	}

	/**
	 * Create a new death effect centered on the given coordinates
	 * 
	 * @param x
	 *            - the x coordinate
	 * @param y
	 *            - the y coordinate
	 * @return
	 */
	public static DeathEffect create(float x, float y, boolean isAlien) {
		if (mBitmapAlien == null || mBitmapHuman == null || mGameView == null) {
			throw new NullPointerException(
					"mBitmap == null or mGameView == null, probably DeathEffect.initialize() method has not been called");
		}
		return new DeathEffect(x, y, isAlien);
	}

	private DeathEffect(float x, float y, boolean isAlien) {
		// Lazy initialize the static synchronized list with all objects of this class
		if (deathEffects == null) {
			deathEffects = Collections.synchronizedList(new ArrayList<DeathEffect>());
		}

		mIsAlien = isAlien;
		mX = Math.round(Math.min(Math.max(x - mBitmapWidth / 2, 0), mGameView.getWidth() - mBitmapWidth));
		mY = Math.round(Math.min(Math.max(y - mBitmapHeight / 2, 0), mGameView.getHeight() - mBitmapHeight));

		// Add the newly created object to the controlled static list
		synchronized (deathEffects) {
			deathEffects.add(this);
		}
	}

	/**
	 * TODO: resolve why this is throwing concurrentModification when using iterator.next() Update the temp sprites if there
	 * are any
	 */
	public static void updateAll() {
		if (deathEffects != null) {
			synchronized (deathEffects) {
				for (Iterator<DeathEffect> iterator = deathEffects.iterator(); iterator.hasNext();) {
					DeathEffect currentDeathEffect = iterator.next();
					currentDeathEffect.mLife--;
					if (currentDeathEffect.mLife == 0) {
						iterator.remove();
					}
				}
			}
		}
	}

	/**
	 * Draw the death effects if there are any. Uses render() of every object in the list.
	 * 
	 * @param canvas
	 */
	public static void renderAll(Canvas canvas) {
		if (deathEffects != null) {
			for (DeathEffect deathEffect : deathEffects) {
				deathEffect.render(canvas);
			}
		}
	}

	// Do nothing. Not used for now, instead use updateAll
	@Override
	public void update() {
		// mLife--;
		// if (mLife == 0) {
		// synchronized (deathEffects) {
		// deathEffects.remove(this);
		// }
		// }
	}

	@Override
	public void render(Canvas canvas) {
		if (mIsAlien) {
			canvas.drawBitmap(mBitmapAlien, mX, mY, null);
		} else {
			canvas.drawBitmap(mBitmapHuman, mX, mY, null);
		}
	}

}
