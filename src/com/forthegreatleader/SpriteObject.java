package com.forthegreatleader;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;

/**
 * A Sprite
 */
public class SpriteObject extends GameObject{
	//Shared Bitmap sprite
	public SpriteObject(Bitmap bitmap, int newHeight, int newWidth){
		mBitmap = bitmap;
		mHeight = newHeight;
		mWidth = newWidth;
		mHalfWidth = newWidth / 2;
	}
	//OLD Local Bitmap sprite
	public SpriteObject(Resources res, int sprite, int newHeight, int newWidth){
		mHeight = newHeight;
		mWidth = newWidth;
		mHalfWidth = newWidth / 2;
        mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, sprite), newWidth, newHeight, true);
	}

	protected int mHalfWidth;
	protected int mHeight;
	protected int mWidth;

    protected Bitmap mBitmap;

    public void update(double time) {	}
    public void setPosX(float pos)	{    	mPosX = pos;	}
    public void setPosY(float pos) 	{    	mPosY = pos;	}
    public float getPosX() 			{    	return mPosX;	}
    public float getPosY() 			{		return mPosY;	}


    
    public void draw(Canvas c){

    	if(mBitmap != null)
    		c.drawBitmap(mBitmap, mPosX-mHalfWidth, mPosY-mHalfWidth, null);
 
    
    }


       

}
