package com.forthegreatleader;

import android.content.res.Resources;

import android.graphics.Canvas;
import android.graphics.Bitmap;


/**
 * An object which holds one level tile
 * Stored in levelData.mBGSpriteList
 */
public class LevelTileSpriteObject extends SpriteObject{
	public LevelTileSpriteObject(Bitmap tileBitmap, int newHeight, int newWidth){
		super(tileBitmap, newHeight, newWidth);

        //mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, sprite), newWidth, newHeight, true);
	}
	
	public LevelTileSpriteObject(Resources res, int sprite, int newHeight, int newWidth){
		super(res, sprite, newHeight, newWidth);

        //TODO STOP CREATING THE BITMAP AGAIN AND PASS THE SHARED BITMAP LIST
	}


	
	protected int mColumns = 0;
	protected int mSpeed = 0;
   // protected Bitmap mBitmap;
    
    public void setSpeed(int speed) {    	mSpeed = speed;	}
    public int getSpeed() 			{    	return mSpeed;	}
    public void setColumns(int columns){	mColumns = columns;	}
    public int getColumns() 			{    	return mColumns;	}
    private boolean mMirrored = false;

    public void mirror(){mMirrored = true;};
    

    public void draw(Canvas c){
    	if(mBitmap != null)
	    	if(mMirrored){
		    	c.save();
		    	c.scale(1.0f, -1.0f);
		    	//c.translate(0,  -mBitmap.getHeight());
		    	c.drawBitmap(mBitmap, mPosX-mHalfWidth, -mPosY-mHalfWidth, null);
		    	c.restore();    
	    	}
	    	else
	    		super.draw(c);
    };
       

}
