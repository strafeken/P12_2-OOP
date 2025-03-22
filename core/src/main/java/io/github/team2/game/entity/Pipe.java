package io.github.team2.Game.entity;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.PipeBehaviour.Move;
import io.github.team2.PipeBehaviour.State;
import io.github.team2.PipeBehaviour;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.EntityType;

public class Pipe extends DynamicTextureObject<PipeBehaviour.State, PipeBehaviour.Move>{

	public Pipe(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
			float speed, State state, Move actionState) {
		super(type, texture, size, position, direction, rotation, speed, state, actionState);
		// TODO Auto-generated constructor stub
	}
	
}
