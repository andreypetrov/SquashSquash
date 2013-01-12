package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

public class MainMenuActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

	
	public void onClickNewGame(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	//TODO maybe remove this for now?
	public void onClickOptions(View view) {
		Log.i(this.getClass().getSimpleName(), "onOptions called");
	}
	
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
