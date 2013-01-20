package com.petrovdevelopment.squashsquash.sound;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.petrovdevelopment.squashsquash.R;
import com.petrovdevelopment.squashsquash.sound.MediaService.MediaBinder;

/**
 * Base activity client of the MediaService. All activities that want to bound to the media can extend this class. This should
 * be usually all activities in the game.
 * @author andrey
 * 
 */
public abstract class MediaClientActivity extends Activity {
	private ServiceConnection mMediaConnection;
	private MediaService mMediaService;
	private boolean mIsBound;
	private Intent mMediaIntent;

	private ImageView mMusicButton;
	private ImageView mSfxButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMediaConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mMediaService = ((MediaBinder) service).getService();
				// Try to do this twice - here and in onStart - depending which one will execute first
				updateMusicButtonImage();
				updateSfxButtonImage();
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

		// Try to do this twice - here and in onServiceConnected - depending which one will execute first
		updateMusicButtonImage();
		updateSfxButtonImage();
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

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// BUTTONS
	// //////////////////////////////////////////////////////////////////////////////////////////////

	public void initializeMediaButtons(ImageView sfxButton, ImageView musicButton) {
		setAndInitSfxButton(sfxButton);
		setAndInitMusicButton(musicButton);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	//
	// SOUND EFFECTS BUTTTON METHODS
	//
	// //////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Set the sound effects button and its onClick listener
	 * 
	 * @param sfxButton
	 */
	private void setAndInitSfxButton(ImageView sfxButton) {
		mSfxButton = sfxButton;
		if (mSfxButton != null) {
			sfxButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getMediaService().toggleSfx();
					updateSfxButtonImage();
				}
			});
		}
	}

	/**
	 * If there is a sound effects button present in the activity and the activity has connected to the media service, then
	 * change the button's animation based on the media's isSfxOn() value
	 */
	private void updateSfxButtonImage() {
		if (getSfxButton() != null && getMediaService() != null) {
			if (getMediaService().isSfxOn()) {
				getSfxButton().setImageResource(R.drawable.sfx_on);
			} else {
				getSfxButton().setImageResource(R.drawable.sfx_off);
			}
		}
	}

	private ImageView getSfxButton() {
		return mSfxButton;
	}

	public void setSfxButtonInvisible() {
		if (mSfxButton != null) {
			mSfxButton.setVisibility(View.INVISIBLE);
		}
	}

	public void setSfxButtonGone() {
		if (mSfxButton != null) {
			mSfxButton.setVisibility(View.GONE);
		}
	}

	public void setSfxButtonVisible() {
		if (mSfxButton != null) {
			mSfxButton.setVisibility(View.VISIBLE);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	//
	// MUSIC BUTTTON METHODS
	//
	// //////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Set the music button and its onClick listener
	 * 
	 * @param musicButton
	 */
	private void setAndInitMusicButton(ImageView musicButton) {
		mMusicButton = musicButton;
		if (mMusicButton != null) {
			mMusicButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getMediaService().toggleMusic();
					updateMusicButtonImage();
				}
			});
		}
	}

	/**
	 * If there is a music button present in the activity and the activity has connected to the media service, then change the
	 * button's animation based on the media's isMusicOn() value
	 */
	private void updateMusicButtonImage() {
		if (getMusicButton() != null && getMediaService() != null) {
			if (getMediaService().isMusicOn()) {
				getMusicButton().setImageResource(R.drawable.music_on);
			} else {
				getMusicButton().setImageResource(R.drawable.music_off);
			}
		}
	}

	private ImageView getMusicButton() {
		return mMusicButton;
	}

	public void setMusicButtonInvisible() {
		if (mMusicButton != null) {
			mMusicButton.setVisibility(View.INVISIBLE);
		}
	}

	public void setMusicButtonGone() {
		if (mMusicButton != null) {
			mMusicButton.setVisibility(View.GONE);
		}
	}

	public void setMusicButtonVisible() {
		if (mMusicButton != null) {
			mMusicButton.setVisibility(View.VISIBLE);
		}
	}

}
