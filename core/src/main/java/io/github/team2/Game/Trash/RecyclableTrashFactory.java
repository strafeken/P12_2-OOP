package io.github.team2.Game.Trash;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.Game.Trash.TrashBehaviour.Move;
import io.github.team2.Game.Trash.TrashBehaviour.State;
import io.github.team2.Game.entity.EntityType;

public class RecyclableTrashFactory implements TrashFactory {

	@Override
	public Trash createTrash(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction,
			Vector2 rotation, float speed, State state, Move actionState) {
    	RecycleType recyclableType = getRecyclableTypeFromTexture(texture);
        return new RecyclableTrash(texture, size, position, recyclableType);
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
}
