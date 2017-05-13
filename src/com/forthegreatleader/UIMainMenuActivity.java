package com.forthegreatleader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * UI Main Menu
 * This is the main activity, loaded on application start
 * Newgame
 * Continue
 */
public class UIMainMenuActivity extends Activity {
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
        	
        	moveTaskToBack(true);
            return true; 
        } 
        return super.onKeyDown(keyCode, event); 
    } 
	
    /**
     * Handle result returned by MainMenu Intent
     * Listen for results.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        switch (resultCode) {
            case GlobalState.LEVEL_END:
            	//mRtypeThread.setState(RtypeThread.STATE_READY);
            	//mRtypeThread.loadNextLevel(((GlobalState)getApplication()).playerInfo);
            	break;
            default:
            	break;
            	
        }
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  
	  	setContentView(R.layout.main_menu_layout2);
	  	//LayerDrawable transition;

	  	// Find and fill Main Menu image
	 	//ImageView image = (ImageView)findViewById(R.id.mainmenuscreen);
	 	//image.setImageResource(R.drawable.green_tile);
	  	ImageView imgBtnNewGame = (ImageView)findViewById(R.id.newgame_button);
	  	ImageView imgBtnContinue = (ImageView)findViewById(R.id.continue_button);
	  	FrameLayout menuBG = (FrameLayout)findViewById(R.id.mainmenu_bg);
	  	
/*	  	int original_width = imgBtnContinue.getDrawable().getIntrinsicWidth();
	  	int original_height = imgBtnContinue.getDrawable().getIntrinsicHeight();
	  	//float dimensionConstraint = original_width / original_height;
	  	float new_width = menuBG.getWidth();
	  	float widthScale = new_width /original_width;
	  	int new_height = (int)(original_height * widthScale);
	  	
	  	imgBtnContinue.setScaleY(new_height);*/

	  	

	  	menuBG.setBackgroundResource(getResources().getIdentifier("menu" + (int)(4 * Math.random() + 1), "drawable", getPackageName()));
	 	
	 	imgBtnNewGame.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View view) {
		 		
		 		Intent i = new Intent(view.getContext(), RtypeActivity.class);
		 		i.putExtra("continue", false);
		    	startActivity(i);
				finish();
	 		}
	 	});
	 	imgBtnContinue.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View view) {
		 		
		 		Intent i = new Intent(view.getContext(), RtypeActivity.class);
		 		i.putExtra("continue", true);
		    	startActivity(i);
				finish();
	 		}
	 	});
	 	
	}
	
}

