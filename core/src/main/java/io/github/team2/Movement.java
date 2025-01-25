package io.github.team2;

import com.badlogic.gdx.Gdx;



public interface Movement {
	
	public void moveTo();
	public void rotateTo(float num);
	
	
	public default boolean checkOutOfBound(float left, float right, float up, float down) {
		
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		//System.out.println("print screen Width:" + screenWidth + " Height:" + screenHeight);
		
		if (left < 0 || right > screenWidth) { 
			System.out.println("hit wall");
			return false; 
		}
		
		if (down < -1 || up > screenHeight) { 
			System.out.println("hit wall");
			return false; 
		}
		else {
			return true;
		}
		
		
		
		
	}
	
	
	}


	
