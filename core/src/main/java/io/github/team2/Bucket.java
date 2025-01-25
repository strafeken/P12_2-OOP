package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

public class Bucket extends TextureObject {
	
	public Bucket()
	{
		
	}
	
	public Bucket(String texture)
	{
		setTexture(new Texture(texture));
		setX(50);
		setY(50);
		setSpeed(10);
	}
	
	public Bucket(EntityType type, String texture, float x, float y, float speed)
	{
		setEntityType(type);
		setTexture(new Texture(texture));
		setX(x);
		setY(y);
		setSpeed(speed);
	}
	
	@Override
	public void moveUserControlled()
	{
        if (getBody() == null)
        	return;
        
		if (Gdx.input.isKeyPressed(Keys.LEFT))	
			this.moveDirection("LEFT");
			
			//getBody().setLinearVelocity(-getSpeed(), 0);
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			this.moveDirection("RIGHT");
			//getBody().setLinearVelocity(getSpeed(), 0);
		else
			getBody().setLinearVelocity(0, 0);
	}
	
	
	
	@Override
	public void moveDirection(String direction)
	{
		if (direction == "LEFT") {getBody().setLinearVelocity(-getSpeed(), 0);}
		if (direction == "RIGHT") {getBody().setLinearVelocity(getSpeed(), 0);}
		if (direction == "UP") {getBody().setLinearVelocity(0, getSpeed());}
		if (direction == "DOWN") {getBody().setLinearVelocity(0, -getSpeed());}
		
	}
	
	@Override
	public void update()
	{
		updateBody();
	}
}
