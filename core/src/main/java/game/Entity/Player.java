package game.Entity;


import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.DynamicTextureObject;

public class Player extends DynamicTextureObject<PlayerBehaviour.State, PlayerBehaviour.Move > {
	public Player(
			EntityType type, 
			String texture, 
			Vector2 size,
			Vector2 position, 
			Vector2 direction, 
			Vector2 rotation, 
			float speed,
			PlayerBehaviour.State state, 
			PlayerBehaviour.Move actionState) {
		super(type, texture, size, position, direction,rotation, speed, state, actionState);
	}
}
