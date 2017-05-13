package com.forthegreatleader;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;



public class ParticleSystem{
	public class Point{
		public Point(float lifeTime, float x, float y, float startVelocity, int angle){
			mX = x;
			mY = y;
			mLastPosX = x;
			mLastPosY = y;
			mVelocity = startVelocity;
			mAngle = angle;
			mLifeTime = lifeTime;
			mAlphaPercent = 255 / mLifeTime;
			
		}
		public float mX, mY;
		public boolean mAlive = true;
		
		//Physics variables
		private int mAngle;
		private float mVelocity;
		final private float mMass = 1000f;
	    final private float mFriction = 0.1f;
	    final private double mOneMinusFriction = 1.0 - mFriction;
	    private float mAccelX = 0;
	    private float mAccelY = 0;
	    private float mLastPosX, mLastPosY;
	    public int mAlpha = 255;
	    private float mAlphaPercent = 0;
	    
	    //Time Variables
	    private float mTimeElapsed = 0, mLifeTime = 1.0f;
	    
	    //Particle Member functions
	   
	    
	    public void mInitialiseNewParticle(float lifeTime, float x, float y, float startVelocity, int angle){
	    	mX = x;
			mY = y;
			mVelocity = startVelocity;
			mAngle = angle;
			mLifeTime = lifeTime;
			mTimeElapsed = 0;
			mAlive = true;
			mAccelX = 0;
			mAccelY = 0;
			mLastPosX = x;
			mLastPosY = y;
			mAlpha = 255;
			//mAlphaPercent = 255 / mLifeTime;
	    }
	    
	    
	    
	    //Update Particle
	    void update(double dT, double dTC){

	    	float sx = (float)(mVelocity * Math.sin(mAngle));
			float sy = (float)(mVelocity * Math.cos(mAngle));
			
	        final float gx = sx * mMass;
	        final float gy = sy * mMass;

	        final float invm = 1.0f / mMass;
	        final float ax = gx * invm;
	        final float ay = gy * invm;

			final float x = (float)(mX + mOneMinusFriction * dTC * (mX - mLastPosX) + mAccelX * dT);
			final float y = (float)(mY + mOneMinusFriction * dTC * (mY - mLastPosY) + mAccelY * dT);

			mLastPosX = mX;
			mX = x;
			mAccelX = ax;

	        mLastPosY = mY;
	        mY = y;
	        mAccelY = ay;
	        
	        mTimeElapsed+= dT;
	        if(mFade == true){
		        float timeLeft = mLifeTime - mTimeElapsed;
		        mAlpha = (int)(mAlphaPercent * timeLeft);
	        }
	        
	        if(mTimeElapsed>mLifeTime)
	        	mAlive = false;
	        	
	    }
	}//End point class
	
	public ParticleSystem(float creationTime, float pointLifeTime, float posX, float posY, float pointVelocity, int pointAngle, boolean fade){
		mPointList = new ArrayList<Point>();
		mDeadIndexList = new ArrayList<Integer>();
		mPosX = posX;
		mPosY = posY;
		mParticleStartAngle = pointAngle;
		mParticleStartVelocity = pointVelocity;
		mParticleLife = pointLifeTime;
		mCreationSpeed = creationTime;
		mFade = fade;
		outlinePaint = new Paint();
		outlinePaint.setStrokeWidth(5);
		outlinePaint.setColor(Color.BLACK);
	}
	
	public void clearPoints(){
		mPointList.clear();
	}
	
	//Particle creation variables
	private ArrayList<Point> mPointList;
	private ArrayList<Integer> mDeadIndexList;
	private int mDeadCount = 0, mDeadBuffer = 15;
	
	//Time variables
	private float mCreationTimer = 0, mCreationSpeed;
	private float mParticleLife;
	
	//Physics variables
	private float mPosX, mPosY;
	private int mParticleStartAngle;
	private float mParticleStartVelocity;
	
	//Draw variables
	public boolean mFade = false;
	private Paint outlinePaint;
	
void drawLine(Canvas c, Paint paint){
		
		if(mPointList.size()>0){
			for(int i=0; i < mPointList.size(); i++){
				if(mPointList.get(i).mAlive == true){
					paint.setAlpha(mPointList.get(i).mAlpha);
					if(i==0)
						c.drawLine(mPosX, mPosY, mPointList.get(i).mX, mPointList.get(i).mY, paint);
					else
						c.drawLine(mPointList.get(i-1).mX, mPointList.get(i-1).mY, mPointList.get(i).mX, mPointList.get(i).mY, paint);
				}
			}
		}
	}

void drawLines(Canvas c, int width, Paint paint){
	int trailLength = mPointList.size();
	if(trailLength > 0){
		for(int i=0; i < trailLength; i++){
			if(mPointList.get(i).mAlive == true){
				paint.setAlpha(mPointList.get(i).mAlpha);
				if(i==0){
					c.drawLine(mPosX + width, mPosY, mPointList.get(i).mX + width, mPointList.get(i).mY, paint);
					c.drawLine(mPosX - width, mPosY, mPointList.get(i).mX - width, mPointList.get(i).mY, paint);
				}
				else{
					c.drawLine(mPointList.get(i-1).mX + width, mPointList.get(i-1).mY, mPointList.get(i).mX + width, mPointList.get(i).mY, paint);
					c.drawLine(mPointList.get(i-1).mX - width, mPointList.get(i-1).mY, mPointList.get(i).mX - width, mPointList.get(i).mY, paint);
				}
		
			}
		}
	}
}
	
	
// same as arrow but with fork tongue at the end
void drawDragonTongue(Canvas c, Paint paint){
		
		if(mPointList.size()>0){
			int lastPoint = 0;
			float width = 0;
			//Path path = new Path();
			paint.setStyle(Style.FILL);
			
			//path.moveTo(mPosX, mPosY);
			//float width = 100/mPointList.size();
			for(int i=0; i < mPointList.size()-2; i++){
				if(mPointList.get(i).mAlive == true){
					
					if(mFade == true)
						paint.setAlpha(mPointList.get(i).mAlpha);
					width = mPointList.get(i).mTimeElapsed * 20;
					 //width;// * (i-1);
					float nextSpread = width;// * i;
		//			float outlineWidth = 5;
					Path path = new Path();
					if(i==0){
						
		    			path.moveTo(mPosX, mPosY);
		    			path.lineTo(mPointList.get(i).mX + nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPointList.get(i).mX - nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPosX, mPosY);

					}
					
					else{
						float spread = mPointList.get(i-1).mTimeElapsed * 20;
						path.moveTo(mPointList.get(i-1).mX  + spread, mPointList.get(i-1).mY);
		    			path.lineTo(mPointList.get(i).mX + nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPointList.get(i).mX  - nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPointList.get(i-1).mX - spread, mPointList.get(i-1).mY);
		    			path.lineTo(mPointList.get(i-1).mX  + spread, mPointList.get(i-1).mY);

						}
					//paint.setStrokeWidth(paint.getStrokeWidth() - paintWidth);
					
					c.drawPath(path, paint);
					lastPoint = i;
				}
				
			}
			//Draw tails tail
			if(lastPoint > 0){
				Path pathTail = new Path();
				pathTail.moveTo(mPointList.get(lastPoint).mX + width, mPointList.get(lastPoint).mY);
				pathTail.lineTo(mPointList.get(lastPoint).mX + width, mPointList.get(lastPoint).mY + width);
				pathTail.lineTo(mPointList.get(lastPoint).mX, mPointList.get(lastPoint).mY);
				pathTail.lineTo(mPointList.get(lastPoint).mX - width, mPointList.get(lastPoint).mY + width);
				pathTail.lineTo(mPointList.get(lastPoint).mX - width, mPointList.get(lastPoint).mY);
				c.drawPath(pathTail, paint);
			}
		}
		
	}
	
	void drawArrow(Canvas c, Paint paint){
		
		if(mPointList.size()>0){
		//	int lastPoint = 0;
			float width = 0;
	//		float outlineWidth = 5;
			//Path path = new Path();
			paint.setStyle(Style.FILL);
			
			//path.moveTo(mPosX, mPosY);
			//float width = 100/mPointList.size();
			for(int i=0; i < mPointList.size()-1; i++){
				if(mPointList.get(i).mAlive == true){
					
					if(mFade == true)
						paint.setAlpha(mPointList.get(i).mAlpha);
					width = mPointList.get(i).mTimeElapsed * 20;
					 //width;// * (i-1);
					float nextSpread = width;// * i;
					Path path = new Path();
					if(i==0){
						
		    			path.moveTo(mPosX, mPosY);
		    			path.lineTo(mPointList.get(i).mX + nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPointList.get(i).mX - nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPosX, mPosY);
		    			c.drawLine(mPosX, mPosY, mPointList.get(i).mX - nextSpread, mPointList.get(i).mY, outlinePaint);
						c.drawLine(mPosX, mPosY, mPointList.get(i).mX + nextSpread, mPointList.get(i).mY, outlinePaint);
					}
					
					else{
						float spread = mPointList.get(i-1).mTimeElapsed * 20;
					
						path.moveTo(mPointList.get(i-1).mX  + spread, mPointList.get(i-1).mY);
		    			path.lineTo(mPointList.get(i).mX + nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPointList.get(i).mX  - nextSpread, mPointList.get(i).mY);
		    			path.lineTo(mPointList.get(i-1).mX - spread, mPointList.get(i-1).mY);
		    			path.lineTo(mPointList.get(i-1).mX  + spread, mPointList.get(i-1).mY);
		    			c.drawLine(mPointList.get(i-1).mX - spread, mPointList.get(i-1).mY, mPointList.get(i).mX - nextSpread, mPointList.get(i).mY, outlinePaint);
						c.drawLine(mPointList.get(i-1).mX + spread, mPointList.get(i-1).mY, mPointList.get(i).mX + nextSpread, mPointList.get(i).mY, outlinePaint);
					}
					//paint.setStrokeWidth(paint.getStrokeWidth() - paintWidth);
					c.drawPath(path, paint);
					//lastPoint = i;
				}
				
			}
		}
		
	}
	//Update Particle System
	public void update(double dT, double dTC){
		//Reset variables
		mDeadIndexList.clear();
		mDeadCount = 0;
		mCreationTimer+=dT;
		if(mPointList.size() > 0){
			// Update particles and count/record dead ones
			for(int i=mPointList.size()-1; i >= 0; i--){
				if(mPointList.get(i).mAlive == true){
					mPointList.get(i).update(dT, dTC);
				}
				else{ // Point dead
					mDeadCount++;
					mDeadIndexList.add(i);
				}
			}
		}
		// Remove dead particles if there are too many
		int q=mDeadIndexList.size()-1;
		for(; q > 0 && mDeadCount > mDeadBuffer; q--){
			mPointList.remove(mDeadIndexList.get(q));
			mDeadIndexList.remove(q);
		}
		
		// Create new particle
		if(mCreationTimer > mCreationSpeed){
			mCreationTimer = 0;
			if(mDeadCount < 1){
				Point newPoint = new Point(mParticleLife, mPosX, mPosY, mParticleStartVelocity, mParticleStartAngle);
				mPointList.add(0, newPoint);
			}
			else{ //Instead of creating a new variable, reinitialise the nearest dead one, and shift it to the start of the list
				int firstDeadParticleIndex = mDeadIndexList.get(mDeadIndexList.size()-1);
				mPointList.get(firstDeadParticleIndex).mInitialiseNewParticle(mParticleLife, mPosX, mPosY, mParticleStartVelocity, mParticleStartAngle);
				for(int v = firstDeadParticleIndex; v > 0; v--){
					Collections.swap(mPointList, v, v-1);
				}
			}
		}
	}
	
	//Update particle system position
	void setPos(float x, float y){
		mPosX = x;
		mPosY = y;
	}

}//End particleSystem class
