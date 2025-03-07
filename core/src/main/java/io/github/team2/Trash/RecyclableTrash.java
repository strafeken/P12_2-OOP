package io.github.team2.Trash;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.PlayerBehaviour;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.Trash.TrashBehaviour.Move;
import io.github.team2.Trash.TrashBehaviour.State;

public class RecyclableTrash extends Trash {

	public RecyclableTrash(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction,
			Vector2 rotation, float speed, State state, Move actionState) {
		super(type, texture, size, position, direction, rotation, speed, state, actionState);
	}
}
