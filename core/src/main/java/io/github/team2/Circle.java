package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.GameShape;

public class Circle extends GameShape {

	private float radius;

	public Circle() {
		setEntityType(EntityType.CIRCLE);
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(0);
		setColor(Color.WHITE);
		radius = 10;
	}

	public Circle(EntityType type, Vector2 position, Vector2 direction, float speed, Color color, float radius) {
		setEntityType(type);
		setColor(color);
		setPosition(position);
		setDirection(direction);
		setSpeed(speed);
		this.radius = radius;

		// set width and height
		this.setWidth(radius * 2);
		this.setHeight(radius * 2);

	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public void draw(ShapeRenderer shape) {

		shape.setColor(this.getColor());

		shape.circle(getPosition().x, getPosition().y, radius);
	}

	// movement controls

	@Override
	public void moveUserControlled() {
		if (getBody() == null)
			return;

		
	}

	@Override
	public void moveAIControlled() {

	}



	


	@Override
	public void update() {
//		System.out.println("Circle  XY: " + getX() + " / " + getY() + " Radius: " + radius);
		updateBody();
	}
}
