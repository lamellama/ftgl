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
//import android.util.Log;
/**
 * Player Entity
 * Update()receives gyro sensor information and time
 */
public class PlayerObject extends CharacterObject{
	private static final int LIFE = 40;
	private static final int COLOUR = Color.rgb(25, 100, 202);
	private static final int OUTLINE_COLOUR = Color.BLACK;
	//private static final int PROJECTILE_COLOUR = Color.WHITE;
	private static final int TRAIL_COLOUR = Color.rgb(100, 50, 25);
	private static final float TRAIL_VELOCITY = 0.2f;
    
    //Sprite player constructor
    public PlayerObject(Context context, PositionPacket posPack, CanvasPacket canvasPack, SharedBitmapList playerBitmapList, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
    	super(LIFE, posPack, canvasPack, playerBitmapList, explosionBitmapList, projectileList);

		init();
		
	}
    
    //Line API constructor
    public PlayerObject(Context context, PositionPacket posPack, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
    	super(LIFE, COLOUR, posPack, canvasPack, explosionBitmapList, projectileList);

		init();
		
		//mTrailPaint.setAntiAlias(true);
		
	}
    
    private void init(){
    	mWeaponList = new Vector<WeaponObject>();
    	mParticleTrail = new ParticleSystem(0.1f, 0.4f, mPosX, mPosY + mHalfHeight, 30.0f, 0, true);
    	
    	mPower = 1000;
    	mTrailPaint = new Paint();
		mTrailPaint.setColor(TRAIL_COLOUR);
		mTrailPaint.setStrokeWidth(4);
		mTrailPaint.setStrokeJoin(Paint.Join.ROUND);
		mTrailPaint.setStyle(Style.STROKE);
		mTrailPaint.setStrokeCap(Cap.ROUND);
		
		
		
		mOutlinePaint = new Paint();
		mOutlinePaint.setStrokeWidth(4);
		//mOriginalStrokeWidth = strokeWidth;
		mOutlinePaint.setColor(OUTLINE_COLOUR);
		mOutlinePaint.setStrokeJoin(Paint.Join.MITER);
		mOutlinePaint.setStyle(Style.STROKE);
		mOutlinePaint.setStrokeCap(Cap.BUTT);
		
		
		mPosY = (mCanvasHeight + mHeight);
		
		
    }
    
    @Override
    public void reset(){
    	mLife = LIFE;
    	//Log.d("PlayerObject/Reset()", "Life=" + mLife);
    	super.reset();
    	mCurrentScore = 0;
    	mPosY = (mCanvasHeight + mHeight);
    	mParticleTrail.clearPoints();
    	//initialiseBody();
    }	
    
   /* private void refreshUpgrades(GlobalState.PlayerInformation playerInfo){
    	for(int i = 0; i < playerInfo.msUpgrades.length; i++)
    		if((playerInfo.msUpgrades[i].isCreated())&&(!playerInfo.msUpgrades[i].isPurchased())){ // Upgrade has been sold
    			
    		}
    }*/


	@SuppressWarnings("unused")
	private boolean mFiring = true;
	//private float mTouchTimer = 0;
/*	public void onTouch(MotionEvent event){
		if(event.getAction() == android.view.MotionEvent.ACTION_UP){
			//TODO change colour
			for(int i = 0; i < mWeaponList.size(); i++ )
				mWeaponList.get(i).chargeOn();
			mFiring = false;
			mTouchTimer = 0;
			Log.d("PlayerObject onTouch()", "ACTION_DOWN");
		}
		if(event.getAction() == android.view.MotionEvent.ACTION_DOWN){
			for(int i = 0; i < mWeaponList.size(); i++ )
				mWeaponList.get(i).chargeOff();
			mFiring = true;
			Log.d("PlayerObject onTouch()", "ACTION_UP: ChargeTime = " + mTouchTimer);
		}
	}*/
	
	public void onPress(){
		for(int i = 0; i < mWeaponList.size(); i++ )
			mWeaponList.get(i).chargeOff();
		mFiring = true;
		//Log.d("PlayerObject onPress()", "ACTION_DOWN: ChargeTime = " + mTouchTimer);
	}	
	
	public void onRelease(){
		for(int i = 0; i < mWeaponList.size(); i++ )
			mWeaponList.get(i).chargeOn();
		mFiring = false;
		//mTouchTimer = 0;
	//	Log.d("PlayerObject onRelease()", "ACTION_UP");
	}
	
	/*public void onRelease(){
		mFiring = true;
	}*/
	
	protected Vector<WeaponObject> mWeaponList;
    final private float mMass = 1000f;
    private int mExtraAmmo = 0;
    private float mExtraRange = 0f;
    private float mExtraFireRate = 0f;
    private int mExtraFirePower = 1;
    
    private float mSX;
    private float mSY;
    
    private ParticleSystem mParticleTrail;
    protected Paint mTrailPaint;

    private Paint mOutlinePaint;
   
    final private float mFriction = 0.01f;
    final private float mOneMinusFriction = 1.0f - mFriction;
    
    
    
    public void update(float sx, float sy, double dT, double dTC){
    	super.update(dT);
    	
    	if(mState == GlobalState.STATE_PLAYING){
    		//Used by draw() to display user collecting powerup
    		//Log.d("PlayerObject.run", "state: PLAYING ");
    		if(mCollectedMetal > 0)
    			mCollectedMetal-=dT;
    		
    		
    		
    		
	    	float mTotalMass = mMass;
	    	// Do Player physics
	        // Force of gravity applied to our virtual object
	        //final float m = 1000f; // mass of our virtual object
	        final float gx = -sx * mTotalMass;
	        final float gy = -sy * mTotalMass;
	        
	      //  float totalFriction = 1.0f - (mFriction + mExtraFriction);
	        
	        final float invm = 1.0f  / mTotalMass;
	        final float ax = gx * invm;
	        final float ay = gy * invm;
	        
	        mSX = ax;
	        mSY = ay;
	
			final float x = (float)(mPosX + mOneMinusFriction * dTC * (mPosX - mLastPosX) + mAccelX * dT);
			final float y = (float)(mPosY + mOneMinusFriction * dTC * (mPosY - mLastPosY) + mAccelY * dT);
	
			//if(ax!=0){
			mLastPosX = mPosX;
			mPosX = x;
			mAccelX = ax;
	
	        mLastPosY = mPosY;
	        mPosY = y;
	        mAccelY = ay;
			//}
	        //mFiring = true;
	        
	       
	        // Collision
	        // Collision with canvas edges
			if(x < mHalfWidth)
				mPosX = mHalfWidth;
			else if(x > mCanvasWidth -  mHalfWidth)
				mPosX = mCanvasWidth - mHalfWidth;
			if(y < mHalfWidth)
				mPosY = mHalfWidth;
			else if(y > mCanvasHeight -  mHalfWidth){
				mPosY = mCanvasHeight -  mHalfWidth;
				mLastPosY = mCanvasHeight -  mHalfWidth;
			}
    	 }
    	 else if (mState == GlobalState.STATE_INTRO){
    		// Log.v("PlayerObject.run", "state: INTRO ");
    		 if(mPosY > mCanvasHeight - (mCanvasHeight * 0.25)){
    			 mPosY -= 30*dT;//TODO something more elegant
    			 
    		 }
    		 
    		// mFiring = false;
    		
    		 mLastPosX = mPosX; // To stop the play jumping position when it changes state
    		 mLastPosY = mPosY;
    		 
    	 }
    	 else if (mState == GlobalState.STATE_LOADING){
    		// mFiring = false;
    		// Log.v("PlayerObject.run", "state: LOADING ");
    	 }
    	 if(!hasBeenDestroyed()){
    		 mParticleTrail.setPos(mPosX, mPosY + mHalfHeight);
    	 
    		 mParticleTrail.update(dT, dTC);
	    	//Weapon updating
		    // if(mFiring == true){
    		int percentages= 0; 
		    for(int i = 0; i < mWeaponList.size(); i++ ){
		    	 percentages += mWeaponList.get(i).getPercentAmmoFull();
		    	 mWeaponList.get(i).update(dT, mPosX, mPosY);
		    }
     		
     		mPercentAmmoCapacityFull = percentages/mWeaponList.size();//Set this so the colour can be changed in CharacterObject.draw()

    	 }
    };// End player update
    
    public int getScore(){
    	return mCurrentScore;
    }
    
    private int mMetalCount = 0;

    
    
    private int mCurrentScore = 0;
    // Used to display visual queue of a collision to the player
	public void collectMetal(int metal){
		mMetalCount += metal;
		mCurrentScore += metal;
		mCollectedMetal += metal;
    	if(mCollectedMetal > 255)
    		mCollectedMetal = 255;
	}
    
    /**
     * When upgrades are bought or sold in the UI Menu this function has to be called
     * Loops through Shared Global upgrade list to find current active upgrades
     * Creates new weapons and stores them in mWeaponList
     */
    public void activateUpgrades(GlobalState.PlayerInformation playerInfo, Context context){
    	mWeaponList.clear();
    	mExtraFireRate = 1;
    	mExtraFirePower = 1;
    	mExtraRange = 1;
    	mExtraAmmo = 0;
    //	Log.v("activateUpgrades", "PLAYERINFO" + playerInfo);
    //	Log.v("activateUpgrades", "UPGRADE COUNT: " + playerInfo.msUpgrades.length);
    	for(int i = 0; i < playerInfo.msUpgrades.length; i++){
    		playerInfo.msUpgrades[i].removed();
   // 		Log.v("activateUpgrades", "UPGRADE PURCHASED: " + playerInfo.msUpgrades[i].isPurchased());
   // 		Log.v("activateUpgrades", "UPGRADE CREATED: " + playerInfo.msUpgrades[i].isCreated());
    		//if((playerInfo.msUpgrades[i].isPurchased() == true) && (playerInfo.msUpgrades[i].isCreated() == false)){
    		if(playerInfo.msUpgrades[i].isPurchased() == true){

				if( playerInfo.msUpgrades[i].getType() == GlobalState.UPGRATETYPE_NONWEAPON){				//PLAYER PROPERTIES UPGRADE
					mExtraAmmo = playerInfo.msUpgrades[i].getExtraAmmo();
					mExtraFirePower += (int)playerInfo.msUpgrades[i].getFirePower();
					mExtraFireRate -= playerInfo.msUpgrades[i].getFireRate();
					mExtraRange += playerInfo.msUpgrades[i].getRange();
				}
    		}
    	}
    	
    	
    	for(int i = 0; i < playerInfo.msUpgrades.length; i++){
    		playerInfo.msUpgrades[i].removed();
   // 		Log.v("activateUpgrades", "UPGRADE PURCHASED: " + playerInfo.msUpgrades[i].isPurchased());
   // 		Log.v("activateUpgrades", "UPGRADE CREATED: " + playerInfo.msUpgrades[i].isCreated());
    		//if((playerInfo.msUpgrades[i].isPurchased() == true) && (playerInfo.msUpgrades[i].isCreated() == false)){
    		if(playerInfo.msUpgrades[i].isPurchased() == true){

    			if( playerInfo.msUpgrades[i].getType() != GlobalState.UPGRATETYPE_NONWEAPON){
    					mWeaponList.add(new WeaponObject(GlobalState.PLAYERSIDE, 
    							playerInfo.msUpgrades[i].getProjectileColour(),
    							new ProjectilePacket(playerInfo.msUpgrades[i].getLife(), (int)(playerInfo.msUpgrades[i].getFirePower() * mExtraFirePower), playerInfo.msUpgrades[i].getLifeTime() * mExtraRange),
		    					new Packet.PositionPacket(mPosX, mPosY, playerInfo.msUpgrades[i].getHeight(), playerInfo.msUpgrades[i].getWidth()),
		    					/* heading */180, 
		    					new Packet.MovementPacket(playerInfo.msUpgrades[i].getInitialVelocityX(), playerInfo.msUpgrades[i].getInitialVelocityXRandom(), playerInfo.msUpgrades[i].getInitialVelocityY(), playerInfo.msUpgrades[i].getInitialVelocityYRandom(), playerInfo.msUpgrades[i].getPropulsionX(), playerInfo.msUpgrades[i].getPropulsionY() ),
		    					/* fire delay */playerInfo.msUpgrades[i].getFireRate() * mExtraFireRate, 
		    					/* maximum ammo */ playerInfo.msUpgrades[i].getMaxAmmo() + mExtraAmmo,
		    					/* type */playerInfo.msUpgrades[i].getType(), 
		    					new Packet.CanvasPacket(mCanvasWidth, mCanvasHeight),
		    					new Packet.TrailPacket(playerInfo.msUpgrades[i].getTrailCreationSpeed(), playerInfo.msUpgrades[i].getTrailLife(), playerInfo.msUpgrades[i].getTrailColour(), /* yVelocity */ TRAIL_VELOCITY),
		    					new Packet.ClusterPacket(playerInfo.msUpgrades[i].getClusterCount(), playerInfo.msUpgrades[i].getClusterTime()),
		    					mProjectileList));
    					playerInfo.msUpgrades[i].created();
    			}
    		}
    	}
    }
    
    
    
    private void applyMatrix(Canvas c) {
    	  mCamera.save();
    	  mCamera.rotateX((float)(mSY*8));
   		  mCamera.rotateY((float)(-mSX*8));
    	  mCamera.rotateZ(0);
    	  mCamera.getMatrix(mMatrix);

    	  mMatrix.preTranslate(-mPosX, -mPosY); //This is the key to getting the correct viewing perspective
    	  mMatrix.postTranslate(mPosX, mPosY); 

    	  c.concat(mMatrix);
    	  mCamera.restore();    
    	}
    
    //Override the bounding box functions to give the transformed bounding box
    @Override
    public float getBBBottom()	{ return (float)(mPosY + (mHalfHeight - (Math.abs(mSY))));	}
    @Override
    public float getBBTop()		{ return (float)(mPosY - (mHalfHeight - (Math.abs(mSY))));	}
    @Override
    public float getBBLeft()	{ return (float)(mPosX - (mHalfWidth -(Math.abs(mSX))));	}
    @Override
    public float getBBRight()	{ return (float)(mPosX + (mHalfWidth -(Math.abs(mSX))));	}
    
 
    
    @Override
    public void draw(Canvas c)
    {
    	//for(int i = 0; i < mWeaponList.size(); i++ )
    	//	mWeaponList.get(i).draw(c);
    	if(!hasBeenDestroyed())
    		mParticleTrail.drawLines(c, (int)(mHalfWidth -(Math.abs(mSX))), mTrailPaint);
    	
    	
    	
    	c.save();
    	applyMatrix(c);
    	super.draw(c);
    	
    	if(!hasBeenDestroyed())
    		drawStar(c);
    	
    	c.restore();
    	
   /* 	Paint tempPaint = new Paint();
    	tempPaint.setColor(Color.CYAN);
    	tempPaint.setStyle(Style.FILL);
    	c.drawRect(getBoundingBox(), tempPaint);*/
    }
    
    public Vector<WeaponObject> getWeaponList()	{	return mWeaponList;		}
    

	
	public void firingOn()						{	};
	
	public void firingOff()						{	};

    
	
}
