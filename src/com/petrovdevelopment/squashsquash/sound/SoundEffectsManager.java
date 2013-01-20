package com.petrovdevelopment.squashsquash.sound;

import java.util.HashMap;

import com.petrovdevelopment.squashsquash.MainApplication;
import com.petrovdevelopment.squashsquash.R;
import com.petrovdevelopment.squashsquash.utils.U;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

/**
 * A class responsible for the sound effects of the game, which are happening right now only on clicks.
 * 
 * @author andrey
 * 
 */
public class SoundEffectsManager {
	public static final String SOUND = "sound";
	private boolean mIsSoundOn;

	public enum SoundEffect {
		DEATH_DEMON, DEATH_HUMAN
	}

	private SoundPool mSoundPool;
	private HashMap<SoundEffect, Integer> mSoundPoolMap;
	private HashMap<SoundEffect, Integer> mSoundStreamMap;
	private Context mContext;
	private AudioManager mAudioManager;

	public SoundEffectsManager(Context context) {
		// Get the sound value from the preferences file.
		// If the value is not existing in the file yet, default is true.
		mIsSoundOn = context.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).getBoolean(SOUND, true);

		mContext = context;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

		mSoundPoolMap = new HashMap<SoundEffect, Integer>();
		mSoundStreamMap = new HashMap<SoundEffect, Integer>();

		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				// We do not really care if the sounds have been loaded or not.
				// If they have not been yet, the attempt to play them will fail silently, without throwing an error
				U.log(this, "Sound effect loaded");
			}
		});

		loadSounds();

	}

	/**
	 * Load initially the sounds and initialize the onLoadCompleteListener
	 */
	public void loadSounds() {
		// creates and prepares the MediaPlayer
		int soundDemonId = mSoundPool.load(mContext, R.raw.death_enemy, 1);
		//int soundHumanId1 = mSoundPool.load(mContext, R.raw.death_ally1, 1);
		int soundHumanId2 = mSoundPool.load(mContext, R.raw.death_ally, 1);
		mSoundPoolMap.put(SoundEffect.DEATH_DEMON, soundDemonId);
		mSoundPoolMap.put(SoundEffect.DEATH_HUMAN, soundHumanId2);
	}

	/**
	 * Mute/Unmute the sound effects
	 */
	public void toggleMute() {
		if (mIsSoundOn) {
			mute();
		} else {
			unmute();
		}
	}

	public void mute() {
		// the problem is that the Music is also unmuted if we mute the whole AudioManager.STREAM_MUSIC
		// mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
		mIsSoundOn = false;
		saveMusicPreferences();
	}

	public void unmute() {
		// mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
		mIsSoundOn = true;
		saveMusicPreferences();
	}

	/**
	 * Play a sound effect once.
	 * 
	 * @param soundName
	 * @return
	 */
	public void playSound(SoundEffect soundName) {
		playSound(soundName, 0);
	}

	/**
	 * Play sound given number of loops.
	 * 
	 * @param soundName
	 * @param loop
	 *            number of sound repetitions -1 = infinity, 0 = no repeat
	 * @return
	 */
	public void playSound(SoundEffect soundName, int loop) {
		if (mIsSoundOn) {
			int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			int streamId = mSoundPool.play(mSoundPoolMap.get(soundName), streamVolume, streamVolume, 1, loop, 1f);
			mSoundStreamMap.put(soundName, streamId);
		}
	}

	/**
	 * Since the sound effects are so short this will not be used at all probably
	 * 
	 * @param soundName
	 */
	public void stopSound(SoundEffect soundName) {
		mSoundPool.stop(mSoundStreamMap.get(soundName));
	}

	/**
	 * Save the preferences for the sound effects
	 */
	public void saveMusicPreferences() {
		Editor editor = mContext.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).edit();
		editor.putBoolean(SOUND, mIsSoundOn);
		editor.apply();
	}

	/**
	 * Returns if the sound effects are muted or not
	 * 
	 * @return
	 */
	public boolean isSoundOn() {
		return mIsSoundOn;
	}

}
