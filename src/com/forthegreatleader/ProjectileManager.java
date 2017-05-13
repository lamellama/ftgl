package com.forthegreatleader;

import java.util.Vector;

import android.graphics.Canvas;

public class ProjectileManager {
	public ProjectileManager(int canvasHeight){
		mProjectileList = new Vector<ProjectileObject>();
		mCanvasHeight = canvasHeight;
	}
	public Vector<ProjectileObject> mProjectileList;
	private int mCanvasHeight;
	
	/** 
	 * Reinitialises the projectile list 
	 */
	public void reset(){
		mProjectileList.clear();
	}
	
	public void update(double dT, double dTC){
		if(mProjectileList != null)
	
		for(int i = 0; i < mProjectileList.size(); i++){
			mProjectileList.get(i).update(dT, dTC);
			//If projectile still alive
			if(!mProjectileList.get(i).isAlive()){//TODO no room for death animations
				mProjectileList.remove(i);
			}// Projectile left screen?
			else if((mProjectileList.get(i).getPosY() > mCanvasHeight)||(mProjectileList.get(i).getPosY() < 0)){
				mProjectileList.remove(i);
			}
		}
	}
	
	public void draw(Canvas c){
		if(mProjectileList != null)
			for(int i = 0; i < mProjectileList.size(); i++)
	    		mProjectileList.get(i).draw(c);
    	
	}

}
