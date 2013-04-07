package com.petrovdevelopment.squashsquash.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.petrovdevelopment.squashsquash.R;
import com.petrovdevelopment.squashsquash.game.World;

/**
 * Class responsible for editing the score and time text views
 * Also keeps information of the customFont used by the application]
 * Only one instance of this class is created in the MainApplcation.java
 * @author andrey
 *
 */
public class TextManager {
	public static final String ASSETS_FONT_LOCATION = "fonts/ObelixPro-cyr.ttf";
	public static final int FONT_SIZE = 12;
	private static final int SCORE_YELLOW_THRESHOLD = 30; //TODO should be dependent on the max possible score number (let's say 1/2 of it)
	private static final int SCORE_RED_THRESHOLD = 0;
	private static final int TIME_YELLOW_THRESHOLD = (World.GAME_DURATION_SECONDS*2)/3;
	private static final int TIME_RED_THRESHOLD =  World.GAME_DURATION_SECONDS/3;
	private Context mContext;
	private Typeface mCustomFont; 
	
	public TextManager(Context context) {
		mContext = context;
		mCustomFont = Typeface.createFromAsset(context.getAssets(), ASSETS_FONT_LOCATION);
	}
	
	public Typeface getCustomFont() {
		return mCustomFont;
	}
	/**
	 * Set the value and color of the textView dependent on the score thresholds.
	 * Color can be RED, YELLOW, GREEN
	 * @param textView
	 * @param score
	 */
	public void setScore(TextView textView, int score) {
		if (textView != null) {
			textView.setText(Integer.toString(score));
			int scoreColor = mContext.getResources().getColor(getScoreColorResourceId(score));
			textView.setTextColor(scoreColor);
		}
	}
	
	/**
	 * Set the color of the textView dependent on the time left thresholds.
	 * Color can be BLACK, RED
	 * @param textView
	 * @param score
	 */
	public void setTimeLeft(TextView textView, int timeLeft) {
		if (textView != null) {
			textView.setText(Integer.toString(timeLeft));
			int timeLeftColor = mContext.getResources().getColor(getTimeLeftColorResourceId(timeLeft));
			textView.setTextColor(timeLeftColor);
		}
	}
	
	/**
	 * Set the value and color of the textView dependent on the time passed thresholds,
	 * which are reversed time left thresholds.
	 * Color can be RED, YELLOW, GREEN
	 * @param textView
	 * @param score
	 */
	public void setTimePassed(TextView textView, int timePassed) {
		if (textView != null) {
			textView.setText(Integer.toString(timePassed));
			int timePasedColor = mContext.getResources().getColor(getTimePassedColorResourceId(timePassed));
			textView.setTextColor(timePasedColor);
		}
	}
	
	/**
	 * The color for drawing the score, dependent on the score thresholds.
	 * Could be RED, YELLOW, GREEN
	 * @param score
	 * @return
	 */
	private int getScoreColorResourceId(int score) {
		if (score <= SCORE_RED_THRESHOLD) {
			return R.color.Red;
		} else {
			if (score <= SCORE_YELLOW_THRESHOLD) {
				return R.color.Yellow;
			} else {
				return R.color.Green;
			}
		}
	}

	/**
	 * The color for drawing the time left, dependent on the time thresholds.
	 * Could be BLACK, RED
	 * @param time
	 * @return
	 */
	private int getTimeLeftColorResourceId(int time) {
		if (time <= TIME_RED_THRESHOLD) {
			return R.color.Red;
		} else { 
				return R.color.Black;
		}
	}
	
	/**
	 * The color for the time passed, dependent on the color of the time left.
	 * Could be RED, YELLOW, GREEN
	 * @param timePassed
	 * @return
	 */
	private int getTimePassedColorResourceId(int timePassed) {
		int timeLeft = World.GAME_DURATION_SECONDS - timePassed;
		if (timeLeft <= TIME_RED_THRESHOLD) {
			return R.color.Red;
		} else { 
			if (timeLeft <= TIME_YELLOW_THRESHOLD) {
				return R.color.Yellow;
			} else {
				return R.color.Green;
			}
		}
	}
	
}
