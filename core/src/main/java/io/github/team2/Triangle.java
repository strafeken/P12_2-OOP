package io.github.team2;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.TriangleState;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.GameShape;
import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneManager;

public class Triangle extends GameShape {
	private float offset;
	private float size;
	// TODO: when done shift to dynamic class
	private HashMap<TriangleState.Move, Action> moveMap;
	// movment states can move to dynamic
	private TriangleState.State currentState;
	private TriangleState.Move currentActionState;

	public Triangle() {
		setEntityType(EntityType.TRIANGLE);
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(0);
		setColor(Color.WHITE);
		size = 10;
		moveMap = new HashMap<>();
		currentState = TriangleState.State.IDLE;
		currentActionState = TriangleState.Move.NONE;
		initActionMoveMap();

	}

	public Triangle(EntityType type, Vector2 position, Vector2 direction, float speed, Color color, float size,
			float offset) {
		setEntityType(type);
		setColor(color);
		setPosition(position);
		setDirection(direction);
		setSpeed(speed);
		setSize(size);
		setOffset(offset);
		moveMap = new HashMap<>();
		currentState = TriangleState.State.IDLE;
		currentActionState = TriangleState.Move.NONE;
		initActionMoveMap();

		// auto calculate width and height
		this.setWidth(2 * offset);
		this.setHeight(2 * offset);

	}

	public void setOffset(float offset) {
		this.offset = offset;

	}

	public float getOffset() {

		return this.offset;
	}

	public float getSize() {
		return size * 2;
	}

	public void setSize(float size) {
		this.size = size;
	}

	@Override
	public void draw(ShapeRenderer shape) {

		shape.setColor(this.getColor());

		float x = getPosition().x;
		float y = getPosition().y;

		shape.triangle(x - size, y - size, // bottom-left vertex
				x, y + size, // top vertex
				x + size, y - size // bottom-right vertex
		);

	}

	// TODO: move to dynamic class

	public void setCurrentActionState(TriangleState.Move moveState) {
		currentActionState = moveState;
	}

	public TriangleState.Move getCurrentActionState() {
		return currentActionState;
	}

	public void setCurrentState(TriangleState.State state) {
		currentState = state;
	}

	public TriangleState.State getCurrentState() {
		return currentState;
	}

	public void initActionMoveMap() {

		moveMap.put(TriangleState.Move.LEFT, new Move(this, new Vector2(-1, 0)));
		moveMap.put(TriangleState.Move.RIGHT, new Move(this, new Vector2(1, 0)));

	}

	public void clearMoveMap() {

		moveMap.clear();
	}

//	@Override
//	public Action getAction(TriangleState.Move moveKey) {
//		Action action = moveMap.get(moveKey);
//
//		return action;
//	}

	@Override
	public <E extends Enum<E>> Action getAction(E moveKey) {

		Action action = moveMap.get(moveKey);

		return action;
	}

	public boolean checkPosition() {

		return true;
	}

	public boolean checkPositionLeft() {
		return (getPosition().x - getWidth() / 2) <= SceneManager.screenLeft;
	}

	public boolean checkPositionRight() {
		return (getPosition().x + getWidth() / 2) >= SceneManager.screenWidth;
	}

	public void updateMovement() {
		// move from default
		if (getCurrentState() == TriangleState.State.IDLE) {

			setCurrentActionState(TriangleState.Move.LEFT);
			setCurrentState(TriangleState.State.MOVING);
		} 
		
		else if (getCurrentState() == TriangleState.State.MOVING) {
			switch (getCurrentActionState()) {
			// move left at start
			case NONE:
				// state not changed
				System.out.println("Triangle state stuck in NONE");
				return;

			case LEFT:

				// change dir if reach
				if (checkPositionLeft()) {

					setCurrentActionState(TriangleState.Move.RIGHT);
				}

				break;

			case RIGHT:
				// change dir if reach
				if (checkPositionRight()) {
					setCurrentActionState(TriangleState.Move.LEFT);
				}
				break;

			default:
				System.out.println("Unknown direction");
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
//		System.out.println("Triangle  XY: " + getX() + " / " + getY());
		updateMovement();
		updateBody();
	}
}
