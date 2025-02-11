package io.github.team2;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.MoveCommand;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.GameShape;
import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneManager;

public class Triangle extends GameShape {
	private float offset;
	private float size;
	// TODO: when done shift to dynamic class
	private HashMap<MoveCommand, Action> moveMap;
	private MoveCommand currentActionState;

	public Triangle() {
		setEntityType(EntityType.TRIANGLE);
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(0);
		setColor(Color.WHITE);
		size = 10;
		moveMap = new HashMap<>();
		currentActionState = MoveCommand.none;

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
		currentActionState = MoveCommand.none;

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

	// TODO : should add moves here or in game scene ? allow entity to add themself
	// in game scene ?

	// public void addActionMoveMap(MoveCommand moveKey, Action moveAction) {
	// moveMap.put(moveKey, moveAction);

	public void setCurrentActionState(MoveCommand moveState) {
		currentActionState = moveState;
	}

	public MoveCommand getCurrentActionState() {
		return currentActionState;
	}

	public void addActionMoveMap() {

		moveMap.put(MoveCommand.left, new Move(this, new Vector2(-1, 0)));
		moveMap.put(MoveCommand.right, new Move(this, new Vector2(1, 0)));

	}

	public void clearMoveMap() {

		moveMap.clear();
	}

	@Override
	public Action getAction(MoveCommand moveKey) {
		Action action = moveMap.get(moveKey);

		return action;
	}

	public boolean checkPosition() {

		return true;
	}
	
	
	public boolean checkPositionLeft() {
	    return this.getPosition().x <= SceneManager.screenLeft;
	}

	public boolean checkPositionRight() {
	    return (this.getPosition().x + getWidth()) >= SceneManager.screenWidth;
	}
	
	

	public void updateMovement() {
		// move from default
		
		
		switch (this.getCurrentActionState()) {
		// move right at start
		case none:
			this.setCurrentActionState(MoveCommand.left);
			break;
			
		case left:
			
			// change dir if reach 
			if (this.checkPositionLeft()) {
				this.setCurrentActionState(MoveCommand.right);
			}

			break;
			
		case right:
			// change dir if reach 
			if (this.checkPositionRight()) {
				this.setCurrentActionState(MoveCommand.left);
			}
			break;

		default:
			System.out.println("Unknown direction");
			break;
		}
		
		this.getAction(this.getCurrentActionState()).execute();
		


	}

	@Override
	public void update() {
//		System.out.println("Triangle  XY: " + getX() + " / " + getY());
		updateMovement();
		updateBody();
	}
}
