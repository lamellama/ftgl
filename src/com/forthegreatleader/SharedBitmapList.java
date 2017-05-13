package com.forthegreatleader;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;


public class SharedBitmapList{
	private Resources mRes;
	private Context mContext;
	public SharedBitmapList(Context context){
		mBitmapList = new ArrayList<SharedBitmap>();
//		mExplosionBitmapList = new ArrayList<SharedBitmap>();
//		mEnemyBitmapList = new ArrayList<SharedBitmap>();
		mContext = context;
		mRes = context.getResources();
	}
	
	private class SharedBitmap{
		private SharedBitmap(String filename, int width, int height){
			mId = filename;
			mBitResId = mRes.getIdentifier(filename, "drawable", mContext.getPackageName());
			if(mBitResId == 0){
				Log.e(this.getClass().getName(), "Resource not found, Create new SharedBitmap");
				mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mRes, R.drawable.blue_tile), width, height, false);				
			}
			else{
				mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mRes, mBitResId), width, height, true);
				//TODO Config
			}
		}
		private Bitmap mBitmap;
		private String mId;
		private int mBitResId;
	}
	
	
	private ArrayList<SharedBitmap> mBitmapList;

	
	public void recycle(){
		for(int i = 0; i < mBitmapList.size(); i++){
			mBitmapList.get(i).mBitmap.recycle();
		}
		mBitmapList.clear();
	}
	
	public int addBitmap(String resFilename, int width, int height){
		int i = 0;
		while(i<mBitmapList.size()){
			if(resFilename.contentEquals(mBitmapList.get(i).mId)){
				return i;
			}
			i++;
		}
		SharedBitmap newBitmap = new SharedBitmap(resFilename, width, height);
		
		mBitmapList.add(newBitmap);
		
		return i;
	}
	

	
	public Bitmap getBitmap(int id){
		return mBitmapList.get(id).mBitmap;
	}

	
	public int getListSize(){
		return mBitmapList.size();
	}
}
