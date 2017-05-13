package com.forthegreatleader;


import java.util.Vector;

//import com.android.game.Packet;
import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
//import android.util.Log;
/**
 * Any sprite object that moves should derive this class
 * Damaging
 * Exploding (Stores the explosion bitmap) TODO optimisation, store it somewhere shared
 * Moving
 */
abstract public class CharacterObject extends PhysicsObject{
	protected int BODY_PART_LIFE = 10;
	private static final int BODY_PART_NUMBEROF_ARROWS = 2;
	private static final int DEFAULTCOLOUR = Color.BLUE;
	private static final int DEFAULT_OUTLINE_WIDTH = 4;
	private static final int DEFAULT_OUTLINE_COLOUR = Color.BLACK;

	public CharacterObject(int life, PositionPacket posPack, MovementPacket movePack, CanvasPacket canvasPack, SharedBitmapList spriteBitmapList, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
		super(posPack, canvasPack, movePack, spriteBitmapList);
		initVariables(life, DEFAULTCOLOUR, canvasPack, explosionBitmapList, projectileList);

	}
	
	//ENEMY
	public CharacterObject(int life, int colour, PositionPacket posPack, MovementPacket movePack, CanvasPacket canvasPack,SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
		super(posPack, canvasPack, movePack);
		initVariables(life, colour, canvasPack, explosionBitmapList, projectileList);

	}

	
	public CharacterObject(int life, PositionPacket posPack, CanvasPacket canvasPack, SharedBitmapList spriteBitmapList, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
		super(posPack, canvasPack, spriteBitmapList);
		initVariables(life, DEFAULTCOLOUR, canvasPack, explosionBitmapList, projectileList);

	}
	
	//PLAYER
	public CharacterObject(int life, int colour, PositionPacket posPack, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList) {
		super(posPack, canvasPack);
		initVariables(life, colour, canvasPack, explosionBitmapList, projectileList);

	}
	
	//float mQrtHeight = 0;
	float m6thHeight = 0;
	float m8thWidth = 0;
	
	private void initVariables(int life, int colour, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		
		mResetPosX = mPosX;
		mResetPosY = mPosY;
		//mCanvasHeight = canvasPack.canvasHeight;
		//mCanvasWidth = canvasPack.canvasWidth;
		mProjectileList = projectileList;
		mExplosionBitmapList = explosionBitmapList;
		mProjectileBodyList = new Vector<ProjectileCharacterBodyObject>();
		
		m6thHeight = mHalfHeight * 0.5f;
		m8thWidth =  m6thHeight * 0.5f;
		
		
	//		xLeft += m8thWidth;
	//		xRight -= m8thWidth;
	//		y -= m8thWidth;
	//		//LINES
	//		for(int i=0; i < (parts - maxArrows) ;i++){
	//			c.drawLine(xLeft, y, xRight, y, mOutlinePaint);
	//			c.drawLine(xLeft, y, xRight, y, mArrowPaint);
	//			y += mQrtHeight;
	//		}
		
		mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mArrowPaint.setColor(colour);
		mArrowPaint.setStrokeWidth(2);
		mArrowPaint.setStrokeJoin(Paint.Join.MITER);
		mArrowPaint.setStyle(Style.STROKE);
		mArrowPaint.setStrokeCap(Cap.BUTT);
		
		mRedPaint = new Paint();
		mRedPaint.setColor(Color.rgb(192, 61, 30));
		mRedPaint.setStyle(Style.FILL);
		
		mVariablePaint = new Paint();
		//mVariablePaint.setStrokeWidth(4);
		mVariablePaint.setStrokeJoin(Paint.Join.MITER);
		mVariablePaint.setStyle(Style.FILL);
		mVariablePaint.setStrokeCap(Cap.BUTT);
		
		mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mOutlinePaint.setColor(DEFAULT_OUTLINE_COLOUR);
		mOutlinePaint.setStrokeWidth(DEFAULT_OUTLINE_WIDTH);
		mOutlinePaint.setStyle(Style.STROKE);
		//mArrowPaint.setAntiAlias(true);
		mArrowArcPaint = new Paint();
		mArrowArcPaint.setColor(Color.RED);
		mArrowArcPaint.setStrokeWidth(1);
		//mArrowArcPaint.setStrokeJoin(Paint.Join);
		mArrowArcPaint.setStyle(Style.FILL_AND_STROKE);
		mArrowArcPaint.setStrokeCap(Cap.BUTT);
		//mArrowArcPaint.setAntiAlias(true);
		mColourMatrix = new ColorMatrix();
		
		mCamera = new Camera();
		mMatrix = new Matrix();
		
		mLife = life;
		initialiseBody();
		
		mLastPosX = mPosX;
		mLastPosY = mPosY;
	}
	
	@Override
	public void reset(){
		super.reset();
    	mLastPosX = mPosX;
    	mLastPosY = mPosY;
    	initialiseBody();
    	//TODO mParticleTrail.reset();
    }
	
	protected void initialiseBody(){
		mProjectileBodyList.clear();
		int parts = getNumberOfBodyParts();
		//Log.d("CharacterObject/initialiseBody", "parts=" + parts);
	//	int parts = 5;
		int maxArrows = BODY_PART_NUMBEROF_ARROWS;
		if(parts < maxArrows)
			maxArrows = parts;
		
		
		//float spacer = m6thHeight;
		//CREATE ARROWS
		int i = 0;
		for(; i < maxArrows; i++){
			float points[] = new float[6];

			points[0] = -mHalfWidth;				
			points[1] = 0 + (m6thHeight * i);
			points[2] = 0;
			points[3] = -mHalfHeight + (m6thHeight * i);
			points[4] = mHalfWidth;
			points[5] = 0 + (m6thHeight * i);
			ProjectileCharacterBodyObject newArrow = new ProjectileCharacterBodyObject(points, new PositionPacket(mPosX, mPosY, mWidth, mHeight), new CanvasPacket(mCanvasWidth, mCanvasHeight), mArrowPaint.getColor(), mOutlinePaint.getColor(), m6thHeight/3);
			mProjectileBodyList.add(newArrow);
			//mProjectileList.add(newArrow);
		}
		
		//CREATE LINES
		for(i = maxArrows; i < parts; i++){
			float points[] = new float[4];
			points[0] = -mHalfWidth;
			points[1] = 0 + (m6thHeight * i);
			points[2] = mHalfWidth;
			points[3] = 0 + (m6thHeight * i);
			ProjectileCharacterBodyObject newLine = new ProjectileCharacterBodyObject(points, new PositionPacket(mPosX, mPosY, mWidth, mHeight), new CanvasPacket(mCanvasWidth, mCanvasHeight), mArrowPaint.getColor(), mOutlinePaint.getColor(),  mArrowPaint.getStrokeWidth());
			mProjectileBodyList.add(newLine);
			
		}
	}
	
	protected SharedBitmapList mExplosionBitmapList;
	private Paint mVariablePaint;
    
	protected Vector<ProjectileCharacterBodyObject> mProjectileBodyList;
	protected Vector<ProjectileObject> mProjectileList;
   // private int[] mPSprites;
    protected int[] mPExplosionSprites;
    protected Bitmap[] mExplosionSprites;
    protected int mExplosionFrame = 0;
    protected int mTotalExplosionFrames = 6;
    private float mExplosionTimer = 0;

    public float mSpeed = 0;
  //  protected int mCanvasWidth;
  //  protected int mCanvasHeight;
    protected float mLastPosX = 0;
    protected float mLastPosY = 0;
    
    protected Camera mCamera;
    protected Matrix mMatrix;
    
    protected ColorMatrix mColourMatrix;
 
    protected Paint mArrowPaint;
    protected Paint mOutlinePaint;
    protected Paint mArrowArcPaint;
    private Paint mRedPaint;
    
    protected int getNumberOfBodyParts(){
    	if(mLife < 100)
    		return (int)Math.ceil(lifeLeft()/(float)BODY_PART_LIFE);
    	else if (mLife < 1000)
    		return (int)Math.ceil(lifeLeft()/100f);
    	else
    		return (int)Math.ceil(lifeLeft()/1000f);
    }
    
	protected int mState = GlobalState.STATE_LOADING;
	public int getState(){return mState;}
	public void setState(int state){
		mState = state;
	}
    
    public void update(double dT){
    	if(hasBeenDestroyed()){
    		mExplosionTimer += dT;
    		if(mExplosionTimer > (0.1 * mExplosionFrame)){
    			if(mExplosionFrame < mTotalExplosionFrames){
    				mExplosionFrame++;
    			}
    			else
    			{
    				//Ready to be released
    				mFinished = true;
    			}
    		}
    		
    	}
    	
    	for(int i =mProjectileBodyList.size()-1; i >= 0 ; i--){
    		if(i+1 > getNumberOfBodyParts()){
    			mProjectileBodyList.get(i).detach();
    			mProjectileList.add(mProjectileBodyList.get(i));
    			mProjectileBodyList.remove(i);
    		}
    		else{
	    		mProjectileBodyList.get(i).updatePos(mPosX, mPosY);
	    		mProjectileBodyList.get(i).update(dT);
    		}
		}
    	
    	mTimeElapsed += dT;
    	mPosY += mSpeed * dT;
    	
    	
    		
    		//if(mProjectileBodyList.get(i).isDetached()){
    		//	mProjectileBodyList.remove(i);
    		//}
    	

    };
    

    
	
	//Used by draw() to display user collecting powerup
    protected int mCollectedMetal = 0;
    public void collectedMetal(int amount){
    	mCollectedMetal += amount;
    	if(mCollectedMetal > 255)
    		mCollectedMetal = 255;
    }
    
    protected void drawStar(Canvas c){
    	Path p = new Path();
    	float qtrWidth = mHalfWidth * 0.5f;
    	//float qtrHeight = mHalfHeight * 0.5f;
    	
    	float eigththWidth = qtrWidth * 0.5f;
    	float sixteenththWidth = eigththWidth * 0.5f;
    	//float eigthHeight = qtrHeight * 0.5f;
    	
    	p.moveTo(mPosX, mPosY - qtrWidth); // Top of star
    	
    	p.lineTo(mPosX + sixteenththWidth, mPosY - sixteenththWidth);
    	
    	p.lineTo(mPosX + qtrWidth, mPosY - sixteenththWidth); // Right point
    	
    	p.lineTo(mPosX + eigththWidth, mPosY + sixteenththWidth);
    	
    	p.lineTo(mPosX + eigththWidth, mPosY + qtrWidth);	//Right Bottom
    	
    	p.lineTo(mPosX, mPosY + eigththWidth);
    	
    	p.lineTo(mPosX - eigththWidth, mPosY + qtrWidth);	//Left Bottom
    	
    	p.lineTo(mPosX - eigththWidth, mPosY + sixteenththWidth);
    	
    	p.lineTo(mPosX - qtrWidth, mPosY - sixteenththWidth); // Left point
    	
    	p.lineTo(mPosX - sixteenththWidth, mPosY - sixteenththWidth);
    	
    	p.close();
    	//p.lineTo(mPosX, mPosY - qtrWidth); // Top
    	
    	c.drawPath(p, mOutlinePaint);
    	c.drawPath(p, mRedPaint);
    }
    
    protected int mPercentAmmoCapacityFull = 0;
	
    @Override
    public void draw(Canvas c)
    {
     	
    	if(!hasBeenDestroyed()){ //If not killed
    		//TODO draw trail
    		if(mBitmap != null)
    			c.drawBitmap(mBitmap[mAnimFrame], mPosX - mHalfWidth, mPosY - mHalfHeight, null);
    		else if(mBitmapList != null){
    			if(mBitmapList.getListSize() > 0)
    				c.drawBitmap(mBitmapList.getBitmap(0), mPosX - mHalfWidth, mPosY - mHalfHeight, null);
    		}
    		else{
    			
    			for(int i =mProjectileBodyList.size()-1; i >= 0 ; i--){
    				int multi = 0;
    				int bodyPartNum = mProjectileBodyList.size() - i;
    				if((bodyPartNum!=0)&&(mPercentAmmoCapacityFull!=0))
    					multi = 255/100 * mPercentAmmoCapacityFull / bodyPartNum; //(mProjectileBodyList.size() - i);
    				
    				int r = (mArrowPaint.getColor() >> 16) & 0xFF;
    				int g = (mArrowPaint.getColor() >> 8) & 0xFF;
    				int b = (mArrowPaint.getColor() >> 0) & 0xFF;
    				
    				r+= multi;
    				if(r > 255)
    					r=255;
    				g+= multi;
    				if(g > 255)
    					g=255;
    				b+= multi;
    				if(b > 255)
    					b=255;
    				
    				
    			    
    				mVariablePaint.setARGB(255, r, g, b);
    				
    	    		mProjectileBodyList.get(i).drawShape(c, mVariablePaint);
    			}
    		}
    	}
    	else if (mExplosionFrame < mExplosionBitmapList.getListSize()){	// Doing the explosion animation
    		c.drawBitmap(mExplosionBitmapList.getBitmap(mExplosionFrame), mPosX - mHalfWidth, mPosY - mHalfHeight, null);
    	}
    	else
    		mFinished = true;
    }
    

    
    @Override
    public void takeDamage(int damage){
    	super.takeDamage(damage);
    	if(damage > 0){
    		mColourMatrix.setSaturation((float)(1.0f/mLife * lifeLeft()));
    		//mArrowPaint.setColorFilter(new ColorMatrixColorFilter(mColourMatrix));
    	}
    	
    }
    

}
