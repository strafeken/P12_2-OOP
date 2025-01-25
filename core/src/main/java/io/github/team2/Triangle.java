package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Triangle extends Entity {
	
	private Color color;
//	private float x1, y1, x2, y2, x3, y3;
	private float offset = 50;
	
	public Triangle()
	{
		color = Color.WHITE;
		setX(50);
		setY(50);
		setSpeed(10);
//		x1 = getX() - offset;
//		y1 = getY() - offset;
//		x2 = getX();
//		y2 = getY() + offset;
//		x3 = getX() + offset;
//		y3 = getY() - offset;
	}
	
	public Triangle(EntityType type, Color color, float x, float y, float speed)
	{
		setEntityType(type);
		this.color = color;
		setX(x);
		setY(y);
		setSpeed(speed);
//		x1 = getX() - offset;
//		y1 = getY() - offset;
//		x2 = getX();
//		y2 = getY() + offset;
//		x3 = getX() + offset;
//		y3 = getY() - offset;
	}
	
	// THIS IS DUMB
	public Triangle(Color color, float x, float y, float speed, 
			float x1, float y1, 
			float x2, float y2, 
			float x3, float y3)
	{
		this.color = color;
		setX(x);
		setY(y);
		setSpeed(speed);
//		this.x1 = x1;
//		this.y1 = y1;
//		this.x2 = x2;
//		this.y2 = y2;
//		this.x3 = x3;
//		this.y3 = y3;
	}

	@Override
	public void draw(ShapeRenderer shape)
	{
		shape.setColor(color);
//		shape.triangle(x1, y1, x2, y2, x3, y3);
		shape.triangle(getX() - offset, getY() - offset, getX(), getY() + offset, getX() + offset, getY() - offset);
	}
	
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
	public void update()
	{
//		System.out.println("Triangle  XY: " + getX() + " / " + getY());
		updateBody();
	}
}
