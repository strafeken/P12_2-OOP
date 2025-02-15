package io.github.team2.EntitySystem;

import com.badlogic.gdx.math.Vector2;

public abstract class Static extends Entity {

    public Static() {
        super();
    }


    public Static(EntityType type, Vector2 position,  Vector2 direction) {
        super(type, position, direction, new Vector2(1, 0));

    }

//    @Override
//    public boolean isOutOfBound(Vector2 direction) {
//        return false; // Static objects don't move, so they can't be out of bounds
//    }

    @Override
    public void update() {
        updateBody(); // Only update physics body position
    }
}
