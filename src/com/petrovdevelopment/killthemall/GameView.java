package com.petrovdevelopment.killthemall;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private SurfaceHolder mHolder;
	private GameLoopThread gameLoopThread;

	private List<Sprite> mAllSprites;

	private long mLastClick = 0;
	// if user is currently touching the surface
	private boolean isTouched = false;
	private Bitmap mBitmapBlood;

	private Bitmap mBackground;
	private BitmapDrawable mBackgroundDrawable;
	private Matrix mBackgroundMatrix;

	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);

		mHolder = getHolder();
		mHolder.addCallback(new Callback() {

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				initializeSpites();
				// MainActivity mainActivity = (MainActivity) getContext();
				// mainActivity.startGameLoop();

				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

			/**
			 * Make sure to close the thread before the view is destroyed
			 */
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				System.out.println("Surface destroyed start");
				boolean retry = true;
				gameLoopThread.setRunning(false);
				while (retry) {
					try {
						gameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
					}
				}
				System.out.println("Surface destroyed end");
			}
		});

		mBackgroundMatrix = new Matrix();
		mBackgroundMatrix.reset();

		mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.blue_bg);
		mBackgroundDrawable = new BitmapDrawable(getResources(), mBackground);
		mBackgroundDrawable.setTileModeX(Shader.TileMode.REPEAT);
		mBackgroundDrawable.setTileModeY(Shader.TileMode.REPEAT);

		mBitmapBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
	}

	private void initializeSpites() {
		mAllSprites = new ArrayList<Sprite>();
		mAllSprites.add(createSprite(R.drawable.bad1));
		mAllSprites.add(createSprite(R.drawable.bad2));
		mAllSprites.add(createSprite(R.drawable.bad3));
		mAllSprites.add(createSprite(R.drawable.bad4));
		mAllSprites.add(createSprite(R.drawable.bad5));
		mAllSprites.add(createSprite(R.drawable.bad6));
		mAllSprites.add(createSprite(R.drawable.good1));
		mAllSprites.add(createSprite(R.drawable.good2));
		mAllSprites.add(createSprite(R.drawable.good3));
		mAllSprites.add(createSprite(R.drawable.good4));
		mAllSprites.add(createSprite(R.drawable.good5));
		mAllSprites.add(createSprite(R.drawable.good6));
	}

	private Sprite createSprite(int resourceId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
		return new Sprite(this, bitmap);
	}

	public void update() {
		synchronized (getHolder()) {
			TempSprite.updateTempSprites();
			// TODO: pull this in a separate update method
			for (Sprite sprite : mAllSprites) {
				sprite.update();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(Color.BLACK);

		// create background texture, which wipes out previous drawing
		// mBackgroundDrawable.draw(canvas);

		canvas.drawBitmap(mBackground, mBackgroundMatrix, null);

		TempSprite.onDrawTempSprites(canvas);
		for (Sprite sprite : mAllSprites) {
			sprite.onDraw(canvas);
		}
	}

	// TODO: to disallow a new touch event before a user actually clicks second time, we need to set a flag = false
	// and then reset it on event.ACTION_UP, so that the new touch event can happen only on new click, not on holding the
	// click
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isTouched = false;
		}
		// do not allow a touch event more often than half a second
		// do not allow a touch event if a user has his finger on the screen still
		// TODO: maybe remove the half a second limitation
		if (event.getAction() == MotionEvent.ACTION_DOWN && System.currentTimeMillis() - mLastClick > 500 && !isTouched) {
			mLastClick = System.currentTimeMillis();
			isTouched = true;
			// in the GameLoopThread we have another synchronized (gameView.getHolder()) on the same object
			// this allows us to avoid ConcurrentModificationException! between this method and onDraw
			synchronized (getHolder()) {
				float touchX = event.getX();
				float touchY = event.getY();

				// TODO: rework this with using Iterator for safe removal
				for (int i = mAllSprites.size() - 1; i >= 0; i--) {
					Sprite sprite = mAllSprites.get(i);
					if (sprite.isTouched(touchX, touchY)) {
						mAllSprites.remove(i);
						TempSprite tempSprite = new TempSprite(this, touchX, touchY, mBitmapBlood);
						tempSprite.register();

						break;// kill only the last sprite, i.e. only the last one
					}
				}
			}
		}
		return true; // it is more efficient to pass true here to stop handling the event
	}

}
