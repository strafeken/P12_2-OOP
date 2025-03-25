package application.minigame.asteroids;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
import abstractengine.utils.DisplayManager;
import application.minigame.utils.DummyPhysicsBody;

/**
 * AsteroidDodgeGame - A mini-game where the player controls a spaceship to dodge asteroids.
 */
public class AsteroidDodgeGame extends AbstractMiniGame implements CollisionListener {
    // Game components
    private Ship ship;
    private AsteroidDodgeUI gameUI;
    private ShapeRenderer shapeRenderer;
    private CollisionDetector collisionDetector;

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

    // Collision state
    private boolean collisionEnabled = false;
    private float gracePeriodTimer = 0f;

    // Asteroid management constants (from AsteroidManager)
    private static final float INITIAL_SPAWN_INTERVAL = 1.5f;
    private static final float MIN_SPAWN_INTERVAL = 0.5f;
    private static final float MIN_ASTEROID_SPEED = 50f;
    private static final float MAX_ASTEROID_SPEED = 100f;
    private static final float BASE_ASTEROID_SIZE = 40f;
    private static final float SPEED_INCREASE_INTERVAL = 9.0f;
    private static final float ASTEROID_COUNT_INCREASE_INTERVAL = 11.0f;
    private static final int MAX_SIMULTANEOUS_ASTEROIDS = 6;
    private static final float SPEED_INCREASE_FACTOR = 0.1f;

    // Asteroid management fields (from AsteroidManager)
    private Array<Asteroid> asteroids;
    private float timeSinceLastAsteroid;
    private float spawnInterval;
    private float asteroidSizeMultiplier = 1.0f;
    private float minAsteroidSize;
    private float maxAsteroidSize;
    private float minSpawnInterval;
    private float maxSpawnInterval;
    private int speedLevel = 1;
    private int asteroidCountLevel = 1;
    private int simultaneousAsteroidsCount = 1;
    private float speedMultiplier = 1.0f;

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

        // Initialize collision detector
        this.collisionDetector = new CollisionDetector();
        this.collisionDetector.addListener(this);
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

    /**
     * Update asteroid positions and generate new asteroids as needed
     *
     * @param deltaTime Time since last update
     * @param currentGameTime Current game time
     */
    private void updateAsteroids(float deltaTime, float currentGameTime) {
        // Check if speed should increase (every 9 seconds)
        int newSpeedLevel = 1 + (int)(currentGameTime / SPEED_INCREASE_INTERVAL);
        if (newSpeedLevel > speedLevel) {
            increaseAsteroidSpeed();
            speedLevel = newSpeedLevel;
        }

        // Check if asteroid count should increase (every 11 seconds)
        int newAsteroidCountLevel = 1 + (int)(currentGameTime / ASTEROID_COUNT_INCREASE_INTERVAL);
        if (newAsteroidCountLevel > asteroidCountLevel) {
            increaseAsteroidCount();
            asteroidCountLevel = newAsteroidCountLevel;
        }

        // Update all existing asteroids
        for (int i = 0; i < asteroids.size; i++) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update(deltaTime);

            // Remove asteroids that are off screen
            Vector2 position = asteroid.getPosition();
            Vector2 size = asteroid.getSize();

            // Add debug output to see when asteroids are removed
            System.out.println("Asteroid position: " + position.y + ", screen height: " + Gdx.graphics.getHeight());

            // Only remove when completely below the bottom of the screen
            if (position.y + size.y < -50) { // Give some buffer below the screen
                System.out.println("Removing asteroid at y=" + position.y);
                entityManager.markForRemoval(asteroid);
                asteroids.removeIndex(i);
                i--;
            }
        }

        // Spawn new asteroids as needed
        timeSinceLastAsteroid += deltaTime;
        if (timeSinceLastAsteroid > spawnInterval) {
            // Spawn multiple asteroids based on current difficulty
            for (int i = 0; i < simultaneousAsteroidsCount; i++) {
                spawnAsteroid();
            }
            timeSinceLastAsteroid = 0;
            spawnInterval = MathUtils.random(minSpawnInterval, maxSpawnInterval);
        }
    }

    /**
     * Increase asteroid speed
     */
    private void increaseAsteroidSpeed() {
        speedMultiplier += SPEED_INCREASE_FACTOR;
        System.out.println("Speed increased to level " + speedLevel +
                          ": multiplier = " + speedMultiplier);
    }

    /**
     * Increase number of simultaneous asteroids
     */
    private void increaseAsteroidCount() {
        // Increase number of simultaneous asteroids (max 6)
        if (simultaneousAsteroidsCount < MAX_SIMULTANEOUS_ASTEROIDS) {
            simultaneousAsteroidsCount++;
            System.out.println("Asteroid count increased to " + simultaneousAsteroidsCount);
        }
    }

    /**
     * Generate a new asteroid
     */
    private void spawnAsteroid() {
        float screenWidth = DisplayManager.getScreenWidth();
        float screenHeight = DisplayManager.getScreenHeight();

        // Random position at top of screen with some spacing for multiple asteroids
        float asteroidSize = MathUtils.random(minAsteroidSize, maxAsteroidSize);
        float x = MathUtils.random(0, screenWidth - asteroidSize);
        float y = screenHeight + asteroidSize/2; // Start slightly above top of screen

        // Base speed with current difficulty multiplier applied
        float baseSpeed = MathUtils.random(MIN_ASTEROID_SPEED, MAX_ASTEROID_SPEED);
        float speed = baseSpeed * speedMultiplier;

        System.out.println("Spawning asteroid at x=" + x + ", y=" + y +
                          " with size=" + asteroidSize + ", speed=" + speed);

        // Create asteroid with explicit texture
        Asteroid asteroid = new Asteroid(asteroidTexture, x, y, asteroidSize, asteroidSize, speed);

        // Add to our list
        asteroids.add(asteroid);

        // Register with entity manager
        entityManager.addEntities(asteroid);

        // Ensure the physics body is set for collision detection
        asteroid.setPhysicsBody(new DummyPhysicsBody(asteroid));
    }

    /**
     * Reset all asteroids and difficulty settings
     */
    private void resetAsteroids() {
        asteroids.clear();
        timeSinceLastAsteroid = 0;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        speedLevel = 1;
        asteroidCountLevel = 1;
        simultaneousAsteroidsCount = 1;
        speedMultiplier = 1.0f;
    }

    /**
     * Get all asteroids
     */
    public Array<Asteroid> getAsteroids() {
        return asteroids;
    }

    @Override
    protected void handlePlayingState(float deltaTime) {
        // Update game time
        gameTime += deltaTime;

        // Handle player movement
        handlePlayerMovement(deltaTime);

        // Update asteroids spawning
        updateAsteroids(deltaTime, gameTime);

        // Entity manager update should come after all our manual updates
        entityManager.update();

        // Update grace period timer
        if (!collisionEnabled) {
            gracePeriodTimer += deltaTime;
            if (gracePeriodTimer >= collisionGracePeriod) {
                collisionEnabled = true;
                System.out.println("Collision detection enabled after grace period");
            }
        }

        // Process collisions through the physics system
        processCollisions();

        // Update score based on survival time
        int newScore = (int)(gameTime * 2.5f);
        setScore(newScore);

        // Check if game time is up
        if (gameTime >= timeLimit) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;
        }
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        // Skip collision handling during the grace period
        if (!collisionEnabled) return;

        if (type == CollisionType.ASTEROID_PLAYER) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;

            // Play collision sound
            if (audioManager != null) {
                audioManager.playSoundEffect("collision");
            }

            System.out.println("Ship hit an asteroid!");
        }
    }

    /**
     * Process any potential collisions between entities
     * This uses the abstractengine physics system rather than direct Rectangle checking
     */
    private void processCollisions() {
        if (!collisionEnabled) return;

        // Loop through all asteroids
        for (Asteroid asteroid : asteroids) {
            // Check if the asteroid's physics body is intersecting with the ship's physics body
            if (isEntitiesOverlapping(ship, asteroid)) {
                // If overlapping, manually trigger the collision event
                onCollision(ship, asteroid, CollisionType.ASTEROID_PLAYER);
                // Break after first collision for better performance
                break;
            }
        }
    }

    /**
     * Check if two entities are overlapping based on their positions and sizes
     */
    private boolean isEntitiesOverlapping(Entity a, Entity b) {
        // We know these are Ship and Asteroid objects which provide getPosition and getSize

        // Get position and size for entity A (Ship)
        Vector2 posA = ((Ship)a).getPosition();
        float widthA = ((Ship)a).getWidth();
        float heightA = ((Ship)a).getHeight();

        // Get position and size for entity B (Asteroid)
        Vector2 posB = ((Asteroid)b).getPosition();
        float widthB = ((Asteroid)b).getWidth();
        float heightB = ((Asteroid)b).getHeight();

        // Apply collision margin for more forgiving collisions
        float margin = Math.min(widthA, heightA) * 0.2f;

        // Check for overlap (simplified AABB collision)
        return !(posA.x + widthA/2 - margin < posB.x - widthB/2 ||
                 posA.x - widthA/2 + margin > posB.x + widthB/2 ||
                 posA.y + heightA/2 - margin < posB.y - heightB/2 ||
                 posA.y - heightA/2 + margin > posB.y + heightB/2);
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
        if (asteroidTexture != null) asteroidTexture.dispose(); // Fixed - was disposing shipTexture twice
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

        // Initialize collision state
        collisionEnabled = false;
        gracePeriodTimer = 0f;

        // Initialize asteroid management parameters
        minAsteroidSize = 30f;
        maxAsteroidSize = 80f;
        minSpawnInterval = 1.5f;
        maxSpawnInterval = 3.0f;
        asteroids = new Array<>();
        timeSinceLastAsteroid = 0;
        spawnInterval = INITIAL_SPAWN_INTERVAL;

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
        ship.setPhysicsBody(new DummyPhysicsBody(ship));

        entityManager.addEntities(ship);

        Color skyColor = new Color(0.1f, 0.1f, 0.3f, 1);
        shapeRenderer = new ShapeRenderer();
        gameUI = (AsteroidDodgeUI) createGameUI();

        if (gameUI instanceof AsteroidDodgeUI) {
            ((AsteroidDodgeUI) gameUI).setTimeLimit(timeLimit);
        }

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
        collisionEnabled = false;
        gracePeriodTimer = 0f;

        // Reset game components
        ship.reset(Gdx.graphics.getWidth() / 2f, 100f);
        resetAsteroids();
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Call the parent method directly instead
        super.update(deltaTime);

        // Adding debug to verify the score
        if (state == GameState.PLAYING) {
            System.out.println("AsteroidDodgeGame.update() - Final score: " + score);
        }
    }
}
