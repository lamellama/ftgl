package com.forthegreatleader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
//import android.util.Log;

/**
 * A script Entity, separate from gameObjects
 * Used by Script objects
 */
public class ScriptObject{
	//private static final float SCRIPT_PAUSE = 2.0f;
	private static final int TEXT_SIZE = 20;
	private static final int TEXT_SIZE_SMALL = 15;
	private static final int HINT_CONTINUE_DELAY = 5;
	private static final int PAGE_MINIMUM_DISPLAY_TIME = 1;
	
	public ScriptObject(Context mContext, int sprite, Resources res, String text[], float startTime, float endTime, int width, int height, int yPos){
		mWidth = width;
		mHalfWidth = width/2;
		mHeight = height;
		mXPos = -width*2; //TODO BODGE
		mYPos = yPos;
		mStartTime = startTime;
		mEndTime = endTime;
		mBorder = 10;
		
		
		
		mPaintBlackText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mFace = Typeface.createFromAsset(mContext.getAssets(), GlobalState.TYPEFACE_MOLOT);
		
		if(text!=null)
			mPages = text.length;
		else
			mPages = 0;
		if(mPages < 1){ // No Text
			isThereAnyText = false;
		}
		else{
			isThereAnyText = true;
			mText = new String[mPages];
			for(int i = 0; i < mPages; i++)
				mText[i] = text[i];
			mTextSize = TEXT_SIZE;
			

			
			
			mPaintWhiteText = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaintWhiteText.setTypeface(mFace);
			mPaintWhiteText.setColor(Color.WHITE);
			mPaintWhiteText.setAntiAlias(true);
			mPaintWhiteText.setTextSize(mTextSize);
			mPaintWhiteText.setTextAlign(Paint.Align.LEFT);
			
			
			mPaintBlackText.setTypeface(mFace);
			mPaintBlackText.setColor(Color.BLACK);
			mPaintBlackText.setAntiAlias(true);
			mPaintBlackText.setTextSize(mTextSize);
			mPaintBlackText.setTextAlign(Paint.Align.LEFT);
			
			
			
			initialisePageText();
			

			
			
		}
		
		mPaintGreyText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintGreyText.setTypeface(mFace);
		mPaintGreyText.setColor(Color.LTGRAY);
		mPaintGreyText.setAntiAlias(true);
		mPaintGreyText.setTextSize(TEXT_SIZE_SMALL);
		mPaintGreyText.setTextAlign(Paint.Align.LEFT);
		
		//init continue.. tag
		mClickWidths = new float[mSClick.length()];
		mPaintGreyText.getTextWidths(mSClick, mClickWidths);
		for(int p=0; p< mClickWidths.length; p++)
			mClickWidth+=mClickWidths[p];
		
		
		
		mSpriteHeight = mHalfWidth/3;
		mPaintBlack = new Paint();
		mPaintBlack.setColor(Color.BLACK);
		
		mPaintWhite = new Paint();
		mPaintWhite.setColor(Color.WHITE);
		
		
		
		if(sprite > 0){
			isThereASprite = true;
			if(isThereAnyText)
				mSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, sprite), (int)mSpriteHeight, (int)mSpriteHeight, true);
			else
				mSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, sprite), width-mBorder, mHalfWidth-mBorder, true);
		}
		else
			isThereASprite = false;
	}
	private boolean isThereAnyText = false;
	private boolean isThereASprite = false;
	private float mSpriteHeight;
	//public ScriptObject(){}
	private float mScriptDuration = 0, mPageDuration =0;
	private float mPageHeight = 25;
	private int mPages;
	private boolean mActive = false; 
	private boolean mFinished = false;
	private int mLineCount = 0;
	//TODO variable memory alloc
	private int mLineCharacterCount[] = new int[25];
	private int mTextSize;
	public int mWidth, mHeight;
	public int mHalfWidth = 0;
	public Bitmap mSprite;
	public String mText[];
	private float mStartTime;
	private float mEndTime;
	private Paint mPaintBlack, mPaintWhite, mPaintWhiteText, mPaintBlackText, mPaintGreyText;
	private int mBorder;
	private float mTextWidths[];
	private float mXPos;
	private int mCurrentPage = 0;
	
	String mSClick = "Press to continue ..";
	float mClickWidths[];
	float mClickWidth =0;
	
	public float getStartTime(){return mStartTime;	}
	public float getEndTime(){return mEndTime;	}
	public boolean isActive(){return mActive;}
	public boolean isFinished(){return mFinished;}
	
	public void update(float mElapsedPlayTime, double dT){
		if((mElapsedPlayTime > getStartTime()) && (mElapsedPlayTime < getEndTime())){
			mActive = true;
			if(mXPos < 0)
				mXPos+=dT*1000; // TODO script scroll in speed
			if(mXPos > 0)
				mXPos = 0;
			
			mScriptDuration += dT;
			mPageDuration +=dT;
		}
		else
			mActive = false;

		
	}
	

	
	//Handles rendering pages of text in scripted overlays
	private void initialisePageText(){
		mTextWidths = new float[mText[mCurrentPage].length()];

		
		
		mPaintBlackText.getTextWidths(mText[mCurrentPage], mTextWidths);
		mLineCount = 1;
		mLineCharacterCount[0] = 0;

		float lineWidthCount = 0;
		int i, j;
		
		for(i=0 ; i < mText[mCurrentPage].length(); i++){
			j = i;
			while((!mText[mCurrentPage].regionMatches(j, " ", 0, 1))&&(j < mText[mCurrentPage].length())){
				lineWidthCount += mTextWidths[j];
				j++;
			}
			if(lineWidthCount >= mWidth-(mBorder*10)){ // TODO (border*5) (left border + right border + left and right text space + 2? = 7) should use member variables
				mLineCharacterCount[mLineCount] = i;
				mLineCount++;
				lineWidthCount = 0;
				
			}
			else
			{
				mLineCharacterCount[mLineCount] = j;
				i = j;
			}
		}
		// Do the end of the last line
		mLineCount++;
		mLineCharacterCount[mLineCount] =  mText[mCurrentPage].length();
		
		mPageHeight = mTextSize * mLineCount;
				
	}
	
	public Rect getBoundingRect(){
		Rect r = new Rect(0, (int)mHighestY, mWidth, (int)mLowestY);
		return r;
	}
	
	
	// If the script contains multiple pages, this function moves to the next page
	// Also returns a time to pause before loading the next script
	public boolean skipText(){
		//Change to next page of text
		if(mPageDuration>PAGE_MINIMUM_DISPLAY_TIME){
			mPageDuration = 0;
			if(mCurrentPage+1 < mPages){
				
				mCurrentPage++;
				initialisePageText();
				return true;
			}
			else
			{	//No more pages
				//Will be removed from script list upon next update and cleaned up
				mFinished = true;
				return true;
				//return PAGE_MINIMUM_DISPLAY_TIME - mPageDuration;
			}
		}
		return false;
	}
	//private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Typeface mFace;
    private int mYPos = 50;
    public void setVerticlePosition(int yPos){
    	mYPos = yPos;
    }
    
    private int mLowestY, mHighestY;
    
	public void draw(Canvas c){
		//int YPos = 50;
		//float pageHeight = mTextSize * mLineCount;
		mWidth = c.getWidth(); // TODO BODGE ALERT, scripts can only be the full width of the screen because of this but its needed for the (text newline)
		float border2 = mBorder*2;
		float whiteLeft = mXPos+border2;
		float whiteRight = mXPos + c.getWidth()-mBorder;
		float whiteTop = mYPos;
		float whiteBottom;
		if(isThereAnyText)
			whiteBottom = mPageHeight + mYPos;
		else
			whiteBottom = mHalfWidth+mYPos;
		float blackBottom = whiteBottom + mBorder;
		float blackRight = mXPos+c.getWidth();
		float blackLeft = whiteLeft - mBorder;
		float blackTop = whiteTop - mBorder;
		float distance = blackLeft - mXPos;
		
		//BLACK
		Path path = new Path();
		path.moveTo(blackLeft, blackBottom);
		path.lineTo(blackRight, blackBottom);
		path.lineTo(blackRight, blackTop);
		path.lineTo(blackRight - distance, blackTop - distance);
		path.lineTo(mXPos, blackTop - distance);
		path.lineTo(mXPos, blackBottom - distance);
		
		mHighestY = (int)blackTop;
		
			if((isThereAnyText)&&(isThereASprite)){ //Draw a small sprite in top left and text over a white background, and black outline
				
				mHighestY = (int)(blackTop - distance);
				//Black sprite border
				path.moveTo(mXPos, blackTop - distance);
				path.lineTo(whiteLeft + mSpriteHeight + mBorder, blackTop - distance);
				path.lineTo(whiteLeft + mSpriteHeight + mBorder, whiteTop - mSpriteHeight - mBorder);
				path.lineTo(whiteLeft + mSpriteHeight - distance, whiteTop - mSpriteHeight - distance - mBorder);
				path.lineTo(mXPos, whiteTop - mSpriteHeight - distance - mBorder);
				
				c.drawPath(path, mPaintBlack);
				
				//White text background rect
				c.drawRect(whiteLeft, whiteTop, whiteRight, whiteBottom, mPaintWhite);
				
				c.drawBitmap(mSprite, whiteLeft, whiteTop - mSpriteHeight, null);
				
				for(int i = 0; i < mLineCount ; i++)
					c.drawText(mText[mCurrentPage], mLineCharacterCount[i], mLineCharacterCount[i+1], whiteLeft+mBorder, mTextSize+(mTextSize*i)+mYPos + (mTextSize/2), mPaintBlackText);
			}
			else if((isThereASprite)&&(!isThereAnyText)){ //Draw just a sprite with a black outline

				c.drawPath(path, mPaintBlack);
			
				c.drawBitmap(mSprite, whiteLeft, whiteTop, null);
			}
			else if((!isThereASprite)&&(isThereAnyText)){ //Draw white text in a black box
				c.drawPath(path, mPaintBlack);
				
				//Log.d("ScriptObject", "draw() just text");
				
				for(int i = 0; i < mLineCount ; i++)
					c.drawText(mText[mCurrentPage], mLineCharacterCount[i], mLineCharacterCount[i+1], whiteLeft+mBorder, mTextSize+(mTextSize*i)+mYPos + (mTextSize/2), mPaintWhiteText);
			}
			
			//Draw the Click .. tag
			if(mPageDuration > HINT_CONTINUE_DELAY)
				if((isThereASprite) || (isThereAnyText)){
					 
					Rect r = new Rect((int)(blackRight - mClickWidth - mBorder), (int)blackBottom, (int)blackRight, (int)(blackBottom + TEXT_SIZE_SMALL + mBorder));
					c.drawRect(r, mPaintBlack);
					c.drawText(mSClick, (int)(blackRight - mClickWidth), (int)(blackBottom + TEXT_SIZE_SMALL), mPaintGreyText);
					
					
				}
			mLowestY = (int)(blackBottom + TEXT_SIZE_SMALL + mBorder);
		//}
	//	else
	//		Log.e("ScriptObject", "draw() sprite == null");
	}
	
}
