package com.petrovdevelopment.killthemall;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	private static final int BMP_COLUMNS = 3;
	private static final int BMP_ROWS = 4;
	
	
	private int mX = 0;
	private int mY = 0;
	
	private int mXSpeed = -5;
	private GameView mGameView;
	private Bitmap mBitmap;
	private int mWidth;
	private int mHeight;
	private int mCurrentFrame = 0;
	
	
	private Rect mSrc; //rectangle as part of the sprite bitmap
	private Rect mDst; //rectangle as part of the canvas

	
	public Sprite(GameView mGameView, Bitmap mBitmap) {
		this.mGameView = mGameView;
		this.mBitmap = mBitmap;
		
		this.mWidth = mBitmap.getWidth()/ BMP_COLUMNS;
		this.mHeight = mBitmap.getHeight()/ BMP_ROWS;
	}
	
	public void update() {
		//TODO: should we subtract the mXSpeed as well?
		//if reached the edge of the screen turn around
		if (mX >= (mGameView.getWidth() - mWidth)|| mX<= 0){ 
			mXSpeed = (-1)*mXSpeed;
		} 
		
		mX = mX + mXSpeed;
		
		
		//advance the animation, creating illusion of walking
		mCurrentFrame++; //increment should be first so that in the onDraw we have mCurrentFrame = 0/1/2	
		mCurrentFrame = mCurrentFrame%BMP_COLUMNS;	
		int srcX = mCurrentFrame * mWidth;
		int srcY = 1 * mHeight;
		
		//preallocate objects to avoid creating any new object in the onDraw method
		mSrc = new Rect(srcX, srcY, srcX + mWidth, srcY + mHeight);
		mDst = new Rect(mX, mY, mX + mWidth, mY + mHeight);
	}
	
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, mSrc, mDst, null);
	}
	
}
