package io.github.team2;

import com.badlogic.gdx.math.Vector2;


public interface Movement {
	
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


	
