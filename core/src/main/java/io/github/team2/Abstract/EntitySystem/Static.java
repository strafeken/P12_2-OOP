package io.github.team2.Abstract.EntitySystem;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.Game.Entity.EntityType;

public abstract class Static extends Entity {

    public Static() {
        super();
    }


    public Static(EntityType type, Vector2 position,  Vector2 direction) {
        super(type, position, direction);

    }


    @Override
    public void update() {
        updateBody(); // Only update physics body position
    }
}
