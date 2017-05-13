package com.forthegreatleader;

import java.util.Vector;

import android.graphics.Color;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.ProjectilePacket;

/**
 * Derived from EnemyObject
 * Each enemy defined in res/raw/level*.txt has a level# param which defines which class is used for its creation
 * TODO Level1 Enemy object will have complex movement and have given weapons.
 */
public class Level11EnemyObject extends EnemyObject{
	private static final int HEIGHT = 9;
	private static final int WIDTH = 9;
	private static final int WEAPON_HEIGHT = 3;
	private static final int WEAPON_WIDTH = 2;
	private static final int LIFE = 20;
	private static final int WEAPON_LIFE = 1;
	private static final int WEAPON_POWER = 5;
	private static final int PROJECTILE_COLOUR = Color.rgb(100, 255, 255);
	private static final int TRAIL_COLOUR = Color.BLUE;
	private static final int COLOUR = Color.rgb(55, 105, 55);
	private static final float WEAPON_FIRE_RATE = 2.5f;
	private static final int  MAX_AMMO = 1;
	private static final float TRAIL_VELOCITY = -0.2f;
	
	public Level11EnemyObject(int posX, int posY, MovementPacket movementPack, CanvasPacket canvasPack, SharedBitmapList spriteList, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		super(LIFE, new Packet.PositionPacket( posX, posY, HEIGHT, WIDTH), movementPack,  canvasPack, spriteList , explosionBitmapList, projectileList);
		init();
	}
	//No bitmap
	public Level11EnemyObject(int posX, int posY, MovementPacket movementPack, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		super(LIFE, new Packet.PositionPacket( posX, posY, HEIGHT, WIDTH), movementPack,  canvasPack, explosionBitmapList, projectileList);
		init();
	}
	
	private void init(){
		mWeapon = new WeaponObject(GlobalState.ENEMYSIDE,
				PROJECTILE_COLOUR,
				new ProjectilePacket(WEAPON_LIFE, WEAPON_POWER),
				new Packet.PositionPacket(mPosX, mPosY, /* height */ WEAPON_HEIGHT, /* width */ WEAPON_WIDTH),
				/* heading */0, 
				new Packet.MovementPacket(/* initialVelocityX */ 0, /* Random */ 0, /* initialVelocityY */ 40.0f, /* Random */ 0, /* propulsionX */ 0, /* propulsionY */ -40.0f),
				/* fire delay */ WEAPON_FIRE_RATE, 
				/* Max Ammo ->*/ MAX_AMMO, 
				/* type */GlobalState.UPGRATETYPE_SIMPLE,
				new Packet.CanvasPacket(mCanvasWidth, mCanvasHeight),
				new Packet.TrailPacket(/* trail Creation*/ 1.5f, /* trail life */ 2.0f, TRAIL_COLOUR, /* yVelocity */ TRAIL_VELOCITY),
				new Packet.ClusterPacket(/* clusterCount */ 0, /* clusterTime */ 0),
				mProjectileList);
		
		mWeapon.chargeOff();// Firing on
		mArrowPaint.setColor(COLOUR);
	}

	private WeaponObject mWeapon;

	@Override
    public void update(double dT){
        super.update(dT);

        if(mWeapon != null)
        	mWeapon.update(dT, mPosX, mPosY);

    };

}
