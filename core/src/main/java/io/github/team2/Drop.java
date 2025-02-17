package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.Utils.DisplayManager;

import io.github.team2.Actions.DropBehaviour;
import io.github.team2.Actions.Move;

public class Drop extends DynamicTextureObject<DropBehaviour.State, DropBehaviour.Move> {



	public Drop(String texture) {
		super(new Texture(texture));
		setEntityType(EntityType.DROP);
		setPosition(new Vector2(0, 0));
		initActionMap();
	}

	public Drop(EntityType type, String texture, Vector2 position, Vector2 direction, Vector2 rotation, float speed,
			DropBehaviour.State state, DropBehaviour.Move actionState) {

		super(type, texture, position, direction, rotation, speed, state, actionState);

		

		//setEntityType(type);
		
		initActionMap();

	}

	public boolean checkPosition() {

		if (this.getPosition().y < 0) {

			return true;
		}

		return false;
	}

//	public boolean isOutOfBounds() {
//		return getPosition().y < 0;
//	}

	private void resetPosition() {
		Random random = new Random();
		getBody().setLocation(random.nextFloat() * DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
	}

	private void handleDropMiss() {
//		GameManager.getInstance().getPointsManager().incrementFails();
	}

	
	@Override
	public void initActionMap() {
	 
		  getActionMap().put(DropBehaviour.Move.DROP, new Move(this, new Vector2(0, -1)));
	 
	 }
	 
	 

	public void updateMovement() {

		
		if (getCurrentState() == DropBehaviour.State.IDLE) {

			setCurrentActionState(DropBehaviour.Move.DROP);
			setCurrentState(DropBehaviour.State.MOVING);
		}

		else if (getCurrentState() == DropBehaviour.State.MOVING) {

			switch (getCurrentActionState()) {

			case NONE:
				// state not changed
				System.out.println("drop state stuck in NONE");
				break;

			case DROP:

				// check if reach
				if (checkPosition()) {

					handleDropMiss();
					resetPosition();

				}
				break;

			default:

				System.out.println("Unknown direction drop");
				break;
			}

			getAction(getCurrentActionState()).execute();

		}

		else {

			System.out.println("Unknown state");

		}

	}


	@Override
	public void update() {
		if (getBody() != null) { // Add null check

			updateMovement();
			/*
			 * if (!isOutOfBounds()) { if (getAction() != null) { getAction().execute(); } }
			 * else { handleDropMiss(); resetPosition(); }
			 */
			updateBody();
		}
	}

}
