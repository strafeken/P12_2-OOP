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
	
	TextureObject(String texture, Vector2 position, Vector2 direction, float speed)
	{
		tex = new Texture(texture);
		setPosition(position);
		setDirection(direction);
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

	public float getSide(String side)
	{	
		float value = 0;
		if (side == "TOP") 		value = this.getPosition().y + (this.getHeight()/2); 
		if (side == "BOTTOM")	value = this.getPosition().y - (this.getHeight()/2);
		
		if (side == "LEFT")		value = this.getBody().getPosition().x - this.getWidth()/2;
		if (side == "RIGHT")	value = this.getPosition().x + (this.getWidth()/2);
		
		
		System.out.println("X:"+ this.getPosition().x + " Y:" +this.getPosition().y + "Value:" +
				value);
		
		return value;
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
	public void moveTo(Vector2 position)
	{
		
	}
	
	@Override
	public void moveDirection(String direction)
	{
		
	}
	
	@Override
	public void rotateTo(float num)
	{
		
	}
	
	@Override
	public boolean checkPosition(Vector2 position)
	{
		
		
		return false;
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
