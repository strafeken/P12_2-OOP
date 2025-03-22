package io.github.team2.Game.Trash;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.Game.Trash.TrashBehaviour.Move;
import io.github.team2.Game.Trash.TrashBehaviour.State;

public class NonRecyclableTrashFactory implements TrashFactory {

	@Override
	public Trash createTrash(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction,
			Vector2 rotation, float speed, State state, Move actionState) {
        NonRecyclableTrash.Type nonRecyclableType = getNonRecyclableTypeFromTexture(texture);
        return new NonRecyclableTrash(texture, size, position, nonRecyclableType);
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
