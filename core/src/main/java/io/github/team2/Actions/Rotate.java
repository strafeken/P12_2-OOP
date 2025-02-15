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
	private static final float ROTATION_SPEED = 7.0f; // Adjust for faster/slower rotation

	public Rotate(Entity entity, Vector2 direction) {
		this.entity = entity;
		this.direction = direction;
	}

	@Override
	public void execute() {
		if (entity.getBody() == null) return; // Prevent null pointer errors

		float rotationSpeed = ROTATION_SPEED * Gdx.graphics.getDeltaTime(); // Scale speed properly
		float currentAngle = entity.getBody().getAngle(); // Get current angle in radians

		// 🔹 Update the rotation based on direction.x
		if (direction.x < 0) { // Anticlockwise (Q)
	        currentAngle += rotationSpeed;
	        System.out.println("[DEBUG] Rotating Anticlockwise: New Angle = " + currentAngle);
	    } 
	    else if (direction.x > 0) { // Clockwise (E)
	        currentAngle -= rotationSpeed;
	        System.out.println("[DEBUG] Rotating Clockwise: New Angle = " + currentAngle);
	    } 
	    else {
	        System.out.println("[DEBUG] Stopping Rotation");
	    }

		// 🔹 Convert new angle into a Vector2 direction
		Vector2 newRotation = new Vector2((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));

		// 🔹 Apply the new rotation as a direction vector
		entity.setRotation(newRotation);
		// ✅ Ensure setTransform() is only called when body is valid
	    if (entity.getBody() != null) {
	        entity.getBody().setTransform(entity.getBody().getPosition(), currentAngle);
	    }
	}
}