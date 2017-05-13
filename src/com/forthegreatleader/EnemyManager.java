package com.forthegreatleader;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
//import android.util.Log;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.LevelPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Spawner;

/**
 * Loads enemies from a spawner list(attained from LevelData -> level.txt in res/raw)
 * Handles updating and drawing enemies
 */
public class EnemyManager {
	public EnemyManager(Context context, LevelPacket levelInfoPacket, List<Spawner> spawnerList, int canvasWidth, int canvasHeight, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		mSpawnerList = spawnerList;
		mLevelTimer = 0;
		mContext = context;
		mCanvasWidth = canvasWidth;
		mCanvasHeight = canvasHeight;
		mEnemyList = new Vector<EnemyObject>();
		mLevelInfo = levelInfoPacket;
		mExplosionBitmapList = explosionBitmapList;
		mProjectileList = projectileList;
		//mExplosionSpriteIdList = new int[6];
		//for(int i = 0; i < mExplosionSpriteIdList.length; i++)
			//mExplosionSpriteIdList[i] = bitmapInfo.addExplosionBitmap("explosion" + i, 50, 50); // TODO explosion height, width
		//mLevel1EnemySpritesList = new SharedBitmapList(context);
		//mLevel1EnemySpritesList.addBitmap("enemy1", 20, 20);// TODO height, width
	}
	Vector<ProjectileObject> mProjectileList;
	//private SharedBitmapList mLevel1EnemySpritesList;
	private LevelPacket mLevelInfo;
	//private int[] mExplosionSpriteIdList;
	private SharedBitmapList mExplosionBitmapList;
	Context mContext;
	private double mLevelTimer;
	private int mCanvasWidth;
	private int mCanvasHeight;
	private int mEnemySpawnedCount = 0;
	private int mEnemyDestroyedCount = 0;
	private int mEnemyDestroyedTotalCashValue = 0;
	
	private int mState = GlobalState.STATE_LOADING;
	public int getState(){return mState;}
	//public void stateIntro(){mState = GlobalState.STATE_INTRO;} // CURRENTLY NOT USED
	//public void stateLoading(){mState = GlobalState.STATE_LOADING;} // DEFAULT
	public void setState(int state){mState = state;}
	//public void statePlay(){mState = GlobalState.STATE_PLAYING;} // Has to be called for the level timer to begin
	public int getCashEarned(){return mEnemyDestroyedTotalCashValue;}
	public int getEnemyDestroyedCount(){return mEnemyDestroyedCount;}
	public int getEnemySpawnedCount(){return mEnemySpawnedCount;}
	// Spawn point list obtained from level.cfg
	private List<Spawner> mSpawnerList;
	
	// List of all active enemies
	public Vector<EnemyObject> mEnemyList;
	
	public Vector<EnemyObject> getEnemyList(){
		return mEnemyList;
	}
	
	public void drawEnemies(Canvas canvas){
		if(mEnemyList != null)
			for(int i = 0; i < mEnemyList.size(); i++)
	    		mEnemyList.get(i).draw(canvas);
	}
	
	public void update(double dT){

		int i = 0;
		//Update Enemy physics
		if(mEnemyList != null)
			for(i = 0; i < mEnemyList.size(); i++){
				if((mEnemyList.get(i).getBoundingBox().top > mCanvasHeight)||(mEnemyList.get(i).getBoundingBox().left > mCanvasWidth)||(mEnemyList.get(i).getBoundingBox().right < 0)||(mEnemyList.get(i).getBoundingBox().bottom < 0)){		// Enemy below bottom of canvas
					mEnemyList.get(i).finish();
				}
				
				if(!mEnemyList.get(i).isFinished())
					mEnemyList.get(i).update(dT);
				else{
					mEnemyDestroyedTotalCashValue += mEnemyList.get(i).getPointValue();
					mEnemyList.remove(i); // Kill enemy
					mEnemyDestroyedCount++;
					
				}
			}
		
		// Create new enemies from spawner list
		if(mState == GlobalState.STATE_PLAYING){		// Create new enemies when in playing state
			mLevelTimer += dT;
			for(i = 0; i < mSpawnerList.size(); i++){
				if(mSpawnerList.get(i) != null)
				if( mSpawnerList.get(i).mStartTime < mLevelTimer ){		// Spawner start time passed
					
					if( mLevelTimer > mSpawnerList.get(i).mEndTime ){ 	// Spawner ended, DESTROY
						mSpawnerList.remove(i);
					}
					else if(mSpawnerList.get(i).mTimer <= 0){	// Spawner interval passed
						// SPAWN SOME STUFF
						mSpawnerList.get(i).mTimer = mSpawnerList.get(i).getNextInterval(); // Reset interval timer
						//Log.v("EnemyManager/update", "Enemy SPAWNED: " + mEnemySpawnedCount);
						mEnemySpawnedCount++;
						switch (mSpawnerList.get(i).mLevel){
							//Regular basic enemy
							case 1:		mEnemyList.add(new Level1EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							//Regular basic enemy with a bit more health
							case 2:		mEnemyList.add(new Level2EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							// Mini ship
							case 3:		mEnemyList.add(new Level3EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							//LARGE ships
							case 4:		mEnemyList.add(new Level4EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 5:		mEnemyList.add(new Level5EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 6:		mEnemyList.add(new Level6EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 7:		mEnemyList.add(new Level7EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							//Ships with guns
							case 11:		mEnemyList.add(new Level11EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 12:		mEnemyList.add(new Level12EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 13:		mEnemyList.add(new Level13EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							// tanks
							case 101:		mEnemyList.add(new Level101EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															mLevelInfo.scrollSpeed,
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 102:		mEnemyList.add(new Level102EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															mLevelInfo.scrollSpeed,
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 103:		mEnemyList.add(new Level103EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															mLevelInfo.scrollSpeed,
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							// long blue tank
							case 104:		mEnemyList.add(new Level104EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															mLevelInfo.scrollSpeed,
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							case 105:		mEnemyList.add(new Level105EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															mLevelInfo.scrollSpeed,
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							default:	mEnemyList.add(new Level1EnemyObject((int)(mCanvasWidth * mSpawnerList.get(i).mPosX), 0, // XY Position
															new MovementPacket(mSpawnerList.get(i).getXSpeed(), mSpawnerList.get(i).getYSpeed(), mSpawnerList.get(i).getXSpeedCurl(), mSpawnerList.get(i).getYSpeedCurl()),
															new CanvasPacket(mCanvasWidth, mCanvasHeight), 		
															mExplosionBitmapList,
															mProjectileList));
							break;
							
						} // End Switch mLevel
						mEnemyList.lastElement().setPosY(-mEnemyList.lastElement().getRelativeHeight()); //Set Y start position
					}
					if(i < mSpawnerList.size())
							mSpawnerList.get(i).mTimer -= dT;
				}
			}
		}
	}
}
