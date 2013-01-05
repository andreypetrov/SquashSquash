package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
	public static final String ASSETS_FONT_LOCATION = "fonts/ObelixPro-cyr.ttf";
	public static final String DIALOG = "dialog";
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
	
	//TODO maybe remove this for now?
	public void onClickOptions(View view) {
		Log.i(this.getClass().getSimpleName(), "onOptions called");
	}
	
	public void onClickInfo(View view) {
		Log.i(this.getClass().getSimpleName(), "onInfo called");
		InfoDialog infoDialog = new InfoDialog();
		infoDialog.show(getFragmentManager(), DIALOG);
	}
	
	public void onClickExit(View view) {
		Log.i(this.getClass().getSimpleName(), "onExit called");
		//TODO add confirmation fragment dialog
		finish();
	}
	
	public void setTextViewFont(TextView textView) {
		Typeface font = Typeface.createFromAsset(getAssets(), ASSETS_FONT_LOCATION);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		textView.setTypeface(font);
		//set text size to 12dip. Problem: it reverts the font back to plain
		
	}
}
