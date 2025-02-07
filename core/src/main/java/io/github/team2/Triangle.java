package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.GameShape;

public class Triangle extends GameShape {
	private float offset;
	private float size;

	public Triangle() {
		setEntityType(EntityType.TRIANGLE);
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(0);
		setColor(Color.WHITE);
		size = 10;
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
//		System.out.println("Triangle  XY: " + getX() + " / " + getY());
		updateBody();
	}
}
