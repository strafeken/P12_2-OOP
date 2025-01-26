package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Drop extends TextureObject {
	
	private float dropSpeed = 100;
	
	public Drop()
	{
		
	}
	
	public Drop(String texture)
	{
		setTexture(new Texture(texture));
		setX(50);
		setY(50);
		setSpeed(10);
	}
	
	public Drop(EntityType type, String texture, float x, float y, float speed)
	{
		setEntityType(type);
		setTexture(new Texture(texture));
		setX(x);
		setY(y);
		setSpeed(speed);
	}
	
	@Override
	public void moveAIControlled()
	{
		
		if (getBody().getPosition().y < 1)
			getBody().setLocation(getX(), Gdx.graphics.getHeight());
		
		updateBody();
	}
	
	@Override
	public void moveTo(Vector2 position) {
		
		
	}
	
}
