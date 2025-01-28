package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameShape extends Entity {
	
	private Color color;
	private float width;
	private float height;
	

	public GameShape() {
		

		
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
	
	public Color getColor() {
		
		return this.color;
		
	}
	
	public void setColor(Color color) {
		
		this.color = color;
	}
	
	

	@Override
	public void moveUserControlled()
	{
		
	}
	
	@Override
	public void moveAIControlled()
	{
		
	}
	
	@Override
	public void moveTo(Vector2 position)
	{
		
	}
	
	@Override
	public void moveDirection(String direction)
	{
		
	}
	
	@Override
	public void rotateTo(float num)
	{
		
	}
	
	
	public abstract void draw(ShapeRenderer shape);
	
	
	@Override
	public void update()
	{
//		System.out.println(tex.toString() + " XY: " + getX() + " / " + getY());
	}

}
