package io.github.team2.Actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.InputSystem.Action;

public class Rotate implements Action{
	
	private Entity entity;
	private Vector2 direction;
	private static final float ROTATION_SPEED = 50f;


	public Rotate(Entity entity, Vector2 direction) {
		this.entity = entity;
		this.direction = direction;
	}

	@Override
	public void execute() {
        if (entity.getBody() == null) return; 

        float rotationSpeed = ROTATION_SPEED * Gdx.graphics.getDeltaTime(); 

        if (direction.x > 0) { 
            entity.getBody().setAngularVelocity(-rotationSpeed); 
        } 
        else if (direction.x < 0) { 
            entity.getBody().setAngularVelocity(rotationSpeed); 
        }
        else {
        	entity.getBody().setAngularVelocity(0); 
        }

    }
	
}


