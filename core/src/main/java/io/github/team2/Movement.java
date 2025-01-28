package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;


public interface Movement {
	
	// TODO: possibility to shift screen global variables to scene manager
	
	public float screenWidth = Gdx.graphics.getWidth();
	public float screenHeight = Gdx.graphics.getHeight();
	
	static final float screenLeft = 0;
	static final float screenBottom = -1;
	
	
	// movement
	public void moveTo(Vector2 newPos);
	public void moveDirection(String direction);
	public void moveAIControlled();
	public void moveUserControlled();
	public boolean checkPosition(Vector2 position);
	
	
	// rotation movement 
	public void rotateTo(float num);
	
	
	public default boolean checkOutOfBound(TextureObject object, String projectedMovement) {
		
		if (projectedMovement.equals("LEFT") && object.getSide("LEFT") < screenLeft ) {
			

			System.out.println("hit left wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
		}
			
		if (projectedMovement.equals("RIGHT") &&  object.getSide("RIGHT")  > screenWidth) {
			System.out.println("hit right wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
			
		}
		
		if  (projectedMovement.equals("TOP") &&  object.getSide("TOP")  > screenHeight) {
			System.out.println("hit top wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
			
		}
		
		if  (projectedMovement.equals("BOTTOM") &&  object.getSide("BOTTOM") < screenBottom) {
			System.out.println("bottom hit wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
			
		}
		

		return false;
	}
	
	
	
	public default boolean checkOutOfBound(GameShape object, String projectedMovement) {
		
		if (projectedMovement.equals("LEFT") && object.getSide("LEFT") < screenLeft ) {
			

			System.out.println("hit left wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
		}
			
		if (projectedMovement.equals("RIGHT") &&  object.getSide("RIGHT")  > screenWidth) {
			System.out.println("hit right wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
			
		}
		
		if  (projectedMovement.equals("TOP") &&  object.getSide("TOP")  > screenHeight) {
			System.out.println("hit top wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
			
		}
		
		if  (projectedMovement.equals("BOTTOM") &&  object.getSide("BOTTOM") < screenBottom) {
			System.out.println("bottom hit wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
			
		}
		

		return false;
	}
	
	
	
	
	}


	
