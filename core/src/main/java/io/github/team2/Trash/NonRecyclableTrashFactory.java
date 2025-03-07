package io.github.team2.Trash;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;

public class NonRecyclableTrashFactory implements TrashFactory {

    @Override
    public Trash createTrash(EntityType type, String texture, Vector2 size,
    		Vector2 position, Vector2 direction, Vector2 rotation, float speed, 
    		TrashBehaviour.State state, TrashBehaviour.Move actionState) {
        return new NonRecyclableTrash(type, texture, size, position, direction, rotation, speed, state, actionState);
    }
}
