package com.petrovdevelopment.killthemall;

import android.graphics.Canvas;

/**
 * All game elements - NPC, PC, Background, etc. - implement this interface
 * @author andrey
 *
 */
public interface GameElement {
	/**
	 * Update the state of the game element
	 */
	void update();
	
	/**
	 * Render the game element on screen
	 * @param canvas
	 */
	void render(Canvas canvas);
}
