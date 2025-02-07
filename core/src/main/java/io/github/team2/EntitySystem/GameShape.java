package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.SceneSystem.SceneManager;

public abstract class GameShape extends Entity {

	private Color color;
	private float width;
	private float height;

	public GameShape() {
		setEntityType(EntityType.UNDEFINED);
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(0);
		color = Color.WHITE;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getWidth() {

		return this.width;

	}

	public void setWidth(float width) {

		this.width = width;
	}

	public float getHeight() {

		return this.height;

	}

	public void setHeight(float height) {

		this.height = height;
	}

	@Override
	public void moveUserControlled() {

	}

	@Override
	public void moveAIControlled() {

	}



	@Override 	
	public  boolean isOutOfBound(Vector2 direction) {
		
		
		Vector2 projectedPos = this.getPosition();
		projectedPos.add(direction);
		
		if (direction.x < 0 && (projectedPos.x - getWidth()/2) < SceneManager.screenLeft) {
			System.out.println("Hit left");
			return true;
		}
		if (direction.x > 0 && (projectedPos.x + getWidth()/2) > SceneManager.screenWidth) {
			System.out.println("hit right");
			return true;
		}
		
		if (direction.y < 0 && (projectedPos.y - getHeight()/2) < SceneManager.screenBottom) {
			return true;
		}
		if (direction.y > 0 && (projectedPos.y + getHeight()/2) > SceneManager.screenHeight) {
			return true;
		}
		
		return false;
		
	}
	




	public abstract void draw(ShapeRenderer shape);

	public abstract void update();

}
