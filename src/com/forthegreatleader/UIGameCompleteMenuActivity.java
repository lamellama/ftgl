package com.forthegreatleader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * UI Game Complete
 * Congratulations screen
 */
public class UIGameCompleteMenuActivity extends Activity {

	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
        	Intent i = new Intent(getApplicationContext(), UIMainMenuActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(i);
			finish();
            return true; 
        } 
        return super.onKeyDown(keyCode, event); 
    } 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_complete_menu_layout);
		
		
		TextView txt = (TextView) findViewById(R.id.TextView_congratulations);  
		TextView txt2 = (TextView) findViewById(R.id.TextView_under_construction); 
		TextView txt3 = (TextView) findViewById(R.id.TextView_please); 
		TextView txt4 = (TextView) findViewById(R.id.TextView_credits); 
		
		Typeface font = Typeface.createFromAsset(getAssets(), GlobalState.TYPEFACE_MOLOT);  
		txt.setTypeface(font);  
		txt2.setTypeface(font);  
		txt3.setTypeface(font);  
		txt4.setTypeface(font); 

	 	
	}
	
}

