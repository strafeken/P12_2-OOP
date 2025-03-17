package io.github.team2;

import java.awt.Image;
import java.sql.BatchUpdateException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.Actions.ExitGame;
import io.github.team2.Actions.GoToSettings;
import io.github.team2.Actions.PauseGame;
import io.github.team2.Actions.PlayerBehaviour;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.CollisionExtensions.CollisionAudioHandler;
import io.github.team2.CollisionExtensions.CollisionRemovalHandler;
import io.github.team2.CollisionExtensions.PlayerLifeHandler;
import io.github.team2.CollisionExtensions.PointsSystem;
import io.github.team2.CollisionExtensions.RecyclableCarrierHandler;
import io.github.team2.CollisionExtensions.RecyclingBinHandler;
import io.github.team2.CollisionExtensions.StartLevelHandler;
import io.github.team2.CollisionExtensions.StartMiniGameHandler;
import io.github.team2.CollisionSystem.CollisionDetector;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.StaticTextureObject;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.InputSystem.PlayerInputManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.Utils.DisplayManager;

public class LevelSelectScene extends Scene {

	private int width = (int) DisplayManager.getScreenWidth();
	private int height = (int) DisplayManager.getScreenHeight();

	// Physics constants
	private static final float TIME_STEP = 1 / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;

	// Physics world
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private float accumulator;

	private PlayerInputManager playerInputManager;
	private GameManager gameManager;
	private CollisionDetector collisionDetector;

	private Camera camera1;

	private StaticTextureObject image;
	private Entity player;
	private Entity level1;
	
	private StartLevelHandler startLevelHandler;

	public LevelSelectScene() {

	}

	public void load() {
		System.out.println("Level Select Scene => LOAD");

		entityManager = new EntityManager();
		gameInputManager = new GameInputManager();

		collisionDetector = new CollisionDetector();

		initializeWorld();

		player = new Player(EntityType.PLAYER, "rocket-2.png", new Vector2(70, 100),
				new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2),
				new Vector2(0, 0), new Vector2(100, 0), 200, PlayerBehaviour.State.IDLE, PlayerBehaviour.Move.NONE);
		player.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
		player.getPhysicsBody().getBody().setFixedRotation(true);

		initializeInput();
		initializeCollisionHandlers();

		gameManager = GameManager.getInstance();
		gameManager.setPlayerInputManager(playerInputManager);

		camera1 = new Camera(width, height);

		// Setup background image
		image = new StaticTextureObject(EntityType.UNDEFINED, "space_background.jpg",
				new Vector2(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight()),
				new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2),
				new Vector2(0, 0));
		entityManager.addEntities(image);

		level1 = new Planet(EntityType.LEVEL1, "level_planet2_yellow.jpg", new Vector2(70, 100), new Vector2(70, 50),
				new Vector2(0, 0));
		level1.initPhysicsBody(world, BodyDef.BodyType.StaticBody);
		level1.getPhysicsBody().getBody().setFixedRotation(true);

		entityManager.addEntities(player);
		entityManager.addEntities(level1);

		// Add all collision handlers/listeners after initializing audioManager
		world.setContactListener(collisionDetector);

	}

	private void initializeWorld() {
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
	}

	private void initializeInput() {
		playerInputManager = new PlayerInputManager(player);

		// Use interface instead of concrete class
		ISceneManager sceneManager = SceneManager.getInstance();
		gameInputManager.registerKeyUp(Input.Keys.ESCAPE, new PauseGame(sceneManager));
		gameInputManager.registerKeyUp(Input.Keys.X, new ExitGame(sceneManager));

	}

	private void initializeCollisionHandlers() {
		// Add collision listeners (using CollisionType enum)
		StartLevelHandler startLevelHandler = new StartLevelHandler();
		collisionDetector.addListener(startLevelHandler);
		
		this.startLevelHandler = startLevelHandler;

	}

	private void updatePhysics(float deltaTime) {

		accumulator += deltaTime;

		while (accumulator >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}
		
		processPending();
	}
	
	private void processPending() {
		
		
	}
	
	@Override
	public void update() {

		float delta = Gdx.graphics.getDeltaTime();
		updatePhysics(delta);
		entityManager.update();
		gameInputManager.update();
		playerInputManager.update();
		

		camera1.cameraUpdate(delta, player.getPosition());
		
		startLevelHandler.processAction();
		
	}

	@Override
	public void draw(SpriteBatch batch) {

		batch.setProjectionMatrix(camera1.camera.combined);
		entityManager.draw(batch);

	}

	@Override
	public void draw(ShapeRenderer shape) {
		// TODO Auto-generated method stub

		shape.setProjectionMatrix(camera1.camera.combined);
		entityManager.draw(shape);
		debugRenderer.render(world, shape.getProjectionMatrix());

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

		System.out.println("Level Select => UNLOADED");

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

		if (debugRenderer != null) {
			debugRenderer.dispose();
			debugRenderer = null;
		}
		if (world != null) {
			world.dispose();
			world = null;
		}

		entityManager.dispose();
		// Use interface
		IAudioManager audioManager = AudioManager.getInstance();
		audioManager.stopMusic(); // Stop music when leaving menu

	}
}
