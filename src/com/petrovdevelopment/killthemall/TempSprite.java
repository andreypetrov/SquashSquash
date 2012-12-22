package com.petrovdevelopment.killthemall;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TempSprite {
	private final int mX;
	private final int mY;
	private Bitmap mBitmap;
	private int mLife = 15;

	// TODO: improve the encapsulation of this list, without providing public access to it
	// keep static count of all the sprites
	private static List<TempSprite> tempSprites;

	/**
	 * Update the temp sprites if there are any
	 */
	public static void updateTempSprites() {
		if (tempSprites != null) {
			for (Iterator<TempSprite> iterator = tempSprites.iterator(); iterator.hasNext();) {
				TempSprite currentSprite = iterator.next();
				currentSprite.mLife--;
				if (currentSprite.mLife == 0) {
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Draw the temp sprites if there are any
	 * @param canvas
	 */
	public static void onDrawTempSprites(Canvas canvas) {
		if (tempSprites != null) {
			for (TempSprite tempSprite : tempSprites) {
				tempSprite.onDraw(canvas);
			}
		}
	}
	
	/**
	 * Add the sprite to the array of tracked instances.
	 */
	public void register(){
		tempSprites.add(this);
	}
	
	public TempSprite(GameView gameView, float x, float y, Bitmap bitmap) {
		// Lazy initialize the temp sprites static list
		if (tempSprites == null) {
			tempSprites = new ArrayList<TempSprite>();
		}
		
		this.mX = Math.round(Math.min(Math.max(x - bitmap.getWidth() / 2, 0), gameView.getWidth() - bitmap.getWidth()));
		this.mY = Math.round(Math.min(Math.max(y - bitmap.getHeight() / 2, 0), gameView.getHeight() - bitmap.getHeight()));
		this.mBitmap = bitmap;		
	}

//	private void update() {
//		mLife--;
//		if (mLife == 0) {
//			tempSprites.remove(this);
//		}
//	}

	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, mX, mY, null);
	}

}
