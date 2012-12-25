package com.petrovdevelopment.killthemall;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * TODO: use darker icons for the ActionBar
 * TODO: add overflow icon to the ActionBar
 * TODO: In this branch build the Activities and menus and all extra things around the maing game
 * TODO: Add scoring, begin and end activities
 * TODO: Pause it onPause and stop it onStop! etc. Use LunarLander to get the ideas from
 * @author andrey
 *
 */
public class GameActivity extends Activity {
	
	private GameView mGameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//remove title bar
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	
		setContentView(R.layout.activity_main);		
//		mGameView = (GameView) findViewById(R.id.game_view);
		
		//load GameView dynamically
		//mGameView = new GameView(this);
		//setContentView(mGameView);
				
		//set transparent background of the action bar
		//getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
		
		//TODO: for testing only now
		getActionBar().hide();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
}
