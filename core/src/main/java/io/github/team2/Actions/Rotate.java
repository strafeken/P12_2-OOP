package io.github.team2.Actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.Dynamics;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.InputSystem.Action;

public class Rotate implements Action {
	private final Entity entity;
	private final Vector2 direction;
	private static final float ROTATION_SPEED = 7.0f; 

	public Rotate(Entity entity, Vector2 direction) {
		this.entity = entity;
		this.direction = direction;
	}

	@Override
	public void execute() {
		if (entity.getBody() == null) return; 

		float rotationSpeed = ROTATION_SPEED * Gdx.graphics.getDeltaTime(); 
		float currentAngle = entity.getBody().getAngle(); 

		
		if (direction.x < 0) { 
	        currentAngle += rotationSpeed;
	        System.out.println("[DEBUG] Rotating Anticlockwise: New Angle = " + currentAngle);
	    } 
	    else if (direction.x > 0) { 
	        currentAngle -= rotationSpeed;
	        System.out.println("[DEBUG] Rotating Clockwise: New Angle = " + currentAngle);
	    } 
	    else {
	        System.out.println("[DEBUG] Stopping Rotation");
	    }
		
		Vector2 newRotation = new Vector2((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));
		
		entity.setRotation(newRotation);
		
	    if (entity.getBody() != null) {
	        entity.getBody().setTransform(entity.getBody().getPosition(), currentAngle);
	    }
	}
}