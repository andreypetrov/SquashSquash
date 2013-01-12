package com.petrovdevelopment.killthemall;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.TextView;

import com.petrovdevelopment.killthemall.game.World;

/**
 * Main application class. 
 * Extending Application to load the custom font asset
 * @author andrey
 *
 */
public class MainApplication extends Application{
	public static final String ASSETS_FONT_LOCATION = "fonts/ObelixPro-cyr.ttf";
	public static final int FONT_SIZE = 12;
	private static final int SCORE_YELLOW_THRESHOLD = 30; //TODO should be dependent on the max possible score number (let's say 1/2 of it)
	private static final int SCORE_RED_THRESHOLD = 0;
	private static final int TIME_YELLOW_THRESHOLD = (World.GAME_DURATION_SECONDS*2)/3;
	private static final int TIME_RED_THRESHOLD =  World.GAME_DURATION_SECONDS/3;
		
	public static final String DIALOG = "dialog"; //title of the dialog fragment
	
	private Typeface mCustomFont;
	@Override
	public void onCreate() {
		super.onCreate();		
		mCustomFont = Typeface.createFromAsset(getAssets(), ASSETS_FONT_LOCATION);
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
			int scoreColor = getResources().getColor(getScoreColorResourceId(score));
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
			int timeLeftColor = getResources().getColor(getTimeLeftColorResourceId(timeLeft));
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
			int timePasedColor = getResources().getColor(getTimePassedColorResourceId(timePassed));
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
