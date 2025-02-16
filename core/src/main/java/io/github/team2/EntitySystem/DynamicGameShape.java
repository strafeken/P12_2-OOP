package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Utils.DisplayManager;

public abstract class  DynamicGameShape <S extends Enum<S>, A extends Enum<A>> extends Dynamics<S,A> {

	private Color color;
	private float width;
	private float height;

	public DynamicGameShape() {
		super();
		setEntityType(EntityType.UNDEFINED);

		color = Color.WHITE;

	}


	public DynamicGameShape(EntityType type, Vector2 position, Vector2 direction,Vector2 rotation, float speed, Color color , S state, A actionState) {
		super(type, position, direction, rotation, speed,  state,actionState);

		
		this.color = color;
        System.out.println("check if still work in game shape ");


	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public boolean isOutOfBound(Vector2 direction) {

		Vector2 projectedPos = this.getPosition();
		projectedPos.add(direction);

		if (direction.x < 0 && (projectedPos.x - getWidth() / 2) < DisplayManager.getScreenOriginX()) {
			System.out.println("Hit left ");
			return true;
		}
		if (direction.x > 0 && (projectedPos.x + getWidth() / 2) > DisplayManager.getScreenWidth()) {
			System.out.println("hit right");
			return true;
		}

		if (direction.y < 0 && (projectedPos.y - getHeight() / 2) < DisplayManager.getScreenOriginY()) {
			return true;
		}
		if (direction.y > 0 && (projectedPos.y + getHeight() / 2) > DisplayManager.getScreenHeight()) {
			return true;
		}

		return false;

	}
	
	public void initActionMap() {
		
	};

	
	public abstract void draw(ShapeRenderer shapeRenderer);
	
}
