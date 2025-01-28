package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Circle extends Entity {
	
	private Color color;
	private float radius;
	
	public Circle()
	{
		setEntityType(EntityType.CIRCLE);
		color = Color.WHITE;
		radius = 10;
		setPosition(new Vector2(0, 0));
		setSpeed(0);
	}
	
	public Circle(EntityType type, Color color, float radius, Vector2 position, float speed)
	{
		setEntityType(type);
		this.color = color;
		this.radius = radius;
		setPosition(position);
		setSpeed(speed);
	}
	
	public float getRadius()
	{
		return radius;
	}
	
	public void setRadius(float radius)
	{
		this.radius = radius;
	}
	
	@Override
	public void draw(ShapeRenderer shape)
	{
		shape.setColor(color);
		shape.circle(getPosition().x, getPosition().y, radius);
	}
	
	
	// movement controls
	
	@Override
	public void moveUserControlled()
	{
        if (getBody() == null)
        	return;
        
		if (Gdx.input.isKeyPressed(Keys.A))
			getBody().setLinearVelocity(-getSpeed(), 0);
		else if (Gdx.input.isKeyPressed(Keys.D))
			getBody().setLinearVelocity(getSpeed(), 0);
		else
			getBody().setLinearVelocity(0, 0);
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
	
	@Override
	public boolean checkPosition(Vector2 position)
	{
		
		
		return false;
	}
	
	@Override
	public void update()
	{
//		System.out.println("Circle  XY: " + getX() + " / " + getY() + " Radius: " + radius);
		updateBody();
	}
}
