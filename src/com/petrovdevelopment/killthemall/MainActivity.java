package com.petrovdevelopment.killthemall;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

/**
 * Add scoring, begin and end activities
 * @author andrey
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//load GameView
		setContentView(new GameView(this));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
