package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Circle extends GameShape {
	
	private float radius;
	
	public Circle()
	{
		setEntityType(EntityType.CIRCLE);
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(0);
		setColor(Color.WHITE);
		radius = 10;
	}
	
	public Circle(EntityType type, Vector2 position, Vector2 direction, float speed, Color color, float radius)
	{
		setEntityType(type);
		setPosition(position);
		setDirection(direction);
		setSpeed(speed);
		setColor(color);
		this.radius = radius;
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
		shape.setColor(getColor());
		shape.circle(getPosition().x, getPosition().y, radius);
	}
	
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
	public void update()
	{
//		System.out.println("Circle  XY: " + getX() + " / " + getY() + " Radius: " + radius);
		updateBody();
	}
}
