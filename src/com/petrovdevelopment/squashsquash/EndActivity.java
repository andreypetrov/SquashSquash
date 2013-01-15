package com.petrovdevelopment.squashsquash;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.petrovdevelopment.squashsquash.game.World;
import com.petrovdevelopment.squashsquash.game.World.GameEndReason;
import com.petrovdevelopment.squashsquash.utils.TextManager;
import com.petrovdevelopment.squashsquash.utils.Utils;

/**
 * 3 different layouts for the different GameEndReasons TODO: fix colors of points and time
 * 
 * @author andrey
 * 
 */
public class EndActivity extends MediaClientActivity {
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
		Typeface customFont = ((MainApplication) getApplication()).getTextManager().getCustomFont();
		Utils.setCustomFont(endLayout, customFont, TextManager.FONT_SIZE);
		// Set the activity's layout
		setContentView(endLayout);
		setTimeAndScoreFontColors();
	}

	/**
	 * Assigns value and colors if the score and/or time views exist.
	 */
	private void setTimeAndScoreFontColors() {
		TextView timeTextView = (TextView) findViewById(R.id.timeValue);
		((MainApplication) getApplication()).getTextManager().setTimePassed(timeTextView, mTimePassed);

		TextView scoreTextView = (TextView) findViewById(R.id.scoreValue);
		((MainApplication) getApplication()).getTextManager().setScore(scoreTextView, mScore);
	}

	/**
	 * Called when the Ok ImageButton is clicked
	 */
	public void onClickOk(View view) {
		finish();
	}

}
