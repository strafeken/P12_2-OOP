package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextureObject extends Entity {
	
	private Texture tex;
	
	TextureObject()
	{
		tex = null;
	}
	
	TextureObject(String texture)
	{
		tex = new Texture(texture);
	}
	
	TextureObject(String texture, Vector2 position, float speed)
	{
		tex = new Texture(texture);
		setPosition(position);
		setSpeed(speed);
	}
	
	public Texture getTexture()
	{
		return tex;
	}
	
	public void setTexture(Texture texture)
	{
		tex = texture;
	}
	
	public float getWidth()
	{
		return tex.getWidth();
	}
	
	public float getHeight()
	{
		return tex.getHeight();
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		batch.draw(tex, getPosition().x - tex.getWidth() / 2, getPosition().y - tex.getHeight() / 2);
	}
	
	@Override
	public void moveUserControlled()
	{
		
	}
	
	@Override
	public void moveAIControlled()
	{
		
	}
	
	@Override
	public void update()
	{
//		System.out.println(tex.toString() + " XY: " + getX() + " / " + getY());
	}
	
	public void dispose()
	{
		tex.dispose();
	}
}
