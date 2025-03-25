package application.minigame.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import abstractengine.entity.CollisionListener;
import abstractengine.entity.Entity;
import abstractengine.entity.CollisionDetector;
import application.entity.CollisionType;
import application.entity.EntityType;
import application.minigame.common.AbstractMiniGame;
import application.minigame.common.GameState;
import application.minigame.common.MiniGameUI;
import application.scene.PointsManager;
import application.scene.StartMiniGameHandler;

/**
 * AsteroidDodgeGame - A mini-game where the player controls a spaceship to dodge asteroids.
 */
public class AsteroidDodgeGame extends AbstractMiniGame {
    // Game components
    private Ship ship;
    private AsteroidManager asteroidManager;
    private AsteroidDodgePhysics physics;
    private AsteroidDodgeUI gameUI;
    private ShapeRenderer shapeRenderer;

    // Game assets
    private Texture shipTexture;
    private Texture asteroidTexture;
    private Texture backgroundTexture;

    // Game parameters
    private int score = 0;
    private float collisionGracePeriod = 1.5f;
    private Color skyColor = new Color(0.1f, 0.1f, 0.3f, 1);
    private float timeLimit = 30f;
    // Flag to track if using fallback textures
    private boolean usingFallbackTexture = false;

    // Game over timer
    private float gameOverTimer = 0;
    private int finalScore = 0;

    /**
     * Creates a new AsteroidDodgeGame
     *
     * @param pointsManager The points manager for scoring
     * @param miniGameHandler The handler that manages mini-games
     */
    public AsteroidDodgeGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        super(pointsManager, miniGameHandler, 30f); // 30 second time limit

        // Create a basic shape renderer
        this.shapeRenderer = new ShapeRenderer();

        // Note: Don't initialize other components yet
        // We'll do that in load() when the game is actually starting
    }

    /**
     * Load game assets
     */
    private void loadAssets() {
        debugAssetDirectories();
        usingFallbackTexture = false;

        try {
            // Create full file paths with proper directory structure
            String assetDir = "";  // LibGDX automatically looks in the assets folder

            // Load textures with verbose logging
            System.out.println("Attempting to load background texture...");
            backgroundTexture = new Texture(Gdx.files.internal(assetDir + "space_backgroundv2.jpg"));
            System.out.println("Successfully loaded background: " + backgroundTexture.getWidth() + "x" + backgroundTexture.getHeight());

            System.out.println("Attempting to load ship texture...");
            shipTexture = new Texture(Gdx.files.internal(assetDir + "rocket-2.png"));
            System.out.println("Successfully loaded ship: " + shipTexture.getWidth() + "x" + shipTexture.getHeight());

            System.out.println("Attempting to load asteroid texture...");
            asteroidTexture = new Texture(Gdx.files.internal(assetDir + "barrel-2.png"));
            System.out.println("Successfully loaded asteroid: " + asteroidTexture.getWidth() + "x" + asteroidTexture.getHeight());
        } catch (Exception e) {
            System.err.println("Error loading textures: " + e.getMessage());
            e.printStackTrace();
            createFallbackTextures();
        }
    }

    private void createFallbackTextures() {
        System.out.println("Creating fallback textures with custom colors");

        // Properly create and assign fallback textures
        usingFallbackTexture = true;

        // Create ship texture (light blue)
        Pixmap shipPixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        shipPixmap.setColor(0.8f, 0.8f, 0.9f, 1);
        shipPixmap.fillCircle(32, 32, 30);  // Make it a circle for better visibility
        shipTexture = new Texture(shipPixmap);
        shipPixmap.dispose();

        // Create asteroid texture (brown)
        Pixmap asteroidPixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        asteroidPixmap.setColor(0.5f, 0.3f, 0.1f, 1);
        asteroidPixmap.fillCircle(32, 32, 30);
        asteroidTexture = new Texture(asteroidPixmap);
        asteroidPixmap.dispose();

        // Create background texture (dark blue)
        Pixmap bgPixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(0.1f, 0.1f, 0.3f, 1);
        bgPixmap.fill();
        backgroundTexture = new Texture(bgPixmap);
        bgPixmap.dispose();

        System.out.println("Fallback textures created successfully");
    }

    private void debugAssetDirectories() {
        System.out.println("--- Asset Directory Debug ---");
        System.out.println("Working directory: " + Gdx.files.getLocalStoragePath());

        // Check the files in your assets.txt
        String[] fileList = {"space_backgroundv2.jpg", "alien.png", "backBtn.png", "barrel-2.png", "barrel.png"};
        for (String file : fileList) {
            boolean exists = Gdx.files.internal(file).exists();
            System.out.println("Asset '" + file + "' exists: " + exists);
        }
        System.out.println("--- End Debug ---");
    }

    @Override
    protected void handlePlayingState(float deltaTime) {
        // Update game time
        gameTime += deltaTime;

        // Handle player movement
        handlePlayerMovement(deltaTime);

        // Update asteroids spawning - now passing current game time
        asteroidManager.update(deltaTime, gameTime);

        // Update physics and check collisions
        physics.update(deltaTime, gameTime);

        // Check for collision
        if (physics.hasCollided()) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;

            // Play collision sound
            if (audioManager != null) {
                audioManager.playSoundEffect("collision");
            }
        }

        // Update score based on survival time (+5 points every 2 seconds)
        // IMPORTANT CHANGE: Use parent's setScore method instead of local score variable
        int newScore = (int)(gameTime * 2.5f); // This gives +5 points every 2 seconds

        // Update parent class's score
        setScore(newScore);

        // Debug output to track score calculation
        System.out.println("AsteroidDodgeGame.handlePlayingState - Calculated and set score: " + score);

        // Check if game time is up
        if (gameTime >= timeLimit) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;
        }
    }

    @Override
    protected void handleGameOverState(float deltaTime) {
        // Use a separate timer for game over countdown
        gameOverTimer += deltaTime;

        // Save the final score when first entering game over state
        if (gameOverTimer == 0) {
            finalScore = score;
            System.out.println("Saving final score: " + finalScore);
        }

        // Use the saved final score for display
        if (gameUI != null) {
            ((AsteroidDodgeUI)gameUI).setScore(finalScore);
        }

        // Wait for a few seconds before auto-returning
        if (gameOverTimer > 3.0f) {
            completeGame(true);
        }

        // Let player return quickly with a key press - REMOVED SPACE key
        if (gameOverTimer > 0.5f && (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                                  Gdx.input.isKeyJustPressed(Input.Keys.Q) ||
                                  Gdx.input.justTouched())) {
            completeGame(true);
        }
    }

    /**
     * Handle player movement based on input
     */
    private void handlePlayerMovement(float deltaTime) {
        float speed = 150f; // reduced from 300f
        boolean moveUp = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean moveDown = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);

        // Apply movement based on pressed keys
        float dx = 0, dy = 0;

        if (moveLeft) dx -= speed * deltaTime;
        if (moveRight) dx += speed * deltaTime;
        if (moveUp) dy += speed * deltaTime;
        if (moveDown) dy -= speed * deltaTime;

        // Update ship position
        ship.move(dx, dy);

        // Check for exit with Q key
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            state = GameState.CONFIRM_EXIT;
        }
    }

    @Override
    protected void drawGame(SpriteBatch batch) {
        // Draw the background texture first if available
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        // Delegate drawing of dynamic entities to IEntityManager
        entityManager.draw(batch);
    }

    @Override
    protected MiniGameUI createGameUI() {
        return new AsteroidDodgeUI();
    }

    @Override
    public void dispose() {
        if (shipTexture != null) shipTexture.dispose();
        if (asteroidTexture != null) asteroidTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

    }

    /**
     * Get the current score
     */
    public int getScore() {
        return score;
    }

    @Override
    public void load() {
        System.out.println("Starting Asteroid Dodge load process...");

        // Initialize game state
        state = GameState.READY;
        gameTime = 0;
        score = 0;
        timeLimit = 60f;

        loadAssets();

        // Use proper constructor with explicit size
        Vector2 shipSize = new Vector2(40f, 40f); // Set desired size
        ship = new Ship(EntityType.PLAYER,
                       "rocket-2.png",
                       shipSize,
                       new Vector2(Gdx.graphics.getWidth() / 2f, 100f),
                       new Vector2(0, 0),
                       new Vector2(0, 0),
                       300f);

        // Assign a dummy physics body using the ship as the entity.
        ship.setPhysicsBody(new application.minigame.utils.DummyPhysicsBody(ship));

        entityManager.addEntities(ship);

        asteroidManager = new AsteroidManager(
            entityManager,
            asteroidTexture,
            30f,   // min asteroid size
            80f,   // max asteroid size
            1.5f,  // min spawn interval (seconds)
            3.0f   // max spawn interval (seconds)
        );
        physics = new AsteroidDodgePhysics(ship, asteroidManager, collisionGracePeriod);
        Color skyColor = new Color(0.1f, 0.1f, 0.3f, 1);
        shapeRenderer = new ShapeRenderer();
        gameUI = (AsteroidDodgeUI) createGameUI();

        if (gameUI instanceof AsteroidDodgeUI) {
            ((AsteroidDodgeUI) gameUI).setTimeLimit(timeLimit);
        }






        // Create and register the collision detector
        CollisionDetector collisionDetector = new CollisionDetector();

        collisionDetector.addListener(new CollisionListener() {
            @Override
            public void onCollision(Entity a, Entity b, CollisionType type) {
                if (type == CollisionType.ALIEN_PLAYER) {
                    state = GameState.GAME_OVER;
                    gameOverTimer = 0;
                    System.out.println("Ship hit an asteroid!");
                }
            }
        });

        System.out.println("Asteroid Dodge mini-game loaded successfully");
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // Delegate debug drawing to IEntityManager (if supported)
        // For now, remove renderer debug calls.
        // entityManager.draw(shape);
    }

    @Override
    public void unload() {
        // Stop any sounds related to this mini-game
        if (audioManager != null) {
            audioManager.stopSoundEffect("minigame");
        }

        // Clean up any resources
        gameCompleted = false;

        System.out.println("Asteroid Dodge mini-game unloaded successfully");
    }


    protected void reset() {
        // Reset game variables to starting state
        gameTime = 0;
        gameOverTimer = 0;
        score = 0;
        state = GameState.READY;

        // Reset game components
        // Updated ship reset call with new position values (centered horizontally, near bottom)
        ship.reset(Gdx.graphics.getWidth() / 2f, 100f);
        asteroidManager.reset();
        physics.reset();
    }

    @Override
    public void update() {
        // FIXED VERSION - don't call update(deltaTime) which creates recursion
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Call the parent method directly instead
        super.update(deltaTime);

        // Adding debug to verify the score
        if (state == GameState.PLAYING) {
            System.out.println("AsteroidDodgeGame.update() - Final score: " + score);
        }
    }
}
