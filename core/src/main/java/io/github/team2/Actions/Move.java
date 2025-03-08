package io.github.team2.Actions;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.Dynamics;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.InputSystem.Action;

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
				if (entity.getEntityType() == EntityType.DROP) {

					entity.getPhysicsBody().setLinearVelocity(direction.x * ((Dynamics<?, ?>) entity).getSpeed(),
							direction.y * ((Dynamics<?, ?>) entity).getSpeed());
				} else {

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
}