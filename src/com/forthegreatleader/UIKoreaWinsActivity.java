package com.forthegreatleader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * UI Korea wins Main Menu
 * This is the end game screen if you choose to follow orders
 */
public class UIKoreaWinsActivity extends Activity {
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
        	
        	moveTaskToBack(true);
            return true; 
        } 
        return super.onKeyDown(keyCode, event); 
    } 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  
	  	setContentView(R.layout.north_korea_wins_layout);
	  	//LayerDrawable transition;

	  	// Find and fill Main Menu image
	 	//ImageView image = (ImageView)findViewById(R.id.mainmenuscreen);
	 	//image.setImageResource(R.drawable.green_tile);
	  //	ImageView imgBtnNewGame = (ImageView)findViewById(R.id.newgame_button);
	  	ImageView imgBtnTryagain = (ImageView)findViewById(R.id.tryagain_button);
	 	
	/* 	imgBtnNewGame.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View view) {
		 		
		 		Intent i = new Intent(view.getContext(), RtypeActivity.class);
		 		i.putExtra("continue", false);
		    	startActivity(i);
				finish();
	 		}
	 	});*/
	 	imgBtnTryagain.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View view) {
		 		
		 		Intent i = new Intent(view.getContext(), RtypeActivity.class);
		 		i.putExtra("continue", true);
		    	startActivity(i);
				finish();
	 		}
	 	});
	 	
	}
	
}

