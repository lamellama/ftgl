
package com.forthegreatleader;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import com.forthegreatleader.GlobalState.PlayerInformation;
import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.LevelPacket;
import com.forthegreatleader.Packet.PositionPacket;
import com.forthegreatleader.SharedBitmapList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * The main game view and a separate thread RtypeThread
 * Instantiates all the main components of the game
 * Player, Enemy Manager, LevelManager
 */
class RtypeView extends SurfaceView implements SurfaceHolder.Callback {
    class RtypeThread extends Thread {

         /* 
          * State-tracking constants
          */
    	
    	public static final int LEVEL_COMPLETE_BONUS = 1000;
       
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;
    	public static final int STATE_ENDED = 6;
        
		
		public boolean bVisible = true;
		public boolean mSurfaceCreated = false;

		public static final int PLAYER_WIDTH = 10;
		public static final int PLAYER_HEIGHT = 10;
		public static final int AIPLAYER_WIDTH = 9;
		public static final int AIPLAYER_HEIGHT = 9;
		
		public static final int STARTING_LEVEL = 1;
		public static final int PLAYER_ENTRANCE_DELAY_SECONDS = 1; // How long before the player starts his entrance
		public static final int PLAYER_INTRO_TIME_SECONDS = 2; // How long the entrance lasts before starting play
		//public static final int LEVEL_END_PAUSE_TIME_SECONDS = 3; // How long the mission complete message stays
		
		//private float mLevelEndedConfirmationTime =0;
     
        /**
         * Current height of the surface/canvas.
         * 
         * @see #setSurfaceSize
         */
        public int mCanvasHeight = 100;

        /**
         * Current width of the surface/canvas.
         * 
         * @see #setSurfaceSize
         */
        public int mCanvasWidth = 100;

        private Paint mBGPaint;


        
        /** Message handler used by thread to interact with TextView */
        private Handler mHandler;

        /** Used to figure out elapsed time between frames */
        private double mLastTime = 0;
        private double mLastDeltaTime = 0;
        private long mSensorTimeStamp;
        private long mCpuTimeStamp;
        
        private int mCurrentLevel = STARTING_LEVEL;

        /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
        private int mMode;

        /** Indicate whether the surface has been created & is ready to draw */
        private boolean mRun = false;

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;
        
     //   private Typeface mFace;
        
        //private int mKillCount = 0;
        private PlayerObject mPlayer;
        private AIPlayerObject mAIPlayer;
        private EnemyObject mEnemy;
        private EnemyManager mEnemyManager;
        private ProjectileManager mProjectileManager;
        private LevelData levelManager;
        public PlayerInformation mPlayerInfo;
        public SharedBitmapList mExplosionBitmapList;
        public SharedBitmapList mPlayerBitmapList;
        
        private float mSensorPosX;
        private float mSensorPosY;
        
       public RtypeThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;
            
            mBGPaint = new Paint();
            mBGPaint.setARGB(255, 0, 255, 0);

            levelManager = new LevelData(context);   
            
            mExplosionBitmapList = new SharedBitmapList(mContext);
        	for(int i = 1; i <= 6; i++)
        		mExplosionBitmapList.addBitmap("explosion" + i, 50, 50); // TODO explosion height, width
        	
        	mPlayerBitmapList = new SharedBitmapList(mContext);
        	mPlayerBitmapList.addBitmap("player_pointer", PLAYER_WIDTH, PLAYER_HEIGHT);
            
        	//mFace = Typeface.createFromAsset(mContext.getAssets(), GlobalState.TYPEFACE_MOLOT);
            

        }
        
       	private boolean mHasLevelLoaded = false;
        public boolean hasLevelLoaded(){
        	return mHasLevelLoaded;
        }
        

        /**
         * Starts the game, setting parameters for the current difficulty.
         */       
        public boolean loadNextLevel(boolean bContinue, GlobalState.PlayerInformation playerInfo){
        	synchronized (mSurfaceHolder) {
        		if(bContinue) // Continue save game game
        			loadSavedGame();
        		else{// Save upgrades purchased after exiting menu but not after loading game
	        		try {
						saveGame(); 
						//Log.d("RtypeView/loadNextLevel", "Saving game");
					} catch (IOException e) {
						//Log.e("RtypeView/loadNextLevel/saveGame", "IOException while saving game:" + e);
						e.printStackTrace();
					}
        		}
        		
        		mHasLevelLoaded = true;
        		//mLastTime = System.currentTimeMillis() + 1000; // Only do this if we are resuming the game?
            	mLastDeltaTime = 0; // TODO figure a better place to put this
            	
            	if(mProjectileManager == null)
            		mProjectileManager = new ProjectileManager(mCanvasHeight);
                else
                	mProjectileManager.reset();
            	
            	if(mPlayer == null){
            		mPlayer = new PlayerObject(mContext, 
            				new PositionPacket((mCanvasWidth / 2), mCanvasHeight  - 50, PLAYER_WIDTH, PLAYER_HEIGHT), 
            				new CanvasPacket(mCanvasWidth, mCanvasHeight),  
            				mExplosionBitmapList, mProjectileManager.mProjectileList );
            	}
            	else
            		mPlayer.reset();
            	
            	
            	
            	upgradePlayer(playerInfo);
            	//mMissionCompleteText.setTypeface(mFace);
            	
            	//mMissionCompleteText.setVisibility(ImageView.INVISIBLE);
            	
        		levelManager.release();
        		if(!levelManager.loadLevel( mCurrentLevel, mCanvasWidth, mCanvasHeight)){
        			//Level failed to load
        			mHasLevelLoaded = false;
        			//Assume game complete, load game complete screen
        			//This should be caught by doLevelStateConditionChecks()
        			//Log.d("!levelManager.loadLevel", "No more levels found, End of game");

        			setState(STATE_ENDED);
        			
        	//		Intent i = new Intent(mContext, UIMainMenuActivity.class);
        	//		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        	//		mContext.startActivity(i);
            		
        			
        			
        			Intent i = new Intent(mContext, UIGameCompleteMenuActivity.class);
            		mContext.startActivity(i);
            		setRunning(false);
            		//thread.stop();
            		return false;
            		//TODO activity.finish()
            		//stop();
   
            		
        		}
        		
        		if(levelManager.doWeNeedAnAIPlayer())
	            	mAIPlayer = new AIPlayerObject(mContext, 
	        				new PositionPacket((int)(mCanvasWidth * 0.25), mCanvasHeight  - 50, AIPLAYER_WIDTH, AIPLAYER_HEIGHT), 
	        				new CanvasPacket(mCanvasWidth, mCanvasHeight),  
	        				mExplosionBitmapList, mProjectileManager.mProjectileList );
        		else
        			if(mAIPlayer != null){
        				mAIPlayer.finish(); //Remove AIPlayer if one exists and is not in this level
        				mAIPlayer = null;
        			}
        			

        		//TODO reset()
        		mEnemyManager = new EnemyManager(mContext, new LevelPacket(levelManager.getLevelSpeed()), levelManager.getSawnerList(), mCanvasWidth, mCanvasHeight, mExplosionBitmapList, mProjectileManager.mProjectileList);
        		
        		
        		
        		unpause();
        		return true;
        	}
        }

        /**
         * Pauses the physics update & animation.
         */
        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
            }
        }
        
        /**
         * Resumes from a pause.
         */
        public void unpause() {

        	mLastTime = getCurrentTime(); //so the game resumes at the current time
        	
            setState(STATE_RUNNING);
        }      
        

        
        // desired fps
        private final static int    MAX_FPS = 40;
        // maximum number of frames to be skipped
        private final static int    MAX_FRAME_SKIPS = 5;
        // the frame period
        private final static int    FRAME_PERIOD = 1000 / MAX_FPS; 
        
        @Override
        public void run() {
        	Canvas canvas;

        	//Log.d(TAG, "Starting game loop");

        	long beginTime;     // the time when the cycle begun

        	long timeDiff;      // the time it took for the cycle to execute

        	int sleepTime;      // ms to sleep (<0 if we're behind)

        	int framesSkipped;  // number of frames being skipped 
        	
        	sleepTime = 0;

        
        	while(mMode != STATE_ENDED) // This is so that the game loop and thread don't end when a new activity is started
            while (mRun) {
            	canvas = null;


                
	                try {
	                	canvas = mSurfaceHolder.lockCanvas(null);
	                    synchronized (mSurfaceHolder) {
	                    	beginTime = System.currentTimeMillis();
	                    	framesSkipped = 0;  // resetting the frames skipped

	                        if (mMode == STATE_RUNNING){ 
	                        	updatePhysics();
	                        }
	                        if(bVisible == true)
	                        	doDraw(canvas);
	                        // calculate how long did the cycle take
	                        timeDiff = System.currentTimeMillis() - beginTime;
	                        // calculate sleep time
	                        sleepTime = (int)(FRAME_PERIOD - timeDiff);
	                        
	                        if (sleepTime > 0) {
	                        	// if sleepTime > 0 we're OK
	                        try {
	                        	// send the thread to sleep for a short period
	                        	// very useful for battery saving
	                        	Thread.sleep(sleepTime);
	                        	} catch (InterruptedException e) {}
	                        }
	                        while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
	                        	// we need to catch up
	                        	// update without rendering
	                        	if (mMode == STATE_RUNNING){ 
		                        	updatePhysics();
		                        	//doCollisionTesting();
		                        }
	                        	// add frame period to check if in next frame
	                        	sleepTime += FRAME_PERIOD;
	                        	framesSkipped++;
	                        	
	                        	
	                        	
	                        	
	                        }

	                      
	                        	 

	                    }
	                } finally {
	                    // do this in a finally so that if an exception is thrown
	                    // during the above, we don't leave the Surface in an
	                    // inconsistent state
	                    if (canvas != null) {
	                        mSurfaceHolder.unlockCanvasAndPost(canvas);
	                    }
	                }
                
            }
        }



        /**
         * Sets the current difficulty.
         * 
         * @param difficulty
         */
 /*       public void setDifficulty(int difficulty) {
            synchronized (mSurfaceHolder) {

            }
        }*/

        /**
         * Sets if the engine is currently firing.
         
        public void setFiring(boolean firing) {
            synchronized (mSurfaceHolder) {

            }
        }*/

        /**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         * 
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
           // setState(STATE_RUNNING);
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @see #setState(int, CharSequence)
         * @param mode one of the STATE_* constants
         */
        public void setState(int mode) {
            synchronized (mSurfaceHolder) {
            	mMode = mode;
            }
        }


        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;

                // don't forget to resize the background image
 //               mBackgroundImage = mBackgroundImage.createScaledBitmap(
 //                       mBackgroundImage, width, height, true);
            }
        }
        
        public void upgradePlayer(GlobalState.PlayerInformation playerInfo){
        	mPlayer.activateUpgrades(playerInfo, mContext);
        }

        
        void doOrientationUpdate(float sx, float sy, long timestamp){
        	//mPlayer.updateSensorPosition(sx, sy);
        	mSensorPosX = sx;
        	mSensorPosY = sy;
        	
        	mSensorTimeStamp = timestamp;
            mCpuTimeStamp = System.nanoTime();
        }

   

        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void doDraw(Canvas canvas) {
            // Draw the background image. Operations on the Canvas accumulate
            // so this is like clearing the screen.
        	canvas.drawARGB(255, 0, 0, 0 );
        	
        	if(levelManager != null)
        		levelManager.drawBelow(canvas);
        	if(mAIPlayer != null)
        		mAIPlayer.draw(canvas);
        	if(mPlayer != null)
        		mPlayer.draw(canvas);
        	
        	if(mEnemy != null)
        		mEnemy.draw(canvas);
        	if(mEnemyManager != null)
        		mEnemyManager.drawEnemies(canvas);
        	if(mProjectileManager != null)
        		mProjectileManager.draw(canvas);
        	if(levelManager != null)
        		levelManager.drawOnTop(canvas);

        	
        }
        
        public int FPSCount = 0;
        public double perSecTime = 0;
        
        public double getCurrentTime(){
        	return mSensorTimeStamp + (System.nanoTime() - mCpuTimeStamp);
        }

        /**
         *  Finds current timings and calls all Objects update function
         *  Updates current level
         */
        private Boolean updatePhysics() {

            
            double now = getCurrentTime();
            if (mLastTime != 0) {
                double dT = (now - mLastTime) * (1.0 / 1000000000.0);
                if (mLastDeltaTime != 0) {
                	
                    double dTC = dT / mLastDeltaTime;
                    float sx = mSensorPosX;
                    float sy = mSensorPosY;
                    
                    if(mPlayer != null)
                    	mPlayer.update(sx, sy, dT, dTC ); // Update player with new time and sensor headings
                    if(mAIPlayer != null)
                    	mAIPlayer.update( dT, dTC );
                    
                    if(mEnemyManager != null)
                    	mEnemyManager.update(dT); // Update Enemies
                    
                    if(mProjectileManager != null)
                    	mProjectileManager.update(dT, dTC);
                    	
                    if(levelManager != null){
                    	levelManager.update(dT);
                    	if(!doLevelStateConditionChecks())
                    		return false; //Game ended
                    }
                    
                
                    doCollisionTesting();
                }
                perSecTime+=dT;
                if(perSecTime < 1.0){
                	FPSCount++;
                }
                else{
                	Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", "FPS: " + FPSCount);
                    //b.putString("text", str.toString());
                   // b.putInt("viz", View.VISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                	FPSCount = 0;
                	perSecTime = 0;
                }
            	
                
                mLastDeltaTime = dT;
            }
            mLastTime = now;  
            return true;
        }
        
        String FILENAME = "SaveFile";
        //Loads a previous save game

		public void loadSavedGame(){
        	String saveFile = ReadSaveFile(mContext);
            String data[] = saveFile.split(",");
            if(data.length>0){
            	mCurrentLevel = Integer.parseInt(data[0]);						// READ Level
            	thread.mPlayerInfo.setBalance(Integer.parseInt(data[1]));		// READ Balance
	            for(int j =1; j < data.length; j++){							// READ Upgrades
	            	//data[j].;
	            	for(int i = 0; i < thread.mPlayerInfo.msUpgrades.length; i++){ //Loop all the upgrades
						//Find the upgrade name
						if(data[j].contentEquals(thread.mPlayerInfo.msUpgrades[i].getName())){
							thread.mPlayerInfo.msUpgrades[i].purchase();		// Buy upgrade
						}
	            	}
	            }
            }
        }
        
        //Opens FILENAME and returns the contents as a string
        public String ReadSaveFile(Context context){
        	FileInputStream fIn = null; 
        	InputStreamReader isr = null;

        	char[] inputBuffer = new char[255]; 
        	String data = null;

        	try{
        		fIn = context.openFileInput(FILENAME);       
        		isr = new InputStreamReader(fIn); 
        		isr.read(inputBuffer); 
        		data = new String(inputBuffer);
        		//Log.d("ReadSaveFile", "Save data: " + data);
        		Toast.makeText(context, "Save read",Toast.LENGTH_SHORT).show();
        	} 
        	catch (Exception e) {       
        		e.printStackTrace(); 
        		Toast.makeText(context, "Save info not read",Toast.LENGTH_SHORT).show();
        	} 
        	finally { 
        		try { 
        			isr.close(); 
        			fIn.close(); 
        		} catch (IOException e) { 
        			e.printStackTrace(); 
        		} 
        	}
        	return data; 
        }
   
        //Saves current level, cash balance and purchased upgrades
        private void saveGame() throws IOException{

        	String s = new String();
        	s += mCurrentLevel + ",";										// ADD Current Level
        	s += thread.mPlayerInfo.getBalance() + ",";						// ADD Current Balance
        	for(int i = 0; i < thread.mPlayerInfo.msUpgrades.length; i++){	// ADD Upgrades
        		if(thread.mPlayerInfo.msUpgrades[i].isPurchased() == true){	
        			s = s + thread.mPlayerInfo.msUpgrades[i].getName() + ",";
        		}
        	}
        	//Log.d("RtypeView/saveGame()", "String to write to file: " + s);
        	if(s.length() > 0)
        		WriteToSaveFile(mContext, s);
        }
        
        
        // Write data String to FILENAME
        public void WriteToSaveFile(Context context, String data){
        	FileOutputStream fOut = null;
        	OutputStreamWriter osw = null;

        	try{
        		fOut = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);      
        		osw = new OutputStreamWriter(fOut);
        		osw.write(data);
        		//Log.v("WriteToSaveFile", "Written to save: " + data);
        		osw.flush();

        	}

        	catch (Exception e) {    
        		Log.e("WriteToSaveFile", "While opening/writing save game file: " + e);
        		e.printStackTrace();
        	}

        	finally {
        		try {
        			osw.close();
        			fOut.close();
        		} catch (IOException e) {
        			Log.e("WriteToSaveFile", "While closing save game file: " + e);
        			e.printStackTrace();
        		}
        	}
        }

        
        //Checks the current condition of the level. eg. has the level ended? player died?
        private Boolean doLevelStateConditionChecks(){
        	// LEVEL HAS REACHED THE TIME LIMIT
        	if(levelManager.hasLevelEnded()){ // Level Finished, Load UI
        		mHasLevelLoaded = false;
        		
        		//InputStream is = getResources().openRawResource(getResources().getIdentifier("level" + mCurrentLevel, "raw", mContext.getPackageName()));
        		
        		if(levelManager.hasLevelFailed()){ // (FAILED COMPLETION)
        			pause();
            		Intent i = new Intent(mContext, UITabsActivity.class);
            		
            		i.putExtras(prepareBundleForUIActivity(GlobalState.LEVEL_FAILED));
            		mContext.startActivity(i);
            		setRunning(false);
            	
                	//pause();
        		}
        		else {//(SUCCESSFUL COMPLETION)
        			pause();
        			Intent i;
        			if(levelManager.didKoreaWin()){
        				i = new Intent(mContext, UIKoreaWinsActivity.class);
        				try {
							saveGame();
						} catch (IOException e) {
							Log.e("saveGame", "While saving game" + e);
							e.printStackTrace();
						}
        			
        			}else{

	        			mCurrentLevel++;
	        			
	        			try {
							saveGame();
						} catch (IOException e) {
							Log.e("saveGame", "While saving game" + e);
							e.printStackTrace();
						}
						//GAME END CHECK (Check if there are more levels)
		        		if(getResources().getIdentifier("level" + mCurrentLevel, "raw", mContext.getPackageName()) == 0){
		        			//no more levels, reached end of game
		        			//Assume game complete, load game complete screen
		        			//Log.d("RtypeView.doLevelCheckConditionChecks()", "No more levels found, End of game");
	
		        			setState(STATE_ENDED);
	
		        			i = new Intent(mContext, UIGameCompleteMenuActivity.class);
		            		mContext.startActivity(i);
		            		setRunning(false);
		            		return false;
		        		}
		        		
	        			i = new Intent(mContext, UITabsActivity.class);
	            		i.putExtras(prepareBundleForUIActivity(GlobalState.LEVEL_SUCCEEDED));
	            		
        			}
        			mContext.startActivity(i);
            		setRunning(false);
        		}
        	}
        	// LEVEL HAS NOT FINISHED (STILL PLAYING)
        	else if ((levelManager.hasLevelLoaded()) && (mPlayer != null)){// Check if we are ready for the player to enter
        		//PLAYER HAS BEEN KILLED
        		
        		if(mPlayer.hasBeenDestroyed()){
        			levelManager.levelFailed();
        		}
        		if(mAIPlayer!=null)
	        		if(mAIPlayer.hasBeenDestroyed()){
	        			if(levelManager.doesAIDeathEndGameEarly()){
	        				//Game ended early, North Korea wins
	        				// Show North Korea wins View
	        				levelManager.gameCompletedKoreaWin();
	        			}
	        			else{
	        				levelManager.levelFailed();
	        			}
	        		}
        		//DELAY BEFORE PLAYER ENTERS (PRE-INTRO)
        		if(levelManager.getElapsedPlayingTime() > PLAYER_ENTRANCE_DELAY_SECONDS){
        			mPlayer.setState(GlobalState.STATE_INTRO);
        			if(mAIPlayer!=null)
        				mAIPlayer.setState(GlobalState.STATE_INTRO);
        		}
        		
        		// PLAYER SCRIPTED INTRO
        		if(levelManager.getElapsedPlayingTime() > PLAYER_ENTRANCE_DELAY_SECONDS + PLAYER_INTRO_TIME_SECONDS){
        			mPlayer.setState(GlobalState.STATE_PLAYING);
        			if(mAIPlayer!=null)
        				mAIPlayer.setState(GlobalState.STATE_PLAYING);
        			
        			//TELL ENEMIES TO STOP SPAWNING WHILE SCRIPTS ARE BEING DISPLAYED
        			if ((levelManager.getState() == GlobalState.STATE_SCRIPTING) && (mEnemyManager != null)){
                		mEnemyManager.setState(GlobalState.STATE_SCRIPTING);
                	}
        			// TELL ENEMIES THEY CAN START SPAWNING
                	else if ((levelManager.getState() == GlobalState.STATE_PLAYING) && (mEnemyManager != null)){
                		mEnemyManager.setState(GlobalState.STATE_PLAYING);
                	}
        		}
        	}
        	return true;
        }
        
        private Bundle prepareBundleForUIActivity(int endOfLevelCondition){
        	Bundle bundle = new Bundle();
    		bundle.putInt("level", endOfLevelCondition);
    		
    		bundle.putInt("metal", mMetalCollectedValue);
    		int score = 0, bonus = 0, profit =0;
    		if(mEnemyManager!= null){
    			score = mEnemyManager.getCashEarned();
    			bundle.putInt("score", score);
    			bundle.putInt("kills", mEnemyManager.getEnemyDestroyedCount());
    			bundle.putInt("percent", (int)((100/mEnemyManager.getEnemySpawnedCount()) * mEnemyManager.getEnemyDestroyedCount()));
    		}
    		if(mPlayer!=null)
    			bundle.putInt("damage", mPlayer.getDamageTaken());
    		if(levelManager!=null){
    			
	    		if(endOfLevelCondition == GlobalState.LEVEL_SUCCEEDED)
	    			bonus = levelManager.getLevelBonus();

	    		bundle.putInt("bonus", bonus);
    			
    		}
    		profit = mMetalCollectedValue + score - mPlayer.getDamageTaken() + bonus;
    		bundle.putInt("profit", profit);
    		bundle.putInt("oldbalance", mPlayerInfo.getBalance());
    		mPlayerInfo.addCash(profit);
    		bundle.putInt("total", mPlayerInfo.getBalance());
    		return bundle;
        }
        
        public void onRelease(){

        	if(mPlayer!= null){
        		mPlayer.onRelease();
        	}
        }
        
        public void onScreenPress(MotionEvent event){
        	if(levelManager!= null)
        		levelManager.onTouch(event);
        	if(mPlayer!= null){
        		mPlayer.onPress();
        	}
        }
        
        public void onKeyPress(){
        	if(mPlayer!= null){
        		mPlayer.onPress();
        	}
        }
        
        private void testProjectileVSPlayer(int projectileID){
        	if(mPlayer != null){
        		if(mProjectileManager.mProjectileList.get(projectileID).getPosY() >= mPlayer.getBoundingBox().top){
	        		float lengthX;
	    			float lengthY;
	    			lengthX = Math.abs(mPlayer.getPosX() - mProjectileManager.mProjectileList.get(projectileID).getPosX());
	    			lengthY = Math.abs(mPlayer.getPosY() - mProjectileManager.mProjectileList.get(projectileID).getBBTop());
	    			lengthX *= lengthX;
	    			lengthY *= lengthY;
	    			if((lengthX + lengthY) <= (mPlayer.getRadius()*mPlayer.getRadius()) ){
	    				//BOX vs BOX
						if(mPlayer.getBoundingBox().intersect(mProjectileManager.mProjectileList.get(projectileID).getBoundingBox())){
							// Collided, do something about it
	    					int power = mPlayer.getPower();
	    					mPlayer.takeDamage(mProjectileManager.mProjectileList.get(projectileID).getPower());
	    					mPlayer.hasBeenDestroyed();
	    					mPlayer.COLLIDED = true;
	
	    	        		mProjectileManager.mProjectileList.get(projectileID).takeDamage(power);
	    	        		mProjectileManager.mProjectileList.get(projectileID).hasBeenDestroyed();
	    	        		mProjectileManager.mProjectileList.get(projectileID).COLLIDED = true;
						}
	    	        }
        		}
        	}
        }
        
       // int mPlayerCash = 0;
		
		private void testProjectileVSEnemy(int projectileID){
			float lengthX;
			float lengthY;
			for(int i = 0; i < mEnemyManager.mEnemyList.size(); i++){
				// Remove enemy if OUT OF BOUNDS
				//TODO move this to enemyManager
        		if(mEnemyManager.mEnemyList.get(i).finished()) // HAS FINISHED BLOWING UP
        			mEnemyManager.mEnemyList.remove(i); 
        		else if(!mEnemyManager.mEnemyList.get(i).hasBeenDestroyed() && (!mProjectileManager.mProjectileList.get(projectileID).hasBeenDestroyed())){//TEST FOR COLLISION VS PROJECTILE (Circle vs Circle)
					
        			//if(mProjectileManager.mProjectileList.get(projectileID).getPosY() >= mEnemyManager.mEnemyList.get(i).getPosY() - mEnemyManager.mEnemyList.get(i).getRadius())
						if(mProjectileManager.mProjectileList.get(projectileID).getPosY() <= mEnemyManager.mEnemyList.get(i).getBoundingBox().bottom){
        			
							//Log.d("RtypeView/testProjectileVSEnemy", "Enemy Y = projectile Y");
		        			lengthX = Math.abs(mEnemyManager.mEnemyList.get(i).getPosX() - mProjectileManager.mProjectileList.get(projectileID).getPosX());
							lengthY = Math.abs(mEnemyManager.mEnemyList.get(i).getPosY() - mProjectileManager.mProjectileList.get(projectileID).getPosY());
							lengthX *= lengthX;
							lengthY *= lengthY;
							
							//if( Math.sqrt(lengthX + lengthY) <= (mEnemyManager.mEnemyList.get(i).getRadius()) ){ // if Circles have collided
							if( (lengthX + lengthY) <= (mEnemyManager.mEnemyList.get(i).getRadius()*mEnemyManager.mEnemyList.get(i).getRadius()) ){	
								//Log.d("RtypeView/testProjectileVSEnemy", "Enemy Circle collision");
							//BOX vs BOX
								//if(mEnemyManager.mEnemyList.get(i).getBoundingBox().intersect(mProjectileManager.mProjectileList.get(projectileID).getBoundingBox())){
									// Collided, do something about it
									int power = mEnemyManager.mEnemyList.get(i).getPower();
									mEnemyManager.mEnemyList.get(i).takeDamage(mProjectileManager.mProjectileList.get(projectileID).getPower());
									if((mEnemyManager.mEnemyList.get(i).hasBeenDestroyed()) && (mEnemyManager.mEnemyList.get(i).COLLIDED == false)){
										//mEnemyManager.mEnemyDestroyedCount++;
										//mPlayerCash += GlobalState.CASH_VALUE_ENEMY_HIT;
										//mPlayerCash += mEnemyManager.mEnemyList.get(i).getPointValue();
										//Log.d("EnemyManager/update", "Enemy Destroyed, Points: " + mEnemyManager.mEnemyList.get(i).getPointValue() + " Total Destroyed: " + mEnemyManager.mEnemyDestroyedCount + "Total Points: " + mPlayerCash);
									}
				        			mEnemyManager.mEnemyList.get(i).COLLIDED = true;
				        			
				        			
				        				
				        			mProjectileManager.mProjectileList.get(projectileID).takeDamage(power);
				        			mProjectileManager.mProjectileList.get(projectileID).hasBeenDestroyed();
				        			mProjectileManager.mProjectileList.get(projectileID).COLLIDED = true;
								
				        	}
						}
					}
			}
		}
		
		private boolean testProjectileVSAIPlayer(int projectileID){
			boolean inRange = false;
			if(mAIPlayer != null){
				if(mProjectileManager.mProjectileList.get(projectileID).getBoundingBox().bottom >= mAIPlayer.getBoundingBox().top){
	        		float lengthX;
	    			float lengthY;
	    			lengthX = Math.abs(mAIPlayer.getPosX() - mProjectileManager.mProjectileList.get(projectileID).getPosX());
	    			lengthY = Math.abs(mAIPlayer.getPosY() - mProjectileManager.mProjectileList.get(projectileID).getBBTop());
	    			lengthX *= lengthX;
	    			lengthY *= lengthY;
	    			//if( Math.sqrt(lengthX + lengthY) <= (mAIPlayer.getSightRadius()) ){
	    			if((lengthX + lengthY) <= (mAIPlayer.getSightRadius()*mAIPlayer.getSightRadius()) ){
	    				inRange = true;
	    				mAIPlayer.avoid(mProjectileManager.mProjectileList.get(projectileID).getPosX(), mProjectileManager.mProjectileList.get(projectileID).getBBTop());
	    	        		
	    			}
    	        }
        	}
			return inRange;
		}
		
		private void testEnemyVSAIPlayer(int enemyID){
			if((!mEnemyManager.mEnemyList.get(enemyID).hasBeenDestroyed())&&(!mAIPlayer.hasBeenDestroyed())){
				if(mEnemyManager.mEnemyList.get(enemyID).getBoundingBox().bottom >= mAIPlayer.getBoundingBox().top){
					//Log.d("RtypeView/testEnemyVSAIPlayer", "Test Collision");
					mEnemyManager.mEnemyList.get(enemyID).COLLIDED = false;
		    		float lengthX, lengthY;
		    		lengthX = Math.abs(mEnemyManager.mEnemyList.get(enemyID).getPosX() - mAIPlayer.getPosX());
		    		lengthX *= lengthX;
		    		lengthY = Math.abs(mEnemyManager.mEnemyList.get(enemyID).getPosY() - mAIPlayer.getPosY());
		    		lengthY *= lengthY;
		    		//if( Math.sqrt(lengthX + lengthY) <= (mAIPlayer.getRadius() + mEnemyManager.mEnemyList.get(enemyID).getRadius()) ){
		    		float combinedRadius = mAIPlayer.getRadius() + mEnemyManager.mEnemyList.get(enemyID).getRadius();
		    		if((lengthX + lengthY) <= (combinedRadius * combinedRadius) ){
		    			// TODO do more accurate collision detection here
		    			// TODO DIAMOND BB!!!!!!!
		    				// Collided, do something about it
		    				mAIPlayer.COLLIDED = true;
		    				mEnemyManager.mEnemyList.get(enemyID).COLLIDED = true;
		    				
		    				int lifeLeft = mEnemyManager.mEnemyList.get(enemyID).lifeLeft();
		    				mEnemyManager.mEnemyList.get(enemyID).takeDamage(500);

		    				
		    				mAIPlayer.takeDamage(lifeLeft);
		    				mAIPlayer.hasBeenDestroyed();
		    					
		    				
		    		
		    		}
				}
			}
		}
		
		private void testEnemyVSPlayer(int enemyID){
			if((!mEnemyManager.mEnemyList.get(enemyID).hasBeenDestroyed())&&(!mPlayer.hasBeenDestroyed())){
				if(mEnemyManager.mEnemyList.get(enemyID).getBoundingBox().bottom >= mPlayer.getBoundingBox().top){
					//Log.d("RtypeView/testEnemyVSPlayer", "Test Collision");
					mEnemyManager.mEnemyList.get(enemyID).COLLIDED = false;
		    		float lengthX, lengthY;
		    		lengthX = Math.abs(mEnemyManager.mEnemyList.get(enemyID).getPosX() - mPlayer.getPosX());
		    		lengthX *= lengthX;
		    		lengthY = Math.abs(mEnemyManager.mEnemyList.get(enemyID).getPosY() - mPlayer.getPosY());
		    		lengthY *= lengthY;
		    		float combinedRadius = (mPlayer.getRadius() + mEnemyManager.mEnemyList.get(enemyID).getRadius());
		    		if( (lengthX + lengthY) <= (combinedRadius * combinedRadius) ){
		    			// TODO do more accurate collision detection here
		    			// TODO DIAMOND BB!!!!!!!
		    				// Collided, do something about it
		    				mPlayer.COLLIDED = true;
		    				mEnemyManager.mEnemyList.get(enemyID).COLLIDED = true;
		    				
		    				int lifeLeft = mEnemyManager.mEnemyList.get(enemyID).lifeLeft();
		    				mEnemyManager.mEnemyList.get(enemyID).takeDamage(500);

		    				mPlayer.takeDamage(lifeLeft);
		    				mPlayer.hasBeenDestroyed();
		    					
		    		}
		    		
	    		}
			}
		}
		
		private int mMetalCollectedValue = 0;
		
		private void testCollectibleVSPlayer(int projectileID){
        	if(mPlayer != null){
        		if(mProjectileManager.mProjectileList.get(projectileID).getBoundingBox().bottom >= mPlayer.getBoundingBox().top){
        			//Log.d("RtypeView/testCollectibleVSPlayer", "Test Collision");
	        		float lengthX;
	    			float lengthY;
	    			lengthX = Math.abs(mPlayer.getPosX() - mProjectileManager.mProjectileList.get(projectileID).getPosX());
	    			lengthY = Math.abs(mPlayer.getPosY() - mProjectileManager.mProjectileList.get(projectileID).getPosY());
	    			lengthX *= lengthX;
	    			lengthY *= lengthY;
	    			float combinedRadius = mPlayer.getRadius() + mProjectileManager.mProjectileList.get(projectileID).getRadius();
	    			if((lengthX + lengthY) <= (combinedRadius * combinedRadius) ){
	    	        	// TODO do more accurate collision detection here
	    	       		// Collided, do something about it
	    				mPlayer.collectMetal(50); //REDUNDANT
	    				mMetalCollectedValue += mProjectileManager.mProjectileList.get(projectileID).getPointValue();
	    				//mPlayerCash += mProjectileManager.mProjectileList.get(projectileID).getPointValue(); //Metal collected add to points
	    				mProjectileManager.mProjectileList.get(projectileID).takeDamage(1000);//KILL IT
	    				mProjectileManager.mProjectileList.get(projectileID).hasBeenDestroyed();
	    				mProjectileManager.mProjectileList.remove(projectileID);
	    	        	
	    	        }
        		}
        	}
        }
        
        private void doCollisionTesting(){
        	//boolean projectileSpotted = false;
        	//PROJECTILE COLLISION
        	for(int k = mProjectileManager.mProjectileList.size()-1; k >= 0 ; k-- ){
        		
        		if(mProjectileManager.mProjectileList.get(k).getSide() == GlobalState.PLAYERSIDE){
        			testProjectileVSEnemy(k);
        			
        			
        		}
        		else if(mProjectileManager.mProjectileList.get(k).getSide() == GlobalState.ENEMYSIDE){
        			testProjectileVSPlayer(k);
        			if(mAIPlayer!=null)
        				testProjectileVSAIPlayer(k);
        			
        		}
        		else if(mProjectileManager.mProjectileList.get(k).getSide() == GlobalState.NEUTRALSIDE){
        			testCollectibleVSPlayer(k);
        		}
        	}

        	// ENEMY VS PLAYER
        	for(int i = 0; i < mEnemyManager.mEnemyList.size(); i++){
        		testEnemyVSPlayer(i);
        		if(mAIPlayer!=null)
        			testEnemyVSAIPlayer(i);
        	}
        
     
        }// end of doCollisionTesting()
        
   
        
    }// end of RtypeThread




    
   

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;
   // protected ImageView mMissionCompleteText;
    
    /** The thread that actually draws the animation */
    private RtypeThread thread;

    public RtypeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        // create thread only; it's started in surfaceCreated()
        thread = new RtypeThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                //mStatusText.setVisibility(m.getData().getInt("viz"));
                //mStatusText.setText(m.getData().getString("text"));
            }
        });
        
        setFocusable(true); // make sure we get key events
        
    }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     * 
     * @return the animation thread
     */
    public RtypeThread getThread() {
        return thread;
    }

    /**
     * Standard override to get key-press events.
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    } */


    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
        else
        	thread.unpause();
    }

    /**
     * Installs a pointer to the text view used for Debug message.
     */
    public void setTextView(TextView textView, boolean visibility) {
        mStatusText = textView;
        if(visibility == true)
        	mStatusText.setVisibility(TextView.VISIBLE);
        else
        	mStatusText.setVisibility(TextView.INVISIBLE);
    }
    

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }
    
   
    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
      
    	if (thread.getState() == Thread.State.NEW) {

			thread.start(); // Must only be called once, and never stop()ed

		}  
    	
    	//Tell the game thread what the surface size is, incase it has changed
        thread.setSurfaceSize(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
        
    	
    	if(thread.mMode == RtypeThread.STATE_ENDED){
    		Intent i = new Intent(mContext, UIMainMenuActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		mContext.startActivity(i);
    		
    	}else if(!thread.hasLevelLoaded()){	
    		thread.loadNextLevel(false ,thread.mPlayerInfo);
    		
    	}else	// Game has regained focus
    		thread.unpause();
    	
    	thread.setRunning(true);

    }
    

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	if(event.getAction() == android.view.MotionEvent.ACTION_DOWN)
    		thread.onScreenPress(event);
    	else if(event.getAction() == android.view.MotionEvent.ACTION_UP)
    		thread.onRelease();
    	return true; // Required for subsequent events ACTION_UP
    	//return super.onTouchEvent(event);
    }
    
 


    /*
     * Callback invoked when the Surface has been destroyed or a new activity has been started
     */
    public void surfaceDestroyed(SurfaceHolder holder) {

      
       thread.setRunning(false);  //Ignores everything in the games run loop but does not destroy it

      // thread.bVisible = false;
      //  thread.pause();

        
    }

}
