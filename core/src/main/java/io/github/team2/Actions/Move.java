package io.github.team2.Actions;

import com.badlogic.gdx.math.Vector2;
import io.github.team2.Action;
import io.github.team2.Entity;

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
            entity.getBody().setLinearVelocity(
                direction.x * entity.getSpeed(),
                direction.y * entity.getSpeed()
            );
        }
    }
}