package com.petrovdevelopment.squashsquash;

import android.app.Application;

import com.petrovdevelopment.squashsquash.utils.TextManager;

/**
 * Main application class. 
 * Extending Application to load the custom font asset
 * @author andrey
 *
 */
public class MainApplication extends Application{
	public static final String DIALOG = "dialog"; //title of the dialog fragment
	public static final String PREFERENCES = "preferences";
	
	private TextManager mTextManager;
	private SoundManager mSoundManager;
	private MediaManager mMediaManager;
	
	@Override
	public void onCreate() {
		super.onCreate();		
		mTextManager = new TextManager(this);
		mMediaManager = new MediaManager(this);
		mSoundManager = new SoundManager(this);
	}

	public TextManager getTextManager() {
		return mTextManager;
	}
	
	public SoundManager getSoundManager() {
		return mSoundManager;
	}
	
	public MediaManager getMediaManager() {
		return mMediaManager;
	}
}
