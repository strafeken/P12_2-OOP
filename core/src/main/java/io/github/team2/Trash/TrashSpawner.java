package io.github.team2.Trash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.Utils.DisplayManager;

import java.util.HashMap;
import java.util.Map;

public class TrashSpawner {
    // Add constants for max objects
    private static final int MAX_TOTAL_OBJECTS = 20;
    private static final int MAX_NON_RECYCLABLE = 5;

    private final World world;
    private final IEntityManager entityManager;

    // Texture mappings for different trash types
    private static final Map<RecyclableTrash.Type, String> recyclableTextures = new HashMap<>();
    private static final Map<NonRecyclableTrash.Type, String> nonRecyclableTextures = new HashMap<>();

    static {
        // Initialize texture mappings
        recyclableTextures.put(RecyclableTrash.Type.PAPER, "item/cardboard-glow.png");
        recyclableTextures.put(RecyclableTrash.Type.PLASTIC, "item/plastic-glow.png");
        recyclableTextures.put(RecyclableTrash.Type.GLASS, "item/glass-glow.png");
        recyclableTextures.put(RecyclableTrash.Type.METAL, "item/soda-can-glow.png");

        nonRecyclableTextures.put(NonRecyclableTrash.Type.HAZARDOUS, "item/toxic-waste-glow.png");
        nonRecyclableTextures.put(NonRecyclableTrash.Type.ELECTRONIC_WASTE, "item/broken-device-glow.png");
        nonRecyclableTextures.put(NonRecyclableTrash.Type.MIXED_WASTE, "item/half-eaten-food-glow.png");
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
        // Get current counts
        int currentTotal = entityManager.getEntitiesByType(EntityType.RECYCLABLE).size() +
                         entityManager.getEntitiesByType(EntityType.NON_RECYCLABLE).size();
        int currentNonRecyclable = entityManager.getEntitiesByType(EntityType.NON_RECYCLABLE).size();

        // Calculate how many we can spawn
        int remainingSlots = MAX_TOTAL_OBJECTS - currentTotal;
        int toSpawn = Math.min(count, remainingSlots);

        if (toSpawn <= 0) return;

        for (int i = 0; i < toSpawn; i++) {
            if (currentNonRecyclable < MAX_NON_RECYCLABLE && MathUtils.random() > recyclableRatio) {
                spawnRandomNonRecyclable();
                currentNonRecyclable++;
            } else {
                spawnRandomRecyclable();
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
        Vector2 size = new Vector2(50, 50); // Adjust size as needed

        RecyclableTrash trash = new RecyclableTrash(texture, size, position, type);
        setupTrashPhysics(trash);

        return trash;
    }

    /**
     * Spawns a single random non-recyclable trash item
     */
    public Entity spawnRandomNonRecyclable() {
        // Check if we've hit the non-recyclable limit
        if (entityManager.getEntitiesByType(EntityType.NON_RECYCLABLE).size() >= MAX_NON_RECYCLABLE) {
            return null;
        }

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
