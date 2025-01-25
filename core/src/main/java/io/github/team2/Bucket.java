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
		setPosition(50, 50);
		/*
		setX(50);
		setY(50);
		*/
		setSpeed(10);
	}
	
	public Bucket(String texture, float x, float y, float speed)
	{
		tex = new Texture(texture);
		setPosition(x, y);
		/*
		setX(x);
		setY(y);
		*/
		setSpeed(speed);
	}
	
	@Override
	public void moveTo() {
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			if (this.checkOutOfBound(this.getPosition().x,
									(this.getPosition().x + this.getWidth()),
									this.getPosition().y + - this.getHeight(),
									(this.getPosition().y )) == true) {
				this.getPosition().add(- (speed * Gdx.graphics.getDeltaTime()), 0);
				
				} 
			else {
				this.getPosition().add(5, 0);
				}
			
			}
			
	
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if (this.checkOutOfBound(this.getPosition().x,
									(this.getPosition().x + this.getWidth()),
									this.getPosition().y + this.getHeight(),
									(this.getPosition().y  )) == true) {
			this.getPosition().add((speed * Gdx.graphics.getDeltaTime()), 0);
				}
			else {
				this.getPosition().add(-5, 0);
			}
			}
	}
	
	/*
	@Override
	public void moveUserControlled()
	{
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			setX(getX() - speed * Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			setX(getX() + speed * Gdx.graphics.getDeltaTime());
	}
	*/
}
