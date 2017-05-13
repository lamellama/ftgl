package com.forthegreatleader;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.PositionPacket;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;

/**
 * 
 * Uses the Google API to draw a line
 * Creates a line object that can be DETACHED from its creator to become an object of its own
 */
public class ProjectileCharacterBodyObject extends ProjectileObject{
	public ProjectileCharacterBodyObject(float[] localPoints, PositionPacket posPack, CanvasPacket canvasPack, int colour, int outlineColour,  float strokeWidth) {
		super(posPack, canvasPack);
		mLocalisedPoints = localPoints;
		mWorldPoints = new float[mLocalisedPoints.length];
		mPaint = new Paint();
		mPaint.setStrokeWidth(strokeWidth);
		mOriginalStrokeWidth = strokeWidth;
		mStrokeWidth = strokeWidth;
		mOriginalOutlineStrokeWidth = strokeWidth+2;
		mPaint.setColor(colour);
		//mPaint.setColor(Color.WHITE);
		mPaint.setStrokeJoin(Paint.Join.MITER);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeCap(Cap.SQUARE);
		
		mOutlinePaint = new Paint();
		mOutlinePaint.setStrokeWidth(strokeWidth+4);
		//mOriginalStrokeWidth = strokeWidth;
		mOutlinePaint.setColor(outlineColour);
		mOutlinePaint.setStrokeJoin(Paint.Join.MITER );
		mOutlinePaint.setStyle(Style.STROKE);
		mOutlinePaint.setStrokeCap(Cap.SQUARE);
		
		mCamera = new Camera();
		mMatrix = new Matrix();
		
	}
	float[] mLocalisedPoints;
	float[] mWorldPoints;
	private Paint mPaint;
	private Paint mOutlinePaint;
	private float mLifeTime = 5;
	private float mOriginalStrokeWidth = 4, mOriginalOutlineStrokeWidth = 4, mStrokeWidth = 2;

	//DETACHED UPDATE
	@Override
    public void update(double dT, double dTC){//TODO duplicate functions, this one is called by the projectileList
		super.update(dT, dTC);
		for(int i = 0; i < mLocalisedPoints.length-1; i+=2){
			mWorldPoints[i] = mLocalisedPoints[i] + mPosX;
			mWorldPoints[i+1] = mLocalisedPoints[i+1] + mPosY;	
		}
		if(mDetached){
			if(mTimeElapsed > mLifeTime)
				mAlive = false;
			
			//Narrow the stroke width of the lines over time
			mStrokeWidth = mOriginalStrokeWidth - ((mOriginalStrokeWidth/mLifeTime) * mTimeElapsed);
			mPaint.setStrokeWidth(mStrokeWidth);
			mOutlinePaint.setStrokeWidth(mOriginalOutlineStrokeWidth - ((mOriginalOutlineStrokeWidth/mLifeTime) * mTimeElapsed));
			mZRotation+=mRotationTransformMultiplier;
		}
    }
	//ATTACHED UPDATE
	@Override
    public void update(double dT ){
		for(int i = 0; i < mLocalisedPoints.length-1; i+=2){
			mWorldPoints[i] = mLocalisedPoints[i] + mPosX;
			mWorldPoints[i+1] = mLocalisedPoints[i+1] + mPosY;	
		}
    }

	
	//When this function is called it needs to be added to the Global projectileList
	public void detach(){
		mDetached = true;
		mRandomisedInitialVelocityX = (float) ((Math.random()*100)-50);
		mRandomisedInitialVelocityY = (float) ((Math.random()*100)-50);
		mRotationTransformMultiplier = (float) ((Math.random()*10)-5);
	}
	
	private float mRotationTransformMultiplier = 0;
	private boolean mDetached = false;
	public boolean isDetached(){
		return mDetached;
	}
	
	public void updatePos(float x, float y){
		mPosX = x;
		mPosY = y;

	}
	
	//TODO These could potentially be in physicsObject class but not all projectiles need transformations yet
	protected Camera mCamera;
    protected Matrix mMatrix;
    private float mZRotation = 0;
	public void updateRotation(float  rotation){
		mZRotation = rotation;

	}
    private void applyMatrix(Canvas c) {

    	
		mCamera.save();

		mCamera.rotateZ(mZRotation);
		mCamera.getMatrix(mMatrix);
			
		mMatrix.preTranslate(-mPosX, -mPosY);
		mMatrix.postTranslate(mPosX, mPosY); 
			
		c.concat(mMatrix);
		mCamera.restore();    
  	}
    
    // This object applies transormations once it is detached from its creator
	public void draw(Canvas c){

		Path path = new Path();
		path.moveTo(mWorldPoints[0], mWorldPoints[1]);

		for(int i = 2; i < mWorldPoints.length-1; i+=2)
			path.lineTo(mWorldPoints[i], mWorldPoints[i+1]);
		
		if(isDetached()){
			c.save();
	    	applyMatrix(c);
			c.drawPath(path, mOutlinePaint);
			c.drawPath(path, mPaint);
			c.restore();
		}else{
			
			c.drawPath(path, mOutlinePaint);
			c.drawPath(path, mPaint);
		}
	}
	
    // This object applies transormations once it is detached from its creator
	public void drawLines(Canvas c, Paint p){

		Path path = new Path();
		path.moveTo(mWorldPoints[0], mWorldPoints[1]);
		
		for(int i = 2; i < mWorldPoints.length-1; i+=2)
			path.lineTo(mWorldPoints[i], mWorldPoints[i+1]);
		
		if(isDetached()){
			c.save();
	    	applyMatrix(c);
			c.drawPath(path, mOutlinePaint);
			c.drawPath(path, mPaint);
			c.restore();
		}else{
			
		//	c.drawPath(path, mOutlinePaint);
			c.drawPath(path, p);
		}
	}
	
    // This object applies transormations once it is detached from its creator
	public void drawShape(Canvas c, Paint p){

		Path path = new Path();
		path.moveTo(mWorldPoints[0], mWorldPoints[1] + mStrokeWidth);
		int i = 2;
		
		for(; i < mWorldPoints.length-1; i+=2)
			path.lineTo(mWorldPoints[i], mWorldPoints[i+1] + mStrokeWidth);
		
		for(i = mWorldPoints.length-2; i >= 0; i-=2)
			path.lineTo(mWorldPoints[i], mWorldPoints[i+1] - mStrokeWidth);
		
		//path.lineTo(mWorldPoints[0], mWorldPoints[1] + mStrokeWidth);
		path.close();
		
	//	for(int i = 2; i < mWorldPoints.length-1; i+=2)
	//		path.lineTo(mWorldPoints[i], mWorldPoints[i+1]);
		
		if(isDetached()){
			c.save();
	    	applyMatrix(c);
			c.drawPath(path, mOutlinePaint);
			c.drawPath(path, mPaint);
			c.restore();
		}else{
			
			c.drawPath(path, mOutlinePaint);
			c.drawPath(path, p);
		}
	}
}
