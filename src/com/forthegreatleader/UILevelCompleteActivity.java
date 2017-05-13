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
 * Displays end of level information
 */
public class UILevelCompleteActivity extends Activity {
	
	//private AdView adView;
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
				
	  	setContentView(R.layout.level_complete_layout);
	  	
	  	UITabsActivity.updatePlayerValues();
	  	
	  	//View mScreen = (LinearLayout) findViewById(R.layout.level_complete_layout);
	  	//mScreen.setBackgroundColor(GlobalState.blue);
	  	TextView textview_score = (TextView)this.findViewById(R.id.tv_score);
	  	TextView textview_bonus = (TextView)this.findViewById(R.id.tv_bonus);
	  	TextView textview_metal = (TextView)this.findViewById(R.id.tv_metal);
	  	TextView textview_damage = (TextView)this.findViewById(R.id.tv_damage);
	  	TextView textview_percentDestroyed = (TextView)this.findViewById(R.id.tv_percent_destroyed);
	  	TextView textview_profit = (TextView)this.findViewById(R.id.tv_profit);
	  	TextView textview_oldbalance = (TextView)this.findViewById(R.id.tv_oldbalance);
	  	textview_total = (TextView)this.findViewById(R.id.tv_total);
	  	Button button_next = (Button)this.findViewById(R.id.lc_next_button);
	  	
	  	textview_score.append(Integer.toString(UITabsActivity.mExtras.getInt("score")));
	  	textview_bonus.append(Integer.toString(UITabsActivity.mExtras.getInt("bonus")));
	  	textview_metal.append(Integer.toString(UITabsActivity.mExtras.getInt("metal")));
	  	textview_damage.append(Integer.toString(UITabsActivity.mExtras.getInt("damage")));
	  	textview_percentDestroyed.append(Integer.toString(UITabsActivity.mExtras.getInt("percent")));
	  	textview_profit.append(Integer.toString(UITabsActivity.mExtras.getInt("profit")));
	  	textview_oldbalance.append(Integer.toString(UITabsActivity.mExtras.getInt("oldbalance")));
	  	textview_total.append(Integer.toString(UITabsActivity.gTotalCash));
	  	
	  	textview_score.setTypeface(UITabsActivity.mTypeFace);
	  	textview_bonus.setTypeface(UITabsActivity.mTypeFace);
	  	textview_metal.setTypeface(UITabsActivity.mTypeFace);
	  	textview_damage.setTypeface(UITabsActivity.mTypeFace);
	  	textview_percentDestroyed.setTypeface(UITabsActivity.mTypeFace);
	  	textview_profit.setTypeface(UITabsActivity.mTypeFace);
	  	textview_oldbalance.setTypeface(UITabsActivity.mTypeFace);
	  	textview_total.setTypeface(UITabsActivity.mTypeFace);
	  	button_next.setTypeface(UITabsActivity.mTypeFace);
	  
	  	
	  	
	  	button_next.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View view) {
	 			UITabsActivity.tabHost.setCurrentTab(1);
	 		
	 		}
	  	});
	  	
	   
	}
	
	@Override
	public void onResume(){
		super.onResume();
		UITabsActivity.updatePlayerValues();
		textview_total.setText(getString(R.string.menu_label_totalcash) + " " +  Integer.toString(UITabsActivity.gTotalCash));
		
	}
}
	