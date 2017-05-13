package com.forthegreatleader;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.PositionPacket;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;

/**
 * Any sprite object should derive this class
 * Holds any number of Bitmaps and can animate between them
 */
public class AnimatableObject extends GameObject{
	
	
	public AnimatableObject(Resources res, PositionPacket posPack, CanvasPacket canvasPack, int frames, int[] sprites){
		
		
		mBitmap = new Bitmap[frames];
		mFrames = frames;

		init(posPack, canvasPack);
		for(int i = 0; i < frames; i++)
				mBitmap[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, sprites[i]), (int)mPosX + mWidth, (int)mPosY + mHeight, true);
	}
	public AnimatableObject(Resources res, PositionPacket posPack, CanvasPacket canvasPack, int sprite){
		

		mBitmap = new Bitmap[1];
		mFrames = 1;

		init(posPack, canvasPack);
		
		mBitmap[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, sprite), (int)mPosX + mWidth, (int)mPosY + mHeight, true);
	}
	public AnimatableObject(PositionPacket posPack, CanvasPacket canvasPack){


		init(posPack, canvasPack);

	}
	
	public AnimatableObject(PositionPacket posPack, CanvasPacket canvasPack, SharedBitmapList sprites){

		init(posPack, canvasPack);
		mBitmapList = sprites;
		

	}
	
	private void init(PositionPacket posPack, CanvasPacket canvasPack){
		mPosX = posPack.x;
		mPosY = posPack.y;
		
		
		
		if(canvasPack.canvasWidth > 1){
			mWidth =  posPack.getPixelWidth(canvasPack.canvasWidth);
			mHeight = posPack.getPixelHeight(canvasPack.canvasWidth);
		}
		else{
			mWidth = (int)posPack.width;
			mHeight = (int)posPack.height;
		}
		
		mCanvasWidth = canvasPack.canvasWidth;
		mCanvasHeight = canvasPack.canvasHeight;
		mScreenAspectRatio = (float)((float)canvasPack.canvasHeight / (float)canvasPack.canvasWidth);
		mHalfWidth = mWidth * 0.5f;
		mHalfHeight = mHeight * 0.5f;
		redPaint.setARGB(100, 205, 55, 55);
	}
	
	
	protected SharedBitmapList mBitmapList;
	final protected Paint redPaint = new Paint();
	protected int mAnimFrame = 0;
	protected int mFrames = 0;
    protected float mAccelX = 0;
    protected float mAccelY = 0;
    protected int mWidth;
    protected int mHeight;
    protected int mCanvasWidth= 100;
    protected int mCanvasHeight = 100;
    protected float mScreenAspectRatio = 1;
    protected float mHalfWidth;
    protected float mHalfHeight;
    protected Bitmap[] mBitmap;
    
    public float getRelativeWidth(){
    	return (float)(mWidth / (mCanvasWidth * 0.01));
    }
    
    public float getRelativeHeight(){
    	return (float)(mHeight / (mCanvasWidth * 0.01));
    }
    
    public float getWidth(){
    	return mWidth;
    }
    public float getRadius(){
    	return mHalfWidth;
    }
    public void update(double time) {
	}

    
    
    public void draw(Canvas c){
    	if(mBitmap != null)
			c.drawBitmap(mBitmap[mAnimFrame], mPosX - mHalfWidth, mPosY - mHalfHeight, null);
		else if(mBitmapList != null){
			if(mBitmapList.getListSize() > 0)
				c.drawBitmap(mBitmapList.getBitmap(0), mPosX - mHalfWidth, mPosY - mHalfHeight, null);
		}
    		
    }
    
    public float getPosX(){return mPosX;}
    public float getPosY(){return mPosY;}
    public void setPosX(float pos){mPosX = pos;}
    public void setPosY(float pos){mPosY = pos;}
    
    public int getAniIndex(){return mAnimFrame;}
    public void setAniIndex(int iVar){mAnimFrame = iVar;}

    

}
