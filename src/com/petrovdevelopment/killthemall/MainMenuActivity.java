package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		Log.i(this.getClass().getSimpleName(), "onNewGame called");	
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void onClickOptions(View view) {
		Log.i(this.getClass().getSimpleName(), "onOptions called");
	}
	
	public void onClickInfo(View view) {
		Log.i(this.getClass().getSimpleName(), "onInfo called");
	}
	
	public void onClickExit(View view) {
		Log.i(this.getClass().getSimpleName(), "onExit called");
	}
}
