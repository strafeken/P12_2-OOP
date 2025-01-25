package io.github.team2;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EntityManager {
	
	private List<Entity> entityList;
	
	public EntityManager()
	{
		entityList = new ArrayList<>();
	}
	
	public void addEntities(Entity entity)
	{
		entityList.add(entity);
	}
	
	public void remove(int index)
	{
		entityList.remove(index);
	}
	
	public Entity get(int index)
	{
		return entityList.get(index);
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.begin();
		
		for(Entity entity : entityList)
			entity.draw(batch);
		
		batch.end();
	}
	
	public void draw(ShapeRenderer shape)
	{
		shape.begin(ShapeRenderer.ShapeType.Filled);
		
		for(Entity entity : entityList)
			entity.draw(shape);
		
		shape.end();
	}
	
	public void update()
	{
		for(Entity e : entityList)
		{
			e.moveUserControlled();
			e.moveAIControlled();
			e.update();
		}
	}
	
	public void dispose()
	{
		for (Entity entity : entityList)
		{
			if (entity instanceof TextureObject)
				((TextureObject) entity).dispose();
		}
	}
}
