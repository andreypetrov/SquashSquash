package com.petrovdevelopment.killthemall.game;

import com.petrovdevelopment.killthemall.R;

public class GameUtils {
	private static final int SCORE_YELLOW_THRESHOLD = 30; //TODO should be dependent on the max possible score number (let's say 1/2 of it)
	private static final int SCORE_RED_THRESHOLD = 0;
	private static final int TIME_YELLOW_THRESHOLD = (World.GAME_DURATION_SECONDS*2)/3;
	private static final int TIME_RED_THRESHOLD =  World.GAME_DURATION_SECONDS/3;

	/**
	 * The color for drawing the score, dependent on the score thresholds.
	 * Could be RED, YELLOW, GREEN
	 * @param score
	 * @return
	 */
	public static int getScoreColorResource(int score) {
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
	 * Could be RED, YELLOW, GREEN
	 * @param time
	 * @return
	 */
	public static int getTimeColorResource(int time) {
		if (time <= TIME_RED_THRESHOLD) {
			return R.color.Red;
		} else { 
			if (time <= TIME_YELLOW_THRESHOLD) {
				return R.color.Yellow;
			} else {
				return R.color.GreenYellow;
			}
		}
	}
	
	/**
	 * The color for the time passed, dependent on the color of the time left.
	 * Could be RED, YELLOW, GREEN
	 * @param timePassed
	 * @return
	 */
	public static int getTimePassedColorResource(int timePassed) {
		int timeLeft = World.GAME_DURATION_SECONDS - timePassed;
		return getTimeColorResource(timeLeft);
	}
	
}
