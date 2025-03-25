package application.minigame.asteroids;

import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.DynamicTextureObject;
import application.entity.EntityType;

/**
 * Asteroid class for the Asteroid Dodge mini-game
 */
public class Asteroid extends DynamicTextureObject<AsteroidBehaviour.State, AsteroidBehaviour.Action > {
	public Asteroid(
			EntityType type, 
			String texture, 
			Vector2 size,
			Vector2 position, 
			Vector2 direction, 
			Vector2 rotation, 
			float speed,
			AsteroidBehaviour.State state, 
			AsteroidBehaviour.Action actionState) {
		super(type, texture, size, position, direction,rotation, speed, state, actionState);
	}
}