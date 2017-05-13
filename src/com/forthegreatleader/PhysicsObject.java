package com.forthegreatleader;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Any sprite object should derive this class
 * Holds any number of Bitmaps and can animate between them
 */
public class PhysicsObject extends AnimatableObject{
	
	//Player constructor (no bitmap)
	public PhysicsObject(PositionPacket posPack, CanvasPacket canvasPack) {
		super(posPack, canvasPack);
		//to prevent null pointer
		mMovePack = new Packet.MovementPacket(0,0,0,0,0,0);
	}
	//Player Constructor (bitmap)
	public PhysicsObject(PositionPacket posPack, CanvasPacket canvasPack, SharedBitmapList bitmapList) {
		super(posPack, canvasPack, bitmapList);
	}
	//No bitmap
	public PhysicsObject(PositionPacket posPack, CanvasPacket canvasPack, MovementPacket movePack) {
		super(posPack, canvasPack);
		init(movePack);
	}
	//Bitmap
	public PhysicsObject(PositionPacket posPack, CanvasPacket canvasPack, MovementPacket movePack, SharedBitmapList bitmapList) {
		super(posPack, canvasPack, bitmapList);
		init(movePack);
	}
	
	protected float mRandomisedInitialVelocityX = 0, mRandomisedInitialVelocityY = 0;
 
    public void update(double time) {
    	super.update(time);
    	if(isAlive()){
    		
    		mRandomisedInitialVelocityY -= mMovePack.yPropulsion * time;
    		mRandomisedInitialVelocityX -= mMovePack.xPropulsion * time;
    		mPosY -= mRandomisedInitialVelocityY * time;
	    	mPosX -= mRandomisedInitialVelocityX * time;
	    	
	    	
    	}
    	mTimeElapsed += time;
	}
    
    private void init(MovementPacket movePack){
    	
    	
    	//mMovePack = new Packet.MovementPacket(movePack.initialVelocityX, movePack.initialVelocityY, movePack.xPropulsion,  movePack.yPropulsion);
    	mMovePack = movePack;
    	mMovePack.initialVelocityY *= mScreenAspectRatio;
    	mMovePack.yPropulsion*=mScreenAspectRatio;
    	mRandomisedInitialVelocityX = (float)(mMovePack.initialVelocityX + (((Math.random()-0.5) * mMovePack.xRand) * (mMovePack.initialVelocityX / 100)));
    	mRandomisedInitialVelocityY = (float)(mMovePack.initialVelocityY + (((Math.random()-0.5) * mMovePack.yRand) * (mMovePack.initialVelocityY / 100)));
		mResetPosX = mPosX;
		mResetPosY = mPosY;
		
    }
    
    //protected float mInitialVelocityX, mInitialVelocityY, mPropulsionX, mPropulsionY;

    protected Packet.MovementPacket mMovePack;
    
	public void reset(){
    	mAlive = true;
    	
    	mDamage = 0;
    	
    	mPosY = mResetPosY;
    	mPosX = mResetPosX;
    }
    
    protected float mResetPosX = 0;
    protected float mResetPosY = 0;
    protected boolean COLLIDED = false;
    protected boolean CIRCLE_COLLIDED = false;
    protected boolean mFinished = false;
    public void finish(){mFinished = true;}
    public boolean isFinished(){return mFinished;}
    protected int mLife = 20;
    protected double mLifeTime;
    protected boolean mImmortal = true;
    protected int mDamage = 0;
    protected boolean mAlive = true;
    protected int mPower = 1;
    protected float mTimeElapsed = 0;
    public int getPower(){return mPower;}
    public int lifeLeft(){return mLife-mDamage;}
    public boolean isAlive(){return mAlive;}
    
    
    public int getPointValue(){
    	return mLife;
    }
    
    public Rect getBoundingBox(){
    	return new Rect((int)getBBLeft(), (int)getBBTop(), (int)getBBRight(), (int)getBBBottom());
    }
    
    public float getBBBottom()	{ return (mPosY + mHalfHeight);	}
    public float getBBTop()		{ return (mPosY - mHalfHeight);	}
    public float getBBLeft()	{ return (mPosX - mHalfWidth);	}
    public float getBBRight()	{ return (mPosX + mHalfWidth);	}
    
    public boolean finished(){return mFinished;}
    public int getDamageTaken(){return mDamage;}
    
    //Take damage and check if dead
    public boolean hasBeenDestroyed(){
    	if((mImmortal == false) && (mTimeElapsed > mLifeTime)){
    		mAlive = false;
    		return true;
    	}
    	else if(mLife <= mDamage){
	    	mAlive = false;
	    	return true;
	    }
	   	else
	   		return false;
    }
    public void takeDamage(int damage){
    	if(damage > 0){
    		mDamage += damage;
    	}
    	
    }

    
    public void draw(Canvas c){
    	super.draw(c);
    		
    }
    

}
