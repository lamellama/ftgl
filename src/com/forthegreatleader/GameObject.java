package com.forthegreatleader;
/**
 * Base Entity class which all entities should derive from
 */
abstract public class GameObject {
    
    protected float mPosY = 0;
    protected float mPosX = 0;
    
    abstract public void update(double time);

}
