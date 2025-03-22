package game.Entity;

import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.StaticTextureObject;

public class Planet extends StaticTextureObject {
	private int level;
	
	public Planet(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, int level) {
		super( type,  texture,  size,  position,  direction);
		
		this.level = level;
	}
	
	
	public int getLevel() {
		return level;
		
	}


}
