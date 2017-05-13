package com.forthegreatleader;

import android.app.Application;
import android.graphics.Color;
//import android.util.Log;


/**
 * Shared global values
 * Holds information used for reference by the menu's
 */
public class GlobalState extends Application{
	
	public static final int GAME_NEW = 8;
	public static final int GAME_CONTINUE = 9;
	public static final int PLAYER_UPGRADE = 7;
	public static final int NEXT_LEVEL = 6;
	public static final int LEVEL_END = 5;
	public static final int STARTING_BALANCE = 0;
	public static final int CASH_VALUE_ENEMY_HIT = 50;
	
	//public static final String TYPEFACE_CHINA = new String("fonts/CHINESER.ttf");
	public static final String TYPEFACE_MOLOT = new String("fonts/molot.ttf");
	
	public static final int STATE_PLAYING = 701;
	public static final int STATE_INTRO = 702;
	public static final int STATE_LOADING = 703;
	public static final int STATE_SCRIPTING = 704;
	
	
	public static final int UPGRATETYPE_SIMPLE = 201;
	public static final int UPGRATETYPE_SIMPLE_ARROW = 202;
	public static final int UPGRATETYPE_SPRITE = 211;
	public static final int UPGRATETYPE_NONWEAPON = 221;
	
	public static final int LEVEL_FAILED = 901;
	public static final int LEVEL_SUCCEEDED = 902;
	
	//REQUEST CODES
	public static final int RTYPEACT = 301;
	public static final int MAINMENUACT = 302;
	
	public static final int ENEMYSIDE = 451;
	public static final int PLAYERSIDE = 450;
	public static final int NEUTRALSIDE = 452;
	
	public static final int lightGrey = Color.rgb(205, 207, 208);
	public static final int darkGrey = Color.rgb(109, 110, 111);
	public static final int red = Color.rgb(194, 31, 34);
	public static final int blue = Color.rgb(67, 79, 147);
	
	public boolean readyForNextLevel = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//bitmapList = new ArrayList<Bitmap>();
		//bitmapInfo = new BitmapInfo();
		playerInfo = new PlayerInformation();
	}
	
	//public ArrayList<Bitmap> bitmapList;
	

	
	public class Upgrade{
		//Non Weapon
		public Upgrade(String name, int ID,
				int dependancy, String iconResourceFilename, int type, int cost, int ammo, float firePower, float fireRate, float range){
			mName = name;
			mIcon = iconResourceFilename;
			mDependantID = dependancy;
			mID = ID;
			mFirePower = firePower;
			mFireRate = fireRate;
			mCost = cost;
			mRange = range;
			mAmmo = ammo;
			mType = type;
			
			//mPurchased = true;
		}
		//Weapon
		public Upgrade(int ID,
				int dependancy,
				String name, 
				String iconResourceFilename,
				int type,
				boolean purchased,
				int cost, 
				int power,
				int colour,
				int life,
				int maxAmmo,
				double lifeTime, 
				float width, float height,
				float fireRateRand,
				float fireRate,
				float propulsionY,
				float propulsionX,
				float propulsionTime,
				float initialVelocityXRand,
				float initialVelocityX, 
				float initialVelocityYRand,
				float initialVelocityY,
				int mass,
				boolean trailable,
				float trailCreationTime,
				float trailLifeTime,
				int trailColour,
				int clusterCount,
				float clusterTime){
			
			mDependantID = dependancy;
			mID = ID;
			mName = name;
			mPurchased = purchased;
			mIcon = iconResourceFilename;
			mType = type;
			mProjectileColour = colour;
			mLife = life;
			mMaxAmmo = maxAmmo;
			mLifeTime = lifeTime;
			mCost = cost;
			mFirePower = power;
			mFireRateRandom = fireRateRand;
			mFireRate = fireRate;
			mHeight = height; 
			mWidth = width;
			mPropulsionY = propulsionY; 
			mPropulsionX = propulsionX; 
			mPropulsionFuelTime = propulsionTime;
			mInitialVelocityXRandom = initialVelocityXRand;
			mInitialVelocityX = initialVelocityX;
			mInitialVelocityYRandom = initialVelocityYRand;
			mInitialVelocityY = initialVelocityY;
			mMass = mass;
			mTrailable = trailable;
			mTrailLifeTime = trailLifeTime;
			mTrailCreationTime = trailCreationTime;
			mTrailColour = trailColour;
			if(clusterCount > 0)
				mClustered = true;
			mClusterCount = clusterCount;
			mClusterTime = clusterTime;
		}
		//Sprite Weapon
		public Upgrade(String name, 
				
				int type,
				String resourceFilename,
				String iconResourceFilename,
				boolean purchased,
				int cost, 
				int power,
				int width, int height,
				float fireRate,
				float propulsionY,
				float propulsionX,
				float propulsionTime,
				float initialVelocityX, float initialVelocityY,
				int mass){
			mName = name;
			mType = type;
			mPurchased = purchased;
			mSpriteResourceFilename = resourceFilename;
			mIcon = iconResourceFilename;
			mCost = cost;
			mFirePower = power;
			mFireRate = fireRate;
			mHeight = height; 
			mWidth = width;
			mPropulsionY = propulsionY; 
			mPropulsionX = propulsionX; 
			mPropulsionFuelTime = propulsionTime;
			mInitialVelocityX = initialVelocityX;
			mInitialVelocityY = initialVelocityY;
			mMass = mass;
		}
		
		private int mDependantID = 0;
		private int mID = 0;
		public int getID(){return mID;}
		private String mName;
		public String getName(){return mName;}
		private String mSpriteResourceFilename;
		public String getSpriteResourceName(){return mSpriteResourceFilename;}
		private int mType = 0;
		public int getType(){
			if(mType == 0)
				return UPGRATETYPE_SIMPLE;
			return mType;}
		private int mLife;
		private double mLifeTime = 0;
		public double getLifeTime(){return mLifeTime;}
		public int getLife(){return mLife;}
		private int mCost;
		public int getCost(){return mCost;}
		private float mFirePower = 0;
		public float getFirePower(){return mFirePower;}
		private float mFireRate = 100, mFireRateRandom = 0;
		public float getFireRate(){return mFireRate;}
		private int mMaxAmmo = 5;
		public int  getMaxAmmo(){return mMaxAmmo;}
		public float getFireRateRandom(){return mFireRateRandom;}
		private int mAmmo = 0;
		public int getExtraAmmo(){return mAmmo;}
		private float mHeight = 1, mWidth = 1;
		public float getHeight(){return mHeight;}
		public float getWidth(){return mWidth;}
		private float mPropulsionY = 0, mPropulsionX = 0, mPropulsionFuelTime = 0;
		public float getPropulsionY(){return mPropulsionY;}
		public float getPropulsionX(){return mPropulsionX;}
		public float getPropulsionFuelTime(){return mPropulsionFuelTime;}
		private float mInitialVelocityX = 0, mInitialVelocityY = 0;
		private float mInitialVelocityXRandom = 0, mInitialVelocityYRandom = 0;
		public float getInitialVelocityXRandom(){return mInitialVelocityXRandom;}
		public float getInitialVelocityYRandom(){return mInitialVelocityYRandom;}
		public float getInitialVelocityX(){return mInitialVelocityX;}
		public float getInitialVelocityY(){return mInitialVelocityY;}
		//public int mInitialVelocityMinX, mInitialVelocityMinY;
		
		private float mMass = 10;
		public float getMass(){return mMass;}
		private float mRange = 0;
		public float getRange(){return mRange;}
		private boolean mPurchased = false;
		public boolean mCreated = false;
		public void purchase(){mPurchased = true;}
		public void unPurchase(){mPurchased = false;}
		public boolean isPurchased(){return mPurchased;}
		public void created(){mCreated = true;}
		public void removed(){mCreated = false;}
		public boolean isCreated(){return mCreated;}
		
		private int mProjectileColour = 0;
		public int getProjectileColour(){return mProjectileColour;}
		
		private float mTrailLifeTime = 0;
		public float getTrailLife(){return mTrailLifeTime;}
		private float mTrailCreationTime = 0;
		public float getTrailCreationSpeed(){return mTrailCreationTime;}
		private boolean mTrailable = false;
		public boolean hasTrail(){return mTrailable;}
		private int mTrailColour = Color.WHITE;
		public int getTrailColour(){return mTrailColour;}
		
		private boolean mClustered = false;
		public boolean isClustered(){return mClustered;}
		private int mClusterCount = 0;
		public int getClusterCount(){return mClusterCount;}
		private float mClusterTime = 0;
		public float getClusterTime(){return mClusterTime;}
		private String mIcon;
		public String getIcon(){return mIcon;}
	}
	
	public static final int UPGRADE_NON_DEPENDANT = 1;
	public static final int UPGRADE_DEPENDANT_NOT_PURCHASED = -1;
	public static final int UPGRADE_DEPENDANT_PURCHASED = 2;
	
	private static final int ID_MACHINEGUN_1 = 901;
	private static final int ID_MACHINEGUN_2 = 902;
	private static final int ID_MACHINEGUN_3 = 903;
	
	private static final int ID_CHAINGUN_1 = 904;
	private static final int ID_CHAINGUN_2 = 905;
	private static final int ID_CHAINGUN_3 = 906;
	
	private static final int ID_MISSILE_1 = 907;
	private static final int ID_MISSILE_2 = 908;
	private static final int ID_MISSILE_3 = 909;
	
	private static final int ID_CLUSTERMISSILE_1 = 910;
	private static final int ID_CLUSTERMISSILE_2 = 911;
	private static final int ID_CLUSTERMISSILE_3 = 912;
	
	//private static final int ID_ACCURACY_1 = 920;
	//private static final int ID_ACCURACY_2 = 921;
	//private static final int ID_ACCURACY_3 = 922;
	private static final int ID_FIREPOWER_1 = 925;
	private static final int ID_FIREPOWER_2 = 926;
	private static final int ID_FIREPOWER_3 = 927;
	private static final int ID_FIRESPEED_1 = 930;
	private static final int ID_FIRESPEED_2 = 931;
	private static final int ID_FIRESPEED_3 = 932;
	private static final int ID_AMMO_1 = 935;	
	private static final int ID_AMMO_2 = 936;	
	private static final int ID_AMMO_3 = 937;
	private static final int ID_DISTANCE_1 = 940;	
	private static final int ID_DISTANCE_2 = 941;	
	private static final int ID_DISTANCE_3 = 942;
	

	/**
	 * PlayerInformation Class stores shared information about the player
	 * Upgrades and Money
	 */
	public class PlayerInformation{

		public PlayerInformation(){
			// Upgrades added here will be populated in the UIUpgradesView and ready for use in game
			msUpgrades = new Upgrade[] 
			{
					new Upgrade(
							/* ID 				->*/ ID_MACHINEGUN_1,
							/* Dependent ID 	->*/ 0,
							/* Name 			->*/"Machinegun v1", 
							/* Icon 			->*/ "ic_machinegun",
							/* Type 			->*/ UPGRATETYPE_SIMPLE,
							/* Purchased on start 	->*/ true,
							/* Cost 				->*/ 0, 
							/* Power 				->*/ 4, 
							/* Colour 				->*/ Color.rgb(255, 64, 64),
							/* Life 				->*/ 10, 
							/* Max Ammo 			->*/ 6, 
							/* LifeTime 			->*/ 1.8, 
							/* width 				->*/ 1.5f, 
							/* height 				->*/ 2.0f,
							/* fireRateRandom 		->*/ 50, 
							/* fireRate 			->*/ 0.5f,
							/* propulsionY 			->*/ -10, 
							/* propulsionX 			->*/ 1, 
							/* propulsionTime 		->*/ 0,
							/* initialVelocityXRandom 	->*/ 100, 
							/* initialVelocityMaxX 		->*/ 4, 
							/* initialVelocityYRandom 	->*/ 0, 
							/* initialVelocityMaxY 		->*/ -100,
							/* mass 					->*/ 500,
							/* trail 					->*/ false,
							/* trail creation speed 	->*/ 0,
							/* trail Life 				->*/ 0,
							/* trail Colour 			->*/ 0,
							/* clusterCount 			->*/ 0,
							/* clusterTimer 			->*/ 0),
							new Upgrade(
									/* ID ->*/ ID_MACHINEGUN_2,
									/* Dependent ID ->*/ ID_MACHINEGUN_1,
									/* Name ->*/"Machinegun v2", 
									/* Icon ->*/ "ic_machinegun",
									/* Type ->*/ UPGRATETYPE_SIMPLE,
									/* Purchased on start ->*/ false,
									/* Cost ->*/ 2500, 
									/* Power ->*/ 5, 
									/* Colour ->*/ Color.rgb(204, 64, 64),
									/* Life ->*/ 10, 
									/* Max Ammo ->*/ 5, 
									/* LifeTime ->*/ 1.9, 
									/* width ->*/ 1.5f, 
									/* height ->*/ 2.0f,
									/* fireRateRandom ->*/ 50, 
									/* fireRate ->*/ 1.5f,
									/* propulsionY ->*/ -10, 
									/* propulsionX ->*/ 1, 
									/* propulsionTime ->*/ 0,
									/* initialVelocityXRandom ->*/ 100, 
									/* initialVelocityMaxX ->*/ 4, 
									/* initialVelocityYRandom ->*/ 0, 
									/* initialVelocityMaxY ->*/ -100,
									/* mass ->*/ 500,
									/* trail ->*/ false,
									/* trail creation speed ->*/ 0,
									/* trail Life ->*/ 0,
									/* trail Colour ->*/ 0,
									/* clusterCount ->*/ 0,
									/* clusterTimer ->*/ 0),
									new Upgrade(
											/* ID ->*/ ID_MACHINEGUN_3,
											/* Dependent ID ->*/ ID_MACHINEGUN_2,
											/* Name ->*/"Machinegun v3", 
											/* Icon ->*/ "ic_machinegun",
											/* Type ->*/ UPGRATETYPE_SIMPLE,
											/* Purchased on start ->*/ false,
											/* Cost ->*/ 3000, 
											/* Power ->*/ 5, 
											/* Colour ->*/ Color.rgb(162, 64, 64),
											/* Life ->*/ 10, 
											/* Max Ammo ->*/ 14, 
											/* LifeTime ->*/ 2.2, 
											/* width ->*/ 1.5f, 
											/* height ->*/ 2.0f,
											/* fireRateRandom ->*/ 50, 
											/* fireRate ->*/ 1.5f,
											/* propulsionY ->*/ -10, 
											/* propulsionX ->*/ 1, 
											/* propulsionTime ->*/ 0,
											/* initialVelocityXRandom ->*/ 100, 
											/* initialVelocityMaxX ->*/ 4, 
											/* initialVelocityYRandom ->*/ 0, 
											/* initialVelocityMaxY ->*/ -100,
											/* mass ->*/ 500,
											/* trail ->*/ false,
											/* trail creation speed ->*/ 0,
											/* trail Life ->*/ 0,
											/* trail Colour ->*/ 0,
											/* clusterCount ->*/ 0,
											/* clusterTimer ->*/ 0),
							
					new Upgrade(
							/* ID ->*/ ID_CHAINGUN_1,
							/* Dependent ID ->*/ 0,
							/* Name ->*/"Chaingun v1", 
							/* Icon ->*/ "ic_machinegun",//TODO
							/* Type ->*/ UPGRATETYPE_SIMPLE,
							/* Purchased on start ->*/ false,
							/* Cost ->*/ 8000, 
							/* Power ->*/ 6, 
							/* Colour ->*/ Color.rgb(64, 64, 255),
							/* Life ->*/ 10, 
							/* Max Ammo ->*/ 6, 
							/* LifeTime ->*/ 1.8, 
							/* width ->*/ 1.5f, 
							/* height ->*/ 2.5f,
							/* fireRateRandom ->*/ 100, 
							/* fireRate ->*/ 0.6f,
							/* propulsionY ->*/ 20, 
							/* propulsionX ->*/ 0, 
							/* propulsionTime ->*/ 0,
							/* initialVelocityXRandom ->*/ 100, 
							/* initialVelocityX ->*/ 5, 
							/* initialVelocityYRandom ->*/ 25, 
							/* initialVelocityY ->*/ -100,
							/* mass ->*/ 500,
							/* trail ->*/ true,
							/* trail creation speed ->*/ 0.1f,
							/* trail Life ->*/ 0.5f,
							/* trail Colour ->*/ 0,
							/* clusterCount ->*/ 0,
							/* clusterTimer ->*/ 0),
							new Upgrade(
									/* ID ->*/ ID_CHAINGUN_2,
									/* Dependent ID ->*/ ID_CHAINGUN_1,
									/* Name ->*/"Chaingun v2", 
									/* Icon ->*/ "ic_machinegun",//TODO
									/* Type ->*/ UPGRATETYPE_SIMPLE,
									/* Purchased on start ->*/ false,
									/* Cost ->*/ 3000, 
									/* Power ->*/ 7, 
									/* Colour ->*/ Color.rgb(64, 64, 204),
									/* Life ->*/ 10, 
									/* Max Ammo ->*/ 4, 
									/* LifeTime ->*/ 1.8, 
									/* width ->*/ 1.5f, 
									/* height ->*/ 2.5f,
									/* fireRateRandom ->*/ 100, 
									/* fireRate ->*/ 1.5f,
									/* propulsionY ->*/ 20, 
									/* propulsionX ->*/ 0, 
									/* propulsionTime ->*/ 0,
									/* initialVelocityXRandom ->*/ 150, 
									/* initialVelocityX ->*/ 5, 
									/* initialVelocityYRandom ->*/ 25, 
									/* initialVelocityY ->*/ -100,
									/* mass ->*/ 500,
									/* trail ->*/ true,
									/* trail creation speed ->*/ 0.1f,
									/* trail Life ->*/ 0.5f,
									/* trail Colour ->*/ 0,
									/* clusterCount ->*/ 0,
									/* clusterTimer ->*/ 0),
									new Upgrade(
											/* ID ->*/ ID_CHAINGUN_3,
											/* Dependent ID ->*/ ID_CHAINGUN_2,
											/* Name ->*/"Chaingun v3", 
											/* Icon ->*/ "ic_machinegun",
											/* Type ->*/ UPGRATETYPE_SIMPLE,
											/* Purchased on start ->*/ false,
											/* Cost ->*/ 4000, 
											/* Power ->*/ 10, 
											/* Colour ->*/ Color.rgb(64, 64, 162),
											/* Life ->*/ 8, 
											/* Max Ammo ->*/ 6, 
											/* LifeTime ->*/ 1.8, 
											/* width ->*/ 1.5f, 
											/* height ->*/ 2.5f,
											/* fireRateRandom ->*/ 100, 
											/* fireRate ->*/ 1.5f,
											/* propulsionY ->*/ 20, 
											/* propulsionX ->*/ 0, 
											/* propulsionTime ->*/ 0,
											/* initialVelocityXRandom ->*/ 200, 
											/* initialVelocityX ->*/ 5, 
											/* initialVelocityYRandom ->*/ 25, 
											/* initialVelocityY ->*/ -100,
											/* mass ->*/ 500,
											/* trail ->*/ true,
											/* trail creation speed ->*/ 0.1f,
											/* trail Life ->*/ 0.5f,
											/* trail Colour ->*/ 0,
											/* clusterCount ->*/ 0,
											/* clusterTimer ->*/ 0),
					new Upgrade(
							/* ID ->*/ ID_MISSILE_1,
							/* Dependent ID ->*/ 0,
							/* Name ->*/"Missile v1", 
							/* Icon ->*/ "ic_missile",
							/* Type ->*/ UPGRATETYPE_SIMPLE_ARROW,
							/* Purchased on start ->*/ false,
							/* Cost ->*/ 12000, 
							/* Power ->*/ 60,
							/* Colour ->*/ Color.rgb(255, 244, 64), //Yellow
							/* Life ->*/ 40, 
							/* Max Ammo ->*/ 2, 
							/* LifeTime ->*/ 3.2, 
							/* width ->*/ 2, 
							/* height ->*/ 3,
							/* fireRateRandom ->*/ 0, 
							/* fireRate ->*/ 1.5f,
							/* propulsionY ->*/ 30, 
							/* propulsionX ->*/ 50f, 
							/* propulsionTime ->*/ 0,
							/* initialVelocityXRandom ->*/ 0, 
							/* initialVelocityMaxX ->*/ 75, 
							/* initialVelocityYRandom ->*/ 0, 
							/* initialVelocityMaxY ->*/ 0,
							/* mass ->*/ 2000,
							/* trail ->*/ true,
							/* trail creation speed ->*/ 0.2f,
							/* trail Life ->*/ 1.0f,
							/* trail Colour ->*/ 0,
							/* clusterCount ->*/ 0,
							/* clusterTimer ->*/ 0f),
							new Upgrade(
									/* ID ->*/ ID_MISSILE_2,
									/* Dependent ID ->*/ ID_MISSILE_1,
									/* Name ->*/"Missile v2", 
									/* Icon ->*/ "ic_missile",
									/* Type ->*/ UPGRATETYPE_SIMPLE_ARROW,
									/* Purchased on start ->*/ false,
									/* Cost ->*/ 4000, 
									/* Power ->*/ 80, 
									/* Colour ->*/ Color.rgb(255, 188, 58), //Orange
									/* Life ->*/ 60, 
									/* Max Ammo ->*/ 2, 
									/* LifeTime ->*/ 3.6, 
									/* width ->*/ 2, 
									/* height ->*/ 3,
									/* fireRateRandom ->*/ 0, 
									/* fireRate ->*/ 3.5f,
									/* propulsionY ->*/ 30, 
									/* propulsionX ->*/ 55f, 
									/* propulsionTime ->*/ 0,
									/* initialVelocityXRandom ->*/ 0, 
									/* initialVelocityMaxX ->*/ 100, 
									/* initialVelocityYRandom ->*/ 0, 
									/* initialVelocityMaxY ->*/ 0,
									/* mass ->*/ 2000,
									/* trail ->*/ true,
									/* trail creation speed ->*/ 0.2f,
									/* trail Life ->*/ 1.0f,
									/* trail Colour ->*/ 0,
									/* clusterCount ->*/ 0,
									/* clusterTimer ->*/ 0f),
									new Upgrade(
											/* ID ->*/ ID_MISSILE_3,
											/* Dependent ID ->*/ ID_MISSILE_2,
											/* Name ->*/"Missile v3", 
											/* Icon ->*/ "ic_missile",
											/* Type ->*/ UPGRATETYPE_SIMPLE_ARROW,
											/* Purchased on start ->*/ false,
											/* Cost ->*/ 5000, 
											/* Power ->*/ 80, 
											/* Colour ->*/ Color.rgb(235, 100, 58), //Orange
											/* Life ->*/ 100, 
											/* Max Ammo ->*/ 3, 
											/* LifeTime ->*/ 3.7, 
											/* width ->*/ 2, 
											/* height ->*/ 3,
											/* fireRateRandom ->*/ 0, 
											/* fireRate ->*/ 3.5f,
											/* propulsionY ->*/ 30, 
											/* propulsionX ->*/ 60f, 
											/* propulsionTime ->*/ 0,
											/* initialVelocityXRandom ->*/ 0, 
											/* initialVelocityMaxX ->*/ 100, 
											/* initialVelocityYRandom ->*/ 0, 
											/* initialVelocityMaxY ->*/ 0,
											/* mass ->*/ 2000,
											/* trail ->*/ true,
											/* trail creation speed ->*/ 0.2f,
											/* trail Life ->*/ 1.0f,
											/* trail Colour ->*/ 0,
											/* clusterCount ->*/ 0,
											/* clusterTimer ->*/ 0f),
							
							
					new Upgrade(
							/* ID ->*/ ID_CLUSTERMISSILE_1,
							/* Dependent ID ->*/ 0,
							/* Name ->*/"Cluster Missile v1", 
							/* Icon ->*/ "ic_cluster", // TODO
							/* Type ->*/ UPGRATETYPE_SIMPLE_ARROW,
							/* Purchased on start ->*/ false,
							/* Cost ->*/ 16000, 
							/* Power ->*/ 20, 
							/* Colour ->*/ Color.rgb(154, 75, 163), //Purple,
							/* Life ->*/ 10, 
							/* Max Ammo ->*/ 5, 
							/* LifeTime ->*/ 2, 
							/* width ->*/ 1.5f, 
							/* height ->*/ 3,
							/* fireRateRandom ->*/ 100, 
							/* fireRate ->*/ 1.75f,
							/* propulsionY ->*/ 35, 
							/* propulsionX ->*/ 35, 
							/* propulsionTime ->*/ 0,
							/* initialVelocityXRandom ->*/ 25, 
							/* initialVelocityMaxX ->*/ 45, 
							/* initialVelocityYRandom ->*/ 25,  
							/* initialVelocityMaxY ->*/ 0,
							/* mass ->*/ 1000,
							/* trail ->*/ true,
							/* trail creation speed ->*/ 0.25f,
							/* trail Life ->*/ 3.0f,
							/* trail Colour ->*/ Color.rgb(122, 96, 134),
							/* clusterCount ->*/ 1,
							/* clusterTimer ->*/ 2.0f),
							new Upgrade(
									/* ID ->*/ ID_CLUSTERMISSILE_2,
									/* Dependent ID ->*/ ID_CLUSTERMISSILE_1,
									/* Name ->*/"Cluster Missile v2", 
									/* Icon ->*/ "ic_cluster", // TODO
									/* Type ->*/ UPGRATETYPE_SIMPLE_ARROW,
									/* Purchased on start ->*/ false,
									/* Cost ->*/ 5000, 
									/* Power ->*/ 30, 
									/* Colour ->*/ Color.rgb(200, 140, 200), //Purple,
									/* Life ->*/ 20, 
									/* Max Ammo ->*/ 3, 
									/* LifeTime ->*/ 2, 
									/* width ->*/ 1.5f, 
									/* height ->*/ 3,
									/* fireRateRandom ->*/ 100, 
									/* fireRate ->*/ 3.75f,
									/* propulsionY ->*/ 35, 
									/* propulsionX ->*/ 35, 
									/* propulsionTime ->*/ 0,
									/* initialVelocityXRandom ->*/ 25, 
									/* initialVelocityMaxX ->*/ 45, 
									/* initialVelocityYRandom ->*/ 25,  
									/* initialVelocityMaxY ->*/ 0,
									/* mass ->*/ 1000,
									/* trail ->*/ true,
									/* trail creation speed ->*/ 0.25f,
									/* trail Life ->*/ 3.0f,
									/* trail Colour ->*/ Color.rgb(122, 96, 134),
									/* clusterCount ->*/ 2,
									/* clusterTimer ->*/ 1.8f),
									new Upgrade(
											/* ID 					->*/ ID_CLUSTERMISSILE_3,
											/* Dependent ID 		->*/ ID_CLUSTERMISSILE_2,
											/* Name 				->*/"Cluster Missile v3", 
											/* Icon 				->*/ "ic_cluster", // TODO
											/* Type			 		->*/ UPGRATETYPE_SIMPLE_ARROW,
											/* Purchased on start 	->*/ false,
											/* Cost 				->*/ 7000, 
											/* Power 				->*/ 40, 
											/* Colour 			->*/ Color.rgb(255, 200, 255), //Purple,
											/* Life 			->*/ 20, 
											/* Max Ammo 		->*/ 4, 
											/* LifeTime 		->*/ 2, 
											/* width 			->*/ 1.5f, 
											/* height 			->*/ 3,
											/* fireRateRandom 	->*/ 100, 
											/* fireRate 		->*/ 3.75f,
											/* propulsionY 		->*/ 35, 
											/* propulsionX 		->*/ 35, 
											/* propulsionTime 			->*/ 0,
											/* initialVelocityXRandom 	->*/ 25, 
											/* initialVelocityMaxX 		->*/ 45, 
											/* initialVelocityYRandom 	->*/ 25,  
											/* initialVelocityMaxY 		->*/ 0,
											/* mass 					->*/ 1000,
											/* trail 					->*/ true,
											/* trail creation speed 	->*/ 0.25f,
											/* trail Life 		->*/ 3.0f,
											/* trail Colour 		->*/ Color.rgb(122, 96, 134),
											/* clusterCount 	->*/ 3,
											/* clusterTimer 	->*/ 1.7f),
							
				
			
					////////////////////////////////////////////////////////////////
					// Non - weapons						
											
					/*
					 * Weight reduction level 1
					 */		
			//		new Upgrade(
							/* Name 		->*///"Weight Reduction", 
							/* ID 			->*/// ID_WEIGHT_1,
							/* Dependent ID ->*/// 0,
							/* Icon 		->*/// "ic_aero",
							/* Type 		->*/// UPGRATETYPE_NONWEAPON,
							/* Cost 		->*/// 2500, 
							/* playerMass 	->*/// 50000,
							/* firePower 	->*/// 0f,
							/* fireSpeed 	->*/// 0f,
							/* friction 	->*/// 0),
							
					/*
					 * Aerodynamics level 1
					 */
				//	new Upgrade(
							/* Name 		->*///"Aerodynamics 1", 
							/* ID 			->*/// ID_AERODYNAMICS_1,
							/* Dependent ID ->*/// 0,
							/* Icon 		->*/// "ic_weight",
							/* Type 		->*/// UPGRATETYPE_NONWEAPON,
							/* Cost 		->*/// 2500, 
							/* playerMass 	->*/// 0,
							/* firePower 	->*/// 0f,
							/* fireSpeed 	->*/// 0,
							/* friction 	->*/ //50f),
							
					
					/*
					 * Fire Speed
					 */
					new Upgrade(
							/* Name 		->*/"Reload Speed 1", 
							/* ID 			->*/ ID_FIRESPEED_1,
							/* Dependent ID ->*/ 0,
							/* Icon 		->*/ "ic_firerate",
							/* Type 		->*/ UPGRATETYPE_NONWEAPON,
							/* Cost 		->*/ 4000, 
							/* playerMass 	->*/ 0,
							/* firePower 	->*/ 0f,
							/* fireSpeed 	->*/ 0.1f,
							/* friction 	->*/ 0),
							new Upgrade(
									/* Name 		->*/"Reload Speed 2", 
									/* ID 			->*/ ID_FIRESPEED_2,
									/* Dependent ID ->*/ ID_FIRESPEED_1,
									/* Icon 		->*/ "ic_firerate",
									/* Type 		->*/ UPGRATETYPE_NONWEAPON,
									/* Cost 		->*/ 8000, 
									/* playerMass 	->*/ 0,
									/* firePower 	->*/ 0f,
									/* fireSpeed 	->*/ 0.1f,
									/* friction 	->*/ 0),
									new Upgrade(
											/* Name 		->*/"Reload Speed 3", 
											/* ID 			->*/ ID_FIRESPEED_3,
											/* Dependent ID ->*/ ID_FIRESPEED_2,
											/* Icon 		->*/ "ic_firerate",
											/* Type 		->*/ UPGRATETYPE_NONWEAPON,
											/* Cost 		->*/ 16000, 
											/* playerMass 	->*/ 0,
											/* firePower 	->*/ 0f,
											/* fireSpeed 	->*/ 0.1f,
											/* friction 	->*/ 0),
							
					/*
					 * Fire Power
					 */
					new Upgrade(
							/* Name 		->*/"Fire Power 1", 
							/* ID 			->*/ ID_FIREPOWER_1,
							/* Dependent ID ->*/ 0,
							/* Icon 		->*/ "ic_firerate",
							/* Type 		->*/ UPGRATETYPE_NONWEAPON,
							/* Cost 		->*/ 5000, 
							/* playerMass +	->*/ 0,
							/* firePower +	->*/ 1f,
							/* fireSpeed +	->*/ 0f,
							/* friction +	->*/ 0),
							new Upgrade(
									/* Name 		->*/"Fire Power 2", 
									/* ID 			->*/ ID_FIREPOWER_2,
									/* Dependent ID ->*/ 0,
									/* Icon 		->*/ "ic_firerate",
									/* Type 		->*/ UPGRATETYPE_NONWEAPON,
									/* Cost 		->*/ 15000, 
									/* playerMass +	->*/ 0,
									/* firePower +	->*/ 1f,
									/* fireSpeed +	->*/ 0f,
									/* friction +	->*/ 0),
									new Upgrade(
											/* Name 		->*/"Fire Power 3", 
											/* ID 			->*/ ID_FIREPOWER_3,
											/* Dependent ID ->*/ 0,
											/* Icon 		->*/ "ic_firerate",
											/* Type 		->*/ UPGRATETYPE_NONWEAPON,
											/* Cost 		->*/ 35000, 
											/* playerMass +	->*/ 0,
											/* firePower +	->*/ 1f,
											/* fireSpeed +	->*/ 0f,
											/* friction +	->*/ 0),

									
					/*
					 * Ammo Capacity
					 */
					new Upgrade(
							/* Name 		->*/"Ammo Capacity 1", 
							/* ID 			->*/ ID_AMMO_1,
							/* Dependent ID ->*/ 0,
							/* Icon 		->*/ "ic_accuracy",
							/* Type 		->*/ UPGRATETYPE_NONWEAPON,
							/* Cost 		->*/ 3000, 
							/* Ammo +		->*/ 2,
							/* firePower +	->*/ 0,
							/* fireSpeed +	->*/ 0f,
							/* range time +	->*/ 0),
							new Upgrade(
									/* Name 		->*/"Ammo Capacity 2", 
									/* ID 			->*/ ID_AMMO_2,
									/* Dependent ID ->*/ ID_AMMO_1,
									/* Icon 		->*/ "ic_accuracy",
									/* Type 		->*/ UPGRATETYPE_NONWEAPON,
									/* Cost 		->*/ 6000, 
									/* Ammo +		->*/ 2,
									/* firePower +	->*/ 0,
									/* fireSpeed +	->*/ 0f,
									/* range time +	->*/ 0),
									new Upgrade(
											/* Name 		->*/"Ammo Capacity 3", 
											/* ID 			->*/ ID_AMMO_3,
											/* Dependent ID ->*/ ID_AMMO_2,
											/* Icon 		->*/ "ic_accuracy",
											/* Type 		->*/ UPGRATETYPE_NONWEAPON,
											/* Cost 		->*/ 14000, 
											/* Ammo +		->*/ 2,
											/* firePower +	->*/ 0,
											/* fireSpeed +	->*/ 0f,
											/* range time +	->*/ 0),
							
					/*
					 * Weapon Range
					 */
					new Upgrade(
							/* Name 		->*/"Weapon Range 1", 
							/* ID 			->*/ ID_DISTANCE_1,
							/* Dependent ID ->*/ 0,
							/* Icon 		->*/ "ic_accuracy",
							/* Type 		->*/ UPGRATETYPE_NONWEAPON,
							/* Cost 		->*/ 3000, 
							/* Ammo	+		->*/ 0,
							/* firePower +	->*/ 0,
							/* fireSpeed +	->*/ 0f,
							/* range time +	->*/ 0.35f),
							new Upgrade(
									/* Name 		->*/"Weapon Range 2", 
									/* ID 			->*/ ID_DISTANCE_2,
									/* Dependent ID ->*/ ID_DISTANCE_1,
									/* Icon 		->*/ "ic_accuracy",
									/* Type 		->*/ UPGRATETYPE_NONWEAPON,
									/* Cost 		->*/ 6000, 
									/* Ammo	+		->*/ 0,
									/* firePower +	->*/ 0,
									/* fireSpeed +	->*/ 0f,
									/* range time +	->*/ 0.35f),
									new Upgrade(
											/* Name 		->*/"Weapon Range 3", 
											/* ID 			->*/ ID_DISTANCE_3,
											/* Dependent ID ->*/ ID_DISTANCE_2,
											/* Icon 		->*/ "ic_accuracy",
											/* Type 		->*/ UPGRATETYPE_NONWEAPON,
											/* Cost 		->*/ 14000, 
											/* Ammo	+		->*/ 0,
											/* firePower +	->*/ 0,
											/* fireSpeed +	->*/ 0f,
											/* range time +	->*/ 0.35f),

			};
		}
		
		
		//Global Player variables
		private int mCash = STARTING_BALANCE;
		public Upgrade[] msUpgrades;
		
		//Determine whether an upgrades previous upgrade has been purchased, ie so you can't buy "missile 2" without first buying "missile 1"
		public int upgradeDependancyState(int ID){
			for(int i = 0; i< msUpgrades.length; i++){
				if(msUpgrades[i].mID == ID){
					if(msUpgrades[i].mDependantID == 0){
						return UPGRADE_NON_DEPENDANT; 
					}
					else{
						for(int j = 0; j< msUpgrades.length; j++){
							if(msUpgrades[i].mDependantID == msUpgrades[j].mID){
								if(msUpgrades[j].mPurchased)
									return UPGRADE_DEPENDANT_PURCHASED;
								else
									return UPGRADE_DEPENDANT_NOT_PURCHASED;
							}
						}
					}
				}
			}
			//Log.d("PlayerInformation.upgradeDependancy()", "Did not find a matching dependant for ID: " + ID); //This should never get called
			return UPGRADE_NON_DEPENDANT;
		}
		
		// Functions
		public void addCash(int amount){	mCash += amount;		}
		public void setBalance(int amount){mCash = amount;}
		public int getBalance(){return mCash;}
		//public int getTotalRevenue(){return mTotalRevenue;}
		public boolean removeCash(int amount){
			if(amount <= mCash)
				mCash -= amount;
			else
				return false;
			
			return true;
			}
		public String[] getUpgradeList(){
			String upgradesList[] = new String[msUpgrades.length];
			for(int i = 0; i < msUpgrades.length; i++)
				upgradesList[i] = msUpgrades[i].mName;
			
			return upgradesList;
		}
	}

	public PlayerInformation playerInfo;
	//public BitmapInfo bitmapInfo;
}
