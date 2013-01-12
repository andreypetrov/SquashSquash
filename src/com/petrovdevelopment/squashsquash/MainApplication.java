package com.petrovdevelopment.squashsquash;

import android.app.Application;
import android.graphics.Typeface;

import com.petrovdevelopment.squashsquash.utils.TextEditor;

/**
 * Main application class. 
 * Extending Application to load the custom font asset
 * @author andrey
 *
 */
public class MainApplication extends Application{
	public static final String DIALOG = "dialog"; //title of the dialog fragment

	private TextEditor mTextEditor;
	private SoundManager mSoundManager;
	
	@Override
	public void onCreate() {
		super.onCreate();		
		mTextEditor = new TextEditor(this);
		mSoundManager = new SoundManager(this);
	}

	public TextEditor getTextEditor() {
		return mTextEditor;
	}
}
