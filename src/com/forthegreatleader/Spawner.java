package com.forthegreatleader;
/**
 * A Spawner, Used by enemyManager to create enemies
 * Uses random values to vary enemies
 */
public class Spawner{
	public Spawner(){}
	public float mStartTime;
	public float mEndTime;
	public float mIntervalTime;
	public int mIntervalRandomPercent = 0;
	public float mTimer = 0;
	public float mPosX;
	public float mPosY;
	public int mLevel;
	public int mYSpeed = 0;
	public int mYSpeedRandomPercent = 0;
	public float mYSpeedCurl = 0;
	public int mXSpeed = 0;
	public int mXSpeedRandomPercent = 0;
	public float mXSpeedCurl = 0;
	
	public float getNextInterval(){
		if(mIntervalRandomPercent > 0)
			return (float)(mIntervalTime + ((Math.random() - 0.5) * ((float)mIntervalRandomPercent / 100))) ;
		else 
			return mIntervalTime;
	}
	
	public float getYSpeed(){
			//TODO random
			return (float)((float)mYSpeed + ((Math.random() - 0.5) * (float)mYSpeedRandomPercent));

	}
	public float getYSpeedCurl(){

			return mYSpeedCurl ;

	}
	public float getXSpeed(){

			return (float)((float)mXSpeed + ((Math.random() - 0.5) * (float)mXSpeedRandomPercent)) ;

			

	}
	public float getXSpeedCurl(){

			return mXSpeedCurl ;


	}
	
}
