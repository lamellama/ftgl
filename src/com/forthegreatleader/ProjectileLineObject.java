package com.forthegreatleader;

import java.util.Vector;

import com.forthegreatleader.Packet.CanvasPacket;
import com.forthegreatleader.Packet.ClusterPacket;
import com.forthegreatleader.Packet.MovementPacket;
import com.forthegreatleader.Packet.PositionPacket;
import com.forthegreatleader.Packet.ProjectilePacket;
import com.forthegreatleader.Packet.TrailPacket;

import android.graphics.Canvas;

/**
 * A simple projectile object (eg. Machinegun)
 * Uses the Google API to draw a line
 */
public class ProjectileLineObject extends ProjectileObject{
	public ProjectileLineObject(int type, int colour, int side, ProjectilePacket proPack, PositionPacket posPack, CanvasPacket canvasPack, MovementPacket movementPack, TrailPacket trailPack, Vector<ProjectileObject> projectileList, ClusterPacket clusterPack) {
		super(type, colour, side, proPack, posPack, canvasPack , movementPack, trailPack, projectileList, clusterPack);
		
	}
	
	@Override
    public void update(double dT, double dTC){
		super.update(dT, dTC);
     /*   double accel = mSpeed * dT;
        double radians = 2 * Math.PI * mHeading / 360;
        mPosX += Math.sin(radians) * accel;
        mPosY += Math.cos(radians) * accel;
     */
		
		

    }
	
	@Override
	public void draw(Canvas c){
		super.draw(c);
		
	}
}
