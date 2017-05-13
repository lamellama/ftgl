package com.forthegreatleader;


import java.util.Vector;
import com.forthegreatleader.GlobalState;
import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.PositionPacket;
import com.forthegreatleader.Packet.ProjectilePacket;

import android.content.Context;
//import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;

/**
 * Player Entity
 * Update()receives gyro sensor information and time
 */
public class AIPlayerObject extends CharacterObject{
	private static final int COLOUR = Color.rgb(133, 0, 0);
	private static final int LIFE = 20;
	private static final int TRAIL_COLOUR = Color.rgb(200, 150, 100);
	
	private static final int WEAPON_HEIGHT = 3;
	private static final int WEAPON_WIDTH = 2;
	private static final int WEAPON_LIFE = 1;
	private static final int WEAPON_POWER = 25;
	private static final int PROJECTILE_COLOUR = Color.rgb(100, 255, 255);
	private static final float WEAPON_FIRE_RATE = 1.5f;
	private static final int  MAX_AMMO = 1;
	private static final float TRAIL_VELOCITY = -0.2f;
	
    //Line API constructor
    public AIPlayerObject(Context context, PositionPacket posPack, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
    	super(LIFE, COLOUR, posPack, canvasPack, explosionBitmapList, projectileList);
    	
		//mRes = context.getResources();
		//mProjectileList = projectileList;
		
		init();
		
		//mTrailPaint.setAntiAlias(true);
		
	}
    
    private void init(){
    	///mWeaponList = new Vector<WeaponObject>();
    	mParticleTrail = new ParticleSystem(0.1f, 0.5f, mPosX, mPosY + mHalfHeight, 15.0f, 0, true);
    	mPower = 1000;
    	mTrailPaint = new Paint();
		mTrailPaint.setColor(TRAIL_COLOUR);
		mTrailPaint.setStrokeWidth(4);
		mTrailPaint.setStrokeJoin(Paint.Join.ROUND);
		mTrailPaint.setStyle(Style.STROKE);
		mTrailPaint.setStrokeCap(Cap.ROUND);
		mPosY = (mCanvasHeight + mHeight);
		
		mWeapon = new WeaponObject(GlobalState.PLAYERSIDE,
				PROJECTILE_COLOUR,
				new ProjectilePacket(WEAPON_LIFE, WEAPON_POWER),
				new Packet.PositionPacket(mPosX, mPosY, /* height */ WEAPON_HEIGHT, /* width */ WEAPON_WIDTH),
				/* heading */0, 
				new Packet.MovementPacket(/* initialVelocityX */ 0, /* Random */ 0, /* initialVelocityY */ -40.0f, /* Random */ 0, /* propulsionX */ 0, /* propulsionY */ 40.0f),
				/* fire delay */ WEAPON_FIRE_RATE, 
				/* Max Ammo ->*/ MAX_AMMO, 
				/* type */GlobalState.UPGRATETYPE_SIMPLE,
				new Packet.CanvasPacket(mCanvasWidth, mCanvasHeight),
				new Packet.TrailPacket(/* trail Creation*/ 1.5f, /* trail life */ 2.0f, TRAIL_COLOUR, /* yVelocity */ TRAIL_VELOCITY),
				new Packet.ClusterPacket(/* clusterCount */ 0, /* clusterTime */ 0),
				mProjectileList);
		
		mWeapon.chargeOff();// Firing on
		mArrowPaint.setColor(COLOUR);
    }
    
    private WeaponObject mWeapon;
    

	private final static int SIGHT_RADIUS = 3;
    final private float mMass = 100f;
    //private float mExtraMass = 0f;
    
    private float mSX;
    @SuppressWarnings("unused")
	private float mSY;
    
    private ParticleSystem mParticleTrail;
    private Paint mTrailPaint;
   
    final private float mFriction = 0.01f;
    final private double mOneMinusFriction = 1.0 - mFriction;
    private boolean mAvoiding = true;
    private float mAvoidX;
    private float mOldDistance = 10000000;
    public void avoid(float x, float y){
    	// if point is closer than previous closest point
    	// avoid this one instead
    		mAvoiding = true;
    		float lengthX;
			float lengthY;
			lengthX = Math.abs(getPosX() - x);
			lengthY = Math.abs(getPosY() - y);
			float distance = (float) Math.sqrt(lengthX + lengthY);
    		if(distance < mOldDistance){
    			mAvoidX = x;
    			mOldDistance = distance;
    		}
   
    }
    // Not used (automatically stops avoiding each update)
    public void stopAvoiding(){
    	mAvoiding = false;
    }
    
    public float getSightRadius(){
    	return mWidth*SIGHT_RADIUS;
    }

    public void update(double dT, double dTC){
    	super.update(dT);
    	if(mState == GlobalState.STATE_PLAYING){
	    	float sx =0;  
	    	//sy = 0;
	    	if(mAvoiding == true){// Avoid enemy missiles
	    		if(mAvoidX > mPosX){// find side is the missile on and move away from it
	    			sx = mAvoidX/100;
	    		}
	    		else{
	    			sx = -mAvoidX/100;
	    		}
	    	}
	    	else// just move back and forth
	    	{
	    		//Fluctuate the x position between 0 and 1 to create movement using SINE wave
	    		sx = (float)-(((mCanvasWidth*((Math.sin(mTimeElapsed/5)*0.5)+0.5))-mPosX)/mCanvasWidth);
	    		mSX = sx;
	  	
	    	}
	    	mAvoiding = false;
	    	mOldDistance = 10000;
	    	//float mTotalMass = mMass + mExtraMass;
	    	// Do Player physics
	        // Force of gravity applied to our virtual object
	        //final float m = 1000f; // mass of our virtual object
	        final float gx = -sx * mMass;
	     //   final float gy = -sy * mMass;
	
	        final float invm = 1.0f / mMass;
	        final float ax = gx * invm;
	      //  final float ay = gy * invm;
	        
	        mSX = ax;
	      //  mSY = ay;
	
			final float x = (float)(mPosX + mOneMinusFriction * dTC * (mPosX - mLastPosX) + mAccelX * dT);
			//final float y = (float)(mPosY + mOneMinusFriction * dTC * (mPosY - mLastPosY) + mAccelY * dT);
	
			//if(ax!=0){
			mLastPosX = mPosX;
			mPosX = x;
			mAccelX = ax;
	
	        mLastPosY = mPosY;
	       // mPosY = y;
	   //     mAccelY = ay;
			//}
	        if(mWeapon != null)
	        	mWeapon.update(dT, mPosX, mPosY);
	        

    	}
    	else if (mState == GlobalState.STATE_INTRO){
   		 if(mPosY > mCanvasHeight - (mCanvasHeight * 0.15)){
   			 mPosY -= 30*dT;//TODO something more elegant
   			 
   		 }
   		 mLastPosX = mPosX; // To stop the play jumping position when it changes state
   		 mLastPosY = mPosY;
   		 
   	}
    mParticleTrail.setPos(mPosX, mPosY + mHalfHeight);
    mParticleTrail.update(dT, dTC);
		
    };// End player update
  
    
    
    private void applyMatrix(Canvas c) {
    	  mCamera.save();
    	  mCamera.rotateX((float)(mSX*8));
   		  mCamera.rotateY((float)(-mSX*8));
    	  mCamera.rotateZ(0);
    	  mCamera.getMatrix(mMatrix);

    	  mMatrix.preTranslate(-mPosX, -mPosY); //This is the key to getting the correct viewing perspective
    	  mMatrix.postTranslate(mPosX, mPosY); 

    	  c.concat(mMatrix);
    	  mCamera.restore();    
    	}
    
    @Override
    public void draw(Canvas c)
    {
    	///for(int i = 0; i < mWeaponList.size(); i++ )
    	//	mWeaponList.get(i).draw(c);
    	if(!hasBeenDestroyed())
    		mParticleTrail.drawLines(c, (int)(mHalfWidth -(Math.abs(mSX))), mTrailPaint);
    	//if(mAvoiding == true)
    	//	c.drawCircle(mPosX, mPosY, mWidth*SIGHT_RADIUS, mTrailPaint);
    	c.save();
    	applyMatrix(c);
    	super.draw(c);
    	if(!hasBeenDestroyed())
    		drawStar(c);
    	c.restore();

    }
    
	
	public void firingOn()						{	};
	
	public void firingOff()						{	};

    
	
}
