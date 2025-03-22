package game.entity;

import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.DynamicTextureObject;
import game.entity.PipeBehaviour.Move;
import game.entity.PipeBehaviour.State;

public class Pipe extends DynamicTextureObject<PipeBehaviour.State, PipeBehaviour.Move>{

	public Pipe(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
			float speed, State state, Move actionState) {
		super(type, texture, size, position, direction, rotation, speed, state, actionState);
		// TODO Auto-generated constructor stub
	}
	
}
