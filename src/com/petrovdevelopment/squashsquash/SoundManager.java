package com.petrovdevelopment.squashsquash;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

//TODO choose jolly sounds that go well with the jolly music
public class SoundManager {
	public static final String SOUND = "sound";
	public static final String MUSIC = "music";
	public static final String THEME = "theme";

	private boolean mIsSoundOn;

	private SoundPool mSoundPool;
	private HashMap<String, Integer> mSoundPoolMap;
	private HashMap<String, Integer> mSoundStreamMap;
	private Context mContext;
	private AudioManager mAudioManager;

	public SoundManager(Context context) {
		// get should the sound be on from the preferences file. If the value is not existing in the file yet, default is
		// true.
		mIsSoundOn = context.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).getBoolean(SOUND, true);
		
		mContext = context;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

		mSoundPoolMap = new HashMap<String, Integer>();
		mSoundStreamMap = new HashMap<String, Integer>();

		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

		loadSounds();

	}

	/**
	 * Load initially the sounds and initialize the onLoadCompleteListener
	 */
	public void loadSounds() {
		//creates and prepares the MediaPlayer

		
		/*
		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				// playSound(THEME, -1);
			}
		});
		int soundId = mSoundPool.load(mContext, R.raw.theme, 1);
		mSoundPoolMap.put(THEME, soundId);*/
	}

	/**
	 * Play sound once.
	 * 
	 * @param soundName
	 * @return
	 */
	public void playSound(String soundName) {
		playSound(soundName, 0);
	}

	public void stopSound(String soundName) {
		mSoundPool.stop(mSoundStreamMap.get(soundName));
	}

	/**
	 * Mute/unmute the sound
	 */
	public void toggleSound() {
		mIsSoundOn = !mIsSoundOn;
		mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, !mIsSoundOn);
	}

	
	/**
	 * Play sound given number of loops.
	 * 
	 * @param soundName
	 * @param loop
	 *            number of sound repetitions -1 = infinity, 0 = no repeat
	 * @return
	 */
	public void playSound(String soundName, int loop) {
		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int streamId = mSoundPool.play(mSoundPoolMap.get(soundName), streamVolume, streamVolume, 1, loop, 1f);
		mSoundStreamMap.put(soundName, streamId);
	}
}
