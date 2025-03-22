package io.github.team2.Game.Trash;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.StaticTextureObject;
import io.github.team2.Game.entity.EntityType;

public class RecyclingBin extends StaticTextureObject {
	
	private List<RecyclableTrash.Type> acceptedType;
	
	
	public RecyclingBin(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, List<RecyclableTrash.Type> acceptedType) {
		super(type, texture, size, position, direction);
		
		this.acceptedType = acceptedType;
	}
	
	
	  public boolean accepts(RecyclableTrash trash) {
	        return acceptedType.contains(trash.getRecyclableType());
	    }

}
