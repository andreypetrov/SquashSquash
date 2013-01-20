package com.petrovdevelopment.squashsquash.sound;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.petrovdevelopment.squashsquash.sound.SoundEffectsManager.SoundEffect;

//TODO pause the music only onPause of the game and onMute button click
//TODO resume the music onPlay click (if it is not playing already !isPlaying) and onUnmute button click
//TODO silence the music on losing AutoFocus
//So by default it will be on for the whole game 

/**
 * Service which is a client of both SoundEffectsManager and MusicManager
 * @author andrey
 *
 */
public class MediaService extends Service implements MusicService, SoundEffectsService {

	private IBinder mBinder = new MediaBinder();
	private MusicManager mMusicManager;
	private SoundEffectsManager mSoundEffectsManager;
	
	public MediaService() {
	}

	@Override
	public void onCreate() {
		
		mMusicManager = new MusicManager(this);
		if (mMusicManager.isOn()) {
			resumeMusic();
		}		
		mSoundEffectsManager = new SoundEffectsManager(this);
	}

	/**
	 * Service binder, allowing activities to bind to it and use the service methods
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy() {
		stopMusic();
	}

	public class MediaBinder extends Binder {
		/**
		 * This method will be used to access the current service and through it all its methods
		 * @return
		 */
		MediaService getService() {
			return MediaService.this;
		}
	}


	
	//SOUND EFFECT METHODS
	@Override
	public void toggleSfx() {
		mSoundEffectsManager.toggle();
	}

	@Override
	public void playSound(SoundEffect soundEffect) {
		mSoundEffectsManager.playSound(soundEffect);
	}

	@Override
	public boolean isSfxOn() {
		return mSoundEffectsManager.isOn();
	}
	
	//BACKGROUND MUSIC METHODS
	@Override
	public void toggleMusic() {
		mMusicManager.toggle();
	}

	@Override
	public void resumeMusic() {
		mMusicManager.resume();
	}

	@Override
	public void pauseMusic() {
		mMusicManager.pause();
	}

	@Override
	public void stopMusic() {
		if(mMusicManager!= null) {
			mMusicManager.stop();
		}
	}

	@Override
	public boolean isMusicOn() {
		return mMusicManager.isOn();
	}

}
