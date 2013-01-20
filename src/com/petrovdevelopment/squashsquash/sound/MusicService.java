package com.petrovdevelopment.squashsquash.sound;

/**
 * Background Music Service
 * 
 * @author andrey
 * 
 */
public interface MusicService {
	/**
	 * Pause/resume background music
	 */
	void toggleMusic();

	void resumeMusic();

	void pauseMusic();

	void stopMusic();

	boolean isMusicOn();
}