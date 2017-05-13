package com.forthegreatleader;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;

/**
 * Derived from EnemyObject
 * Each enemy defined in res/raw/level*.txt has a level# param which defines which class is used for its creation
 * TODO Level1 Enemy object will have complex movement and have given weapons.
 */
public class Level101EnemyObject extends EnemyObject{
	private static final int HEIGHT = 25;
	private static final int WIDTH = 25;
	private static final int LIFE = 40;
//	private static final int WEAPON_LIFE = 1;
//	private static final int WEAPON_POWER = 10;
//	private static final int PROJECTILE_COLOUR = Color.RED;
//	private static final int TRAIL_COLOUR = Color.BLUE;
	private static final int COLOUR = Color.rgb(75, 75, 175);
//	private static final float WEAPON_FIRE_RATE = 4.0f;
	private static final int OUTLINE_COLOUR = Color.BLACK;
	
	public Level101EnemyObject(int posX, int posY, float ySpeed, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		super(LIFE, new Packet.PositionPacket( posX, posY, HEIGHT, WIDTH), new MovementPacket(0, ySpeed, 0, 0),  canvasPack , explosionBitmapList, projectileList);
		init();
	}

	private void init(){
		mArrowPaint.setColor(COLOUR);
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
