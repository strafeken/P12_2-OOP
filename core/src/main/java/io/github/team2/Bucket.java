package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bucket extends TextureObject {
	
	public Bucket(String texture)
	{
		setEntityType(EntityType.BUCKET);
		setTexture(new Texture(texture));
		setPosition(new Vector2(0, 0));
		setSpeed(0);
	}
	
	public Bucket(EntityType type, String texture, Vector2 position, float speed)
	{
		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setSpeed(speed);
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
	public void update()
	{
		updateBody();
	}
}
