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
		tex = new Texture(texture);
		setX(50);
		setY(50);
		setSpeed(10);
	}
	
	public Bucket(String texture, float x, float y, float speed)
	{
		tex = new Texture(texture);
		setX(x);
		setY(y);
		setSpeed(speed);
	}
	
	@Override
	public void moveUserControlled()
	{
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			setX(getX() - speed * Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			setX(getX() + speed * Gdx.graphics.getDeltaTime());
	}
}
