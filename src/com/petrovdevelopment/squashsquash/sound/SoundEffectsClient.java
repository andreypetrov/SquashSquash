package com.petrovdevelopment.squashsquash.sound;

import android.widget.ImageView;

import com.petrovdevelopment.squashsquash.sound.SoundEffectsManager.SoundEffect;

public interface SoundEffectsClient {
	public void toggleMute();
	public void playSound(SoundEffect soundEffect);
	
	//Button control methods
	public ImageView getSfxButton();
	void setSfxButtonInvisible();
	void setSfxButtonGone();
	void setSfxButtonVisible();
}
