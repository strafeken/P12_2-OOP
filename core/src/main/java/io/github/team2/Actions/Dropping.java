package io.github.team2.Actions;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.InputSystem.Action;

public class Dropping implements Action {
	// not used
	private final Entity entity;
	private final Vector2 direction;

	public Dropping(Entity entity) {
		this.entity = entity;
		this.direction = new Vector2(0, -1);
	}

	@Override
	public void execute() {
		if (entity.getBody() != null) {

			entity.getBody().setLinearVelocity(direction.x * entity.getSpeed(), direction.y * entity.getSpeed());

		}

	}

}
