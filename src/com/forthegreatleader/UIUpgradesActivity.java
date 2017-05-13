package com.forthegreatleader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.view.View;

/**
 * UI Tab
 * Displays upgrade list view
 * affects Global playerInfo upgrades
 */
public class UIUpgradesActivity extends ListActivity  {
	
	private int j, upgradeID;
	
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

		//UITabsActivity.StaticGlobalState = (GlobalState) getApplication();
		//if(StaticGlobalState.playerInfo.msUpgrades != null)
		setContentView(R.layout.upgrade_list_view_layout2);
		SpecialAdapter<String> adapter2 = new SpecialAdapter<String>(this, R.layout.upgrade_listview, 
				R.id.TextView01, 
				UITabsActivity.StaticGlobalState.playerInfo,
				UITabsActivity.StaticGlobalState.playerInfo.getUpgradeList());
		/*ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.upgrade_listview, 
				R.id.TextView01, 
				((GlobalState)getApplication()).playerInfo.getUpgradeList());*/
		
		
		//ListView lv = new ListView(this);
		setListAdapter(adapter2);
		ListView lv = getListView();
		/**ListView lv.setAdapter(new SpecialAdapter<String>(this, R.layout.upgrade_listview, 
				R.id.TextView01, 
				StaticGlobalState.playerInfo,
				((GlobalState)getApplication()).playerInfo.getUpgradeList()));*/
		
		Button button_next = (Button)this.findViewById(R.id.upgrade_next_button);
		button_next.setTypeface(UITabsActivity.mTypeFace);
		button_next.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View view) {
	 			UITabsActivity.tabHost.setCurrentTab(2);
	 		
	 		}
	  	});
		//
		//lv.setDivider(getResources().getDrawable(R.drawable.div));
		//lv.setBackgroundColor(Color.WHITE);
		//TODO lv.setDivider(findResId(R.drawable.div));

		lv.setOnItemClickListener(new OnItemClickListener() {
			  public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			  {
				  TextView tv = (TextView)view.findViewById(R.id.TextView01);
				  TextView tv2 = (TextView)view.findViewById(R.id.TextView02);
			      // When clicked, show a toast with the TextView text
				  Object o = parent.getItemAtPosition(position);
				  String strUpgrade = o.toString();
				  UITabsActivity.StaticGlobalState = (GlobalState) getApplication();
			 		for(j = 0; j < UITabsActivity.StaticGlobalState.playerInfo.msUpgrades.length; j++)
						  if(strUpgrade.equals(UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[j].getName())){
							  upgradeID = j;
						  }
			 		
			 		
			 		//If upgrade has already been purchased, sell it
			 		if(UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].isPurchased()){
			 			UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].unPurchase();
			 			//view.setSelected(false);
			 			tv.setBackgroundColor(GlobalState.darkGrey);
			 			tv2.setBackgroundColor(GlobalState.darkGrey);
			 			//tv.setTextColor(Color.WHITE);
						view.setBackgroundColor(GlobalState.darkGrey);
			 			Toast.makeText(getApplicationContext(), strUpgrade + " Sold + " + UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].getCost(), Toast.LENGTH_SHORT).show();
			 			UITabsActivity.StaticGlobalState.playerInfo.addCash(UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].getCost());
			 		//if upgrade has not been purchased
			 		}else if(!UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].isPurchased()){
			 			if(UITabsActivity.StaticGlobalState.playerInfo.upgradeDependancyState(UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].getID()) >= 0){
				 			//Check that they have enough cash to afford the upgrade and then purchase it
				 			if(UITabsActivity.StaticGlobalState.playerInfo.removeCash(UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].getCost())){
				 				Toast.makeText(getApplicationContext(), strUpgrade + " Purchased - " + UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].getCost(), Toast.LENGTH_SHORT).show();
				 				UITabsActivity.StaticGlobalState.playerInfo.msUpgrades[upgradeID].purchase();
					 			tv.setBackgroundColor(GlobalState.red);
					 			tv2.setBackgroundColor(GlobalState.red);
					 			//tv.setTextColor(Color.WHITE);
								view.setBackgroundColor(GlobalState.red);
					 			//view.setSelected(true);
				 			}
				 			else{// let the player know they do not have enough money
					 			
				 				Toast.makeText(getApplicationContext(), "Insufficient funds", Toast.LENGTH_SHORT).show();
				 				
				 			}
			 			}
			 			else{// let the player know they do not have enough money
				 			
			 				Toast.makeText(getApplicationContext(), "Requires previous upgrade", Toast.LENGTH_SHORT).show();
			 				
			 			}
			 			
			 		}
				  
				  
			  }
		});
	}
}

	