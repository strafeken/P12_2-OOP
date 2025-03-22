package io.github.team2.Game.Trash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Game.entity.EntityType;

public class RecyclableTrash extends Trash {
    // Different types of recyclable trash
	public enum Type {
		
		PAPER, PLASTIC, GLASS, METAL

	}

    private Type recyclableType;

    public RecyclableTrash(String texture, Vector2 size, Vector2 position, Type type) {
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

    public Type getRecyclableType() {
        return recyclableType;
    }

    @Override
    public void update() {
        super.update();

        // Add any recyclable-specific behavior here
        // For example, recyclables might glow or have particle effects
    }
}
