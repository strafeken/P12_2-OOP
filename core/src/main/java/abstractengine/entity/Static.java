package abstractengine.entity;

import com.badlogic.gdx.math.Vector2;

import game.entity.EntityType;

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
