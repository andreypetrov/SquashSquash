package com.petrovdevelopment.killthemall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The surface on which the game world is being rendered.
 * @author andrey
 *
 */
public class GameView extends SurfaceView {
	private GameActivity mGameActivity;

	private SurfaceHolder mHolder;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGameActivity = (GameActivity) context;
		initialize();
	}

	public GameView(Context context) {
		super(context);
		mGameActivity = (GameActivity) context;
		initialize();
	}

	/**
	 * Assign the GameActivity to handle the callbacks of the view's lifecycle
	 */
	private void initialize() {
		mHolder = getHolder();
		mHolder.addCallback(mGameActivity);
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
				mGameActivity.onGameViewTouchEvent(event.getX(), event.getY());
			}
		}
		return true; // it is more efficient to pass true here to stop handling the event
	}
}
