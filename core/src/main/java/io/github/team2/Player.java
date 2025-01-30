package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player extends TextureObject {
	
	public Player(EntityType type, String texture, Vector2 position, Vector2 direction, float speed)
	{
		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setDirection(direction);
		setSpeed(speed);
	}

	@Override
	public void moveUserControlled()
	{
        if (getBody() == null)
        	return;
	}
	
	@Override
	public void update()
	{
		updateBody();
	}
}
