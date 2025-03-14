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
import io.github.team2.CollisionExtensions.*;
import io.github.team2.EntitySystem.*;
import io.github.team2.InputSystem.*;
import io.github.team2.SceneSystem.*;
import io.github.team2.Trash.*;
import io.github.team2.Utils.DisplayManager;

import java.util.Random;

import io.github.team2.Actions.PlayerBehaviour;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.CollisionExtensions.CollisionAudioHandler;
import io.github.team2.CollisionExtensions.CollisionRemovalHandler;
import io.github.team2.CollisionExtensions.PointsSystem;
import io.github.team2.AudioSystem.IAudioManager;

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

    public GameScene() {
        super();
        random = new Random();
        accumulator = 0f;
    }

    @Override
    public void load() {
        System.out.println("Game Scene => LOAD");

        initializeWorld();
        initializeManagers();
        initializeEntities();
        initializeInput();
        initializeCollisionHandlers(); // New method

        gameManager = GameManager.getInstance();
        gameManager.setPlayerInputManager(playerInputManager);

        // Initialize player status
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        playerStatus.setPlayer(player);
        playerStatus.reset();

        audioManager = AudioManager.getInstance();
        AudioManager.getInstance().stopSoundEffect("mainmenu");
        AudioManager.getInstance().playSoundEffect("start");
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
        collisionDetector.addListener(new PlayerLifeHandler(SceneManager.getInstance()));
        StartMiniGameHandler miniGameHandler = new StartMiniGameHandler(pointsManager, entityManager);
        collisionDetector.addListener(miniGameHandler);
        collisionDetector.addListener(new CollisionAudioHandler(audioManager));
        collisionDetector.addListener(new CollisionRemovalHandler(entityManager));
        collisionDetector.addListener(new PointsSystem(pointsManager));

        // Add collision handlers (using direct contacts)
        collisionDetector.addHandler(new RecyclableCarrierHandler(entityManager));
        collisionDetector.addHandler(new RecyclingBinHandler(pointsManager));

        // Store the handler for updates
        this.miniGameHandler = miniGameHandler;
    }

    private void initializeEntities() {
    	try {
            player = new Player(EntityType.PLAYER,
                              "rocket.png",
                              new Vector2(70, 100),
                              new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2),
                              new Vector2(0, 0), new Vector2(100,0) , 200, PlayerBehaviour.State.IDLE, PlayerBehaviour.Move.NONE
                              );
            player.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
            player.getPhysicsBody().getBody().setFixedRotation(true);
            entityManager.addEntities(player);

            Alien alien = new Alien(EntityType.ALIEN,
                    "alien.png",
                    new Vector2(90, 90),
                    new Vector2(DisplayManager.getScreenWidth() / 2 - 200, DisplayManager.getScreenHeight() / 2 + 200),
                    new Vector2(0, 0), new Vector2(100,0) , 200, AlienBehaviour.State.IDLE, AlienBehaviour.Move.NONE
                    );
            alien.initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
            // Set the target player for the alien to chase
            alien.setTargetPlayer(player);
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
        // Spawn 70% recyclable items, 30% non-recyclable
        trashSpawner.spawnRandomTrash(count, 0.7f);

        System.out.println("Spawned " + count + " trash items");
    }

    @Override
    public void update() {
        try {
            // Update trash spawn timer
            trashSpawnTimer += Gdx.graphics.getDeltaTime();

            // Update mini-game handler cooldown
            if (miniGameHandler != null) {
                miniGameHandler.update(Gdx.graphics.getDeltaTime());
            }

            // Spawn new trash periodically
            if (trashSpawnTimer >= trashSpawnInterval) {
                // Spawn 1-3 new trash items
                int itemsToSpawn = random.nextInt(3) + 1;
                spawnTrash(itemsToSpawn);
                trashSpawnTimer = 0f;

                // Gradually decrease spawn interval for increasing difficulty
                // But don't go below 2 seconds
                trashSpawnInterval = Math.max(2.0f, trashSpawnInterval * 0.99f);
            }

            entityManager.update();
            gameInputManager.update();
            playerInputManager.update();
            updatePhysics();
        } catch (Exception e) {
            System.out.println("Error in game scene: " + e);
            e.printStackTrace();
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

        // Draw player status (carrying item icon, lives)
        drawPlayerStatus(batch);
    }

    private void drawUI(SpriteBatch batch) {
        float padding = 10f; // Scale padding with screen size
        float baseX = padding;
        float baseY = DisplayManager.getScreenHeight() - padding;
        float lineSpacing = 30f; // Vertical spacing between lines

        // Scale font size with screen
        textManager.getFont().getData().setScale(2.0f, 2.0f);

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
