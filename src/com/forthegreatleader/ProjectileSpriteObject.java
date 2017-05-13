package com.forthegreatleader;

import java.util.Vector;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;
import com.forthegreatleader.Packet.ProjectilePacket;

import android.content.res.Resources;

/**
 * A sprite projectile object (eg. Missiles)
 * Stores and draws a sprite
 */
public class ProjectileSpriteObject extends ProjectileObject{
	public ProjectileSpriteObject(Resources res, ProjectilePacket proPack, CanvasPacket canvasPack, PositionPacket posPack, MovementPacket movementPack, int frames, int[] sprites, Vector<ProjectileObject> projectileList) {
		super(res, proPack, canvasPack, posPack, movementPack, frames, sprites, projectileList );

		
	}
	public ProjectileSpriteObject(Resources res, ProjectilePacket proPack, CanvasPacket canvasPack, PositionPacket posPack, MovementPacket movementPack, int sprite, Vector<ProjectileObject> projectileList) {
		super(res, proPack, canvasPack, posPack, movementPack, sprite, projectileList);

	}
	
	/*private int mHeading;
	private float mSpeed;*/
	
	@Override
    public void update(double dT){
		super.update(dT);
        /*double accel = mSpeed * dT;
        double radians = 2 * Math.PI * mHeading / 360;
        mPosX += Math.sin(radians) * accel;
        mPosY += Math.cos(radians) * accel;*/

    }

}
