package com.forthegreatleader;

import java.util.Vector;

import android.graphics.Color;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
//import com.android.game.Packet.PositionPacket;

/**
 * Derived from EnemyObject
 * Each enemy defined in res/raw/level*.txt has a level# param which defines which class is used for its creation
 * Level1 Enemy object will have simple movement.
 */
public class Level5EnemyObject extends EnemyObject{
	//It makes more sense to define these here. More consistent
	private static final int HEIGHT = 25;
	private static final int WIDTH = 25;
	private static final int LIFE = 300;
	private static final int COLOUR = Color.rgb(100, 205, 100);
	//protected int BODY_PART_LIFE = 50;
	
	public Level5EnemyObject(int posX, int posY, MovementPacket movementPack, CanvasPacket canvasPack, SharedBitmapList spriteList, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		
		
		super(LIFE, new Packet.PositionPacket( posX, posY, HEIGHT, WIDTH), movementPack,  canvasPack, spriteList , explosionBitmapList, projectileList);
		init();
	}
	//No bitmap
	public Level5EnemyObject(int posX, int posY, MovementPacket movementPack, CanvasPacket canvasPack, SharedBitmapList explosionBitmapList, Vector<ProjectileObject> projectileList){
		
		
		super(LIFE, new Packet.PositionPacket( posX, posY, HEIGHT, WIDTH), movementPack,  canvasPack, explosionBitmapList, projectileList);
		init();
	}
	private void init(){
		mArrowPaint.setColor(COLOUR);
	}
	


	@Override
    public void update(double dT){
        super.update(dT);
 
    	
    	// have this much acceleration from the engine
       /* double accel = PHYS_FIRE_ACCEL_SEC * elapsedFiring;

        double radians = 2 * Math.PI * mHeading / 360;
        ddx = Math.sin(radians) * accel;
        ddy += Math.cos(radians) * accel;*/

    };

}
