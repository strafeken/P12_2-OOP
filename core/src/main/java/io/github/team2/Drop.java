package io.github.team2;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.DropState;
import io.github.team2.Actions.Move;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneManager;

public class Drop extends TextureObject {

	// private float dropSpeed = 100;

	// TODO: when done shift to dynamic class
	private HashMap<DropState.Move, Action> moveMap;
	// movment states can move to dynamic
	private DropState.State currentState;
	private DropState.Move currentActionState;

	public Drop(String texture) {
		setEntityType(EntityType.DROP);
		setTexture(new Texture(texture));
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(10);

		moveMap = new HashMap<>();
		currentState = DropState.State.IDLE;
		currentActionState = DropState.Move.NONE;
		initActionMoveMap();
	}

	public Drop(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setDirection(direction);
		setSpeed(speed);

		moveMap = new HashMap<>();
		currentState = DropState.State.IDLE;
		currentActionState = DropState.Move.NONE;
		initActionMoveMap();

	}

	public boolean checkPosition() {

		if (this.getPosition().y < 0) {

			return true;
		}

		return false;
	}

	// TODO: move to dynamic class
	
	@Override
	public <E extends Enum<E>> Action getAction(E moveKey) {

		Action action = moveMap.get(moveKey);

		return action;
	}

	public void setCurrentActionState(DropState.Move moveState) {
		currentActionState = moveState;
	}

	public DropState.Move getCurrentActionState() {
		return currentActionState;
	}

	public void setCurrentState(DropState.State state) {
		currentState = state;
	}

	public DropState.State getCurrentState() {
		return currentState;
	}

	public void initActionMoveMap() {

		moveMap.put(DropState.Move.DROP, new Move(this, new Vector2(0, -1)));

	}

	public void clearMoveMap() {

		moveMap.clear();
	}

	public void updateMovement() {
		
		
		if (getCurrentState() == DropState.State.IDLE) {

			setCurrentActionState(DropState.Move.DROP);
			setCurrentState(DropState.State.MOVING);
		} 
		
		else if (getCurrentState() == DropState.State.MOVING) {

			
			switch (getCurrentActionState()) {
			
			case NONE:
				// state not changed
				System.out.println("drop state stuck in NONE");
				break;

			case DROP:

				// check if reach 
				if (checkPosition()) {
					
					// Increment fail counter through GameScene
	                if (GameScene.getInstance().getPointsManager() != null) {
	                    GameScene.getInstance().getPointsManager().incrementFails();
	                }
	           
	            Random random =new Random();
	            getBody().setLocation(random.nextFloat() * SceneManager.screenWidth, SceneManager.screenHeight);
	            
				}
				break;
				

			default:
				
				System.out.println("Unknown direction drop");
				break;
			}

			getAction(getCurrentActionState()).execute();

		}

	else{
		
		System.out.println("Unknown state");

		}

	}

	/*
	 * 
	 * 
	 * if (this.checkPosition() == false) {
	 * 
	 * if (getAction() != null) getAction().execute();
	 * 
	 * } else {
	 * 
	 * // Check if drop hit bottom //if (this.getPosition().y < 0) { // Increment
	 * fail counter through GameScene if (GameScene.getInstance().getPointsManager()
	 * != null) { GameScene.getInstance().getPointsManager().incrementFails(); } //}
	 * Random random =new Random(); getBody().setLocation(random.nextFloat() *
	 * SceneManager.screenWidth, SceneManager.screenHeight); }
	 * 
	 * }
	 */

	@Override
	public void update() {

		updateMovement();
		updateBody();
	}

}
