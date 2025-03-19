package io.github.team2;

import java.util.Random;

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
import io.github.team2.Trash.ConcreteTrashFactory;
import io.github.team2.Trash.RecyclableTrash;
import io.github.team2.Trash.RecyclingBin;
import io.github.team2.Trash.TrashFactory;
import io.github.team2.Trash.TrashSpawner;
import io.github.team2.Utils.DisplayManager;

public class GameScene extends Scene {
    // Physics constants
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    // Physics world
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator;

    // Managers
    private CollisionDetector collisionDetector;
    private PointsManager pointsManager;
    private PlayerInputManager playerInputManager;

    private GameManager gameManager;
    private IAudioManager audioManager;
    private LevelManager levelManager;

    // Game entities
    private Entity player;
    private Button settingsButton;

    // Spawn control
    private Random random;
    private TrashSpawner trashSpawner;

    private float trashSpawnTimer = 0f;
    private float trashSpawnInterval = 5f; // Spawn trash every 5 seconds
    private TrashFactory trashFactory;

    private StartMiniGameHandler miniGameHandler;
    private PlayerLifeHandler playerLifeHandler;


    private Camera camera;

    public GameScene() {
        super();
        random = new Random();
        accumulator = 0f;
    }

    @Override
    public void load() {
        System.out.println("Game Scene => LOAD");

        // Initialize LevelManager at the beginning to ensure it's available for everything else
        levelManager = LevelManager.getInstance();
        System.out.println("Loading game scene with level: " + levelManager.getCurrentLevel());

        // 1. Set up your camera to default size
        camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        initializeWorld();
        initializeManagers();
        initializeEntities();
        initializeInput();
        initializeCollisionHandlers();

        gameManager = GameManager.getInstance();
        gameManager.setPlayerInputManager(playerInputManager);

        // Initialize player status
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        playerStatus.setPlayer(player);
        playerStatus.reset();

        audioManager = AudioManager.getInstance();
        AudioManager.getInstance().stopSoundEffect("levelsect");
        AudioManager.getInstance().playSoundEffect("start");

        // Debug output to verify level
        System.out.println("Game Scene loaded with level: " + levelManager.getCurrentLevel() +
                          ", Alien speed: " + levelManager.getCurrentAlienSpeed());
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

        // Initialize audio manager first
        audioManager = AudioManager.getInstance();

        collisionDetector = new CollisionDetector();
        pointsManager = new PointsManager();

        // Add all collision handlers/listeners after initializing audioManager
        world.setContactListener(collisionDetector);

        // Initialize the trash spawner
        trashSpawner = new TrashSpawner(world, entityManager);
        trashFactory = new ConcreteTrashFactory();
    }

    private void initializeCollisionHandlers() {
        // Add collision listeners (using CollisionType enum)
        PlayerLifeHandler playerLifeHandler = new PlayerLifeHandler(SceneManager.getInstance(), pointsManager);
        collisionDetector.addListener(playerLifeHandler);
        StartMiniGameHandler miniGameHandler = new StartMiniGameHandler(pointsManager, entityManager);
        collisionDetector.addListener(miniGameHandler);
        collisionDetector.addListener(new CollisionAudioHandler(audioManager));
        collisionDetector.addListener(new CollisionRemovalHandler(entityManager));
        collisionDetector.addListener(new PointsSystem(pointsManager));

        // Add collision handlers (using direct contacts)
        collisionDetector.addHandler(new RecyclableCarrierHandler(entityManager));
        collisionDetector.addHandler(new RecyclingBinHandler(pointsManager));

        // Store the handlers for updates
        this.miniGameHandler = miniGameHandler;
        this.playerLifeHandler = playerLifeHandler;
    }

    private void initializeEntities() {
    	try {


	        StaticTextureObject bg_image  = new StaticTextureObject(EntityType.UNDEFINED, "planet/planet2_yellow.jpg", new Vector2(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight()),
	                new Vector2(DisplayManager.getScreenWidth()/2,  DisplayManager.getScreenHeight()/2),
	                new Vector2(0, 0));

	        entityManager.addEntities(bg_image);


            player = new Player(EntityType.PLAYER,
                              "rocket-2.png",
                              new Vector2(70, 100),
                              new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2),
                              new Vector2(0, 0), new Vector2(100,0) , 200, PlayerBehaviour.State.IDLE, PlayerBehaviour.Move.NONE
                              );
            player.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
            player.getPhysicsBody().getBody().setFixedRotation(true);
            entityManager.addEntities(player);

            // When creating aliens, ensure they get the level-specific speed:
            float alienSpeed = levelManager.getCurrentAlienSpeed();
            Entity alien = new Alien(
                EntityType.ALIEN,
                "alien.png",
                new Vector2(80, 80),
                new Vector2(200, 200),
                new Vector2(0, 0),
                new Vector2(0, 0),
                alienSpeed, // Use the level-specific speed
                AlienBehaviour.State.IDLE,
                AlienBehaviour.Move.NONE
            );
            alien.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
            // Set the target player for the alien to chase
            ((Alien)alien).setTargetPlayer(player);
            entityManager.addEntities(alien);

            RecyclingBin bin = new RecyclingBin(EntityType.RECYCLING_BIN, "recycling-bin.png",
            									new Vector2(100, 150),
            									new Vector2(DisplayManager.getScreenWidth() / 2, 100), new Vector2(0, 0));
            bin.initPhysicsBody(world, BodyDef.BodyType.StaticBody);
            bin.getPhysicsBody().setAsSensor();
            entityManager.addEntities(bin);

            spawnTrash(10);

		} catch (Exception e) {
			System.out.println("error in game scene add area" + e);
		}
    }

    private void initializeInput() {
        playerInputManager = new PlayerInputManager(player);

        // Use interface instead of concrete class
        ISceneManager sceneManager = SceneManager.getInstance();
        gameInputManager.registerKeyUp(Input.Keys.ESCAPE, new PauseGame(sceneManager));
        gameInputManager.registerKeyUp(Input.Keys.X, new ExitGame(sceneManager));

        settingsButton = new Button("settingsBtn.png",
                new Vector2(DisplayManager.getScreenWidth() - 80, DisplayManager.getScreenHeight() - 80),
                new GoToSettings(sceneManager), 70, 70);

        gameInputManager.registerClickable(settingsButton);
    }

    private void spawnTrash(int count) {
        try {
            // Use the ratio 0.7 for 70% recyclable trash
            trashSpawner.spawnRandomTrash(count, 0.7f);
        } catch (Exception e) {
            System.out.println("Error spawning trash: " + e);
        }
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();

        // Get player status
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        // Only update physics and check collisions if not in mini-game
        if (!playerStatus.isInMiniGame()) {
            updatePhysics(delta);

            if (playerLifeHandler.checkGameOver()) {
                return;
            }

            // Update mini-game handler cooldown timer
            miniGameHandler.update(delta);

            // Update trash spawn timer
            trashSpawnTimer += delta;
            if (trashSpawnTimer >= trashSpawnInterval) {
                trashSpawnTimer = 0;
                spawnTrash(5);  // Try to spawn up to 5 pieces of trash each interval
            }
        }

        // These updates should happen regardless of mini-game status
        entityManager.update();
        gameInputManager.update();
        playerInputManager.update();
        settingsButton.update();
    }

    private void updatePhysics(float deltaTime) {

        accumulator += deltaTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {

    	batch.setProjectionMatrix(camera.camera.combined);

        entityManager.draw(batch);
        drawUI(batch);
        settingsButton.draw(batch);

        // Draw player status (carrying item icon, lives)
        drawPlayerStatus(batch);
    }

    private void drawUI(SpriteBatch batch) {
        // Draw points
        textManager.draw(batch, "Points: " + pointsManager.getPoints(),
                         DisplayManager.getScreenWidth() - 200, DisplayManager.getScreenHeight() - 20, Color.WHITE);

        // Make sure to use the current level from levelManager
        int level = levelManager.getCurrentLevel();
        textManager.draw(batch, "Level: " + level,
                         DisplayManager.getScreenWidth() - 200, DisplayManager.getScreenHeight() - 50, Color.YELLOW);

        // Draw player lives
        drawPlayerStatus(batch);

        // Display level-up notification if a new level was unlocked
        if (pointsManager.isLevelUnlocked()) {
            textManager.draw(batch, "New Level Unlocked!",
                            DisplayManager.getScreenWidth() / 2 - 100, DisplayManager.getScreenHeight() / 2, Color.GREEN);
        }

        // Debug output - remove in production
        System.out.println("Current level displayed: " + level + ", Alien speed: " + levelManager.getCurrentAlienSpeed());
    }

    private void drawPlayerStatus(SpriteBatch batch) {
        PlayerStatus status = PlayerStatus.getInstance();

        // Draw lives
        float x = 20;
        float y = DisplayManager.getScreenHeight() - 30;
        textManager.draw(batch, "Lives: " + status.getLives(), x, y, Color.WHITE);

        // Draw score
        textManager.draw(batch, "Score: " + pointsManager.getPoints(), x, y - 30, Color.WHITE);

        // Draw carrying status
        if (status.isCarryingRecyclable()) {
            Entity carried = status.getCarriedItem();
            if (carried instanceof RecyclableTrash) {
                RecyclableTrash trash = (RecyclableTrash) carried;
                String type = trash.getRecyclableType().toString();
                textManager.draw(batch, "Carrying: " + type, x, y - 60, Color.GREEN);
            }
        }

        // Draw mini-game status if in one
        if (status.isInMiniGame()) {
            textManager.draw(batch, "IN MINI-GAME", DisplayManager.getScreenWidth() / 2 - 100,
                            DisplayManager.getScreenHeight() - 30, Color.YELLOW);
        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setProjectionMatrix(camera.camera.combined);
        entityManager.draw(shape);
        
        // Use the collisionDetector's render method instead of direct debugRenderer
        if (collisionDetector != null && world != null) {
            collisionDetector.renderDebug(world, shape.getProjectionMatrix());
        }
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
        if (world != null) {
            world.dispose();
            world = null;
        }
        
        // Let collisionDetector handle its own debugRenderer cleanup
        if (collisionDetector != null) {
            collisionDetector.dispose();
        }
        
        if (entityManager != null) {
            entityManager.dispose();
        }
    }
}
