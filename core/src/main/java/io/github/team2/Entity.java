package io.github.team2;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity implements iMovable {
	
	protected float x;
	protected float y;
	protected float speed;
	
	public Entity()
	{
		x = 0;
		y = 0;
		speed = 0;
	}
	
	public Entity(float x, float y, float speed)
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public void draw(ShapeRenderer shape)
	{
		
	}
	
	public void draw(SpriteBatch batch)
	{
		
	}
	
	public abstract void update();
}
