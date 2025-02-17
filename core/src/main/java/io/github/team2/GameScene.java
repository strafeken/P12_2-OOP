package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
//import com.badlogic.gdx.InputMultiplexer;
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
import io.github.team2.Utils.DisplayManager;

import java.util.Random;

import io.github.team2.Actions.PlayerBehaviour;
import io.github.team2.AudioSystem.AudioManager;

public class GameScene extends Scene {
    // Physics constants
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final int MAX_DROPLETS = 10;
    private static final float MIN_SPAWN_INTERVAL = 1f;
    private static final float MAX_SPAWN_INTERVAL = 3f;

    // Physics world
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator;

    // Managers
    private CollisionDetector collisionDetector;
    private PointsManager pointsManager;

//    private PlayerInputManager playerInputManager;
    private PInputManager playerInputManager;
    
    private GameManager gameManager;

    // Game entities
    private Entity[] droplets;
    private Entity circle;
    private Entity triangle;
    private Entity player;
    private Button settingsButton;

    // Spawn control
    private float dropletSpawnTimer;
    private Random random;
 
    public GameScene() {
        super();
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
        
        gameManager = GameManager.getInstance(GameManager.class);
        gameManager.setPlayerInputManager(playerInputManager);
        
        AudioManager.getInstance(AudioManager.class).playSoundEffect("start");
    }

    private void initializeWorld() {
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
    }
    
    private void initializeManagers() {
        // Initialize managers
        entityManager = new EntityManager();
        gameInputManager = new GameInputManager();
        textManager = new TextManager();

        collisionDetector = new CollisionDetector();
        collisionDetector.addListener(new CollisionAudioHandler());
        collisionDetector.addListener(new CollisionRemovalHandler(entityManager));
        pointsManager = new PointsManager();
        collisionDetector.addListener(new PointsSystem(pointsManager));
        
        world.setContactListener(collisionDetector);
    }

    private void initializeEntities() {
    	try {
            // Initialize static entities
            circle = new Circle(EntityType.CIRCLE,
                              new Vector2(500, 400),
                              new Vector2(0, 0),
                               Color.RED, 50);
            circle.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

            triangle = new Triangle(EntityType.TRIANGLE,
                                 new Vector2(100, 250),
                                 new Vector2(0, 0),
                                 new Vector2(1,0), 200, Color.GREEN, 50, 50,
                                 TriangleBehaviour.State.IDLE, TriangleBehaviour.Move.NONE);
            
            triangle.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

            // Initialize player
            player = new Player(EntityType.PLAYER,
                              "bucket.png",
                              new Vector2(300, 50),
                              new Vector2(0, 0), new Vector2(100,0) , 200, PlayerBehaviour.State.IDLE, PlayerBehaviour.Move.NONE
                              );
            
            player.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

//            gameManager.getPlayerEntityManager().addEntity(player);

            // Initialize droplets
            initializeDroplets();

            // Add entities to manager
            entityManager.addEntities(circle);
            entityManager.addEntities(triangle);
            entityManager.addEntities(player);
		} catch (Exception e) {			
			System.out.println("error in game scene add area" + e);
		}
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
                         new Vector2(random.nextFloat() * DisplayManager.getScreenWidth(),
                                   random.nextFloat() * DisplayManager.getScreenHeight()),
                         new Vector2(0, 0), new Vector2(0, 0),
                         100, DropBehaviour.State.IDLE, DropBehaviour.Move.NONE );

        //drop.setAction(new Dropping(drop));
        drop.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
        return drop;
    }

    private void initializeInput() {
    	playerInputManager = new PInputManager(player);

    	gameInputManager.registerKeyUp(Input.Keys.ESCAPE, new PauseGame(SceneManager.getInstance(SceneManager.class)));
    	gameInputManager.registerKeyUp(Input.Keys.X, new ExitGame(SceneManager.getInstance(SceneManager.class)));
    	
        settingsButton = new Button("settingsBtn.png",
                new Vector2(DisplayManager.getScreenWidth() - 80, DisplayManager.getScreenHeight() - 80),
                new GoToSettings(SceneManager.getInstance(SceneManager.class)), 70, 70);
        
        gameInputManager.registerClickable(settingsButton);
    }

    @Override
    public void update() {
    	try {
    		entityManager.update();
    		gameInputManager.update();
    		playerInputManager.update();
    		updateSpawning();
    		updatePhysics();
//        checkGameOver();
//        settingsButton.update();
		} catch (Exception e) {
			System.out.println("error in game scene" + e);
		}
    }

    private void updateSpawning() {
        updateDropletSpawning();
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
        // Determine how many droplets to spawn (1-3)
        int currentDroplets = countCurrentDroplets();
        int maxNewDroplets = Math.min(3, MAX_DROPLETS - currentDroplets);

        if (maxNewDroplets <= 0) {
            return;  // Don't spawn if at or over max limit
        }

        // Randomly choose to spawn 1-3 droplets
        int dropletsToSpawn = random.nextInt(maxNewDroplets) + 1;

        // Spawn the determined number of droplets
        for (int i = 0; i < dropletsToSpawn; i++) {
            Drop drop = new Drop(EntityType.DROP,
                               "droplet.png",
                               new Vector2(random.nextFloat() * DisplayManager.getScreenWidth(),
                                         DisplayManager.getScreenHeight()),
                               new Vector2(0, 0), new Vector2(0, 0),
                               100, DropBehaviour.State.IDLE, DropBehaviour.Move.NONE);
            
            drop.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
            //drop.setAction(new Dropping(drop));
            entityManager.addEntities(drop);
        }
    }

    private void updatePhysics() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        accumulator += deltaTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        entityManager.draw(batch);
        drawUI(batch);
        settingsButton.draw(batch);
    }

    private void drawUI(SpriteBatch batch) {
        float padding = 10 * hudScaleX; // Scale padding with screen size
        float baseX = padding;
        float baseY = viewportHeight - padding;
        float lineSpacing = 30 * hudScaleY; // Vertical spacing between lines

        // Scale font size with screen
        textManager.getFont().getData().setScale(2.0f * hudScaleX, 2.0f * hudScaleY);

        // Draw scene title
        textManager.draw(batch,
            "Game Scene",
            baseX,
            baseY,
            Color.RED);

        // Draw score below title
        textManager.draw(batch,
            "Score: " + pointsManager.getPoints(),
            baseX,
            baseY - lineSpacing,
            Color.RED);
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
}
