package abstractengine.entity;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import application.entity.EntityType;

public interface IEntityManager {
    void addEntities(Entity entity);
    void markForRemoval(Entity entity);
    List<Entity> getEntities();
    int getEntityCount();
    boolean hasEntity(Entity entity);
    List<Entity> getEntitiesByType(EntityType type);
    void removeEntity(Entity entity);
    Entity get(int index);
    void draw(SpriteBatch batch);
    void draw(ShapeRenderer shape);
    void update();
    void dispose();
}
