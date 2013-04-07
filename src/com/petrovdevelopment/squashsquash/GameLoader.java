package com.petrovdevelopment.squashsquash;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.petrovdevelopment.squashsquash.game.Background;
import com.petrovdevelopment.squashsquash.game.DeathEffectContainer;
import com.petrovdevelopment.squashsquash.game.NpcContainer;
import com.petrovdevelopment.squashsquash.game.NpcType;

/**
 * Class responsible for loading of all resources.
 * Bitmaps, sounds, etc.
 * TODO: add progress bar while loading?
 * @author andrey
 *
 */
public class GameLoader {
	protected GameView mGameView;
	protected Context mContext; 
	public final String ENEMIES_COUNT = "enemies_count";
	public final int ENEMIES_COUNT_DEFAULT = 6;
	public final String FRIENDS_COUNT = "friends_count";
	public final int FRIENDS_COUNT_DEFAULT = 6;
	
	public GameLoader(Context context, GameView gameView) {
		mGameView = gameView;
		mContext = context;
	}

	public Background loadBackground(){
		return new Background(mGameView.getResources(), R.drawable.wall_bg, mGameView.getWidth(), mGameView.getHeight());
	}

	public NpcContainer loadNpcContainer() {
		NpcContainer npcContainer = NpcContainer.create(mGameView);
		int enemiesCount = mContext.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).getInt(ENEMIES_COUNT, ENEMIES_COUNT_DEFAULT);
		int friendsCount = mContext.getSharedPreferences(MainApplication.PREFERENCES, Context.MODE_PRIVATE).getInt(FRIENDS_COUNT, FRIENDS_COUNT_DEFAULT);
		//TODO refactor to create only the certain number of enemies/friends (demons/humans)
		//Create one NPC from every possible type
		
		//enemies span 0-5, friends 6-11.
		//Better have in enemyNpc and friendNpc an array with the resources and load from there
		Random random = new Random();
		
		for (int i = 0; i<enemiesCount;i++) {
			int npcTypeIndex = random.nextInt(6);
			npcContainer.createNpc(NpcType.values()[npcTypeIndex]);
		}
		for (int i=0; i<friendsCount;i++) {
			int npcTypeIndex = 6+random.nextInt(6);
			npcContainer.createNpc(NpcType.values()[npcTypeIndex]);
		}
	    
		/*for (NpcType npcType : NpcType.values()) {
			npcContainer.createNpc(npcType);
		}*/
		return npcContainer;
	}

	public DeathEffectContainer loadDeathEffectContainer() {
		Bitmap bitmapBloodDemon = BitmapFactory.decodeResource(mGameView.getResources(), R.drawable.blood_demon);
		Bitmap bitmapBloodHuman = BitmapFactory.decodeResource(mGameView.getResources(), R.drawable.blood_human);
		DeathEffectContainer deathEffectContainer = DeathEffectContainer.create(mGameView, bitmapBloodDemon, bitmapBloodHuman);
		return deathEffectContainer;
	}

}
