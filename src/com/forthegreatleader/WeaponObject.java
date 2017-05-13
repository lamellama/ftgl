package com.forthegreatleader;

import java.util.Vector;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.ClusterPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;
import com.forthegreatleader.Packet.ProjectilePacket;
import com.forthegreatleader.Packet.TrailPacket;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * A weapon object
 * Creates and manages projectileObjects
 */
public class WeaponObject extends AnimatableObject{
	public WeaponObject(int side, int colour, ProjectilePacket proPack, PositionPacket positionPack, int heading, MovementPacket movementPack, float delay, int maxAmmo, int type, CanvasPacket canvasPack, TrailPacket trailPack, ClusterPacket clusterPack, Vector<ProjectileObject> projectileList) {
		super(positionPack, canvasPack);
		
		mColour = colour;
		mTrailColour = trailPack.colour;
		mProjectileProperties = proPack;
		mMovePack = movementPack;
		//mPropulsionY = movementPack.yForce;
		//mInitialVelocityX = movementPack.initialXVelocity;
		//mInitialVelocityY = movementPack.initialYVelocity;
		mLoadDelay = delay;
		mMinLoadDelay = delay * 0.25f;
		//mResources = res;
		//mCanvasHeight = canvasPack.canvasHeight;
		mType = type;
		mProjectileList = projectileList;
		mSide = side;
		mTrailCreationTime=trailPack.creationTime;
		if(trailPack.creationTime > 0)
			mTrailable = true;
		mTrailLifeTime= trailPack.lifeTime;
		mClusterCount = clusterPack.clusterCount;
		mClusterTimer = clusterPack.clusterTimer;
		mTrailPacket = trailPack;
	}
	//Spritable bullets
/*	public WeaponObject(Resources res, float x, float y, int height, int width, int heading, float propulsionX, float propulsionY, float initialVelocityX, float initialVelocityY, float delay, int type, int spriteResourceID, int canvasWidth, int canvasHeight, float trailCreationTime, float trailLife, int clusterCount, float clusterTimer, Vector<ProjectileObject> projectileList) {
		super(x, y, height, width);
		mPropulsionX = propulsionX;
		mPropulsionY = propulsionY;
		mInitialVelocityX = initialVelocityX;
		mInitialVelocityY = initialVelocityY;
		mLoadDelay = delay;
		mResources = res;
		mCanvasHeight = canvasHeight;
		mType = type;
		projectileSpriteID = spriteResourceID;
		mProjectileList = projectileList;
		//projectileList = new Vector<ProjectileObject>();
		mTrailCreationTime=trailCreationTime;
		mTrailLifeTime= trailLife;
		if(trailCreationTime > 0)
			mTrailable = true;
		mClusterCount = clusterCount;
		mClusterTimer = clusterTimer;
	}*/
	
	Vector<ProjectileObject> mProjectileList;
	
	Resources mResources;
	
	TrailPacket mTrailPacket;
	int mColour = Color.RED;
	int mTrailColour = Color.WHITE;
	// mType = type of bullet
	private int mSide = GlobalState.PLAYERSIDE;
	public int getSide(){return mSide;}
	private int mClusterCount = 0;
	float mClusterTimer = 0f;
	@SuppressWarnings("unused")
	private boolean mTrailable = false;
	@SuppressWarnings("unused")
	private float mTrailCreationTime=0, mTrailLifeTime=0;
	private int mType = 0;
	//private Bitmap sprite;
	private int projectileSpriteID;
	//protected int mCanvasHeight;
	//private float mPropulsionX, mPropulsionY, mInitialVelocityX, mInitialVelocityY;
	Packet.MovementPacket mMovePack;
	Packet.ProjectilePacket mProjectileProperties;
	private float mLoadDelay = 100;
	private float mMinLoadDelay = 50;
	private float mTimeSinceLastFired = 0;
	private float mTimeSinceLastLoaded = 0;
	private boolean mAlt = false;
	private int mDamage = 1000;//TODO
	private float mExtraFireRate = 0f;
	public void setExtraFireRate(float extra){
		mExtraFireRate = extra;
	}
	@SuppressWarnings("unused")
	private float mExtraFirePower = 0f;
	public void setExtraFirePower(float extra){
		mExtraFirePower = extra;
	}
	
	public int getDamage(){
		return mDamage;
	}
	private int mLoadedCount = 1;
	private int mMaxAmmo = 5; // TODO
	
	public int getPercentAmmoFull(){
		return (100/mMaxAmmo) * mLoadedCount;
	}
	private boolean mCharging = true;
	public void chargeOn(){mCharging = true;}
	public void chargeOff(){mCharging = false;}

    public void update(double dT, float x, float y){
		mPosX = x;
		mPosY = y;
		mTimeSinceLastLoaded += dT;
		mTimeSinceLastFired += dT;
		
		if((mTimeSinceLastLoaded > (mLoadDelay + mExtraFireRate))&&(mLoadedCount <= mMaxAmmo)){
			mTimeSinceLastLoaded = 0;
			mLoadedCount++;
	//		Log.d("WeaponObject", "Loaded: " + mLoadedCount);
		}
		
		if(!mCharging){// if firing / player pressed the screen
			if(mTimeSinceLastFired > mMinLoadDelay){
				if(mLoadedCount > 0){// Ready to fire
					mTimeSinceLastFired = 0; // Reset
					mLoadedCount--;
					float initialXVel;
					float XProp;
					if(mAlt == false){ // Alternate between firing to the right and left
						mAlt = true;
						initialXVel = mMovePack.initialVelocityX;
						XProp = mMovePack.xPropulsion;
					}else{	// alternate X velocity
						mAlt = false;
						initialXVel = -mMovePack.initialVelocityX;
						XProp = -mMovePack.xPropulsion;
					}
						
					if(mType == GlobalState.UPGRATETYPE_SPRITE){
						ProjectileSpriteObject bullet = new ProjectileSpriteObject(mResources, 
		
								new ProjectilePacket(mProjectileProperties.life, mProjectileProperties.power, mProjectileProperties.lifeTime),
								new CanvasPacket(mCanvasWidth, mCanvasHeight),
								new PositionPacket(mPosX, mPosY, mHeight, mWidth), 
								
								new MovementPacket(initialXVel, mMovePack.xRand, mMovePack.initialVelocityY, mMovePack.yRand, XProp, mMovePack.yPropulsion),
								projectileSpriteID, mProjectileList);
						mProjectileList.add(bullet);
					}
					else{
						//PositionPacket newPos = new PositionPacket(mPosX, mPosY, mHeight, mWidth);
						//newPos.setRelativeSize(mCanvasWidth);
						ProjectileLineObject bullet = new ProjectileLineObject(mType, mColour, mSide, 
								
								new ProjectilePacket(mProjectileProperties.life, mProjectileProperties.power, mProjectileProperties.lifeTime),
								new PositionPacket(mPosX, mPosY, getRelativeHeight(), getRelativeWidth()), 
								//newPos,
								new CanvasPacket(mCanvasWidth, mCanvasHeight),
								new MovementPacket(initialXVel, mMovePack.xRand, -mMovePack.initialVelocityY, mMovePack.yRand, XProp, -mMovePack.yPropulsion),
								mTrailPacket, 
								mProjectileList, 
								new ClusterPacket(mClusterCount, mClusterTimer));
						mProjectileList.add(bullet);			
					}
				}
			}
		}
		/*
		if(mTimeSinceLastFired > (mLoadDelay + mExtraFireRate)){
			mTimeSinceLastFired = 0;
			//TODO This is being called with INVALID mType values
			if(mCharging){ // loads the bullets for when charging ends
				mLoadedCount++;
				Log.v("WeaponObject", "Loaded: " + mLoadedCount);
			}
			else{
				while(mLoadedCount > 0){
					mLoadedCount--;
					float initialXVel;
					float XProp;
					if(mAlt == false){ // Alternate between firing to the right and left
						mAlt = true;
						initialXVel = mMovePack.initialVelocityX;
						XProp = mMovePack.xPropulsion;
					}else{	// alternate X velocity
						mAlt = false;
						initialXVel = -mMovePack.initialVelocityX;
						XProp = -mMovePack.xPropulsion;
					}
						
					if(mType == GlobalState.UPGRATETYPE_SPRITE){
						ProjectileSpriteObject bullet = new ProjectileSpriteObject(mResources, 
		
								new ProjectilePacket(mProjectileProperties.life, mProjectileProperties.power, mProjectileProperties.lifeTime),
								new CanvasPacket(mCanvasWidth, mCanvasHeight),
								new PositionPacket(mPosX, mPosY, mHeight, mWidth), 
								
								new MovementPacket(initialXVel, mMovePack.xRand, mMovePack.initialVelocityY, mMovePack.yRand, XProp, mMovePack.yPropulsion),
								projectileSpriteID, mProjectileList);
						mProjectileList.add(bullet);
					}
					else{
						//PositionPacket newPos = new PositionPacket(mPosX, mPosY, mHeight, mWidth);
						//newPos.setRelativeSize(mCanvasWidth);
						ProjectileLineObject bullet = new ProjectileLineObject(mType, mColour, mSide, 
								
								new ProjectilePacket(mProjectileProperties.life, mProjectileProperties.power, mProjectileProperties.lifeTime),
								new PositionPacket(mPosX, mPosY, getRelativeHeight(), getRelativeWidth()), 
								//newPos,
								new CanvasPacket(mCanvasWidth, mCanvasHeight),
								new MovementPacket(initialXVel, mMovePack.xRand, -mMovePack.initialVelocityY, mMovePack.yRand, XProp, -mMovePack.yPropulsion),
								mTrailPacket, 
								mProjectileList, 
								new ClusterPacket(mClusterCount, mClusterTimer));
						mProjectileList.add(bullet);			
					}
					
				}
				mLoadedCount++;
			}
		}*/
    }
    

	
	@Override
	public void draw(Canvas c){

		super.draw(c);
		
    }
		
/*	public Vector<ProjectileObject> getProjectileList(){
		return mProjectileList;
	}*/
	public void setPos(int x, int y){
		mPosX = x;
		mPosY = y;
	}
}
