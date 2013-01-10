package com.petrovdevelopment.killthemall;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Main application class. 
 * Extending Application to load the custom font asset
 * @author andrey
 *
 */
public class MainApplication extends Application{
	public static final String ASSETS_FONT_LOCATION = "fonts/ObelixPro-cyr.ttf";
	public static final int FONT_SIZE = 12;
			
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
}
