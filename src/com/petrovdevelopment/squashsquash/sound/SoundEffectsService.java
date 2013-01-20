package com.petrovdevelopment.squashsquash.sound;

import com.petrovdevelopment.squashsquash.sound.SoundEffectsManager.SoundEffect;

/**
 * Background Sound Effects Service
 * @author andrey
 *
 */
public interface SoundEffectsService {

	//SOUND EFFECT METHODS
	public abstract void toggleSfx();

	public abstract void playSound(SoundEffect soundEffect);

	public abstract boolean isSfxOn();

}