package application.entity.trash;

import com.badlogic.gdx.math.Vector2;

import application.entity.EntityType;
import application.entity.trash.TrashBehaviour.Move;
import application.entity.trash.TrashBehaviour.State;

public class RecyclableTrashFactory implements TrashFactory {

	@Override
	public Trash createTrash(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction,
			Vector2 rotation, float speed, State state, Move actionState) {
		RecyclableTrash.Type recyclableType = getRecyclableTypeFromTexture(texture);
        return new RecyclableTrash(texture, size, position, recyclableType);
	}

    private RecyclableTrash.Type getRecyclableTypeFromTexture(String texture) {
        if (texture.contains("cardboard") || texture.contains("paper")) {
            return RecyclableTrash.Type.PAPER;
        } else if (texture.contains("plastic") || texture.contains("bottle")) {
            return RecyclableTrash.Type.PLASTIC;
        } else if (texture.contains("glass")) {
            return RecyclableTrash.Type.GLASS;
        } else if (texture.contains("can") || texture.contains("metal")) {
            return RecyclableTrash.Type.METAL;
        }
        // Default
        return RecyclableTrash.Type.PAPER;
    }
}
