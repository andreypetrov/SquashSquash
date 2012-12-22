package com.petrovdevelopment.killthemall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private Bitmap mBitmap;
	private SurfaceHolder mHolder;
	private GameLoopThread gameLoopThread;
	private Sprite mSprite;
	
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		
		mHolder = getHolder();
		mHolder.addCallback(new Callback() {
		
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});

		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bad1);
		mSprite = new Sprite(this, mBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mSprite.update(); //TODO: pull this in a separate update method
		
		canvas.drawColor(Color.BLACK);
		mSprite.onDraw(canvas);
	}
}
