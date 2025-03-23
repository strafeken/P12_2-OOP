package application.entity.trash;

import com.badlogic.gdx.math.Vector2;

import application.entity.EntityType;

public interface TrashFactory {

	public Trash createTrash(EntityType type, String texture, Vector2 size,
			Vector2 position, Vector2 direction, Vector2 rotation, float speed, 
            TrashBehaviour.State state, TrashBehaviour.Move actionState);
}
