package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


import io.github.team2.Actions.PlayerBehaviour;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;

public class Player extends DynamicTextureObject<PlayerBehaviour.State, PlayerBehaviour.Move > {

	public Player(EntityType type, String texture, Vector2 position, Vector2 direction, float speed,
			PlayerBehaviour.State state, PlayerBehaviour.Move actionState) {
		
		
		super(texture, position, direction, speed, state, actionState);
		setEntityType(type);
//		setTexture(new Texture(texture));
//		setPosition(position);
//		setDirection(direction);
		
		//setSpeed(speed);

	}



	@Override
	public void update() {
		updateBody();
	}
}
