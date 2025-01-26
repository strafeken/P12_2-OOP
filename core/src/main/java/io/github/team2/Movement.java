package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;


public interface Movement {
	
	public float screenWidth = Gdx.graphics.getWidth();
	public float screenHeight = Gdx.graphics.getHeight();
	
	static final float screenLeft = 0;
	static final float screenBottom = -1;
	
	
	// movement
	public void moveTo(Vector2 newPos);
	public void moveDirection(String direction);
	public void moveAIControlled();
	public void moveUserControlled();
	
	
	// rotation movement 
	public void rotateTo(float num);
	
	
	public default boolean checkOutOfBound(PhysicsBody body) {
		
			
		if (body.getPosition().x < screenLeft || body.getPosition().x > screenWidth) {
			System.out.println("hit wall");
			return true;
			
		}
		
		if (body.getPosition().y < screenBottom || body.getPosition().y > screenHeight) {
			System.out.println("hit wall");
			return true;
			
		}


		return false;
	}
	
	
	}


	
