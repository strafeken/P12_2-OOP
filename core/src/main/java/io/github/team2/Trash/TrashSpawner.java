package io.github.team2.Trash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.Utils.DisplayManager;

import java.util.HashMap;
import java.util.Map;

public class TrashSpawner {
    private final World world;
    private final IEntityManager entityManager;

    // Texture mappings for different trash types
    private static final Map<RecyclableTrash.Type, String> recyclableTextures = new HashMap<>();
    private static final Map<NonRecyclableTrash.Type, String> nonRecyclableTextures = new HashMap<>();

    static {
        // Initialize texture mappings
        recyclableTextures.put(RecyclableTrash.Type.PAPER, "cardboard-box.png");
        recyclableTextures.put(RecyclableTrash.Type.PLASTIC, "plastic-bottle.png");
        recyclableTextures.put(RecyclableTrash.Type.GLASS, "glass-bottle.png");
        recyclableTextures.put(RecyclableTrash.Type.METAL, "soda-can.png");

        nonRecyclableTextures.put(NonRecyclableTrash.Type.HAZARDOUS, "toxic-waste.png");
        nonRecyclableTextures.put(NonRecyclableTrash.Type.ELECTRONIC_WASTE, "broken-device.png");
        nonRecyclableTextures.put(NonRecyclableTrash.Type.MIXED_WASTE, "half-eaten-food.png");
    }

    public TrashSpawner(World world, IEntityManager entityManager) {
        this.world = world;
        this.entityManager = entityManager;
    }

    /**
     * Spawns a random mix of recyclable and non-recyclable trash
     * @param count Total number of trash items to spawn
     * @param recyclableRatio Ratio of recyclable to total trash (0.0-1.0)
     */
    public void spawnRandomTrash(int count, float recyclableRatio) {
        for (int i = 0; i < count; i++) {
            if (MathUtils.random() < recyclableRatio) {
                spawnRandomRecyclable();
            } else {
                spawnRandomNonRecyclable();
            }
        }
    }

    /**
     * Spawns a single random recyclable trash item
     */
    public Entity spawnRandomRecyclable() {
        RecyclableTrash.Type type = RecyclableTrash.Type.values()[
            MathUtils.random(RecyclableTrash.Type.values().length - 1)];

        String texture = recyclableTextures.getOrDefault(type, "cardboard-box.png");
        Vector2 position = getRandomPosition();
        Vector2 size = new Vector2(40, 40); // Adjust size as needed

        RecyclableTrash trash = new RecyclableTrash(texture, size, position, type);
        setupTrashPhysics(trash);

        return trash;
    }

    /**
     * Spawns a single random non-recyclable trash item
     */
    public Entity spawnRandomNonRecyclable() {
        NonRecyclableTrash.Type type = NonRecyclableTrash.Type.values()[
            MathUtils.random(NonRecyclableTrash.Type.values().length - 1)];

        String texture = nonRecyclableTextures.getOrDefault(type, "half-eaten-food.png");
        Vector2 position = getRandomPosition();
        Vector2 size = new Vector2(40, 40); // Adjust size as needed

        NonRecyclableTrash trash = new NonRecyclableTrash(texture, size, position, type);
        setupTrashPhysics(trash);

        return trash;
    }

    /**
     * Sets up physics body and adds the trash to the entity manager
     */
    private void setupTrashPhysics(Trash trash) {
        trash.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
        entityManager.addEntities(trash);
    }

    /**
     * Generates a random position within the screen bounds
     */
    private Vector2 getRandomPosition() {
        float margin = 50;
        return new Vector2(
            MathUtils.random(margin, DisplayManager.getScreenWidth() - margin),
            MathUtils.random(margin, DisplayManager.getScreenHeight() - margin)
        );
    }
}
