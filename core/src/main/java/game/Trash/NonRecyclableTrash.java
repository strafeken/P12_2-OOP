package game.Trash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import game.Entity.EntityType;

public class NonRecyclableTrash extends Trash {
    // Different types of non-recyclable trash
    public enum Type {
        HAZARDOUS, ELECTRONIC_WASTE, MIXED_WASTE
    }

    private Type nonRecyclableType;

    public NonRecyclableTrash(String texture, Vector2 size, Vector2 position, Type type) {
        super(EntityType.NON_RECYCLABLE, texture, size, position,
              new Vector2(0, 0), new Vector2(0, 0), 50f,
              TrashBehaviour.State.IDLE, TrashBehaviour.Move.NONE);

        this.nonRecyclableType = type;

        // Set random initial velocity for floating effect
        setVelocity(new Vector2(
            MathUtils.random(-60f, 60f),
            MathUtils.random(-60f, 60f)
        ));
    }

    public Type getNonRecyclableType() {
        return nonRecyclableType;
    }

    @Override
    public void update() {
        super.update();

        // Add any non-recyclable-specific behavior here
        // For example, non-recyclables might have warning indicators
    }
}
