package com.forthegreatleader;


import java.util.Vector;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;



import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Enemy Entity (Created and destroyed by EnemyManager)
 * Extends Character object overrides update for specialised movement
 */
public class EnemyObject extends CharacterObject{
	private static final int COLOUR = Color.DKGRAY;
	
	public EnemyObject(int life, PositionPacket posPack, MovementPacket movePack, CanvasPacket canvasPack, SharedBitmapList sprites , SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
    	super(life, posPack, movePack, canvasPack, sprites, explosionBitmapList, projectileList);

    	init();
	
    	
	}
  
    
    public EnemyObject(int life, PositionPacket posPack, MovementPacket movePack, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
    	super(life, COLOUR, posPack, movePack, canvasPack, explosionBitmapList, projectileList);

    	init();
	
	}

    private void init(){
    	mPower = mLife;
    }
    
    private float mZRotation = 0;

    @Override
    public void update(double time){
    	super.update(time);
    	
    	mMovePack.initialVelocityX += mMovePack.xPropulsion * time;
    	mMovePack.initialVelocityY += mMovePack.yPropulsion * time;
		mLastPosX = mPosX;
    	mLastPosY = mPosY;
    	mPosY += mMovePack.initialVelocityY * time;
    	mPosX += mMovePack.initialVelocityX * time;
    	
    	mZRotation = (float)(Math.atan2(mLastPosX - mPosX, mLastPosY - mPosY) * (57.2957795));
    	
    	//TODO This only really needs to be done just before the object is detached as it makes no difference
    	for(int i =mProjectileBodyList.size()-1; i >= 0 ; i--)
    		mProjectileBodyList.get(i).updateRotation(mZRotation);

    };
    
    public double norm(float x1, float y1){
    	return Math.sqrt((x1*x1) + (y1*y1));
    }
    
    public double dot(float x1, float y1, float x2, float y2){
    	return (x1 * x2) + (y1 * y2);
    }
   // private double mAngle; // TODO
    private void applyMatrix(Canvas c) {

    	
		mCamera.save();
		//mCamera.rotateX((float)(0));
		//mCamera.rotateY((float)(0));
		// 57.2957795 = 180 / pi (radians to degrees) - Rotates to the direction it has travelled
		mCamera.rotateZ(mZRotation);
		mCamera.getMatrix(mMatrix);
			
		mMatrix.preTranslate(-mPosX, -mPosY); //This is the key to getting the correct viewing perspective
		mMatrix.postTranslate(mPosX, mPosY); 
			
		c.concat(mMatrix);
		mCamera.restore();    
  	}
    
    @Override
    public void draw(Canvas c)
    {
  //  	if(CIRCLE_COLLIDED)
//    		c.drawCircle(mPosX, mPosY, mHalfWidth, mArrowArcPaint);
 ///   	if(COLLIDED)
  //  		c.drawRect(getBoundingBox(), mArrowArcPaint);
    	
    	c.save();
    	applyMatrix(c);
     	super.draw(c);
     	c.restore();
    	
    	
    }

}
