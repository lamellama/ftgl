package com.forthegreatleader;

import java.util.Vector;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.ClusterPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;
import com.forthegreatleader.Packet.ProjectilePacket;
import com.forthegreatleader.Packet.TrailPacket;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;

/**
 * Base class for all projectiles
 */
abstract public class ProjectileObject extends PhysicsObject{
	public ProjectileObject(PositionPacket posPack, CanvasPacket canvasPack) {
		super(posPack, canvasPack);

		init(posPack, new ProjectilePacket(1, 20));
		
		
	}
	public ProjectileObject(Resources res, ProjectilePacket proPack, CanvasPacket canvasPack, PositionPacket posPack, MovementPacket movementPack, int sprite, Vector<ProjectileObject> projectileList) {
		super(posPack, canvasPack , movementPack);
		mProjectileList = projectileList;
		//mRes = res;
		init(posPack, proPack);
		
	}
	public ProjectileObject(Resources res, ProjectilePacket proPack, CanvasPacket canvasPack, PositionPacket posPack, MovementPacket movementPack, int frames, int sprites[], Vector<ProjectileObject> projectileList) {
		super(posPack, canvasPack , movementPack);
		mProjectileList = projectileList;
		//mRes = res;
		init(posPack, proPack);
		
	}

	
	public ProjectileObject(int type, int colour, int side, ProjectilePacket proPack, PositionPacket posPack, CanvasPacket canvasPack, MovementPacket movementPack, TrailPacket trailPack, Vector<ProjectileObject> projectileList, ClusterPacket clusterPack) {
		super(posPack, canvasPack , movementPack);
		mType = type;
		mProjectileList = projectileList;
		mFrames = 0;
		mClusterCount = clusterPack.clusterCount;	
		mClusterTimer = clusterPack.clusterTimer;
		mSide = side;

		//mRes = res;
		if(trailPack.lifeTime > 0){
			mParticleTrail = new ParticleSystem(trailPack.creationTime, trailPack.lifeTime, mPosX, getBBBottom(), trailPack.yVelocity, 0, true);
			mTrailPacket = trailPack;
			//mTrailCreationTime=trailPack.creationTime;
			//mTrailLifeTime= trailPack.lifeTime;
			//mTrailVelocity = trailPack.yVelocity;
			mTrailPaint = new Paint();
			mTrailPaint.setColor(trailPack.colour);
			mTrailPaint.setStrokeWidth(2);
			mTrailPaint.setStrokeJoin(Paint.Join.ROUND);
			mTrailPaint.setStyle(Style.STROKE);
			mTrailPaint.setStrokeCap(Cap.ROUND);
		}
		
		bulletPaint.setARGB(145, 31, 20, 255);
		bulletPaint.setColor(colour);
		//bulletPaint.setStrokeMiter(arg0);
		//bulletPaint.setStrokeWidth(1);
		bulletPaint.setStyle(Paint.Style.FILL);
		init(posPack, proPack);
	}
	
	private void init(PositionPacket posPack, ProjectilePacket proPack){
		mLife = proPack.life;
		if(proPack.lifeTime > 0){
			mLifeTime = proPack.lifeTime;
			mImmortal = false;
		}
		mPower = proPack.power;
		mpHeight = posPack.height;
		mpWidth = posPack.width;
		mOutlinePaint = new Paint();
		mOutlinePaint.setColor(Color.BLACK);
		mOutlinePaint.setStyle(Paint.Style.FILL);
	}
	
	Paint mOutlinePaint;
	TrailPacket mTrailPacket;
	private float mpHeight, mpWidth;
	
	private int mType;
	public int getSide(){return mSide;}
	protected int mSide = GlobalState.NEUTRALSIDE;
	private ParticleSystem mParticleTrail;
    private Paint mTrailPaint;
    final private Paint bulletPaint = new Paint();
    private Vector<ProjectileObject> mProjectileList;
    //private float mTrailCreationTime=0, mTrailLifeTime=0, mTrailVelocity=0;
    private int mClusterCount = 0;
    private float mClusterTimer = 0;
    private boolean mClusterCreated = false;
    private boolean mGuided = false;
    public boolean isGuided(){
    	return mGuided;
    }
    private int mTargetX = 0;
    @SuppressWarnings("unused")
	private int mTargetY =0;
    public void setTarget(int x, int y){
    	mTargetX = x;
    	mTargetY = y;
    }
	
	
	//@Override
    public void update(double dT, double dTC){
		super.update(dT);
		if(isAlive()){
			//GUIDED
			if(mGuided){
				mMovePack.xPropulsion = mTargetX;
			}
			//CLUSTER CREATION
			if(mClusterCreated == false)
				if(mClusterCount > 0){
					if(mTimeElapsed > mClusterTimer){
						float clusterVeclocityMultiplier = (float) -(mClusterCount * 0.5)*10;
						for(float i= (float)-(mClusterCount * 0.5); i<= mClusterCount*0.5; i++){
							clusterVeclocityMultiplier--;
							ProjectileLineObject bullet = new ProjectileLineObject(mType, bulletPaint.getColor(), mSide, 
																						new ProjectilePacket(mLife, mPower, mLifeTime),
																						new PositionPacket(mPosX, mPosY, mpHeight * 0.5f, mpWidth * 0.5f), 
																						new CanvasPacket(mCanvasWidth, mCanvasHeight),
																						new MovementPacket(mMovePack.initialVelocityX+i*10, 0, mMovePack.initialVelocityY, 0, mMovePack.xPropulsion+i, mMovePack.yPropulsion),
																						mTrailPacket,
																						mProjectileList,
																						new ClusterPacket(0,0));
							mProjectileList.add(bullet);
							
						}
						mAlive = false;
						mClusterCreated = true;
						
					}
				}

	    	if(mParticleTrail!=null){
		    	mParticleTrail.setPos(mPosX, getBBBottom());
		        mParticleTrail.update(dT, dTC);
	    	}
		}
		else
			mFinished = true;

    }
	int mBorder = 1;
	@Override
	public void draw(Canvas c){
		if(mParticleTrail!=null)
			mParticleTrail.drawLine(c, mTrailPaint);
		if(mFrames < 1){
			Path path = new Path();
			Path path2 = new Path();
			float quartWidth = mHalfWidth * 0.5f;
		//	mOutlinePaint.setColor(Color.BLACK);
			
			if(mType == GlobalState.UPGRATETYPE_SIMPLE_ARROW){
				
				
				
				
				path.moveTo(getBBLeft() - quartWidth, getBBBottom() + quartWidth);
				path.lineTo(getBBLeft() - quartWidth, mPosY);
				path.lineTo(mPosX, getBBTop() - quartWidth);
				path.lineTo(getBBRight() + quartWidth, mPosY);
				path.lineTo(getBBRight() + quartWidth, getBBBottom() + quartWidth);
				c.drawPath(path, mOutlinePaint );
				
				path2.moveTo(getBBLeft(), getBBBottom());
				path2.lineTo(getBBLeft(), mPosY);
				path2.lineTo(mPosX, getBBTop());
				path2.lineTo(getBBRight(), mPosY);
				path2.lineTo(getBBRight(), getBBBottom());
				c.drawPath(path2, bulletPaint );
				
			}
			else if (mType == GlobalState.UPGRATETYPE_SIMPLE){
				path.moveTo(getBBLeft(), getBBTop());
				path.lineTo(getBBRight(), getBBTop());
				path.lineTo(getBBRight(), getBBBottom());
				path.lineTo(getBBLeft(), getBBBottom());
				path.lineTo(getBBLeft(), getBBTop());
				c.drawPath(path, mOutlinePaint);
				
				path2.moveTo(getBBLeft() + quartWidth, getBBTop() + quartWidth);
				path2.lineTo(getBBRight() - quartWidth, getBBTop() + quartWidth);
				path2.lineTo(getBBRight() - quartWidth, getBBBottom() - quartWidth);
				path2.lineTo(getBBLeft() + quartWidth, getBBBottom() - quartWidth);
				path2.lineTo(getBBLeft() + quartWidth, getBBTop() + quartWidth);
				c.drawPath(path2, bulletPaint);
			}
		}
	}
	
	
}
