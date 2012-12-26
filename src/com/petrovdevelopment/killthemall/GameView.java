package com.petrovdevelopment.killthemall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	private SurfaceHolder mHolder;

	private World mWorld;
	private GameLoopThread mGameLoopThread;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public GameView(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		mWorld = World.getInstance();
		mGameLoopThread = new GameLoopThread(GameView.this, mWorld);

		mHolder = getHolder();
		mHolder.addCallback(new Callback() {

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				mWorld.initialize(GameView.this);

				// Start the actual game thread
				mGameLoopThread.setRunning(true);
				mGameLoopThread.start();
				System.out.println("mGameLooThread started " + mGameLoopThread);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

			/**
			 * Make sure to close the thread before the view is destroyed
			 */
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				mGameLoopThread.setRunning(false);
				while (retry) {
					try {
						mGameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
					}
				}
				System.out.println("Surface destroyed");
			}
		});
	}

	/**
	 * Delegate to World.onTouchEvent()
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Make sure the touch event can happen only on new touch and not on persisting the touch
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// synchronize to make sure no sprite removal is happening during rendering
			synchronized (getHolder()) {
				mWorld.onTouchEvent(event.getX(), event.getY());
			}
		}
		return true; // it is more efficient to pass true here to stop handling the event
	}

	public GameLoopThread getGameLoopThread() {
		System.out.println("getting Game loop thread: " + mGameLoopThread);
		return mGameLoopThread;
	}

}
