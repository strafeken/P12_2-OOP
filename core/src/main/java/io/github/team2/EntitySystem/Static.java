package io.github.team2.EntitySystem;

import com.badlogic.gdx.math.Vector2;

public abstract class Static extends Entity {

    public Static() {
        super();
    }

    public Static(Vector2 position,  Vector2 direction) {
        super(position, direction);
    }

    @Override
    public boolean isOutOfBound(Vector2 direction) {
        return false; // Static objects don't move, so they can't be out of bounds
    }

    @Override
    public void update() {
        updateBody(); // Only update physics body position
    }
}
