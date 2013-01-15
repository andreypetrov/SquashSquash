package com.petrovdevelopment.squashsquash.utils;

import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Utility class
 * @author andrey
 *
 */
public class U {

	/**
	 * Set a custom font recursively to all TextViews in the given View and its children
	 * 
	 * @param parent
	 * @param font
	 */
	public static void setCustomFont(View view, Typeface font, int fontSize) {
		if (view instanceof TextView) {
			customizeView((TextView) view, font, fontSize);
		} else if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			for (int i = 0; i < parent.getChildCount(); i++) {
				View child = parent.getChildAt(i);
				setCustomFont(child, font, fontSize);
			}
		}
	}

	/**
	 * Customize TextView (and its subclasses - e.g. Button) font and font size
	 * 
	 * @param textView
	 * @param font
	 * @param fontSize
	 */
	private static void customizeView(TextView textView, Typeface font, int fontSize) {
		textView.setTypeface(font);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
	}
	
	/**
	 * A convenience debug method.
	 * Logs an info message in the LogCat.
	 * The log tag is the name of the class generating it (without the package)
	 * Usage: U.log(this, message); 
	 *
	 * @param message
	 */
	public static void log(Object author, String message) {
		Log.i(author.getClass().getSimpleName(), message);
	}

}
