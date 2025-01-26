package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Drop extends TextureObject {
	
	private float dropSpeed = 100;
	
	public Drop(String texture)
	{
		setEntityType(EntityType.DROP);
		setTexture(new Texture(texture));
		setPosition(new Vector2(0, 0));
		setSpeed(10);
	}
	
	public Drop(EntityType type, String texture, Vector2 position, float speed)
	{
		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setSpeed(speed);
	}
	
	@Override
	public void moveAIControlled()
	{
		if (getBody().getPosition().y < 1)
			getBody().setLocation(getPosition().x, Gdx.graphics.getHeight());
		
		updateBody();
	}
}
