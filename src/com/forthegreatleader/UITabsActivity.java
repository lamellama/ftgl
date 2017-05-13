package com.forthegreatleader;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;


//import android.view.KeyEvent;
import android.widget.TabHost;
//import android.widget.TextView;

/**
 * UI Tab
 * hosts all the other tabs
 */
@SuppressWarnings("deprecation")
public class UITabsActivity extends TabActivity {
	public static Bundle mExtras;
	public static GlobalState StaticGlobalState;
	public static TabHost tabHost;
	public static Typeface mTypeFace;
	public static int gTotalCash = 0;
	public static void updatePlayerValues(){
		gTotalCash = UITabsActivity.StaticGlobalState.playerInfo.getBalance();
		
	}
	

	
	@Override
	public void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
	    setContentView(R.layout.tabhost_layout);

	    
	    AdView adView = (AdView)this.findViewById(R.id.adView_menuTop);
	    adView.loadAd(new AdRequest());

	    UITabsActivity.StaticGlobalState = (GlobalState) getApplication();
	    
	    mTypeFace = Typeface.createFromAsset(this.getAssets(), GlobalState.TYPEFACE_MOLOT);
	    Resources res = getResources(); // Resource object to get Drawables
	    tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	
	    mExtras = this.getIntent().getExtras();

	    Intent intent;  // Reusable Intent for each tab
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, UILevelCompleteActivity.class);
	    spec = tabHost.newTabSpec("Complete").setIndicator("Complete", res.getDrawable(R.drawable.ic_tab_complete)).setContent(intent);
	    tabHost.addTab(spec);
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, UIUpgradesActivity.class);
	    spec = tabHost.newTabSpec("Upgrades").setIndicator("Upgrades",res.getDrawable(R.drawable.ic_tab_upgrades)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, UINextLevelActivity.class);
	    spec = tabHost.newTabSpec("Next").setIndicator("Next",res.getDrawable(R.drawable.ic_tab_next)).setContent(intent);
	    tabHost.addTab(spec);

	    
	    if(mExtras.getInt("level") == GlobalState.LEVEL_FAILED){
	    	tabHost.setCurrentTab(2);
	    }
	    else{
	    	tabHost.setCurrentTab(0);
	    }
	}
}
	