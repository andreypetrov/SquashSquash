package com.petrovdevelopment.squashsquash;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.util.Log;

public class MediaManager {
	public static final String SOUND = "sound";
	public static final String MUSIC = "music";
	public static final String THEME = "theme";

	private boolean mIsMusicOn;
	
	private Context mContext;
	private MediaPlayer mMediaPlayer;

	public MediaManager(Context context) {
		mContext = context;
		
		//check if the music should be on or not, by default true
		mIsMusicOn = mContext.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).getBoolean(MUSIC, true);
		Log.i(this.getClass().getSimpleName(), "mIsMusicOn = " + mIsMusicOn);	
		prepareMusic();
		//start the music if need be
		if (mIsMusicOn) {
			resumeMusic(); 
		} 
	}
	
	public void prepareMusic() {
		mMediaPlayer = MediaPlayer.create(mContext, R.raw.theme);
		mMediaPlayer.setLooping(true);
	}
	
	public void toggleMusic() {
		Log.i(this.getClass().getSimpleName(), "mIsMusicOn = " + mIsMusicOn);	
		mIsMusicOn = !mIsMusicOn;
		saveMusicPreferences();
		if (mIsMusicOn) {
			resumeMusic();
		} else {
			pauseMusic();
		}
	}
	
	public void saveMusicPreferences() {
		Editor editor = mContext.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).edit();
		editor.putBoolean(MUSIC, mIsMusicOn);
		editor.commit();
		Log.i(this.getClass().getSimpleName(), "PreferencesSaved");
	}
	
	public void resumeMusic() {
			Log.i(this.getClass().getSimpleName(), "MediaPlayer.start()");
			mMediaPlayer.start();
	}

	public void pauseMusic() {
		if (mMediaPlayer.isPlaying()) {
			Log.i(this.getClass().getSimpleName(), "MediaPlayer.pause()");
			mMediaPlayer.pause();
		}
	}

	
	public void stopMusic(){
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}
}
