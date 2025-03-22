package io.github.team2.Trash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.EntitySystem.EntityType;

public class RecyclableTrash extends Trash {
    // Different types of recyclable trash


    private RecycleType recyclableType;

    public RecyclableTrash(String texture, Vector2 size, Vector2 position, RecycleType type) {
        super(EntityType.RECYCLABLE, texture, size, position,
              new Vector2(0, 0), new Vector2(0, 0), 50f,
              TrashBehaviour.State.IDLE, TrashBehaviour.Move.NONE);

        this.recyclableType = type;

        // Set random initial velocity for floating effect
        setVelocity(new Vector2(
            MathUtils.random(-50f, 50f),
            MathUtils.random(-50f, 50f)
        ));
    }

    public RecycleType getRecyclableType() {
        return recyclableType;
    }

    @Override
    public void update() {
        super.update();

        // Add any recyclable-specific behavior here
        // For example, recyclables might glow or have particle effects
    }
}
