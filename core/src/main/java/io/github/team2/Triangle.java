package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Triangle extends Entity {
	
	private Color color;
	private float size;
	
	public Triangle()
	{
		setEntityType(EntityType.TRIANGLE);
		setPosition(new Vector2(0, 0));
		setSpeed(0);
		color = Color.WHITE;
		size = 10;
	}
	
	public Triangle(EntityType type, Vector2 position, float speed, Color color, float size)
	{
		setEntityType(type);
		setPosition(position);
		setSpeed(speed);
		this.color = color;
		this.size = size;
	}
	
	public float getSize()
	{
		return size * 2;
	}
	
	public void setSize(float size)
	{
		this.size = size;
	}
	
	@Override
	public void draw(ShapeRenderer shape)
	{
		shape.setColor(color);
		
		float x = getPosition().x;
		float y = getPosition().y;
		
		shape.triangle(
				x - size, y - size, // bottom-left vertex
				x, y + size, // top vertex
				x + size, y - size // bottom-right vertex
				);
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
