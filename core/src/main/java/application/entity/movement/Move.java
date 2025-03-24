package application.entity.movement;

import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.Action;
import abstractengine.entity.Dynamics;
import abstractengine.entity.Entity;

public class Move implements Action {
	private final Entity entity;
	private final Vector2 direction;

	public Move(Entity entity, Vector2 direction) {
		this.entity = entity;
		this.direction = direction;
	}

	@Override
	public void execute() {
		// check if is dynamic entity
		if (entity instanceof Dynamics<?, ?>) {
			if (entity.getPhysicsBody() != null) {
				
				if (((Dynamics<?, ?>)entity).isOutOfBound(direction) == false) {
	
					entity.getPhysicsBody().setLinearVelocity(direction.x * ((Dynamics<?, ?>) entity).getSpeed(),
							direction.y * ((Dynamics<?, ?>) entity).getSpeed());
				} else {
					entity.getPhysicsBody().setLinearVelocity(0, 0);
				}
				
			}
		}
	}
}