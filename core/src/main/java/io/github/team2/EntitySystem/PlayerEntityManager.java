package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;


public class PlayerEntityManager {
    private List<Entity> playerEntities;

    public PlayerEntityManager() {
        playerEntities = new ArrayList<>();
    }

    public Entity getPlayer() {
        return playerEntities.isEmpty() ? null : playerEntities.get(0);
    }

    public void addEntity(Entity entity) {
        if (entity.getEntityType() == EntityType.PLAYER) {
            playerEntities.add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        playerEntities.remove(entity);
    }

    public void update() {
        for (Entity entity : playerEntities) {
            entity.update();
        }
    }

    public void draw(SpriteBatch batch) {
        for (Entity entity : playerEntities) {
            entity.draw(batch);
        }
    }

    public List<Entity> getPlayerEntities() {
        return playerEntities;
    }
}
