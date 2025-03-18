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
    private LevelManager levelManager;
    private TextManager textManager;

    private Camera camera1;

    private StaticTextureObject image;
    private Entity player;
    private Entity[] levelPlanets;

    private StartLevelHandler startLevelHandler;

    public LevelSelectScene() {

    }

    public void load() {
        System.out.println("Level Select Scene => LOAD");

        entityManager = new EntityManager();
        gameInputManager = new GameInputManager();
        textManager = new TextManager();
        levelManager = LevelManager.getInstance();

        collisionDetector = new CollisionDetector();

        initializeWorld();

        // Stop mainmenu sound and play night sound
        IAudioManager audioManager = AudioManager.getInstance();
        audioManager.stopSoundEffect("mainmenu");
        audioManager.playSoundEffect("levelsect");

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

        // Create planet entities for each level
        createLevelPlanets();

        entityManager.addEntities(player);

        // Add all collision handlers/listeners after initializing audioManager
        world.setContactListener(collisionDetector);
    }

    // Update the createLevelPlanets method to make sure planets are properly created

    private void createLevelPlanets() {
        // Create planet entities for all levels
        levelPlanets = new Entity[4];  // 4 levels

        // Level 1 planet (always unlocked)
        levelPlanets[0] = new Planet(EntityType.LEVEL1, "planet/level_planet1_purple.png",
                new Vector2(80, 80), new Vector2(150, 150), new Vector2(0, 0));
        levelPlanets[0].initPhysicsBody(world, BodyDef.BodyType.StaticBody);
        entityManager.addEntities(levelPlanets[0]);

        System.out.println("Creating planets. Unlocked levels: " + levelManager.getUnlockedLevels());

        // ONLY show level 2+ planets if they're actually unlocked
        if (levelManager.isLevelUnlocked(2)) {
            System.out.println("Level 2 is unlocked!");
            levelPlanets[1] = new Planet(EntityType.LEVEL2, "planet/level_planet2_yellow.png",
                    new Vector2(80, 80), new Vector2(280, 120), new Vector2(0, 0));
            levelPlanets[1].initPhysicsBody(world, BodyDef.BodyType.StaticBody);
            entityManager.addEntities(levelPlanets[1]);
        } else {
            // Show placeholder or locked indicator for level 2
            System.out.println("Level 2 is LOCKED!");
        }

        // Same for levels 3 and 4
        if (levelManager.isLevelUnlocked(3)) {
            levelPlanets[2] = new Planet(EntityType.LEVEL3, "planet/level_planet3_red.png",
                    new Vector2(80, 80), new Vector2(480, 180), new Vector2(0, 0));
            levelPlanets[2].initPhysicsBody(world, BodyDef.BodyType.StaticBody);
            entityManager.addEntities(levelPlanets[2]);
        }

        if (levelManager.isLevelUnlocked(4)) {
            levelPlanets[3] = new Planet(EntityType.LEVEL4, "planet/level_planet4_grey.png",
                    new Vector2(80, 80), new Vector2(650, 250), new Vector2(0, 0));
            levelPlanets[3].initPhysicsBody(world, BodyDef.BodyType.StaticBody);
            entityManager.addEntities(levelPlanets[3]);
        }
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
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        updatePhysics(delta);
        entityManager.update();
        gameInputManager.update();
        playerInputManager.update();

        camera1.cameraUpdate(delta, player.getPosition());
        startLevelHandler.processAction(camera1);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(camera1.camera.combined);
        entityManager.draw(batch);

        // Make planet descriptions more visible
        textManager.draw(batch, "Level 1", 120, 200, Color.WHITE);

        // Display unlock requirements with clearer indications
        if (!levelManager.isLevelUnlocked(2)) {
            textManager.draw(batch, "Level 2 - LOCKED", 250, 170, Color.RED);
            textManager.draw(batch, "Get " + LevelManager.LEVEL2_POINTS + " points in Level 1 to unlock",
                    250, 140, Color.YELLOW);
        } else {
            textManager.draw(batch, "Level 2", 250, 170, Color.WHITE);
        }

        if (!levelManager.isLevelUnlocked(3)) {
            textManager.draw(batch, "Level 3 - LOCKED", 450, 230, Color.RED);
            textManager.draw(batch, "Get " + LevelManager.LEVEL3_POINTS + " points in Level 2 to unlock",
                    450, 200, Color.YELLOW);
        } else {
            textManager.draw(batch, "Level 3", 450, 230, Color.WHITE);
        }

        if (!levelManager.isLevelUnlocked(4)) {
            textManager.draw(batch, "Level 4 - LOCKED", 620, 300, Color.RED);
            textManager.draw(batch, "Get " + LevelManager.LEVEL4_POINTS + " points in Level 3 to unlock",
                    620, 270, Color.YELLOW);
        } else {
            textManager.draw(batch, "Level 4", 620, 300, Color.WHITE);
        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setProjectionMatrix(camera1.camera.combined);
        entityManager.draw(shape);
        debugRenderer.render(world, shape.getProjectionMatrix());
    }

    @Override
    public void unload() {
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
        audioManager.stopSoundEffect("levelsect"); // Stop night sound specifically
    }
}
