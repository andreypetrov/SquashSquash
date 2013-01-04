package com.petrovdevelopment.killthemall;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.petrovdevelopment.killthemall.game.World;

public class EndGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String gameEndReason = getIntent().getStringExtra(World.GAME_END_REASON);
		super.onCreate(savedInstanceState);
		// Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(gameEndReason);
	    setContentView(textView);
		//setContentView(R.layout.activity_end_game);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_end_game, menu);
		return true;
	}

}
