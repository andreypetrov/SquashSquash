package com.petrovdevelopment.squashsquash.game;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

/**
 * Class representing a single npc. Parceable so that it can restore its state, if the game stops
 * 
 * @author andrey
 * 
 */
public class Npc implements GameElement, Touchable, Parcelable {
	public static final int BMP_COLUMNS = 3;
	public static final int BMP_ROWS = 4;
	// TODO: Refactor this to work with a new enum type
	// direction (array index) = 0 up, 1 left, 2 down, 3 right
	// animation (array value) = 3 back, 1 left, 0 front, 2 right
	private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

	private View mParentView;

	private NpcType mNpcType;
	private Bitmap mBitmap;

	private int mX;
	private int mY;

	private int mXSpeed;
	private int mYSpeed;

	private int mWidth;
	private int mHeight;
	private int mCurrentFrame;
	// Recalculate the current ROW of the BMP only when the NPC changes direction.
	private int mCurrentAnimationRow;

	private Rect mSrc; // rectangle, part of the sprite bitmap
	private Rect mDst; // rectangle, part of the canvas

	/**
	 * Use this constructor when creating npcs for a new game.
	 * 
	 * @param parentView
	 * @param npcType
	 */
	public Npc(View parentView, NpcType npcType) {
		mParentView = parentView;
		mNpcType = npcType;
		Random random = new Random();
		mBitmap = BitmapFactory.decodeResource(mParentView.getResources(), npcType.resourceId());
		mWidth = mBitmap.getWidth() / Npc.BMP_COLUMNS;
		mHeight = mBitmap.getHeight() / Npc.BMP_ROWS;
		mX = random.nextInt(mParentView.getWidth() - mWidth);
		mY = random.nextInt(mParentView.getHeight() - mHeight);
		mXSpeed = random.nextInt(10) - 5; // X speed from -5 to +5
		mYSpeed = random.nextInt(10) - 5; // Y speed from -5 to +5
		// Initialize sprite frame and prepare it for rendering
		mCurrentAnimationRow = getAnimationRow();
		mCurrentFrame = 0;
		preRender();
	}

	/**
	 * Used by the Npc.CREATOR when recovering previous game from a bundle. It requires to call after
	 * setParentViewAndInitialize, before using the Npc
	 * 
	 * @param source
	 */
	private Npc(Parcel source) {
		// It is mandatory to read in the same order in which values were written in the parcel!
		mNpcType = source.readParcelable(NpcType.class.getClassLoader());
		mX = source.readInt();
		mY = source.readInt();
		mXSpeed = source.readInt();
		mYSpeed = source.readInt();
		mWidth = source.readInt();
		mHeight = source.readInt();
		mCurrentFrame = source.readInt();
		mCurrentAnimationRow = source.readInt();
	}

	/**
	 * Sets up the game view the bitmap and some prerender calculations. Mandatory call this after using the private
	 * constructor. The public constructor makes this call redundant.
	 * 
	 * @param parentView
	 */
	public void setParentViewAndInitialize(View parentView) {
		this.mParentView = parentView;
		this.mBitmap = BitmapFactory.decodeResource(mParentView.getResources(), mNpcType.resourceId());
		preRender();
	}

	/**
	 * Calculate which row of the sprite bitmap should be rendered based on the closest axis of movement
	 * 
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
		if (mX > (mParentView.getWidth() - mWidth - mXSpeed) || mX + mXSpeed < 0) {
			mXSpeed = -mXSpeed;
			mCurrentAnimationRow = getAnimationRow();
		}
		// if reached the bottom or top edge of the screen turn around
		if (mY > (mParentView.getHeight() - mHeight - mYSpeed) || mY + mYSpeed < 0) {
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
	 * Advance the animation, creating illusion of walking. increment should be first so that in the onDraw() we have
	 * mCurrentFrame = 0/1/2 Starting from the second frame.
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

	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Works like serialization
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// the second is a flag which could be 0 or PARCELABLE_WRITE_RETURN_VALUE.
		// For this app it does not matter really
		dest.writeParcelable(mNpcType, 0);
		dest.writeInt(mX);
		dest.writeInt(mY);
		dest.writeInt(mXSpeed);
		dest.writeInt(mYSpeed);
		dest.writeInt(mWidth);
		dest.writeInt(mHeight);
		dest.writeInt(mCurrentFrame);
		dest.writeInt(mCurrentAnimationRow);
	}

	/**
	 * Creator responsible for the recreating the Npc if the app was killed while in the background If this field is not
	 * implemented there will be a runtime BadParcelableException
	 */
	public static final Creator<Npc> CREATOR = new Creator<Npc>() {
		@Override
		public Npc createFromParcel(final Parcel source) {
			return new Npc(source);
		}

		@Override
		public Npc[] newArray(int size) {
			return new Npc[size];
		}

	};

	public int getX() {
		return mX;
	}

	public void setX(int x) {
		mX = x;
	}

	public int getY() {
		return mY;
	}

	public void setY(int y) {
		mY = y;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public NpcType getNpcType() {
		return mNpcType;
	}

	public float getCenterX() {
		return mX + (mWidth / 2);
	}

	public float getCenterY() {
		return mY + (mHeight / 2);
	}
}
