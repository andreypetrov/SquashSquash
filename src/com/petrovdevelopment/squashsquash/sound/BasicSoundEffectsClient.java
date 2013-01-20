package com.petrovdevelopment.squashsquash.sound;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.petrovdevelopment.squashsquash.R;
import com.petrovdevelopment.squashsquash.sound.SoundEffectsManager.SoundEffect;

/**
 * A simple client of the sound effects manager. It can optionally have a mute/unmute sound effects button assigned to it
 * TODO refactor the music service to become a music manager and both with sound manager to be in the same service
 * @author andrey
 * 
 */
public class BasicSoundEffectsClient implements SoundEffectsClient {
	SoundEffectsManager mSoundEffectsManager;
	ImageView mSfxButton;

	/**
	 * Create a new sound effects client.
	 * 
	 * @param soundEffectsManager
	 *            - the sound manager, provided by the application context
	 * @param sfxButton
	 *            if this is null, then the client will not be able to mute/unmute the sound effects
	 */
	public BasicSoundEffectsClient(SoundEffectsManager soundEffectsManager, ImageView sfxButton) {
		mSoundEffectsManager = soundEffectsManager;
		mSfxButton = sfxButton;
		
		// if there is a sound effects button, assign a listener to it
		if (mSfxButton != null) {
			sfxButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleMute();
					//TODO: instead like that there should be maybe an event callback in the soundEffects manager, 
					//because we have to update simultaniously different activities
					updateSfxButtonImage();
				}
			});
		}
	}

	/**
	 * Update the button's image. Requires images sfx_on and sfx_off to be in the resources
	 * @param sfxButton
	 */
	@Override
	public void updateSfxButtonImage() {
		if (mSoundEffectsManager.isSoundOn()) {
			mSfxButton.setImageResource(R.drawable.sfx_on);
		} else {
			mSfxButton.setImageResource(R.drawable.sfx_off);
		}
	}

	@Override
	public void toggleMute() {
		mSoundEffectsManager.toggleMute();
	}

	@Override
	public void playSound(SoundEffect soundEffect) {
		mSoundEffectsManager.playSound(soundEffect);
	}

	@Override
	public ImageView getSfxButton() {
		return mSfxButton;
	}

	@Override
	public void setSfxButtonInvisible() {
		if (mSfxButton != null) {
			mSfxButton.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void setSfxButtonGone() {
		if (mSfxButton != null) {
			mSfxButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void setSfxButtonVisible() {
		if (mSfxButton != null) {
			mSfxButton.setVisibility(View.VISIBLE);
		}
	}

}
