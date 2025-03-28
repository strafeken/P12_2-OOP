package application.entity.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.Action;
import abstractengine.entity.Dynamics;
import abstractengine.entity.Entity;

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
		if (entity.getPhysicsBody() == null)
			return;

		if (entity instanceof Dynamics<?, ?>) {

			float rotationSpeed = ROTATION_SPEED * Gdx.graphics.getDeltaTime();
			float currentAngle = entity.getPhysicsBody().getAngle();

			if (direction.x < 0) {
				currentAngle += rotationSpeed;
//				System.out.println("[DEBUG] Rotating Anticlockwise: New Angle = " + currentAngle);
			} else if (direction.x > 0) {
				currentAngle -= rotationSpeed;
//				System.out.println("[DEBUG] Rotating Clockwise: New Angle = " + currentAngle);
			} else {
				System.out.println("[DEBUG] Stopping Rotation");
			}

			Vector2 newRotation = new Vector2((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));

			((Dynamics<?, ?>) entity).setRotation(newRotation);

			if (entity.getPhysicsBody() != null) {
				entity.getPhysicsBody().setTransform(entity.getPhysicsBody().getPosition(), currentAngle);
			}
		}
	}
}