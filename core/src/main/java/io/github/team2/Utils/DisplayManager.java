package io.github.team2.Utils;

import com.badlogic.gdx.Gdx;

public class DisplayManager {
	private static float currentScreenWidth = Gdx.graphics.getWidth();
	private static float currentScreenHeight = Gdx.graphics.getHeight();
	private static final float SCREEN_ORIGIN_X = 0;
	private static final float SCREEN_ORIGIN_Y = 0;
	
	private DisplayManager() {
        // private constructor to prevent instantiation
    }
 
    public static float getScreenWidth() {
        return currentScreenWidth;
    }
 
    public static float getScreenHeight() {
        return currentScreenHeight;
    }
    
    public static float getScreenOriginX() {
    	return SCREEN_ORIGIN_X;
    }
    
    public static float getScreenOriginY() {
    	return SCREEN_ORIGIN_Y;
    }
    
    public static void updateScreenSize() {
        currentScreenWidth = Gdx.graphics.getWidth();
        currentScreenHeight = Gdx.graphics.getHeight();
    }
}
