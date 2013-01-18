package com.petrovdevelopment.squashsquash.sound;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.petrovdevelopment.squashsquash.MainApplication;
import com.petrovdevelopment.squashsquash.R;

//TODO pause the music only onPause of the game and onMute button click
//TODO resume the music onPlay click (if it is not playing already !isPlaying) and onUnmute button click
//TODO silence the music on losing AutoFocus
//So by default it will be on for the whole game 

public class MediaService extends Service {
	public static final String MUSIC = "music";
	public static final String THEME = "theme";

	private boolean mIsMusicOn;
	private MediaPlayer mMediaPlayer;
	private IBinder mBinder = new MediaBinder();

	public MediaService() {
	}

	@Override
	public void onCreate() {
		// check from the preferences file if the music should be on or not, by default true
		mIsMusicOn = this.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).getBoolean(MUSIC, true);
		prepareMusic();
		if (mIsMusicOn) {
			resumeMusic();
		}
	}

	/**
	 * Service binder, allowing activities to bind to it and use the service methods
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy() {
		if (mMediaPlayer != null) {
			stopMusic();
		}
	}

	public class MediaBinder extends Binder {
		MediaService getService() {
			return MediaService.this;
		}
	}

	public void saveMusicPreferences() {
		Editor editor = this.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).edit();
		editor.putBoolean(MUSIC, mIsMusicOn);
		editor.apply(); // async unlike Editor.commit()
	}

	public void prepareMusic() {
		mMediaPlayer = MediaPlayer.create(this, R.raw.theme);
		mMediaPlayer.setLooping(true);
	}

	/**
	 * Pause/resume the background music
	 */
	public void toggleMusic() {
		if (mIsMusicOn) {
			pauseMusic();
		} else {
			resumeMusic();
		}
	}

	public void resumeMusic() {
		mIsMusicOn = true;
		saveMusicPreferences();
		mMediaPlayer.start();
	}

	public void pauseMusic() {
		if (mMediaPlayer.isPlaying()) {
			mIsMusicOn = false;
			saveMusicPreferences();
			mMediaPlayer.pause();
		}
	}

	public void stopMusic() {
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}

	public boolean isMusicOn() {
		return mIsMusicOn;
	}
}
