package com.forthegreatleader;

// This class holds container classes to add info to passed information.
public class Packet {
	//Container for positional info
	public static class PositionPacket{
		public PositionPacket(float tx, float ty, float tHeight, float tWidth){
			x = tx;
			y = ty;
			height = tHeight;
			width = tWidth;
		}
		float getRelativeHeight(int canvasWidth){
			return (float)((canvasWidth * 0.01) / height);
		}
		float getRelativeWidth(int canvasWidth){
			return (float)((canvasWidth * 0.01) / width);
		}
		void setRelativeSize(int canvasWidth){
			height = (float)(height / (canvasWidth * 0.01));
			width = (float)(width / (canvasWidth * 0.01));
		}
		int getPixelWidth(int canvasWidth){
			int pixelSize = (int) ((canvasWidth * 0.01) * width);
			if(pixelSize < 1)
				pixelSize = 1;
			return pixelSize;
		}
		int getPixelHeight(int canvasWidth){
			int pixelSize = (int) ((canvasWidth * 0.01) * height);
			if(pixelSize < 1)
				pixelSize = 1;
			return pixelSize;
		}
		float x, y;
		float height, width;
		
	}
	
	public static class ProjectilePacket{
		public ProjectilePacket(int tlife, int tpower){
			power = tpower;
			life = tlife;
		}
		public ProjectilePacket(int tlife, int tpower, double tLifeTime){
			power = tpower;
			life = tlife;
			lifeTime = tLifeTime;
		}
		int life;
		int power;
		double lifeTime = -1;
	}
	
	public static class LevelPacket{
		public LevelPacket(float speed){
			scrollSpeed = speed;
		}
		float scrollSpeed;
	}
	
	public static class CanvasPacket{
		public CanvasPacket(int width, int height){
			canvasHeight = height;
			canvasWidth = width;
		}
		int canvasHeight, canvasWidth;
	}
	
	//Container for physics info
	public static class MovementPacket{
		public MovementPacket(float xVel, float xRandVel, float yVel, float yRandVel, float xProp, float yProp){
			xRand = xRandVel;
			yRand = yRandVel;
			initialVelocityX = xVel;
			initialVelocityY = yVel;
			xPropulsion = xProp;
			yPropulsion = yProp;
		}
		public MovementPacket(float xVel, float yVel, float xProp, float yProp){
			xRand = 0;
			yRand = 0;
			initialVelocityX = xVel;
			initialVelocityY = yVel;
			xPropulsion = xProp;
			yPropulsion = yProp;
		}
		float xRand, yRand;
		float initialVelocityX, initialVelocityY;
		float xPropulsion, yPropulsion;
	}
	
	//Container for particle system info
	public static class TrailPacket{
		public TrailPacket(float cT, float lT, int tColour, float yVel){
			creationTime = cT;
			lifeTime = lT;
			colour = tColour;
			yVelocity = yVel;
		}
		int colour;
		float creationTime;
		float lifeTime;
		float yVelocity;
		boolean fade;
	}
	
	//Container for projectile cluster info
	public static class ClusterPacket{
		public ClusterPacket(int cC, float cT){
			clusterCount = cC;
			clusterTimer = cT;
		}
		 int clusterCount;
		 float clusterTimer;
		 
	}

}
