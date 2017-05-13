
/* 	VERSION INFO
 * 0.1
 * 		- Accelerometer				COMPLETE
 * 		- File input				COMPLETE
 * 		- Level loading				COMPLETE
 * 0.2
 * 		- Simple Enemy class		COMPLETE
 * 		- Enemy spawning			COMPLETE
 * 		- Collisions				COMPLETE
 * 		- Projectiles				COMPLETE
 * 0.3
 * 		- Enemy classes				90% - (TODO Large enemies)
 * 0.4
 * 		- Player upgrades			COMPLETE
 * 0.5
 * 		- Resource files
 * 			Maps					50%
 * 			Scripting				90%
 * 			Entity Rendering		COMPLETE
 * 0.6
 * 		- UI						99% - (TODO Game completed screen)
 * 		- Saving					COMPLETE
 *
 * 0.7
 * 		- Gameplay UI				COMPLETE
 * 		- Friendly AI				COMPLETE
 *
 * 0.8
 * 		- Alpha Testing				0%
 * 0.9
 * 		- Beta Testing				0%
 * 
 * 1.01 - Saving 2					0%
 * 		- Memory Management			0% (Optimisations, memory pools)
 * 		- Sound						0%
 * 		- Player upgrades 2			0%
 * 		- Maps 2					0%
 * 		- Enemy Classes	2			0%
 */


package com.forthegreatleader;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
//import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.TextView;

import com.forthegreatleader.UIMainMenuActivity;
import com.forthegreatleader.RtypeView.RtypeThread;

/**
 * The main Activity, which handles all calls from Android system
 * Handles Power usage, Sensor management, Android Menu, Key presses
 * Starts the main game view RtypeView
 * Copies the basic layout of the Lunar Lander Sample code
 */
public class RtypeActivity extends Activity {

    private static final int MENU_RESUME = 5;

    private static final int MENU_START = 6;

//	private static final int MAIN_MENU_START = 10;

    
    private SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private Display mDisplay;
    private WakeLock mWakeLock;
    
    private boolean mSensorRunning = false;
   // private static SensorManager mySensorEventListener
    /** A handle to the thread that's actually running the animation. */
    private RtypeThread mRtypeThread;

    /** A handle to the View in which the game is running. */
    private RtypeView mRtypeView;
    
    
    private boolean mContinueGame = false;
    
    /**
     * Invoked during init to give the Activity a chance to set up its Menu.
     * 
     * @param menu the Menu to which entries may be added
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

       // menu.add(0, MENU_START, 0, R.string.menu_start);
       // menu.add(0, MENU_RESUME, 0, R.string.menu_options);

        return true;
    }
    
    /**
     * Handle result returned by MainMenu Intent
     * Listen for results.
     */
 /*   protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        switch (resultCode) {
            case GlobalState.GAME_NEW:
            	mRtypeThread.setState(RtypeThread.STATE_READY);
            	mRtypeThread.loadNextLevel(((GlobalState)getApplication()).playerInfo);
            	
            	
              //case GlobalState.PLAYER_UPGRADE:
            // 	mRtypeThread.upgradePlayer(((GlobalState)getApplication()).playerInfo);
            	break;
            case GlobalState.NEXT_LEVEL:
            	mRtypeThread.loadNextLevel(((GlobalState)getApplication()).playerInfo);
            	break;
            case GlobalState.GAME_CONTINUE:	
			try {
				mRtypeThread.loadSavedGame();
			} catch (IOException e) {
				Log.e("RtypeActivity", "Load saved Game" + e);
			}
            default:
                break;
        }
    }*/
    
    /**
     * Invoked when the user selects an item from the Menu.
     * 
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     *         otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
        switch (item.getItemId()) {
        
            case MENU_START:
                //mRtypeThread.doStart();
            	i = new Intent(this, UIMainMenuActivity.class);
                //  i.putExtra(NotesDbAdapter.KEY_ROWID, id);
            	startActivityForResult(i, GlobalState.MAINMENUACT); // TODO there is no reason to pass MAIN_MENU_START
                return true;

            case MENU_RESUME:
            	i = new Intent(this, UITabsActivity.class);
                //  i.putExtra(NotesDbAdapter.KEY_ROWID, id);
            	startActivityForResult(i, GlobalState.MAINMENUACT); // TODO there is no reason to pass MAIN_MENU_START
                return true;

        }

        return false;
    }
    
    private SensorEventListener mySensorEventListener = new SensorEventListener(){

    	@Override
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	 // TODO Auto-generated method stub

    	}

    	@SuppressWarnings("deprecation")
		@Override
    	public void onSensorChanged(SensorEvent event) {
    		
    		switch (mDisplay.getOrientation()) {
            case Surface.ROTATION_0:
            	mRtypeThread.doOrientationUpdate(((float)event.values[0]), ((float)-event.values[1]), event.timestamp);
                break;
            case Surface.ROTATION_90:
            	mRtypeThread.doOrientationUpdate(((float)-event.values[1]), ((float)event.values[0]), event.timestamp);
                break;
            case Surface.ROTATION_180:
            	mRtypeThread.doOrientationUpdate(((float)-event.values[0]), ((float)-event.values[1]), event.timestamp);
                break;
            case Surface.ROTATION_270:
            	mRtypeThread.doOrientationUpdate(((float)event.values[1]), ((float)-event.values[0]), event.timestamp);
                break;
    		}
    	
    	}
    };
    
    

    /**
     * Invoked when the Activity is created.
     * 
     * @param savedInstanceState a Bundle containing state saved from a previous
     *        execution, or null if this is a new execution
     *        TODO savedInstanceState
     */
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle mExtras;
        mExtras = this.getIntent().getExtras();
        mContinueGame = mExtras.getBoolean("continue");

        // turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // tell system to use the layout defined in our XML file
        
        
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        
     // Create a bright wake lock
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        
        setContentView(R.layout.rtype_layout);
        //mRtypeView.
        mRtypeView = (RtypeView) findViewById(R.id.rtype);
        mRtypeThread = mRtypeView.getThread();
        
      //  mRtypeView.setTextView((TextView) findViewById(R.id.text), true);
       // mRtypeView.setMissionCompleteImageView((ImageView) findViewById(R.id.iv_complete), true);
       // mRtypeThread.loadNextLevel(((GlobalState)getApplication()).playerInfo);
        mRtypeThread.mPlayerInfo = ((GlobalState)getApplication()).playerInfo;
        //TODO I don't understand what is happening here
        //TODO When the app is run on the phone, mCreated == true
        //TODO The only place thats sets created to true is in activatePlayerUpgrades but this has not been called yet
        //TODO Does ApplicationContext remember values?
        for(int i = 0; i < mRtypeThread.mPlayerInfo.msUpgrades.length; i++){
        	mRtypeThread.mPlayerInfo.msUpgrades[i].mCreated = false;
    		//Log.d("RtypeActivity", "UPGRADE PURCHASED: " +  mRtypeThread.mPlayerInfo.msUpgrades[i].isPurchased());
    		//Log.d("RtypeActivity", "UPGRADE CREATED: " +  mRtypeThread.mPlayerInfo.msUpgrades[i].isCreated());
        }
        //TODO BODGE ALERT (RtypeView.surfaceChanged() is called after loadNextLevel so I have to set the canvas size beforehand (Annoying but seems to work fine))
        Display display = getWindowManager().getDefaultDisplay(); 
        mRtypeThread.mCanvasWidth = display.getWidth();
        mRtypeThread.mCanvasHeight = display.getHeight();

        
        mRtypeThread.loadNextLevel(mContinueGame, mRtypeThread.mPlayerInfo);

        
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
     
        if(mySensors.size() > 0){
        	mSensorManager.registerListener(mySensorEventListener, mySensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        	mSensorRunning = true;
        }
        else{
        	mSensorRunning = false;
         finish();
        }
        
        
        
        

        if (savedInstanceState == null) {
            // we were just launched: set up a new game
        	mRtypeThread.setState(RtypeThread.STATE_READY);
            //Log.w(this.getClass().getName(), "SIS is null. Game state ready");
        } else {
            // we are being restored: resume a previous game
        	//mRtypeThread.restoreState(savedInstanceState);
           // Log.w(this.getClass().getName(), "SIS is nonnull, gamestate restored");
        }
    }


    @Override
    protected void onStop(){
    	super.onStop();
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        
        mRtypeThread.pause(); // pause game when Activity pauses
        
        mWakeLock.release();
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if(mSensorRunning == false){
        	mSensorManager.registerListener(mySensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        	mSensorRunning = true;
        }
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
        mWakeLock.acquire();
        
        //TODO thread.resume
        		
        // Start the simulation
 //      
    }
    
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
         if (keyCode == KeyEvent.KEYCODE_SEARCH){
        	mRtypeThread.onKeyPress();
    		return true;
        }
         else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
        	mRtypeThread.onKeyPress();
    		return true;
        }
         else if (keyCode == KeyEvent.KEYCODE_BACK) { 
        	Intent i = new Intent(getApplicationContext(), UIMainMenuActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(i);
            return true; 
        }
        return super.onKeyDown(keyCode, event); 
    } 
	
	@Override 
    public boolean onKeyUp(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_SEARCH){
        	mRtypeThread.onRelease();
    		return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
        	mRtypeThread.onRelease();
    		return true;
        }
        return super.onKeyDown(keyCode, event); 
    } 

    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     * 
     * @param outState a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        if(mSensorRunning == true){
        	mSensorManager.unregisterListener(mySensorEventListener);
        	mSensorRunning = false;
        }
      //  mRtypeThread.saveState(outState);
        //Log.d(this.getClass().getName(), "SIS called");
    }
}
