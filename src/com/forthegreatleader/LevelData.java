package com.forthegreatleader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.forthegreatleader.SharedBitmapList;
import com.forthegreatleader.Spawner;
/**
 * LevelData handles Loading from txt files
 * Loads Background Sprites, Spawn points and level variables
 * Handles movement of background sprites
 */
public class LevelData {
	private int mLevelID;
	private String[] mBitmapList;
	private SharedBitmapList mSharedBitmapList;
	private static final int LEVEL_FAIL_DELAY = 6;
	private static final float SCRIPT_PAUSE = 0.0f;

	
	// Container class which holds a layer information
	// A layer is a 2d array of sprites (eg. clouds, background)
	private class Layer {
		public int[][] mRowColumnArray;
		public int mRows;
		public int mColumns;
		public int mSpeed;
	}
	
	
	public LevelData(Context context){
		mContext = context;
		// mBGSpriteList, Holds all sprites used in layers
		mBGTileList = new ArrayList<LevelTileSpriteObject>();
		// mScriptObjectList, Holds sprites used in scripted overlays
		mScriptObjectList = new ArrayList<ScriptObject>();
		

		mSharedBitmapList = new SharedBitmapList(context);
	}
	


	private List<Layer> mLayerList;
	private List<Spawner> mSpawnerList;
	private List<LevelTileSpriteObject> mBGTileList;
	private List<ScriptObject> mScriptObjectList;
	private int mLayers;
	
	private Resources mRes;
	private Context mContext;
	
	private int mState = GlobalState.STATE_LOADING;
	public int getState(){return mState;}
	public void stateIntro(){mState = GlobalState.STATE_INTRO;} // CURRENTLY NOT USED
	public void stateLoading(){mState = GlobalState.STATE_LOADING;} // DEFAULT
	public void statePlay(){mState = GlobalState.STATE_PLAYING;} // Has to be called for the level timer to begin
	

	private boolean mLevelLoaded = false;
	public boolean hasLevelLoaded(){return mLevelLoaded;}

	private float mCanvasWidth; 
	private float mCanvasHeight; //Only uses width but I don't like to get rid of it
	private int mLevelDuration = 0;
	
	private int mLevelCompletionBonus = 0;
	public int getLevelBonus(){
		return mLevelCompletionBonus;
	}
	
	private boolean mKillingAIEndsGame = false;
	public boolean doesAIDeathEndGameEarly(){return mKillingAIEndsGame;}
	private boolean mIsThereAnAIPlayer = false;
	
	public boolean doWeNeedAnAIPlayer(){
		return mIsThereAnAIPlayer;
	}
	private float mElapsedTime = 0, mElapsedPlayTime = 0;
	public float getElapsedTime(){return mElapsedTime;}
	public float getElapsedPlayingTime(){return mElapsedPlayTime;}
	private float mLevelScrollSpeed = 0;
	
	public float getLevelSpeed(){
		return mLevelScrollSpeed;
	}
	
	/**
	 * getLevelData() handles Loading from txt files
	 * initLevel() instantiates tiles for drawing and updating, Stores them in mBGSpriteList
	 */
	public boolean loadLevel(int levelID, float canvasWidth, float canvasHeight){
		mLayers = 0;
		mLevelID = levelID;
		mLevelLoaded = false;
		//mSharedBitmapList = bitmapInfo;
		mRes = mContext.getResources();
		mIsThereAnAIPlayer= false;
		mCanvasWidth = canvasWidth;
		mCanvasHeight = canvasHeight;
		mLayerList = new ArrayList<Layer>();
		
		
			
        try {
        	// Load level#.cfg resource from res/raw
            InputStream is = mRes.openRawResource(mRes.getIdentifier("level" + levelID, "raw", mContext.getPackageName()));
            
            getLevelData(is); // Load data from file into arrays and stuff
            is.close() ;
           
        }
        catch( NotFoundException nfe ) {
        	Log.e("levelData", "level" +levelID  + ", File Not Found " +nfe) ;
        	return false;
        }
        catch( IOException ioe ) {
        	Log.e("levelData", "Read Levels failed with error: "+ioe) ;
        	return false;
        }
        Log.w(this.getClass().getName(),"loaded Level data from file: " + mLevelID);
        
        initLevel(mRes, mContext);	// Prepare level sprites for updating and drawing
        
        return true;
        	
    }
	
	public boolean release(){
		//for(int i = 0; i < mBGSpriteList.size(); i++){
		//	mBGSpriteList.get(i).recycle();
		//}
		mBGTileList.clear();
		//mSharedBitmapList.recycle();
		return true;
	}
	
	// InitialiseLevel
	// Loop through all the layers, 
	// Automatically sizes tiles depending on how many columns there are in the layer
	// get each tile number and match it with the corresponding sprite 
	// and store in a single list (mBGSpriteList), ready for drawing
	private boolean initLevel(Resources res, Context context){
		mElapsedTime = 0;
		mElapsedPlayTime = 0;
		mLevelFailed = false;
		mLevelFinished = false;
		//mBGSpriteList = new ArrayList<LevelTileSpriteObject>();

		for(int layer = 0; layer < mLayers ; layer++){	// loop layers
			int tileWidth = (int)mCanvasWidth / mLayerList.get(layer).mRows;
			int tileWidthHalf = tileWidth/2;
			for(int i = 0; i < mLayerList.get(layer).mRows; i++){
				int spriteID = 0;
				for(int j = 0; j < mLayerList.get(layer).mColumns; j++){
					if(mLayerList.get(layer).mRowColumnArray[i][j] == 0) // 0 is an empty tile
					{
						// do nothing
					}
					else // Draw the tile
					{	
						int bitmapInt = (mLayerList.get(layer).mRowColumnArray[i][j]);
						spriteID = mSharedBitmapList.addBitmap(mBitmapList[Math.abs(bitmapInt)-1], tileWidth, tileWidth);
						//spriteID = res.getIdentifier(mBitmapList[(mLayerList.get(layer).mRowColumnArray[i][j])-1], "drawable", context.getPackageName());
						LevelTileSpriteObject bgTile;
						bgTile = new LevelTileSpriteObject(mSharedBitmapList.getBitmap(spriteID), tileWidth, tileWidth);
						if(bitmapInt < 0){
							bgTile.mirror();
						}
						

						bgTile.setPosX(tileWidthHalf + (tileWidth * i ));						// Move position by one tile width
						bgTile.setPosY(-(tileWidth * j) + mCanvasHeight) ;	// 0,0 is in the top left, but we need to start in the bottom left
						bgTile.setSpeed(mLayerList.get(layer).mSpeed);		// Scroll speed (this should be stored per layer rather than per tile but its easier this way because the sprites are stored in an 1D list)
						bgTile.setColumns(mLayerList.get(layer).mColumns);	// and this is not right either
						mBGTileList.add(bgTile);
					}
				}
			}
		}
		// garbage collection
		mLayerList.clear();
		return true;
	}
	
	float mScriptBetweenPause = SCRIPT_PAUSE;
	
	// Handles cycling of scripted overlays
	public void onTouch(MotionEvent event){
		
		// Skip the first active script
		boolean activeScriptNotFound = false;
		int i = 0;
		while((activeScriptNotFound != true)&&(i < mScriptObjectList.size())){
			
			if(mScriptObjectList.get(i).isActive()){
				if(mScriptObjectList.get(i).getBoundingRect().contains((int)event.getX(), (int)event.getY())){
					mScriptObjectList.get(i).skipText();
					activeScriptNotFound = true;
					//mScriptBetweenPause = SCRIPT_PAUSE;
				}
			}
			i++;
		}
	}
	
	//private boolean mSucceeded = false;
	
	//public boolean completedSuccessfully(){
	//	return mSucceeded;
	//}
	
	// Update level objects positions and whatever
	public void update(double dT){
		int i = 0;
		//Log.v("LevelManager", "Update(): Playtime = " + mElapsedPlayTime);
		//Log.d("LevelManager", "Update(): Levltime = " + mElapsedTime);
		if(mScriptBetweenPause >= 0)
			mScriptBetweenPause -= dT;
		//mLevelEndKoreaWin
		
		if((mLevelFailed == true)||(mLevelEndKoreaWin)){
			
				mLevelEndTimer -= dT;
			// DELAY BETWEEN THE PLAYER FAILING AND THE LEVEL ENDING
			if(mLevelEndTimer <= 0){
				mLevelFinished = true;
			}
		}
		
		
		// scroll the background tiles
		while(i < mBGTileList.size())
		{	
			// If tile is below bottom of screen, move it to the top
			if(mBGTileList.get(i).getPosY() > mCanvasHeight + mBGTileList.get(i).mHalfWidth){	
				//LevelTileSpriteObject tempSpriteOb = mBGSpriteList.get(i);
				mBGTileList.get(i).mPosY -= (mBGTileList.get(i).mWidth * mBGTileList.get(i).getColumns());
				//mBGSpriteList.remove(i);
				//mBGSpriteList.add(tempSpriteOb);
			}
			// Update tile position (BODGE ALERT, HAS TO INCLUDE ASPECT RATIO)
			mBGTileList.get(i).setPosY((float)(mBGTileList.get(i).getPosY() + ((float)((float)(float)mBGTileList.get(i).getSpeed() * (mCanvasHeight / mCanvasWidth)) * dT)));
			i++;
		}
		if(mScriptObjectList.size() < 1){// Scripts ended
			mState = GlobalState.STATE_PLAYING;
		}else{//update Scripts
			//int previousState = mState;
			int activeScripts = 0;
			for(i = 0; i < mScriptObjectList.size(); i++)
			{
				
				if(!mScriptObjectList.get(i).isFinished()){
					mScriptObjectList.get(i).update(mElapsedPlayTime, dT);
					if(mScriptObjectList.get(i).isActive())
						activeScripts++;
				}
				else{
					mScriptObjectList.remove(i);
				}
			}
			if(activeScripts > 0)
				mState = GlobalState.STATE_SCRIPTING;
			else
				mState = GlobalState.STATE_PLAYING;
		}
		
		mElapsedTime+=dT;
		if(mState == GlobalState.STATE_PLAYING)
			mElapsedPlayTime += dT;
		
	}
	
	private boolean mLevelFailed = false;
	private boolean mLevelEndKoreaWin = false;
	public boolean didKoreaWin(){return mLevelEndKoreaWin;}
	private boolean mLevelFinished = false;
	private float mLevelEndTimer = 0;
	
	public boolean hasLevelFailed(){
		return mLevelFailed;
	}
	
	public void levelFailed(){
		if(mLevelFailed == false){
			mLevelEndTimer = LEVEL_FAIL_DELAY;
		//Display failed message and start a timer to end level
			mScriptObjectList.add(new ScriptObject(mContext, 
					mRes.getIdentifier("mission_failed", "drawable", mContext.getPackageName()), 
					mRes, 
					null, 
					0,
					mLevelDuration,
					(int)mCanvasWidth, (int)mCanvasHeight,
					(int)(mCanvasHeight/2)));
		}
		
		mLevelFailed = true;
	}
	
	
	public void gameCompletedKoreaWin(){
		if(mLevelEndKoreaWin == false){
			mLevelEndTimer = LEVEL_FAIL_DELAY;
		//Display failed message and start a timer to end level
			mScriptObjectList.add(new ScriptObject(mContext, 
					mRes.getIdentifier("general", "drawable", mContext.getPackageName()), 
					mRes, 
					new String[]{"Well done captain", "You have provided a great service to your country"}, 
					0,
					mLevelDuration,
					(int)mCanvasWidth, (int)mCanvasHeight,
					(int)(mCanvasHeight/4)));
		}
		
		mLevelEndKoreaWin = true;
	}
	
	//private boolean mLevelCompleted = false;
	public boolean hasLevelEnded(){
		if(mLevelFinished){
			return true;
		}
		
		if(mElapsedPlayTime > mLevelDuration){
			return true;
		}
			
		
		return false;
	}
	
	public float getLevelDuration(){
		return mLevelDuration;
	}
	
	// Draw all level spites
	public void drawBelow(Canvas c){
		
		
		
		for(int i = 0; i < mBGTileList.size(); i++)
		{
			if(mBGTileList.get(i).mPosY > -mBGTileList.get(i).mHalfWidth) // If its on screen
				mBGTileList.get(i).draw(c);
		}
		
	}
	
	// Draw script spites
	public void drawOnTop(Canvas c){

	/*	for(int i = 0; i < mScriptObjectList.size(); i++)
		{
			if(mScriptObjectList.get(i).isActive())
				mScriptObjectList.get(i).draw(c);
		}
	*/
		
		// Draw the first active script
		boolean activeScriptNotFound = false;
		int i = 0;
		while((activeScriptNotFound != true)&&(i < mScriptObjectList.size()) && (mScriptBetweenPause < 0)){
			if(mScriptObjectList.get(i).isActive()){
				mScriptObjectList.get(i).draw(c);
				activeScriptNotFound = true;
			}
			i++;
		}
		
		
		mLevelLoaded = true;
	}
	
	// Opens a file and reads the Tile data and Bitmap data in the member arrays
	private boolean getLevelData(InputStream aFile){

		Scanner sc = new Scanner(aFile);
		mSpawnerList = new ArrayList<Spawner>();
		mScriptObjectList = new ArrayList<ScriptObject>();
		String tempStr;
		Pattern pattern  = Pattern.compile("(//|BACKGROUNDMAP|BITMAPS|SPAWNER|OBJECT|TIME|SCRIPT|AIPLAYER|KILLINGAIENDSGAME|COMPLETIONBONUS)");
		
		while (sc.hasNextLine()) {
			tempStr = sc.findInLine(pattern);	// Search input stream
			if(tempStr != null){
				if(tempStr.startsWith("//")){
					//skip line
				}
				else
				{
					
					if(tempStr.equals("BACKGROUNDMAP")){		// Found start of background map data
						Layer tempLayer = new Layer();						
						tempLayer.mRows = Integer.parseInt( sc.next() );		//Read columns and row count
						tempLayer.mColumns = Integer.parseInt( sc.next() );
						tempLayer.mSpeed = Integer.parseInt( sc.next() );	// Read the speed which the background should scroll
						tempLayer.mRowColumnArray = new int[tempLayer.mRows][tempLayer.mColumns];	// Initialise map data array
						int j = 0;
						int i = 0;// = tempLayer.mColumns-1;						// loop the columns in reverse so they are in the same order as the .cfg
						while((j < tempLayer.mRows)&&(sc.hasNext())){
							while((i < tempLayer.mColumns)&&(sc.hasNext())){
								tempLayer.mRowColumnArray[j][i] = Integer.parseInt( sc.next() );	// Save background map data to array
								i++;
							}
							j++;
							i = 0;
						}
						mLayerList.add(tempLayer);
						mLayers++;	// A layer count
					}
					
					else if(tempStr.equals("BITMAPS")){						// Read in bitmap list
						int i = 0;
						int numberOfBitmaps = Integer.parseInt( sc.next() );
						mBitmapList = new String[numberOfBitmaps];		// Init array
						while((i < numberOfBitmaps)&&(sc.hasNext())){	// Loop bitmaps and store in string array
							mBitmapList[i] = sc.next();			
							i++;
						}	
					}
					else if(tempStr.equals("SPAWNER")){
						//tempStr = sc.next();
						Spawner newSpawner = new Spawner();
						while((sc.hasNext())&&(!tempStr.equals("END"))){
							tempStr = sc.next();
							if(tempStr.equals("position"))
								newSpawner.mPosX = Float.parseFloat(sc.next());
							else if(tempStr.equals("starttime"))
								newSpawner.mStartTime = Float.parseFloat(sc.next());
							else if(tempStr.equals("endtime"))
								newSpawner.mEndTime = Float.parseFloat(sc.next());
							else if(tempStr.equals("level"))
								newSpawner.mLevel = Integer.parseInt( sc.next() );
							else if(tempStr.equals("interval"))
								newSpawner.mIntervalTime = Float.parseFloat( sc.next() );
							else if(tempStr.equals("intervalrand"))
								newSpawner.mIntervalRandomPercent = Integer.parseInt( sc.next() );
							
							else if(tempStr.equals("yspeed"))
								newSpawner.mYSpeed = Integer.parseInt( sc.next() );
							else if(tempStr.equals("yspeedrand"))
								newSpawner.mYSpeedRandomPercent = Integer.parseInt( sc.next() );
							else if(tempStr.equals("yspeedcurl"))
								newSpawner.mYSpeedCurl = Float.parseFloat( sc.next() );
							
							else if(tempStr.equals("xspeed"))
								newSpawner.mXSpeed = Integer.parseInt( sc.next() );
							else if(tempStr.equals("xspeedrand"))
								newSpawner.mXSpeedRandomPercent = Integer.parseInt( sc.next() );
							else if(tempStr.equals("xspeedcurl"))
								newSpawner.mXSpeedCurl = Float.parseFloat( sc.next() );
						}
						
						mSpawnerList.add(newSpawner);
					}
					else if(tempStr.equals("TIME")){	
						mLevelDuration = Integer.parseInt( sc.next() );
					}
					else if(tempStr.equals("AIPLAYER")){	
						mIsThereAnAIPlayer = Boolean.parseBoolean( sc.next() );
						//Log.d("LevelData/GetLevelData", "AI PLAYER == " + mIsThereAnAIPlayer);
					}
					else if(tempStr.equals("KILLINGAIENDSGAME")){	
						mKillingAIEndsGame = Boolean.parseBoolean( sc.next() );
					}
					else if(tempStr.equals("COMPLETIONBONUS")){	
						mLevelCompletionBonus = Integer.parseInt( sc.next() );
					}
					else if(tempStr.equals("SCRIPT")){	
						//tempStr = sc.next();
						int tempPages =0, yPosition = 50;
						float tempStart = 0.0f, tempEnd = 0.0f;
						String tempCharacter = null; 
						String tempText[] = new String[15];
						
						while((sc.hasNext())&&(!tempStr.equals("END"))){
							tempStr = sc.next();
							if(tempStr.equals("character"))
								tempCharacter = sc.next();
							else if(tempStr.equals("starttime"))
								tempStart = Float.parseFloat(sc.next());
							else if(tempStr.equals("endtime"))
								tempEnd = Float.parseFloat(sc.next());
							else if(tempStr.equals("verticlepos"))
								yPosition = Integer.parseInt( sc.next() );
							else if(tempStr.equals("text"))
							{
								int q=0;
								tempPages = Integer.parseInt( sc.next() );
								
								tempText = new String[tempPages];
								if(tempPages > 0)
									sc.nextLine();
								while((q < tempPages)&&(sc.hasNext())){	// Loop bitmaps and store in string array
									tempText[q] = sc.nextLine();			
									q++;
								}
								
							}
								
								
							
							//ScriptObject scrpt = new ScriptObject(tempSpriteResourceID, mRes, tempWidth, tempHeight);
						}
						int tempID = -1; // -1 = no sprite
						if(tempCharacter != null)
							tempID = mRes.getIdentifier(tempCharacter, "drawable", mContext.getPackageName());
							mScriptObjectList.add(new ScriptObject(mContext, tempID, 
									mRes, 
									tempText, 
									tempStart,
									tempEnd,
									(int)mCanvasWidth, (int)mCanvasHeight,
									yPosition));
					}
					// TODO add the rest of file reading
					
					
					
				}
			}
			sc.nextLine(); // While there is a next line, keep looping
		}
		if(mLayerList.size() > 0)
			mLevelScrollSpeed = mLayerList.get(0).mSpeed;
		
	/*	ScriptObject completeMessage = new ScriptObject(mContext, 
				mRes.getIdentifier("mission_complete", "drawable", mContext.getPackageName()), 
				mRes, 
				new String[]{"wtf"}, 
				(float)3,
				(float)5,
				(int)1,
				(int)mCanvasWidth/2, (int)mCanvasHeight/2);
		mScriptObjectList.add(completeMessage);*/
		
		return true;

	}
	
	public List<Spawner> getSawnerList(){
		return mSpawnerList;
	}
}