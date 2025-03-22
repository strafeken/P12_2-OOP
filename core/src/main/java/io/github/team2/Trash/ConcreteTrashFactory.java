package io.github.team2.Trash;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;

public class ConcreteTrashFactory implements TrashFactory {

    @Override
    public Trash createTrash(EntityType type, String texture, Vector2 size,
            Vector2 position, Vector2 direction, Vector2 rotation, float speed,
            TrashBehaviour.State state, TrashBehaviour.Move actionState) {

        if (type == EntityType.RECYCLABLE) {
            // Determine recyclable type based on texture
        	RecycleType recyclableType = getRecyclableTypeFromTexture(texture);
            return new RecyclableTrash(texture, size, position, recyclableType);
        } else if (type == EntityType.NON_RECYCLABLE) {
            // Determine non-recyclable type based on texture
            NonRecyclableTrash.Type nonRecyclableType = getNonRecyclableTypeFromTexture(texture);
            return new NonRecyclableTrash(texture, size, position, nonRecyclableType);
        }

        // Default to recyclable paper if type is unknown
        return new RecyclableTrash(texture, size, position, RecycleType.PAPER);
    }

    private RecycleType getRecyclableTypeFromTexture(String texture) {
        if (texture.contains("cardboard") || texture.contains("paper")) {
            return RecycleType.PAPER;
        } else if (texture.contains("plastic") || texture.contains("bottle")) {
            return RecycleType.PLASTIC;
        } else if (texture.contains("glass")) {
            return RecycleType.GLASS;
        } else if (texture.contains("can") || texture.contains("metal")) {
            return RecycleType.METAL;
        }

        // Default
        return RecycleType.PAPER;
    }

    private NonRecyclableTrash.Type getNonRecyclableTypeFromTexture(String texture) {
        if (texture.contains("toxic") || texture.contains("hazard")) {
            return NonRecyclableTrash.Type.HAZARDOUS;
        } else if (texture.contains("device") || texture.contains("electronic")) {
            return NonRecyclableTrash.Type.ELECTRONIC_WASTE;
        } else {
            return NonRecyclableTrash.Type.MIXED_WASTE;
        }
    }
}
