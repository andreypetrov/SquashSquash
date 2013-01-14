package com.petrovdevelopment.squashsquash;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;

import com.petrovdevelopment.squashsquash.MediaService.MediaBinder;

/**
 * Base activity client of the MediaService. All activities that want to bound to the media can extend this class. This should
 * be usually all activities in the game.
 * 
 * @author andrey
 * 
 */
public class MediaClientActivity extends Activity {
	private ServiceConnection mMediaConnection;
	private MediaService mMediaService;
	private boolean mIsBound;
	private Intent mMediaIntent;
	private ImageView mMusicButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMediaConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mMediaService = ((MediaBinder) service).getService();
				//Try to do this twice - here and in onStart - depending which one will execute first
				toggleMusicButtonImage(getMusicButton());
				mIsBound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				mMediaService = null;
				mIsBound = false;
			}
		};

		mMediaIntent = new Intent(this, MediaService.class);
	}

	/**
	 * If Activity A starts activity B, then A.onStop() executes AFTER B.onStart(), which ensures the Bound Service will
	 * continue existing if both activities are clients of it
	 */
	@Override
	protected void onStart() {
		bindService(getMediaIntent(), getMediaConnection(), Context.BIND_AUTO_CREATE);

		//Try to do this twice - here and in onServiceConnected - depending which one will execute first
		toggleMusicButtonImage(getMusicButton());
		super.onStart();
	}

	@Override
	protected void onStop() {
		if (isBound()) {
			unbindService(getMediaConnection());
		}
		super.onStop();
	}

	public MediaService getMediaService() {
		return mMediaService;
	}

	public ServiceConnection getMediaConnection() {
		return mMediaConnection;
	}

	public boolean isBound() {
		return mIsBound;
	}

	public Intent getMediaIntent() {
		return mMediaIntent;
	}

	// MUSIC BUTTON METHODS
	/**
	 * Called when the Music ImageButton is clicked. Toggles sound on/off The music button in the layout of the activity
	 * should call this method
	 */
	public void onClickMusic(View view) {
		toggleMusic();
		toggleMusicButtonImage((ImageView) view);
	}

	/**
	 * If there is a music button present in the activity and the activity has connected to the media service, 
	 * then change the button's animation based on the media's isSoundOn()
	 * value
	 */
	protected void toggleMusicButtonImage(ImageView musicButton) {
		if (getMusicButton() != null &&  getMediaService()!= null) {
			if (getMediaService().isMusicOn()) {
				getMusicButton().setImageResource(R.drawable.sound_on);
			} else {
				getMusicButton().setImageResource(R.drawable.sound_off);
			}
		}
	}

	public ImageView getMusicButton() {
		return mMusicButton;
	}

	public void setMusicButton(ImageView musicButton) {
		mMusicButton = musicButton;
	}

	// PLAYBACK METHODS
	/**
	 * pause/resume the background music
	 */
	public void toggleMusic() {
		getMediaService().toggleMusic();
	}

	public void pauseMusic() {
		getMediaService().pauseMusic();
	}

	public void resumeMusic() {
		getMediaService().resumeMusic();
	}

}
