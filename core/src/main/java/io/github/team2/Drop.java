package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Drop extends TextureObject {
	
	//private float dropSpeed = 100;
	
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
	
	
	// movement control
	@Override
	public void moveAIControlled()
	{
		/*
		if (getBody().getPosition().y < 1)
			getBody().setLocation(getPosition().x, Gdx.graphics.getHeight());
		
		updateBody();
		*/
		
		this.moveTo(new Vector2(this.getPosition().x, 0));
	}
	
	@Override
	public void moveTo(Vector2 targetPosition) {
		//boolean isMoving;
		if (this.getIsMoving() == false) {
			this.setIsMoving(true);
			//this.moveDown();
		}
		else {
	        // Calculate the direction vector to the target position
	        Vector2 currentPosition = this.getPosition();
	        //Vector2 direction = targetPosition.sub(currentPosition).nor();
	        this.getBody().setLinearVelocity(0, -this.getSpeed());
	        
	        if (getBody().getPosition().y < 1)
				getBody().setLocation(getPosition().x, Gdx.graphics.getHeight());
	        
	       updateBody();
		}
		
	}
	/*
	public void moveDown() {
		
		this.getBody().setLinearVelocity(0, -this.getSpeed());
		
		
	}
	*/
	
	
	
}
