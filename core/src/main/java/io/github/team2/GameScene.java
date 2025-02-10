package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.team2.Actions.*;
import io.github.team2.CollisionSystem.*;
import io.github.team2.EntitySystem.*;
import io.github.team2.InputSystem.*;
import io.github.team2.SceneSystem.*;
import java.util.Random;
import com.badlogic.gdx.utils.Array;

/**
 * Scene class responsible for game rendering and entity management
 * Follows SRP by handling only scene-specific operations
 */
public class GameScene extends Scene {
    // Physics constants
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final int MAX_DROPLETS = 10;
    private static final float POWERUP_SPAWN_CHANCE = 0.005f;
    private static final float MIN_SPAWN_INTERVAL = 1f;
    private static final float MAX_SPAWN_INTERVAL = 3f;

    // Physics world
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator;

    // Managers
    private GameManager gameManager;
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;
    private InputMultiplexer inputMultiplexer;
    private PlayerInputManager playerInputManager;

    // Game entities
    private Entity[] droplets;
    private Entity circle;
    private Entity triangle;
    private Entity player;

    // Spawn control
    private float dropletSpawnTimer;
    private Random random;

    public GameScene() {
        super();
        this.gameManager = GameManager.getInstance(); // Use singleton instead of new instance
        random = new Random();
        accumulator = 0f;
        dropletSpawnTimer = 0f;

    }

    @Override
    public void load() {
        System.out.println("Game Scene => LOAD");
        initializeWorld();
        initializeManagers();
        initializeEntities();
        initializeInput();
    }

    private void initializeWorld() {
        world = new World(new Vector2(0, -100), true);
        debugRenderer = new Box2DDebugRenderer();
    }

    private void initializeManagers() {
        // Initialize entity manager
        entityManager = new EntityManager();

        // Initialize collision system
        collisionDetector = new CollisionDetector();
        collisionResolver = new CollisionResolver(entityManager);

        // Setup collision listeners
        PointsSystem pointsSystem = new PointsSystem(gameManager.getPointsManager());
        collisionDetector.addListener(collisionResolver);
        collisionDetector.addListener(pointsSystem);
        world.setContactListener(collisionDetector);

        // Initialize input system
        inputManager = new InputManager();
        inputMultiplexer = new InputMultiplexer();
    }

    private void initializeEntities() {
        // Initialize static entities
        circle = new Circle(EntityType.CIRCLE,
                          new Vector2(500, 300),
                          new Vector2(0, 0),
                          200, Color.RED, 50);
        circle.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

        triangle = new Triangle(EntityType.TRIANGLE,
                             new Vector2(100, 100),
                             new Vector2(0, 0),
                             200, Color.GREEN, 50, 50);
        triangle.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

        // Initialize player
        player = new Player(EntityType.PLAYER,
                          "bucket.png",
                          new Vector2(300, 100),
                          new Vector2(0, 0),
                          200);
        player.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

        gameManager.getPlayerEntityManager().addEntity(player);

        // Initialize droplets
        initializeDroplets();

        // Add entities to manager
        entityManager.addEntities(circle);
        entityManager.addEntities(triangle);
        entityManager.addEntities(player);
    }

    private void initializeDroplets() {
        droplets = new Entity[MAX_DROPLETS];
        for (int i = 0; i < droplets.length; i++) {
            Drop drop = createDrop();
            droplets[i] = drop;
            entityManager.addEntities(drop);
        }
    }

    private Drop createDrop() {
        Drop drop = new Drop(EntityType.DROP,
                         "droplet.png",
                         new Vector2(random.nextFloat() * SceneManager.screenWidth,
                                   random.nextFloat() * SceneManager.screenHeight),
                         new Vector2(0, 0),
                         100);

        drop.setAction(new Dropping(drop));
        drop.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
        return drop;
    }

    private void initializeInput() {
        playerInputManager = new PlayerInputManager(player);
        playerInputManager.registerUserInput();

        inputMultiplexer.addProcessor(inputManager);
        inputMultiplexer.addProcessor(playerInputManager);

        // Register global inputs
        inputManager.registerKeyDown(Input.Keys.ESCAPE,
            new PauseGame(SceneManager.getInstance(SceneManager.class)));
        inputManager.registerKeyDown(Input.Keys.X,
            new ExitGame(SceneManager.getInstance(SceneManager.class)));
    }

    @Override
    public void update() {
        updateInput();
        updateEntities();
        updateSpawning();
        updatePhysics();
        checkGameOver();
    }

    private void updateInput() {
        playerInputManager.update();
        inputManager.update();
    }

    private void updateEntities() {
        entityManager.update();
    }

    private void updateSpawning() {
        spawnPowerUp();
        updateDropletSpawning();
    }

    private void spawnPowerUp() {
        if (!isPowerUpPresent() && random.nextFloat() <= POWERUP_SPAWN_CHANCE) {
            PowerUp powerUp = new PowerUp(
                EntityType.POWERUP,
                "pup1.png",
                new Vector2(random.nextFloat() * SceneManager.screenWidth,
                          SceneManager.screenHeight),
                new Vector2(0, 0),
                100
            );
            powerUp.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
            powerUp.setAction(new Dropping(powerUp));
            entityManager.addEntities(powerUp);
        }
    }

    private boolean isPowerUpPresent() {
        return entityManager.getEntities().stream()
            .anyMatch(e -> e.getEntityType() == EntityType.POWERUP);
    }

    private void updateDropletSpawning() {
        dropletSpawnTimer += Gdx.graphics.getDeltaTime();
        if (shouldSpawnDroplet()) {
            spawnDroplet();
            dropletSpawnTimer = 0;
        }
    }

    private boolean shouldSpawnDroplet() {
        float spawnInterval = MIN_SPAWN_INTERVAL +
            random.nextFloat() * (MAX_SPAWN_INTERVAL - MIN_SPAWN_INTERVAL);
        return dropletSpawnTimer >= spawnInterval &&
               countCurrentDroplets() < MAX_DROPLETS;
    }

    private int countCurrentDroplets() {
        return (int) entityManager.getEntities().stream()
            .filter(e -> e.getEntityType() == EntityType.DROP)
            .count();
    }

    private void spawnDroplet() {
        Drop drop = new Drop(EntityType.DROP,
                           "droplet.png",
                           new Vector2(random.nextFloat() * SceneManager.screenWidth,
                                     SceneManager.screenHeight),
                           new Vector2(0, 0),
                           100);
        drop.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
        drop.setAction(new Dropping(drop));
        entityManager.addEntities(drop); // Add this line to add the drop to entity manager
    }

    private void updatePhysics() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        accumulator += deltaTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    private void checkGameOver() {
        if (gameManager.getPointsManager().getFails() >= 20) {
            System.out.println("Game Over triggered! Fails: " + gameManager.getPointsManager().getFails());
            // Switch scene first, then GameManager will handle state
            SceneManager.getInstance(SceneManager.class).setNextScene(SceneID.GAME_OVER);
        }
    }
    @Override
    public void draw(SpriteBatch batch) {
        entityManager.draw(batch);
        drawUI(batch);
    }

    private void drawUI(SpriteBatch batch) {
        float paddingLeft = 20;
        float startY = SceneManager.screenHeight - 20;
        float lineSpacing = 30;

        // Draw UI elements
        textManager.draw(batch, "Game Scene",
                        paddingLeft, startY, Color.RED);
        textManager.draw(batch, "Points: " + GameManager.getInstance().getPointsManager().getPoints(),
                        paddingLeft, startY - lineSpacing, Color.RED);
        textManager.draw(batch, "Fails: " + GameManager.getInstance().getPointsManager().getFails(),
                        paddingLeft, startY - (lineSpacing * 2), Color.RED);


        // System.out.println("Current Points: " + GameManager.getInstance().getPointsManager().getPoints());
    }

    @Override
    public void draw(ShapeRenderer shape) {
        entityManager.draw(shape);
        debugRenderer.render(world, shape.getProjectionMatrix());
    }

    @Override
    public void unload() {
        System.out.println("Game Scene => UNLOAD");
        try {
            // Stop physics simulation updates
            accumulator = 0;

            // Let all entities clean up their physics bodies and other resources
            if (entityManager != null) {
                entityManager.dispose();
            }

            // Now dispose remaining resources (including disposing the physics world)
            dispose();
        } catch (Exception e) {
            System.err.println("Error during unload: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        System.out.println("Game Scene => DISPOSE");
        if (debugRenderer != null) {
            debugRenderer.dispose();
            debugRenderer = null;
        }
        if (world != null) {
            world.dispose();
            world = null;
        }
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    @Override
    protected void resize(int width, int height) {
        // Handle resize if needed
    }
}
