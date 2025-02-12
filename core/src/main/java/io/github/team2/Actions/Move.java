package io.github.team2.Actions;

import com.badlogic.gdx.math.Vector2;

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
		if (entity.getBody() != null) {
			if (entity.getEntityType() == EntityType.DROP) {
				entity.getBody().setLinearVelocity(direction.x * entity.getSpeed(), direction.y * entity.getSpeed());
			}
			else {
				
				if ( entity.isOutOfBound(direction) == false) {
					
					entity.getBody().setLinearVelocity(direction.x * entity.getSpeed(), direction.y * entity.getSpeed());
				}
				else {
					entity.getBody().setLinearVelocity(0,0);
				}
				
			}
			
			
			}
			
	}
}