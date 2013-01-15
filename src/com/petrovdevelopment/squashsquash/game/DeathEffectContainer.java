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
	private Bitmap mBitmapDemon;
	private Bitmap mBitmapHuman;

	private int mBitmapWidth;
	private int mBitmapHeight;

	private List<DeathEffect> mDeathEffects;

	/**
	 * Create a new object.
	 * Images of human and demon should have the same size
	 * @return
	 */
	public static DeathEffectContainer create(GameView gameView, Bitmap bitmapDemon, Bitmap bitmapHuman) {
		return new DeathEffectContainer(gameView, bitmapDemon, bitmapHuman);
	}

	private DeathEffectContainer(GameView gameView, Bitmap bitmapDemon, Bitmap bitmapHuman) {
		mGameView = gameView;
		mBitmapDemon = bitmapDemon;
		mBitmapHuman = bitmapHuman;
		mBitmapWidth = bitmapDemon.getWidth();
		mBitmapHeight = bitmapDemon.getHeight();
	}

	/**
	 * Create a new death effect centered on the given coordinates
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param isDemon
	 *            demon or human death effect
	 * @return
	 */
	public void createDeathEffect(float centerX, float centerY, boolean isDemon) {
		if (mBitmapDemon == null || mBitmapHuman == null || mGameView == null) {
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
		if(isDemon) {
			deathEffect = new DeathEffect(x, y, isDemon, mBitmapDemon);
		} else {
			deathEffect = new DeathEffect(x, y, isDemon, mBitmapHuman);
		}
		mDeathEffects.add(deathEffect);
	}

	/**
	 * Update the temp sprites if there are any
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

}
