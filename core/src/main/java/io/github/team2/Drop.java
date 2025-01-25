package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class Drop extends TextureObject {
	
	private float dropSpeed = 100;
	
	public Drop()
	{
		
	}
	
	public Drop(String texture)
	{
		tex = new Texture(texture);
		
		/*
		setX(50);
		setY(50);
		*/
		
		setPosition(50, 50);
		setSpeed(10);
	}
	
	public Drop(String textureFile, float x, float y, float speed)
	{
		tex = new Texture(textureFile);
		setPosition(x, y);
		/*
		setX(x);
		setY(y);
		*/
		
		setSpeed(speed);
	}
	
	
	/*
	@Override
	public void moveAIControlled()
	{
		setY(getY() - dropSpeed * Gdx.graphics.getDeltaTime());
		
		if (getY() < 1)
		{
			setY(400);
			
			if (dropSpeed < 300)
				dropSpeed += 20;
		}	
	}
	*/
	@Override
	public void moveTo() {
		
		this.getPosition().add(0, -(dropSpeed * Gdx.graphics.getDeltaTime()));
		
		//setY(getY() - dropSpeed * Gdx.graphics.getDeltaTime());
		
		if (this.getPosition().y < 1)
		{
			this.setPosition(this.getPosition().x, 400);
			
			if (dropSpeed < 300)
				dropSpeed += 20;
		}
		
	}
	
	
}
