package com.petrovdevelopment.squashsquash.sound;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;

import com.petrovdevelopment.squashsquash.MainApplication;
import com.petrovdevelopment.squashsquash.R;

/**
 * Manager of the background music MediaPlayer 
 * @author andrey
 *
 */
public class MusicManager {
	public static final String MUSIC = "music";

	private boolean mIsOn;
	private MediaPlayer mMediaPlayer;

	private Context mContext;

	public MusicManager(Context context) {
		mContext = context;
		// check from the preferences file if the music should be on or not, by default true
		mIsOn = mContext.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).getBoolean(MUSIC, true);
		prepare();
	}

	public void savePreferences() {
		Editor editor = mContext.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).edit();
		editor.putBoolean(MUSIC, mIsOn);
		editor.apply(); // async unlike Editor.commit()
	}

	public void prepare() {
		mMediaPlayer = MediaPlayer.create(mContext, R.raw.theme);
		mMediaPlayer.setLooping(true);
	}

	/**
	 * Pause/resume the background music
	 */
	public void toggle() {
		if (mIsOn) {
			pause();
		} else {
			resume();
		}
	}

	public void resume() {
		mIsOn = true;
		savePreferences();
		mMediaPlayer.start();
	}

	public void pause() {
		if (mMediaPlayer.isPlaying()) {
			mIsOn = false;
			savePreferences();
			mMediaPlayer.pause();
		}
	}

	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public boolean isOn() {
		return mIsOn;
	}

}
