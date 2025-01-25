package io.github.team2;

import com.badlogic.gdx.math.Vector2;


public interface movement {
	
	// movement
	public void moveTo(Vector2 newPos);
	
	
	// rotation movement 
	public void rotateTo(float num);
	
	
	public default boolean checkOutOfBound() {
		
		return true;
	}
	
	
	}


	
