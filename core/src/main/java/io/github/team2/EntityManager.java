package io.github.team2;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EntityManager {
	
	private List<Entity> entityList;
	private List<Entity> entitiesToRemove;
	
	public EntityManager()
	{
		entityList = new ArrayList<>();
		entitiesToRemove = new ArrayList<>();
	}
	
	public void addEntities(Entity entity)
	{
		entityList.add(entity);
	}
	
	// remove entities after update to prevent concurrent access
	public void markForRemoval(Entity entity)
	{
		entitiesToRemove.add(entity);
	}
	
	public void removeEntity(Entity entity)
	{
		entityList.remove(entity);
        
		if (entity.getBody() != null)
			entity.getBody().dispose();

		if (entity instanceof TextureObject)
            ((TextureObject) entity).dispose();
	}
	
	public Entity get(int index)
	{
		return entityList.get(index);
	}
	
	public void draw(SpriteBatch batch)
	{
		for(Entity entity : entityList)
			entity.draw(batch);
	}
	
	public void draw(ShapeRenderer shape)
	{
		for(Entity entity : entityList)
			entity.draw(shape);
	}
	
	public void update()
	{
		for (Entity e : entityList)
		{
			e.moveUserControlled();
			e.moveAIControlled();
			e.update();
		}
		
		for (Entity e : entitiesToRemove)
			removeEntity(e);
		
		entitiesToRemove.clear();
	}
	
	public void dispose()
	{
		for (Entity entity : entityList)
		{
			if (entity.getBody() != null)
				entity.getBody().dispose();
			
			if (entity instanceof TextureObject)
				((TextureObject) entity).dispose();
		}
	}
}
