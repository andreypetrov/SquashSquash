package com.petrovdevelopment.killthemall;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Npc implements GameElement, Touchable {
	private static final int BMP_COLUMNS = 3;
	private static final int BMP_ROWS = 4;

	// TODO: Refactor this to work with a new enum type
	// direction (array index) = 0 up, 1 left, 2 down, 3 right
	// animation (array value) = 3 back, 1 left, 0 front, 2 right
	private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

	private int mX;
	private int mY;

	private int mXSpeed;
	private int mYSpeed;

	private GameView mGameView;
	private Bitmap mBitmap;
	private int mWidth;
	private int mHeight;
	private int mCurrentFrame;

	// Better direct call for better performance than event handling on X/Y speed change
	// Recalculate the current ROW of the BMP only when the NPC changes direction.
	private int mCurrentAnimationRow;

	private Rect mSrc; // rectangle, part of the sprite bitmap
	private Rect mDst; // rectangle, part of the canvas

	public Npc(GameView mGameView, Bitmap mBitmap) {
		this.mGameView = mGameView;
		this.mBitmap = mBitmap;

		this.mWidth = mBitmap.getWidth() / BMP_COLUMNS;
		this.mHeight = mBitmap.getHeight() / BMP_ROWS;
		
		
		Random random = new Random();
		mX = random.nextInt(mGameView.getWidth() - mWidth);
		mY = random.nextInt(mGameView.getHeight() - mHeight);
		
		mXSpeed = random.nextInt(10) - 5; // X speed from -5 to +5
		mYSpeed = random.nextInt(10) - 5; // Y speed from -5 to +5
		
		//Initialize sprite frame and prepare it for rendering
		mCurrentAnimationRow = getAnimationRow();
		mCurrentFrame = 0;
		preRender();	
	}

	/**
	 * Calculate which row of the sprite bitmap should be rendered based on the closest axis of movement
	 * @return
	 */
	private int getAnimationRow() {
		double dirDouble = (Math.atan2(mXSpeed, mYSpeed) / (Math.PI / 2) + 2);
		int direction = (int) Math.round(dirDouble) % BMP_ROWS; // convert long to int
		return DIRECTION_TO_ANIMATION_MAP[direction];
	}

	/**
	 * Change direction of the npc, which affects also the sprite used
	 */
	private void changeDirection() {
		// if reached the right or left edge of the screen turn around
		if (mX > (mGameView.getWidth() - mWidth - mXSpeed) || mX + mXSpeed < 0) {
			mXSpeed = -mXSpeed;
			mCurrentAnimationRow = getAnimationRow();
		}
		// if reached the bottom or top edge of the screen turn around
		if (mY > (mGameView.getHeight() - mHeight - mYSpeed) || mY + mYSpeed < 0) {
			mYSpeed = -mYSpeed;
			mCurrentAnimationRow = getAnimationRow();
		}
	}


	/**
	 * Advance the Npc with the speed it is moving
	 */
	private void move() {
		mY = mY + mYSpeed;
		mX = mX + mXSpeed;
	}
	
	
	/**
	 * Advance the animation, creating illusion of walking.
	 * increment should be first so that in the onDraw() we have mCurrentFrame = 0/1/2
	 * Starting from the second frame.
	 */
	private void changeFrame() {
		mCurrentFrame++;
		mCurrentFrame = mCurrentFrame % BMP_COLUMNS;
	}
	
	/**
	 * Prepare for rendering, relatively expensive
	 */
	private void preRender() {
		int srcX = mCurrentFrame * mWidth;
		int srcY = mCurrentAnimationRow * mHeight;

		// preallocate objects to avoid creating any new object in the onDraw() method
		mSrc = new Rect(srcX, srcY, srcX + mWidth, srcY + mHeight);
		mDst = new Rect(mX, mY, mX + mWidth, mY + mHeight);
	}
	
	
	
	@Override
	public void update() {
		changeDirection();
		changeFrame();
		move();
		preRender();
	}

	@Override
	public void render(Canvas canvas) {	
		canvas.drawBitmap(mBitmap, mSrc, mDst, null);
	}


	@Override
	public boolean isTouched(float touchX, float touchY) {
		if ((touchX >= mX && touchX <= mX + mWidth) && (touchY >= mY && touchY <= mY + mHeight)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void touch() {
		//do nothing
	}
}
