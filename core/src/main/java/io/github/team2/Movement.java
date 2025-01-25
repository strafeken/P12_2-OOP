package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;


public interface Movement {
	
	public float screenWidth = Gdx.graphics.getWidth();
	public float screenHeight = Gdx.graphics.getHeight();
	
	// movement
	public void moveTo(Vector2 newPos);
	public void moveDirection(String direction);
	public void moveAIControlled();
	public void moveUserControlled();
	
	
	// rotation movement 
	public void rotateTo(float num);
	
	
	public default boolean checkOutOfBound() {
		
		return true;
	}
	
	
	}


	
