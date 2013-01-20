package com.petrovdevelopment.squashsquash.sound;

import android.widget.ImageView;

import com.petrovdevelopment.squashsquash.sound.SoundEffectsManager.SoundEffect;

public interface SoundEffectsClient {
	void toggleMute();
	void playSound(SoundEffect soundEffect);
	
	//Button control methods
	ImageView getSfxButton();
	void setSfxButtonInvisible();
	void setSfxButtonGone();
	void setSfxButtonVisible();
	void updateSfxButtonImage();
}
