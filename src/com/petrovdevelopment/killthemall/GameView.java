package com.petrovdevelopment.killthemall;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	private GameLoader mGameLoader;

	private SurfaceHolder mHolder;
	private GameLoopThread mGameLoopThread;

	private Background mBackground;
	private List<Npc> mNpcs;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViewAndStartGameLoop();
	}
	
	public GameView(Context context) {
		super(context);
		initializeViewAndStartGameLoop();
	}
	
	private void initializeViewAndStartGameLoop() {
		mGameLoopThread = new GameLoopThread(this);

		mHolder = getHolder();
		mHolder.addCallback(new Callback() {

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				mGameLoader = new GameLoader(GameView.this);
				mBackground = mGameLoader.loadBackground();
				mNpcs = mGameLoader.loadNpcs();
				mGameLoader.loadDeathEffect();
				
				//Start the actual game thread
				mGameLoopThread.setRunning(true);
				mGameLoopThread.start();
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

	public void update() {
		mBackground.update();
		DeathEffect.updateAll();
		for (Npc npc : mNpcs) {
			npc.update();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mBackground.render(canvas);
		DeathEffect.renderAll(canvas);

		for (Npc npc : mNpcs) {
			npc.render(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Make sure the touch event can happen only on new touch and not on persisting the touch
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (getHolder()) {
				float touchX = event.getX();
				float touchY = event.getY();

				// TODO: rework this with using Iterator for safe removal
				for (int i = mNpcs.size() - 1; i >= 0; i--) {
					Npc npc = mNpcs.get(i);
					if (npc.isTouched(touchX, touchY)) {
						npc.touch();
						mNpcs.remove(i);
						// no need this class to keep track of the created deathEffects.
						// the class DeathEffects itself does that
						DeathEffect.create(touchX, touchY);

						// kill only the last (top most) NPC, i.e. only the last one
						break;
					}
				}

			}
		}
		return true; // it is more efficient to pass true here to stop handling the event
	}

	public GameLoopThread getGameLoopThread() {
		return mGameLoopThread;
	}

}
