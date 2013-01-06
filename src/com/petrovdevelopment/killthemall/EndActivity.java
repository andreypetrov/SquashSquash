package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.petrovdevelopment.killthemall.game.GameUtils;
import com.petrovdevelopment.killthemall.game.World;
import com.petrovdevelopment.killthemall.game.World.GameEndReason;
/**
 * TODO rework to have 3 different layouts for the different GameEndReasons, instead of using the enum to hardcode the final strings
 * TODO: either find how to stroke the textView properly or use surfaceView to create nice text stroke... or pregenerate images
 * 
 * TODO add Back/New Game/Main Menu button
 * @author andrey
 *
 */
public class EndActivity extends Activity {
	GameEndReason mGameEndReason; 
	int mScore;
	int mTime;
	int mTimePassed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentExtras();
		setContentView();	
	}

	private void setContentView() {
		switch (mGameEndReason) {
		case ALL_ALIENS_DEAD:
			setContentView(R.layout.end_win);	
			setTextViewsWin();
			break;
		case ALL_HUMANS_DEAD:
			setContentView(R.layout.end_loss);	
			break;
		case TIME_IS_UP:
			setContentView(R.layout.end_time);	
			break;
		case NONE:
			//do nothing
			break;
		default:
			
			break;
		}
		setContentView(R.layout.end_win);	
		
	}

	private void getIntentExtras() {
		String gameEndReason = getIntent().getStringExtra(World.GAME_END_REASON);
		mGameEndReason = GameEndReason.valueOf(gameEndReason);
		mScore = getIntent().getIntExtra(World.SCORE, 0);
		mTime = getIntent().getIntExtra(World.TIME, 0);
		mTimePassed = getIntent().getIntExtra(World.TIME_PASSED, 0);
	}


	/**
	 * On Win set the end game text views with the proper font, messages and colors
	 * @param gameEndReason
	 */
	private void setTextViewsWin() {
		TextView winText1 = (TextView) findViewById(R.id.winText1);	
		TextView winText2 = (TextView) findViewById(R.id.winText2);
		TextView timeValue = (TextView) findViewById(R.id.timeValueWin);
		TextView seconds = (TextView) findViewById(R.id.secondsWin);
		TextView andYouGot = (TextView) findViewById(R.id.gotWin);
		TextView scoreValue = (TextView) findViewById(R.id.scoreValueWin);
		TextView points = (TextView) findViewById(R.id.pointsWin);
		TextView winText3 = (TextView) findViewById(R.id.winText3);
			
		timeValue.setText(Integer.toString(mTimePassed));
		timeValue.setTextColor(GameUtils.getTimePassedColorResource(mTimePassed));
		scoreValue.setText(Integer.toString(mScore));
		timeValue.setTextColor(GameUtils.getScoreColorResource(mScore));
		
		setTextViewFont(winText1);
		setTextViewFont(winText2);
		setTextViewFont(timeValue);
		setTextViewFont(seconds);
		setTextViewFont(andYouGot);
		setTextViewFont(scoreValue);
		setTextViewFont(points);
		setTextViewFont(winText3);
	}
	
	
	

	
	
	
	/**
	 * Helper method to send the font and its size
	 * TODO The same method is in MainMenuActivity rework it to have it only once maybe? 
	 * @param textView
	 */
	public void setTextViewFont(TextView textView) {
		Typeface font = Typeface.createFromAsset(getAssets(), MainMenuActivity.ASSETS_FONT_LOCATION);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		textView.setTypeface(font);	
	}
	
}
