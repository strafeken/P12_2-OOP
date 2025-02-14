package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.TriangleBehaviour;
import io.github.team2.SceneSystem.SceneManager;

public class DynamicGameShape <S extends Enum<S>, A extends Enum<A>> extends Dynamics<S,A> {

	protected Color color;
	protected float width;
	protected float height;

	public DynamicGameShape() {
		super(0);
		setEntityType(EntityType.UNDEFINED);

		color = Color.WHITE;

	}

	public DynamicGameShape(Vector2 position, Vector2 direction, float speed, Color color , S state, A actionState) {
		super(position, direction, speed,  state,actionState);
		
		color = this.color;
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

		if (direction.x < 0 && (projectedPos.x - getWidth() / 2) < SceneManager.screenLeft) {
			System.out.println("Hit left ");
			return true;
		}
		if (direction.x > 0 && (projectedPos.x + getWidth() / 2) > SceneManager.screenWidth) {
			System.out.println("hit right");
			return true;
		}

		if (direction.y < 0 && (projectedPos.y - getHeight() / 2) < SceneManager.screenBottom) {
			return true;
		}
		if (direction.y > 0 && (projectedPos.y + getHeight() / 2) > SceneManager.screenHeight) {
			return true;
		}

		return false;

	}

	public void initActionMoveMap() {

	}

}
