package io.github.team2.EntitySystem;

import java.util.HashMap;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Game.entity.EntityType;
import io.github.team2.InputSystem.Action;

public abstract class Dynamics<S extends Enum<S>, A extends Enum<A>> extends Entity {
	private float speed;
	private HashMap<A, Action> ActionMap;
	private S currentState;
	private A currentActionState;
	private Vector2 rotation;

	public Dynamics() {
		super();
		this.speed = 0;
		ActionMap = new HashMap<>();
		currentState = null;
		currentActionState = null;

	}

	public Dynamics(EntityType type, Vector2 position, Vector2 direction, Vector2 rotation, float speed, S state,
			A actionState) {
		super(type, position, direction);
		this.speed = speed;
		this.rotation = rotation;
		ActionMap = new HashMap<>();
		currentState = state;
		currentActionState = actionState;

	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public Vector2 getRotation() {
		return rotation;
	}

	public void setRotation(Vector2 rotation) {
		this.rotation = rotation;
	}

	public <E extends Enum<E>> Action getAction(E moveKey) {

		Action action = ActionMap.get(moveKey);

		return action;
	}

	public HashMap<A, Action> getActionMap() {
		return (HashMap<A, Action>) ActionMap;
	}

	public void clearActionMap() {

		ActionMap.clear();
	}

	public void setCurrentActionState(A actionState) {
		currentActionState = actionState; // Cast needed if `currentActionState` is strongly typed
	}

	public A getCurrentActionState() {
		return currentActionState;
	}

	public void setCurrentState(S state) {
		currentState = state;
	}

	public S getCurrentState() {
		return currentState;
	}

	@Override
	public void update() {
		// Update position based on direction and speed
		Vector2 currentPos = getPosition();
		Vector2 direction = getDirection();
		currentPos.add(direction.x * speed, direction.y * speed);
		setPosition(currentPos);

		// Update physics body
		updateBody();
	}

	public abstract boolean isOutOfBound(Vector2 direction);

	public abstract void initActionMap();

}
