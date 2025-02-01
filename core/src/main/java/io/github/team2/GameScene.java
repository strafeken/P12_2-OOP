package io.github.team2;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.Actions.ExitGame;
import io.github.team2.Actions.Move;
import io.github.team2.Actions.PauseGame;
import io.github.team2.CollisionSystem.CollisionDetector;
import io.github.team2.CollisionSystem.CollisionResolver;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.InputSystem.InputManager;
import io.github.team2.InputSystem.PlayerInputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class GameScene extends Scene {
	private static GameScene instance;
	/*
	 * Box2D world physics simulation
	 */
	private World world;
	// render collision debugger
	private Box2DDebugRenderer debugRenderer;

	private static final float TIME_STEP = 1 / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	
	private float accumulator = 0f;

	/* Managers */
	private PointsManager pm;

	private CollisionDetector collisionDetector;
	private CollisionResolver collisionResolver;

	private InputMultiplexer multiplexer;

	private PlayerInputManager playerInputManager;

	/* Entities */
	private Entity droplets[];
	private static final int MAX_DROPLETS = 10;
    private static final float POWERUP_SPAWN_CHANCE = 0.005f; // 0.5%
    private float dropletSpawnTimer = 0;
    private static final float MIN_SPAWN_INTERVAL = 1f;
    private static final float MAX_SPAWN_INTERVAL = 3f;
    private Random random = new Random();
	private Entity bucket;
	private Entity circle;
	private Entity triangle;
	private Entity player;
	
	private void spawnPowerUp() {
        // Only try spawning if a powerup doesn't already exist
        boolean powerupExists = false;
        for (Entity entity : em.getEntities()) {
            if (entity.getEntityType() == EntityType.POWERUP) {
                powerupExists = true;
                break;
            }
        }
        
        if (!powerupExists && random.nextFloat() <= POWERUP_SPAWN_CHANCE) {
            PowerUp powerUp = new PowerUp(
                EntityType.POWERUP,
                "pup1.png",
                new Vector2(random.nextInt(600), Gdx.graphics.getHeight()),
                new Vector2(0, 0),
                100
            );
            powerUp.InitPhysicsBody(world, BodyDef.BodyType.DynamicBody);
            em.addEntities(powerUp);
        }
    }
	private void spawnSingleDroplet() {
        
        Drop newDrop = new Drop(EntityType.DROP, "droplet.png",
            new Vector2(random.nextInt(600), Gdx.graphics.getHeight()), 
            new Vector2(0, 0), 100);
        newDrop.InitPhysicsBody(world, BodyDef.BodyType.DynamicBody);
        em.addEntities(newDrop);
    }
	public GameScene() {
        instance = this;
    }

    public static GameScene getInstance() {
        return instance;
    }

    public PointsManager getPointsManager() {
        return pm;
    }

	@Override
	public void load() {
		System.out.println("Game Scene => LOAD");

		world = new World(new Vector2(0, -100), true);

		debugRenderer = new Box2DDebugRenderer();

		em = new EntityManager();
		im = new InputManager();
		pm = new PointsManager();

		collisionDetector = new CollisionDetector();
		collisionResolver = new CollisionResolver(em);

		PointsSystem pointsSystem = new PointsSystem(pm);

		collisionDetector.addListener(collisionResolver);
		collisionDetector.addListener(pointsSystem);

		world.setContactListener(collisionDetector);

		tm = new TextManager();

		droplets = new TextureObject[10];

		
    		for (int i = 0; i < droplets.length; ++i) {
        		droplets[i] = new Drop(EntityType.DROP, "droplet.png",
                	new Vector2(random.nextInt(600), random.nextInt(440)), 
                	new Vector2(0, 0), 100);
        		droplets[i].InitPhysicsBody(world, BodyDef.BodyType.DynamicBody);
    	}

		bucket = new Bucket(EntityType.BUCKET, "bucket.png", new Vector2(200, 50), new Vector2(0, 0), 200);
		bucket.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody);

		circle = new Circle(EntityType.CIRCLE, new Vector2(500, 300), new Vector2(0, 0), 200, Color.RED, 50);
		circle.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody);

		triangle = new Triangle(EntityType.TRIANGLE, new Vector2(100, 100), new Vector2(0, 0), 200, Color.GREEN, 50,
				50);
		triangle.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody);

		player = new Player(EntityType.PLAYER, "bucket.png", new Vector2(300, 100), new Vector2(0, 0), 200);
		player.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody);

		for (int i = 0; i < droplets.length; ++i)
			em.addEntities(droplets[i]);

		em.addEntities(bucket);
		em.addEntities(circle);
		em.addEntities(triangle);
		em.addEntities(player);

		world.setContactListener(collisionDetector);

		playerInputManager = new PlayerInputManager(player);
		playerInputManager.registerUserInput();
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(im);
		multiplexer.addProcessor(playerInputManager);

		im.registerKeyDown(Input.Keys.ESCAPE, new PauseGame(SceneManager.getInstance()));
		im.registerKeyDown(Input.Keys.X, new ExitGame(SceneManager.getInstance()));
	}

	@Override
	public void update() {
		playerInputManager.update();
		im.update();
		em.update();
		spawnPowerUp();
		 // Random spawn interval between MIN and MAX
		 dropletSpawnTimer += Gdx.graphics.getDeltaTime();
		 if (dropletSpawnTimer >= MIN_SPAWN_INTERVAL + random.nextFloat() * (MAX_SPAWN_INTERVAL - MIN_SPAWN_INTERVAL)) {
			 // Count current droplets
			 int currentDroplets = 0;
			 for (Entity entity : em.getEntities()) {
				 if (entity.getEntityType() == EntityType.DROP) {
					 currentDroplets++;
				 }
			 }
			 
			 // Only spawn if below MAX_DROPLETS
			 if (currentDroplets < MAX_DROPLETS) {
				 spawnSingleDroplet();
			 }
			 dropletSpawnTimer = 0;
		 }
		// update physics at the end of render() loop
		// should be under draw() physics will update when game is paused
		// could add a boolean however it'll be a bit messy
		float deltaTime = Gdx.graphics.getDeltaTime();

		accumulator += deltaTime;

		while (accumulator >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}
		
	}

	@Override
    public void draw(SpriteBatch batch) {
        em.draw(batch);
		float paddingLeft = 20;
    	float startY = Gdx.graphics.getHeight() - 20; // Start 20px from top
    	float lineSpacing = 30; // Space between lines

        tm.draw(batch, "Game Scene", paddingLeft, startY, Color.RED);
    	tm.draw(batch, "Points: " + pm.getPoints(), paddingLeft, startY - lineSpacing, Color.RED);
    	tm.draw(batch, "Fails: " + pm.getFails(), paddingLeft, startY - (lineSpacing * 2), Color.RED);
    }
	@Override
	public void draw(ShapeRenderer shape) {
		em.draw(shape);

		debugRenderer.render(world, shape.getProjectionMatrix());
	}

	@Override
	public void unload() {
		System.out.println("Game Scene => UNLOAD");

		dispose();
	}

	@Override
	public void dispose() {
		System.out.println("Game Scene => DISPOSE");

		em.dispose();

		world.dispose();

		debugRenderer.dispose();
	}

	public InputMultiplexer getInputMultiplexer() {
		return multiplexer;
	}
}
