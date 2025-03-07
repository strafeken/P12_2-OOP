package io.github.team2;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.AlienBehaviour.Move;
import io.github.team2.AlienBehaviour.State;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.EntityType;

public class Alien extends DynamicTextureObject<AlienBehaviour.State, AlienBehaviour.Move > {

	public Alien(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
			float speed, State state, Move actionState) {
		super(type, texture, size, position, direction, rotation, speed, state, actionState);
	}
}
