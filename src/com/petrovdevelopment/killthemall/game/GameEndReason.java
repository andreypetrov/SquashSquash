package com.petrovdevelopment.killthemall.game;

public enum GameEndReason {
	NONE, TIME_IS_UP, ALL_ALIENS_DEAD, ALL_HUMEN_DEAD;

	public String getMessage(int mScore, int mTimePassed) {
		String message="";
		switch(this) {
		case NONE:
			message = "This is strange...";
			break;
		case TIME_IS_UP:	
			message = "Sorry, your time is up!\n" +
					  "You got  " + mScore + " points\n" +
					  "and the aliens busted you!";			  
			break;
		case ALL_ALIENS_DEAD:
			message = "Congratulations! You squashed them all!\n" +  
					  "You saved the humankind in " + mTimePassed + " seconds\n" +
					  "and you scored " + mScore + " points! Wow!";
			break;
		case ALL_HUMEN_DEAD:
			message = "Eh... Are your fingers too big or what?\n" +
					"You destroyed the humankind in " + mTimePassed + " seconds\n" +
					"and you scored " + mScore + " points. Eh... ";
			break;
		}
		return message;
	}
}