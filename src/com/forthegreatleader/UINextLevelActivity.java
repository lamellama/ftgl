package com.forthegreatleader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * UI Tab
 * Displays next level Info
 * Loads next level by closing UI
 */

public class UINextLevelActivity extends Activity {
	
	private TextView textview_total;
	//if user exits using the back button, go to main menu
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 

         if (keyCode == KeyEvent.KEYCODE_BACK) { 
        	Intent i = new Intent(getApplicationContext(), UIMainMenuActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(i);
            return true; 
        }
        return super.onKeyDown(keyCode, event); 
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  
		/*TextView textview = new TextView(this);
        textview.setText("This is next level tab");
	  	setContentView(textview);*/
		
	  	setContentView(R.layout.next_level_layout);
	  	Button imgBtnContinue = (Button)findViewById(R.id.continue_button);
	  	textview_total = (TextView)this.findViewById(R.id.tv_nl_total);
	  	textview_total.append(Integer.toString(UITabsActivity.gTotalCash));
	  	textview_total.setTypeface(UITabsActivity.mTypeFace);
	  	imgBtnContinue.setTypeface(UITabsActivity.mTypeFace);
	  	
	  	if(UITabsActivity.mExtras.getInt("level") == GlobalState.LEVEL_FAILED){
	  		imgBtnContinue.setText("Restart Level");
	    }
	    else{
	    	imgBtnContinue.setText("   Begin Next Level   ");
	    }
	  	
	  	imgBtnContinue.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View view) {
	 	
	 			/*Intent data = new Intent();

	 			if (getParent() == null) {
	 			    setResult(Activity.RESULT_CANCELED, data);
	 			} else {
	 				
	 			    getParent().setResult(GlobalState.NEXT_LEVEL, data);
	 			}*/
	 			finish();

	 		}
	 	});
	 	
	}
	
	@Override
	public void onResume(){
		super.onResume();
		UITabsActivity.updatePlayerValues();
		textview_total.setText(getString(R.string.menu_label_totalcash) + " " + Integer.toString(UITabsActivity.gTotalCash));
		
	}
}
	