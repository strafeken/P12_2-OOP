package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Circle extends Entity {
	
	private Color color;
	private float radius;
	
	public Circle()
	{
		color = Color.WHITE;
		radius = 10;
		setX(50);
		setY(50);
		setSpeed(10);
	}
	
	public Circle(Color color, float radius, float x, float y, float speed)
	{
		this.color = color;
		this.radius = radius;
		setX(x);
		setY(y);
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
		shape.circle(getX(), getY(), radius);
	}
	
	@Override
	public void moveUserControlled()
	{
		if (Gdx.input.isKeyPressed(Keys.A))
			setX(getX() - speed * Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Keys.D))
			setX(getX() + speed * Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void moveAIControlled()
	{
		
	}
	
	@Override
	public void update()
	{
		System.out.println("Circle  XY: " + getX() + " / " + getY() + " Radius: " + radius);
	}
}
