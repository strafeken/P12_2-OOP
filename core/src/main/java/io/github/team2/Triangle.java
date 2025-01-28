package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Triangle extends Entity {
	
	private Color color;
	private float offset = 50;
	
	public Triangle()
	{
		setEntityType(EntityType.TRIANGLE);
		color = Color.WHITE;
		setPosition(new Vector2(0, 0));
		setSpeed(0);
	}
	
	public Triangle(EntityType type, Color color, Vector2 position, float speed)
	{
		setEntityType(type);
		this.color = color;
		setPosition(position);
		setSpeed(speed);
	}
	
	@Override
	public void draw(ShapeRenderer shape)
	{
		shape.setColor(color);
		shape.triangle(getPosition().x - offset, getPosition().y - offset, getPosition().x, getPosition().y + offset, getPosition().x + offset, getPosition().y - offset);
	}
	
	
	// movement controls
	
	@Override
	public void moveUserControlled()
	{		
        if (getBody() == null)
        	return;
        
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			getBody().setLinearVelocity(-getSpeed(), 0);
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
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
//		System.out.println("Triangle  XY: " + getX() + " / " + getY());
		updateBody();
	}
}
