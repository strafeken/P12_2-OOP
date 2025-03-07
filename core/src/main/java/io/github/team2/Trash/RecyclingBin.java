package io.github.team2.Trash;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.StaticTextureObject;

public class RecyclingBin extends StaticTextureObject {
	
	public RecyclingBin(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction) {
		super(type, texture, size, position, direction);
	}
}
