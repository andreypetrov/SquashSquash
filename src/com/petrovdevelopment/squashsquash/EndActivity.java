package com.petrovdevelopment.squashsquash;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.petrovdevelopment.squashsquash.game.World;
import com.petrovdevelopment.squashsquash.game.World.GameEndReason;

/**
 * TODO rework to have 3 different layouts for the different GameEndReasons, instead of using the enum to hardcode the final
 * strings TODO: either find how to stroke the textView properly or use surfaceView to create nice text stroke... or
 * pregenerate images
 * 
 * TODO add Back/New Game/Main Menu button
 * 
 * @author andrey
 * 
 */
public class EndActivity extends Activity {
	private GameEndReason mGameEndReason;
	private int mScore;
	private int mTimePassed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getIntentExtras();
		setContentView();
	}

	private void setContentView() {
		switch (mGameEndReason) {
		case ALL_ALIENS_DEAD:
			setLayout(R.layout.end_win);
			break;
		case ALL_HUMANS_DEAD:
			setLayout(R.layout.end_loss);
			break;
		case TIME_IS_UP:
			setLayout(R.layout.end_time);
			break;
		case NONE:
			// do nothing
			break;
		default:
			break;
		}
	}

	/**
	 * Get the info passed in by the GameActivity
	 */
	private void getIntentExtras() {
		String gameEndReason = getIntent().getStringExtra(World.GAME_END_REASON);
		mGameEndReason = GameEndReason.valueOf(gameEndReason);
		mScore = getIntent().getIntExtra(World.SCORE, 0);
		mTimePassed = getIntent().getIntExtra(World.TIME_PASSED, 0);
	}

	/**
	 * Set the end game text views with the proper font, messages and colors
	 * 
	 * @param gameEndReason
	 */
	private void setLayout(int layoutId) {
		View endLayout = getLayoutInflater().inflate(layoutId, null);
		Typeface customFont = ((MainApplication) getApplication()).getCustomFont();
		Utils.setCustomFont(endLayout, customFont, MainApplication.FONT_SIZE);
		//Set the activity's layout
		setContentView(endLayout);
		setTimeAndScoreFontColors();
	}

	/**
	 * Assigns value and colors if the score and/or time views exist.
	 */
	private void setTimeAndScoreFontColors() {
		TextView timeTextView = (TextView) findViewById(R.id.timeValue);
		((MainApplication) getApplication()).setScore(timeTextView,  mTimePassed);
		
		TextView scoreTextView = (TextView) findViewById(R.id.scoreValue);
		((MainApplication) getApplication()).setTimeLeft(scoreTextView, mScore);
	}

	/**
	 * Called when the Ok ImageButton is clicked
	 */
	public void onClickOk(View view) {
		finish();
	}

}
