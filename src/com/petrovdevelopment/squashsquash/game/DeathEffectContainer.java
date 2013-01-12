package com.petrovdevelopment.squashsquash.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.petrovdevelopment.squashsquash.GameView;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 *  Container of death effects
 * 
 * @author andrey
 * 
 */
public class DeathEffectContainer implements GameElement {

	private GameView mGameView;
	private Bitmap mBitmapAlien;
	private Bitmap mBitmapHuman;

	private int mBitmapWidth;
	private int mBitmapHeight;

	private List<DeathEffect> mDeathEffects;

	/**
	 * Create a new object.
	 * Images of human and alien should have the same size
	 * @return
	 */
	public static DeathEffectContainer create(GameView gameView, Bitmap bitmapAlien, Bitmap bitmapHuman) {
		return new DeathEffectContainer(gameView, bitmapAlien, bitmapHuman);
	}

	private DeathEffectContainer(GameView gameView, Bitmap bitmapAlien, Bitmap bitmapHuman) {
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
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param isAlien
	 *            alien or human death effect
	 * @return
	 */
	public void createDeathEffect(float centerX, float centerY, boolean isAlien) {
		if (mBitmapAlien == null || mBitmapHuman == null || mGameView == null) {
			throw new NullPointerException(
					"mBitmap == null or mGameView == null, probably DeathEffect.initialize() method has not been called");
		}
		//Lazy initialize mDeathEffects
		if (mDeathEffects == null) {
			mDeathEffects = Collections.synchronizedList(new ArrayList<DeathEffect>());
		}
		int x = Math.round(Math.min(Math.max(centerX - mBitmapWidth / 2, 0), mGameView.getWidth() - mBitmapWidth));
		int y = Math.round(Math.min(Math.max(centerY - mBitmapHeight / 2, 0), mGameView.getHeight() - mBitmapHeight));
		
		DeathEffect deathEffect;
		if(isAlien) {
			deathEffect = new DeathEffect(x, y, isAlien, mBitmapAlien);
		} else {
			deathEffect = new DeathEffect(x, y, isAlien, mBitmapHuman);
		}
		mDeathEffects.add(deathEffect);
	}

	/**
	 * TODO: resolve why this is throwing concurrentModification when using iterator.next() Update the temp sprites if there
	 * are any
	 */
	@Override
	public void update() {
		if (mDeathEffects != null) {
			synchronized (mDeathEffects) {
				for (Iterator<DeathEffect> iterator = mDeathEffects.iterator(); iterator.hasNext();) {
					DeathEffect currentDeathEffect = iterator.next();
					currentDeathEffect.update();
					if (currentDeathEffect.getLife() == 0) {
						iterator.remove();
					}
				}
			}
		}
	}

	@Override
	public void render(Canvas canvas) {
		if (mDeathEffects != null) {
			synchronized (mDeathEffects) {
				for (DeathEffect deathEffect : mDeathEffects) {
					deathEffect.render(canvas);
				}
			}
		}
	}

	/**
	 * discard the whole list of death effects on game's end.
	 * TODO: Remove? not needed for now?
	 */
	public void removeAll() {
		mDeathEffects = null;
	}
}
