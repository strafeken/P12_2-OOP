package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TextureObject extends Entity {
	
	protected Texture tex;
	
	TextureObject()
	{
		tex = null;
	}
	
	TextureObject(String texture)
	{
		tex = new Texture(texture);
	}
	
	TextureObject(String texture, float x,float y, float speed)
	{
		
		tex = new Texture(texture);
		setPosition(x, y);
		setSpeed(speed);
		
		/*
		setX(x);
		setY(y);
		*/
		
		//setSpeed(speed);
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
		batch.draw(tex, this.getPosition().x, this.getPosition().y);
	}
	
	
	/*
	@Override
	public void moveUserControlled()
	{
		
	}
	
	@Override
	public void moveAIControlled()
	{
		
	}
	*/
	
	@Override
	public void update()
	{
//		System.out.println(tex.toString() + " XY: " + getX() + " / " + getY());
	}
	
	public void dispose()
	{
		tex.dispose();
	}
	
	@Override
	public void moveTo() {}
	
	@Override
	public void rotateTo(float num) {}
	
	
}
