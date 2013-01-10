package com.petrovdevelopment.killthemall;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Utils {

	/**
	 * Set a custom font recursively to all TextViews in the given ViewGroup
	 * @param parent
	 * @param font
	 */
	public static void setCustomFont(ViewGroup parent, Typeface font, int fontSize) {
		for (int i = 0; i<parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ViewGroup) { //if child is null, this fails
				setCustomFont((ViewGroup)child, font, fontSize);
			} else if (child instanceof TextView){
				customizeView((TextView) child, font, fontSize);
			}
		}
	}
	
	/**
	 * Customize TextView (and its subclasses - e.g. Button) font and font size
	 * @param textView
	 * @param font
	 * @param fontSize
	 */
	private static void customizeView (TextView textView, Typeface font, int fontSize) {
		textView.setTypeface(font);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
	}

}
