package io.github.team2;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity implements Movement {
	
	//protected float x;
	//protected float y;
	
	protected Vector2 position;
	protected float speed;
	
	public Entity()
	{
		position = new Vector2();
		speed = 0;
	}
	
	public Entity(float x, float y, float speed)
	{
		this.position = new Vector2(x , y);
		this.speed = speed;
	}
	/*
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
	*/
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public void setPosition(float x, float y)
	{
		
		this.position.set(x, y);
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
