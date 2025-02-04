package io.github.team2.EntitySystem;


import com.badlogic.gdx.math.Vector2;

import io.github.team2.SceneSystem.SceneManager;

public interface Movement {	
	
	
	// movement
	public void moveTo(Vector2 newPos);

	public void moveDirection(String direction);

	public void moveAIControlled();

	public void moveUserControlled();

	public boolean checkPosition(Vector2 position);

	// rotation movement
	public void rotateTo(float num);
	
	
	// TODO: shift out of bound to actions
	
	public default boolean checkOutOfBound(TextureObject object, String projectedMovement) {

		if (projectedMovement.equals("LEFT") && object.getSide("LEFT") <SceneManager.screenLeft) {

			System.out.println("hit left wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
		}

		if (projectedMovement.equals("RIGHT") && object.getSide("RIGHT") > SceneManager.screenWidth) {
			System.out.println("hit right wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;

		}

		if (projectedMovement.equals("TOP") && object.getSide("TOP") > SceneManager.screenHeight) {
			System.out.println("hit top wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;

		}

		if (projectedMovement.equals("BOTTOM") && object.getSide("BOTTOM") < SceneManager.screenBottom) {
			System.out.println("bottom hit wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;

		}

		return false;
	}

	public default boolean checkOutOfBound(GameShape object, String projectedMovement) {

		if (projectedMovement.equals("LEFT") && object.getSide("LEFT") < SceneManager.screenLeft) {

			System.out.println("hit left wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;
		}

		if (projectedMovement.equals("RIGHT") && object.getSide("RIGHT") > SceneManager.screenWidth) {
			System.out.println("hit right wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;

		}

		if (projectedMovement.equals("TOP") && object.getSide("TOP") > SceneManager.screenHeight) {
			System.out.println("hit top wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;

		}

		if (projectedMovement.equals("BOTTOM") && object.getSide("BOTTOM") < SceneManager.screenBottom) {
			System.out.println("bottom hit wall");
			object.getBody().setLinearVelocity(0, 0);
			return true;

		}

		return false;
	}

}
