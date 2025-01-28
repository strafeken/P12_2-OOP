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
		if (direction == "LEFT") 	{getBody().setLinearVelocity(-getSpeed(), 0);}
		if (direction == "RIGHT") 	{getBody().setLinearVelocity(getSpeed(), 0);}
		if (direction == "UP") 		{getBody().setLinearVelocity(0, getSpeed());}
		if (direction == "DOWN") 	{getBody().setLinearVelocity(0, -getSpeed());}
		
	}
	
	@Override
	public void rotateTo(float num)
	{
		
	}
	
	
	
	public float getSide(String side)
	{	
		float value = 0;
		if (side == "TOP") 		value = this.getPosition().y + (this.getHeight()/2); 
		if (side == "BOTTOM")	value = this.getPosition().y - (this.getHeight()/2);
		
		if (side == "LEFT")		value = this.getBody().getPosition().x - this.getWidth()/2;
		if (side == "RIGHT")	value = this.getPosition().x + (this.getWidth()/2);
		
		
		System.out.println("X:"+ this.getPosition().x + " Y:" +this.getPosition().y + "Value:" +
				value);
		
		return value;
	}
	
	
	public abstract void draw(ShapeRenderer shape);
	
	
	@Override
	public void update()
	{
//		System.out.println(tex.toString() + " XY: " + getX() + " / " + getY());
	}

}
