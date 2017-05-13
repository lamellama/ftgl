package com.forthegreatleader;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;
import com.forthegreatleader.Packet.ProjectilePacket;

/**
 * Derived from EnemyObject
 * Each enemy defined in res/raw/level*.txt has a level# param which defines which class is used for its creation
 * TODO Level1 Enemy object will have complex movement and have given weapons.
 */
public class Level102EnemyObject extends EnemyObject{
	private static final int HEIGHT = 15;
	private static final int WIDTH = 15;
	private static final int LIFE = 40;
	private static final int WEAPON_LIFE = 1;
	private static final int WEAPON_POWER = 10;
	private static final int PROJECTILE_COLOUR = Color.RED;
	private static final int TRAIL_COLOUR = Color.BLUE;
	private static final float WEAPON_FIRE_RATE = 4.0f;
	private static final int  MAX_AMMO = 1;
	private static final float TRAIL_VELOCITY = -0.2f;
	private static final float WEAPON_HEIGHT = 1.0f;
	private static final float WEAPON_WIDTH = 0.8f;
	private static final int OUTLINE_COLOUR = Color.BLACK;
	private static final int COLOUR = Color.rgb(55, 100, 55);
	
	public Level102EnemyObject(int posX, int posY, float ySpeed, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		super(LIFE, new Packet.PositionPacket( posX, posY, HEIGHT, WIDTH), new MovementPacket(0, ySpeed, 0, 0),  canvasPack , explosionBitmapList, projectileList);
		init();
	}

	private void init(){
		//mLife = LIFE;
		mArrowPaint.setColor(COLOUR);
		mWeapon = new WeaponObject(GlobalState.ENEMYSIDE,
				PROJECTILE_COLOUR,
				new ProjectilePacket(WEAPON_LIFE, WEAPON_POWER),
				new Packet.PositionPacket(mPosX, mPosY, /* height */ WEAPON_HEIGHT, /* width */ WEAPON_WIDTH),
				/* heading */0, 
				new Packet.MovementPacket(/* initialVelocityX */ 0,/* Random */ 0, /* initialVelocityY */ 40.0f,/* Random */ 0, /* propulsionX */ 0, /* propulsionY */ -40.0f),
				/* fire delay */ WEAPON_FIRE_RATE, 
				/* Max Ammo ->*/ MAX_AMMO, 
				/* type */GlobalState.UPGRATETYPE_SIMPLE,
				new Packet.CanvasPacket(mCanvasWidth, mCanvasHeight),
				new Packet.TrailPacket(/* trail Creation*/ 0.25f, /* trail life */ 1.0f, /* trail colour */ TRAIL_COLOUR, /* yVelocity */ TRAIL_VELOCITY),
				new Packet.ClusterPacket(/* clusterCount */ 0, /* clusterTime */ 0),
				mProjectileList);
		mWeapon.chargeOff();// Firing on
	}
	@Override
	protected void initialiseBody(){
		
		int parts = getNumberOfBodyParts();
    	//int parts = (int)lifeLeft()/BODY_PART_LIFE;
    	float partSpacer = mHeight / parts;
    	//CREATE LINES
		for(int i = 0; i < parts; i++){
			float points[] = new float[4];
			points[0] = -mHalfWidth;
			points[1] = -(mHalfHeight - (partSpacer * i));
			points[2] = mHalfWidth;
			points[3] = -mHalfHeight + (partSpacer * i);
			ProjectileCharacterBodyObject newLine = new ProjectileCharacterBodyObject(points, new PositionPacket(mPosX, mPosY, mWidth, mHeight), new CanvasPacket(mCanvasWidth, mCanvasHeight), mArrowPaint.getColor(), OUTLINE_COLOUR, mArrowPaint.getStrokeWidth());
			mProjectileBodyList.add(newLine);
			
		}		
	}
	

	private WeaponObject mWeapon;
	
    @Override
    public void draw(Canvas c)
    {
    	 super.draw(c);

    }

	@Override
    public void update(double dT){
        super.update(dT);

        if(mWeapon != null)
        	mWeapon.update(dT, mPosX, mPosY);

    };

}
