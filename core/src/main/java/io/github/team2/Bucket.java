package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;

public class Bucket extends TextureObject {

	public Bucket(String texture) {
		setEntityType(EntityType.BUCKET);
		setTexture(new Texture(texture));
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setRotation(new Vector2(0, 0));
		setSpeed(0);
		
		
	}

	public Bucket(EntityType type, String texture, Vector2 position, Vector2 direction, Vector2 rotation, float speed) {
		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setDirection(direction);
		setRotation(rotation);
		setSpeed(speed);
		
	}

	@Override
	public void moveUserControlled() {
		if (getBody() == null)
			return;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			if (this.checkOutOfBound(this, "LEFT") == false) {
				this.moveDirection("LEFT");
			}

		}

		// getBody().setLinearVelocity(-getSpeed(), 0);
		else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if (this.checkOutOfBound(this, "RIGHT") == false)
				this.moveDirection("RIGHT");
			// getBody().setLinearVelocity(getSpeed(), 0);

		} else
			getBody().setLinearVelocity(0, 0);
	}

	@Override
	public void moveDirection(String direction) {
		if (direction == "LEFT") {
			getBody().setLinearVelocity(-getSpeed(), 0);
		}
		if (direction == "RIGHT") {
			getBody().setLinearVelocity(getSpeed(), 0);
		}
		if (direction == "UP") {
			getBody().setLinearVelocity(0, getSpeed());
		}
		if (direction == "DOWN") {
			getBody().setLinearVelocity(0, -getSpeed());
		}
		if (direction == "CLOCKWISE") {
			getBody().setAngularVelocity( -Math.abs(getSpeed()) ); 
		}
	    if (direction == "ANTICLOCKWISE") {
	    	getBody().setAngularVelocity( Math.abs(getSpeed()) );   
	    }

	}

	@Override
	public void update() {
		updateBody();
	}
}
