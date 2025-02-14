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
import java.util.Random;
import com.badlogic.gdx.utils.Array;
import io.github.team2.Actions.PlayerBehaviour;

public class GameScene extends Scene {
    // Physics constants
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final int MAX_DROPLETS = 10;
    private static final float POWERUP_SPAWN_CHANCE = 0.005f;
    private static final float MIN_SPAWN_INTERVAL = 1f;
    private static final float MAX_SPAWN_INTERVAL = 3f;


    

/* check if need remove 
	private void spawnPowerUp() {
        // Only try spawning if a powerup doesn't already exist
        boolean powerupExists = false;
        for (Entity entity : em.getEntities()) {
            if (entity.getEntityType() == EntityType.POWERUP) {
                powerupExists = true;
                break;
            }
        }
     */


    // Physics world
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator;

    // Managers
    private GameManager gameManager;
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;
    //private InputMultiplexer inputMultiplexer;
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
        System.out.println("check if still work 3 ");
        
    }

    private void initializeWorld() {
        world = new World(new Vector2(0, -100), true);
        debugRenderer = new Box2DDebugRenderer();
    }
    
    
    

    private void initializeManagers() {
        // Initialize entity manager
        entityManager = new EntityManager();

        // Initialize collision system directly with detector and resolver
        collisionDetector = new CollisionDetector();
        collisionResolver = new CollisionResolver(entityManager);

        // Setup collision listeners
        PointsSystem pointsSystem = new PointsSystem(gameManager.getPointsManager());
        collisionDetector.addListener(collisionResolver);
        collisionDetector.addListener(pointsSystem);
        world.setContactListener(collisionDetector);

        // Initialize input system
        inputManager = new InputManager();
        //inputMultiplexer = new InputMultiplexer();
    }

    private void initializeEntities() {
    	
    	try {
    		
    		
    		
            // Initialize static entities
            circle = new Circle(EntityType.CIRCLE,
                              new Vector2(500, 300),
                              new Vector2(0, 0),
                              200, Color.RED, 50);
            circle.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

            triangle = new Triangle(EntityType.TRIANGLE,
                                 new Vector2(100, 100),
                                 new Vector2(0, 0),
                                 200, Color.GREEN, 50, 50,
                                 TriangleBehaviour.State.IDLE, TriangleBehaviour.Move.NONE);
            
            triangle.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

            // Initialize player
            player = new Player(EntityType.PLAYER,
                              "bucket.png",
                              new Vector2(300, 100),
                              new Vector2(0, 0),200, PlayerBehaviour.State.IDLE, PlayerBehaviour.Move.NONE
                              );
            
            player.initPhysicsBody(world, BodyDef.BodyType.KinematicBody);

            gameManager.getPlayerEntityManager().addEntity(player);

            // Initialize droplets
            initializeDroplets();

            // Add entities to manager
            entityManager.addEntities(circle);
            entityManager.addEntities(triangle);
            entityManager.addEntities(player);
    		
    		
		} catch (Exception e) {
			
			System.out.println("error in game scene" + e);
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
                         new Vector2(random.nextFloat() * SceneManager.screenWidth,
                                   random.nextFloat() * SceneManager.screenHeight),
                         new Vector2(0, 0),
                         100, DropBehaviour.State.IDLE, DropBehaviour.Move.NONE );

        //drop.setAction(new Dropping(drop));
        drop.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
        return drop;
    }

    private void initializeInput() {
        // Initialize player input
        playerInputManager = new PlayerInputManager(player);
        playerInputManager.registerUserInput();

        // Register global inputs
        inputManager.registerKeyDown(Input.Keys.ESCAPE,
            new PauseGame(SceneManager.getInstance(SceneManager.class)));
        inputManager.registerKeyDown(Input.Keys.X,
            new ExitGame(SceneManager.getInstance(SceneManager.class)));


  /* check 
  
				droplets[i] = tmpDrop;
        		//droplets[i].setAction(new Dropping(droplets[i]));

*/

        inputManager.update();
        playerInputManager.update();
    }


        //inputMultiplexer.addProcessor(inputManager);
        //inputMultiplexer.addProcessor(playerInputManager);

        // Register global inputs
    //}

    @Override
    public void update() {
        
    	try {
        	inputManager.update();
            playerInputManager.update();
            updateInput();
            updateEntities();
            updateSpawning();
            updatePhysics();
            checkGameOver();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in game scene" + e);
		}
    	
    	
    	
    	

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
                               new Vector2(random.nextFloat() * SceneManager.screenWidth,
                                         SceneManager.screenHeight),
                               new Vector2(0, 0),
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
            "Score: " + gameManager.getPointsManager().getPoints(),
            baseX,
            baseY - lineSpacing,
            Color.RED);

        // Draw fails counter below score
        textManager.draw(batch,
            "Fails: " + gameManager.getPointsManager().getFails(),
            baseX,
            baseY - (lineSpacing * 2), // Two lines down from the top
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

    /*public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }*/



    

}
