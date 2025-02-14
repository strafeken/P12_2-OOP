package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;

public class Player extends TextureObject {

	public Player(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {

		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setDirection(direction);
		
		//setSpeed(speed);

	}



	@Override
	public void update() {
		updateBody();
	}
}
