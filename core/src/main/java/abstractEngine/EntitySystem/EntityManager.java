package abstractEngine.EntitySystem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.Game.Entity.EntityType;

public class EntityManager implements IEntityManager {

	private List<Entity> entityList;
	private List<Entity> entitiesToRemove;

	public EntityManager() {
		entityList = new ArrayList<>();
		entitiesToRemove = new ArrayList<>();
	}

	@Override
	public void addEntities(Entity entity) {
		entityList.add(entity);
	}

	@Override
	// remove entities after update to prevent concurrent access
	public void markForRemoval(Entity entity) {
		entitiesToRemove.add(entity);
	}

	@Override
	// Fix the getEntities method to return a defensive copy
	public List<Entity> getEntities() {
		return new ArrayList<>(entityList);
	}

	@Override
	public int getEntityCount() {
		return entityList.size();
	}

	@Override
	public boolean hasEntity(Entity entity) {
		return entityList.contains(entity);
	}

	@Override
	public List<Entity> getEntitiesByType(EntityType type) {
		List<Entity> result = new ArrayList<>();
		for (Entity entity : entityList) {
			if (entity.getEntityType() == type) {
				result.add(entity);
			}
		}
		return result;
	}

	@Override
	public void removeEntity(Entity entity) {
		entityList.remove(entity);

		if (entity.getPhysicsBody() != null)
			entity.getPhysicsBody().dispose();

		if (entity instanceof DynamicTextureObject)
			((DynamicTextureObject<?, ?>) entity).dispose();

		if (entity instanceof StaticTextureObject)
			((StaticTextureObject) entity).dispose();
	}

	@Override
	public Entity get(int index) {
		return entityList.get(index);
	}

	@Override
	public void draw(SpriteBatch batch) {
		for (Entity entity : entityList)
			entity.draw(batch);
	}

	@Override
	public void draw(ShapeRenderer shape) {
		for (Entity entity : entityList)
			entity.draw(shape);
	}

	@Override
	public void update() {
		for (Entity e : entityList) {
			//e.moveUserControlled();
			//e.moveAIControlled();
			e.update();
		}

		for (Entity e : entitiesToRemove)
			removeEntity(e);

		entitiesToRemove.clear();
	}

	@Override
	public void dispose() {
        List<Entity> entitiesToDispose = new ArrayList<>(entityList);

        // First null out physics bodies
        for (Entity entity : entitiesToDispose) {
            if (entity.getPhysicsBody() != null) {
                entity.getPhysicsBody().dispose();
            }
        }

        // Then dispose textures
        for (Entity entity : entitiesToDispose) {
            if (entity instanceof StaticTextureObject) {
                ((StaticTextureObject) entity).dispose();
            }

            if (entity instanceof DynamicTextureObject) {
                ((DynamicTextureObject<?, ?>) entity).dispose();
            }
        }

        entityList.clear();
        entitiesToRemove.clear();
    }
}
