package com.petrovdevelopment.squashsquash;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.petrovdevelopment.squashsquash.fragments.ConfirmDialog;
import com.petrovdevelopment.squashsquash.fragments.InstructionsDialog;
import com.petrovdevelopment.squashsquash.sound.BasicSoundEffectsClient;
import com.petrovdevelopment.squashsquash.sound.MediaClientActivity;
import com.petrovdevelopment.squashsquash.sound.SoundEffectsClient;

//TODO: pause on phone lock (onPause?) the music (check the game music blog entry)
public class MainMenuActivity extends MediaClientActivity {
	SoundEffectsClient mSoundEffectsClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		setMusicButton((ImageView) findViewById(R.id.music));
		ImageView sfxButton = (ImageView) findViewById(R.id.sfx);
		mSoundEffectsClient = new BasicSoundEffectsClient(((MainApplication)getApplication()).getSoundEffectsManager(), sfxButton);
	}
	
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}*/

	@Override
	protected void onStart() {
		super.onStart();
		mSoundEffectsClient.updateSfxButtonImage();
	}
	public void onClickNewGame(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
//	public void onClickOptions(View view) {
//		toggleMusic();
//	}
	
	public void onClickInstructions(View view) {
		InstructionsDialog infoDialog = new InstructionsDialog();
		infoDialog.show(getFragmentManager(), MainApplication.DIALOG);
	}
	
	public void onClickExit(View view) {
		showConfirmDialog();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	showConfirmDialog();
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	/**
	 * Show a dialog asking the user to confirm if they want to leave the game
	 */
	private void showConfirmDialog(){
		ConfirmDialog confirmDialog = new ConfirmDialog();
		confirmDialog.show(getFragmentManager(), MainApplication.DIALOG);
	}
	
}
