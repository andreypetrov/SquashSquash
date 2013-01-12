package com.petrovdevelopment.squashsquash.game;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

/**
 * Class representing the background (mostly just an image on the back of the canvas)
 * It needs to be actually rendered before any other element otherwise it will not be a background.
 * TODO: use loader to load all the images in the beginning (and whenever needed later?)
 * 
 * @author andrey
 * 
 */
public class Background implements GameElement {
	private BitmapDrawable mBitmapDrawable;
	//private Matrix mBitmapMatrix;
	public Background(Resources resources, int resourceId, int width, int height) {
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
		mBitmapDrawable = new BitmapDrawable(resources, bitmap);
		mBitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
		mBitmapDrawable.setTileModeY(Shader.TileMode.REPEAT);
		mBitmapDrawable.setBounds(0, 0, width, height);
	}
	
	@Override
	public void update() {
		//do nothing
	}
	
	@Override
	public void render(Canvas canvas) {
		mBitmapDrawable.draw(canvas);
	}
}
