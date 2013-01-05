package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.petrovdevelopment.killthemall.game.GameEndReason;
import com.petrovdevelopment.killthemall.game.World;

/**
 * TODO rework to have 3 different layouts for the different GameEndReasons, instead of using the enum to hardcode the final strings
 * TODO: either find how to stroke the textView properly or use surfaceView to create nice text stroke... or pregenerate images
 * 
 * TODO add Back/New Game/Main Menu button
 * @author andrey
 *
 */
public class EndGameActivity extends Activity {
	GameEndReason mGameEndReason; 
	int mScore;
	int mTimePassed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentExtras();
		setContentView(R.layout.activity_end_game);	
		setTextView();
	}

	private void getIntentExtras() {
		String gameEndReason = getIntent().getStringExtra(World.GAME_END_REASON);
		mGameEndReason = GameEndReason.valueOf(gameEndReason);
		mScore = getIntent().getIntExtra(World.SCORE, 0);
		mTimePassed = getIntent().getIntExtra(World.TIME_PASSED, 0);
	}


	/**
	 * Set the end game text view with the proper font and message
	 * @param gameEndReason
	 */
	private void setTextView() {
		String gameEndMessage= mGameEndReason.getMessage(mScore, mTimePassed);
		Typeface font = Typeface.createFromAsset(getAssets(), MainMenuActivity.ASSETS_FONT_LOCATION);
		TextView textView = (TextView) findViewById(R.id.endGameText);
		textView.setTypeface(font);
		textView.setText(gameEndMessage);
	}
}
