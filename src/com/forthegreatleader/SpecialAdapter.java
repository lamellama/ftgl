package com.forthegreatleader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An annoying Google oversight
 * A Custom Adapter List used to initialise ListViews Children
 * Specifically the colour of the upgrade_listview views based on the shared global upgrades information
 */
public class SpecialAdapter<Object1> extends ArrayAdapter<Object> {

	Context mContext;
	GlobalState.PlayerInformation mPlayerInfo;
	
	public SpecialAdapter(Context context, int upgradeListview, int label, GlobalState.PlayerInformation playerInfo, Object1[] upgradeList) {
		super(context, upgradeListview, label, upgradeList);
		mPlayerInfo = playerInfo;
		mContext = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = super.getView(position, convertView, parent);
		
		// TODO should not use POSITION, should compare the string values of view and upgrade
		// TODO will cause problems when upgrade list is larger than screen
		// Initialise the view based on its relative upgrade properties
		ImageView iv3 = (ImageView)view.findViewById(R.id.ImageView03);
		TextView tv2 = (TextView)view.findViewById(R.id.TextView02);
		TextView tv = (TextView)view.findViewById(R.id.TextView01);
		tv.setTypeface(UITabsActivity.mTypeFace);
		tv2.setTypeface(UITabsActivity.mTypeFace);
		
		Resources res = mContext.getResources();
		tv2.setText(Integer.toString(mPlayerInfo.msUpgrades[position].getCost()));
		//Set the views upgrade icon
		iv3.setImageResource(res.getIdentifier(mPlayerInfo.msUpgrades[position].getIcon(), "drawable", mContext.getPackageName()));
		tv.setTextColor(Color.WHITE);

		//If upgrade has been bought, make it red
		if(mPlayerInfo.msUpgrades[position].isPurchased()){
			tv.setBackgroundColor(GlobalState.red);
			tv2.setBackgroundColor(GlobalState.red);
			view.setBackgroundColor(GlobalState.red);
		}
		else{// make it grey
			tv.setBackgroundColor(GlobalState.darkGrey);
			tv2.setBackgroundColor(GlobalState.darkGrey);
			view.setBackgroundColor(GlobalState.darkGrey);

		}
		return view;

	}

}






