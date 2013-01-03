package com.petrovdevelopment.killthemall.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.petrovdevelopment.killthemall.GameView;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Singleton container of all death effects
 * 
 * @author andrey
 * 
 */
public class DeathEffectContainer implements GameElement {
	
	public static DeathEffectContainer mInstance;

	private GameView mGameView;
	private Bitmap mBitmapAlien;
	private Bitmap mBitmapHuman;

	private int mBitmapWidth;
	private int mBitmapHeight;

	private List<DeathEffect> mDeathEffects;

	public static DeathEffectContainer getInstance() {
		if (mInstance == null) {
			mInstance = new DeathEffectContainer();
		}
		return mInstance;
	}

	private DeathEffectContainer() {
	}

	/**
	 * Initialize the singleton variables. Images of human and alien should have the same size
	 * 
	 * @param gameView
	 * @param bitmap
	 */
	public void initialize(GameView gameView, Bitmap bitmapAlien, Bitmap bitmapHuman) {
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
		DeathEffect deathEffect = new DeathEffect(x, y, isAlien);
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
					currentDeathEffect.decrementLife();
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
					if (deathEffect.isAlien()) {
						canvas.drawBitmap(mBitmapAlien, deathEffect.getX(), deathEffect.getY(), null);
					} else {
						canvas.drawBitmap(mBitmapHuman, deathEffect.getX(), deathEffect.getY(), null);
					}
				}
			}
		}
	}

	/**
	 * discard the whole list of death effects on game's end
	 */
	public void removeAll() {
		mDeathEffects = null;
	}
}
