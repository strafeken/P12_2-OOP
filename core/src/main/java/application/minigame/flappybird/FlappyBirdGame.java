package application.minigame.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import abstractengine.utils.DisplayManager;
import application.entity.EntityType;
import application.minigame.common.GameState;
import application.minigame.common.AbstractMiniGame;
import application.minigame.common.MiniGameUI;
import application.scene.PointsManager;
import application.scene.StartMiniGameHandler;
import abstractengine.entity.CollisionDetector;
import abstractengine.entity.CollisionListener;
import abstractengine.entity.Entity;
import application.entity.CollisionType;
import application.minigame.utils.DummyPhysicsBody;

/**
 * FlappyBirdGame - A mini-game where the player controls a bird flying through pipes.
 * This class handles the core game logic and coordinates other components.
 */
public class FlappyBirdGame extends AbstractMiniGame implements CollisionListener {
    // Game components
    private Bird bird;
    private Array<Pipe> pipes;
    private CollisionDetector collisionDetector;
    private FlappyBirdUI gameUI;

    // Game assets
    private Texture backgroundTexture;
    private Texture birdTexture;
    private Texture pipeTexture;

    // Game parameters
    private int score = 0;
    private boolean jumpPressed = false;
    private float gameOverTimer = 0;
    private float timeLimit = 30f;
    private int finalScore = 0;

    // Physics constants
    private static final float GRAVITY = 600.0f;
    private static final float JUMP_VELOCITY = 350.0f;

    // Pipe generation parameters
    private static final float PIPE_SPAWN_INTERVAL = 1.25f; // Reduced from 2.5f for closer pipe spacing
    private static final float PIPE_SPEED = 150.0f;
    private static final float PIPE_WIDTH = 60.0f;
    // Make gap size vary between these two values
    private static final float MIN_PIPE_GAP = 150.0f;
    private static final float MAX_PIPE_GAP = 200.0f;
    private static final float PIPE_MIN_HEIGHT = 60.0f;

    // Pipe spawning timer
    private float timeSinceLastPipe = 0;

    /**
     * Creates a new FlappyBirdGame
     *
     * @param pointsManager The points manager for scoring
     * @param miniGameHandler The handler that manages mini-games
     */
    public FlappyBirdGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        super(pointsManager, miniGameHandler, 30f); // 30 second time limit

        // Initialize collision detector
        collisionDetector = new CollisionDetector();
        collisionDetector.addListener(this);

        // Initialize pipes array
        pipes = new Array<>();
    }

    /**
     * Load game assets
     */
    private void loadAssets() {
        debugAssetDirectories();

        try {
            // Create full file paths with proper directory structure
            String assetDir = "";  // LibGDX automatically looks in the assets folder

            // Load textures with verbose logging
            System.out.println("Attempting to load background texture...");
            backgroundTexture = new Texture(Gdx.files.internal(assetDir + "space_backgroundv2.jpg"));
            System.out.println("Successfully loaded background: " + backgroundTexture.getWidth() + "x" + backgroundTexture.getHeight());

            System.out.println("Attempting to load bird texture...");
            birdTexture = new Texture(Gdx.files.internal(assetDir + "rocket-2.png"));
            System.out.println("Successfully loaded bird: " + birdTexture.getWidth() + "x" + birdTexture.getHeight());

            System.out.println("Attempting to load pipe texture...");
            pipeTexture = new Texture(Gdx.files.internal(assetDir + "barrel-2.png"));
            System.out.println("Successfully loaded pipe: " + pipeTexture.getWidth() + "x" + pipeTexture.getHeight());
        } catch (Exception e) {
            System.err.println("Error loading textures: " + e.getMessage());
            e.printStackTrace();
            createFallbackTextures();
        }
    }

    private void createFallbackTextures() {
        System.out.println("Creating fallback textures with custom colors");

        // Create bird texture (yellow)
        Pixmap birdPixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        birdPixmap.setColor(1f, 1f, 0f, 1);  // Yellow
        birdPixmap.fillCircle(32, 32, 30);   // Circle shape
        birdTexture = new Texture(birdPixmap);
        birdPixmap.dispose();

        // Create pipe texture (green)
        Pixmap pipePixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pipePixmap.setColor(0f, 1f, 0f, 1);  // Green
        pipePixmap.fill();
        pipeTexture = new Texture(pipePixmap);
        pipePixmap.dispose();

        // Create background texture (light blue)
        Pixmap bgPixmap = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(0.5f, 0.7f, 1f, 1);  // Light blue
        bgPixmap.fill();

        // Add some white dots for stars
        bgPixmap.setColor(1f, 1f, 1f, 1);  // White
        for (int i = 0; i < 20; i++) {
            int x = (int)(Math.random() * 128);
            int y = (int)(Math.random() * 128);
            bgPixmap.fillCircle(x, y, 1);
        }

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

    private void verifyTextures() {
        System.out.println("=== TEXTURE VERIFICATION ===");

        // Check bird texture
        if (birdTexture != null) {
            System.out.println("Bird texture: " + birdTexture.getWidth() + "x" + birdTexture.getHeight());

            // Try to check if texture is valid by accessing a pixel (will throw exception if disposed)
            try {
                birdTexture.getTextureData().prepare();
                System.out.println("Bird texture appears to be valid");
            } catch (Exception e) {
                System.err.println("Bird texture might be invalid/disposed: " + e.getMessage());
            }
        } else {
            System.err.println("Bird texture is null!");
        }

        // Check pipe texture
        if (pipeTexture != null) {
            System.out.println("Pipe texture: " + pipeTexture.getWidth() + "x" + pipeTexture.getHeight());

            try {
                pipeTexture.getTextureData().prepare();
                System.out.println("Pipe texture appears to be valid");
            } catch (Exception e) {
                System.err.println("Pipe texture might be invalid/disposed: " + e.getMessage());
            }
        } else {
            System.err.println("Pipe texture is null!");
        }

        // Check background texture
        if (backgroundTexture != null) {
            System.out.println("Background texture: " + backgroundTexture.getWidth() + "x" + backgroundTexture.getHeight());

            try {
                backgroundTexture.getTextureData().prepare();
                System.out.println("Background texture appears to be valid");
            } catch (Exception e) {
                System.err.println("Background texture might be invalid/disposed: " + e.getMessage());
            }
        } else {
            System.err.println("Background texture is null!");
        }

        System.out.println("===========================");
    }

    @Override
    protected void handlePlayingState(float deltaTime) {
        // Update game time
        gameTime += deltaTime;

        // Handle jump input
        handleJumpInput();

        // Update pipe positions and spawn new pipes
        updatePipes(deltaTime);

        // Update bird physics
        updateBirdPhysics(deltaTime);

        // Apply gravity to bird
        applyGravity(deltaTime);

        // Check for collisions
        checkCollisions();

        // Check for key press to exit
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            state = GameState.CONFIRM_EXIT;
            System.out.println("Quick exit triggered in FlappyBirdGame");
        }

        // Check if game time is up
        if (gameTime >= timeLimit) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;
        }

        // Update entity manager
        entityManager.update();
    }

    /**
     * Apply gravity to the bird
     */
    private void applyGravity(float deltaTime) {
        // Apply gravity to bird's velocity
        float currentVelocity = bird.getVelocityY();
        bird.setVelocityY(currentVelocity - GRAVITY * deltaTime);
    }

    /**
     * Update bird position based on physics
     */
    private void updateBirdPhysics(float deltaTime) {
        // Update bird position based on velocity
        bird.update(deltaTime);

        // Check if bird is out of bounds
        Vector2 birdPos = bird.getPosition();
        if (birdPos.y < 0) {
            // Hit the ground
            birdPos.y = 0;
            bird.setPosition(birdPos);

            // Game over if hits ground
            state = GameState.GAME_OVER;
            gameOverTimer = 0;

            // Play collision sound
            if (audioManager != null) {
                audioManager.playSoundEffect("collision");
            }
        } else if (birdPos.y > DisplayManager.getScreenHeight()) {
            // Hit the ceiling
            birdPos.y = DisplayManager.getScreenHeight() - bird.getHeight();
            bird.setPosition(birdPos);
            bird.setVelocityY(0);
        }
    }

    /**
     * Update pipe positions and spawn new pipes
     */
    private void updatePipes(float deltaTime) {
        // Update existing pipes
        for (int i = 0; i < pipes.size; i++) {
            Pipe pipe = pipes.get(i);
            pipe.update(deltaTime, PIPE_SPEED);

            // Remove pipes that are off-screen
            if (pipe.getX() + pipe.getWidth() < 0) {
                entityManager.markForRemoval(pipe);
                pipes.removeIndex(i);
                i--;
            }
        }

        // Spawn new pipes
        timeSinceLastPipe += deltaTime;
        if (timeSinceLastPipe >= PIPE_SPAWN_INTERVAL) {
            spawnPipePair();
            timeSinceLastPipe = 0;
        }
    }

    /**
     * Spawn a new pair of pipes
     */
    private void spawnPipePair() {
        float screenHeight = DisplayManager.getScreenHeight();
        float screenWidth = DisplayManager.getScreenWidth();

        // Generate a random gap height
        float gapHeight = MathUtils.random(MIN_PIPE_GAP, MAX_PIPE_GAP);

        // Calculate random gap position (vertical center point)
        float gapCenter = MathUtils.random(
            screenHeight * 0.3f, // Keep gap at least 30% from the top
            screenHeight * 0.7f  // And at least 30% from the bottom
        );

        // Create top pipe - anchored to top of screen
        float topPipeHeight = gapCenter - gapHeight/2;
        Pipe topPipe = new Pipe(
            pipeTexture,
            screenWidth,
            screenHeight - topPipeHeight/2, // Position y at top of screen minus half pipe height
            PIPE_WIDTH,
            topPipeHeight,
            true
        );

        // Create bottom pipe - anchored to bottom of screen
        float bottomPipeHeight = screenHeight - (gapCenter + gapHeight/2);
        if (bottomPipeHeight < PIPE_MIN_HEIGHT) bottomPipeHeight = PIPE_MIN_HEIGHT;

        Pipe bottomPipe = new Pipe(
            pipeTexture,
            screenWidth,
            bottomPipeHeight/2, // Position at bottom of screen (y=0) plus half pipe height
            PIPE_WIDTH,
            bottomPipeHeight,
            false
        );

        // Add physics bodies to pipes
        topPipe.setPhysicsBody(new DummyPhysicsBody(topPipe));
        bottomPipe.setPhysicsBody(new DummyPhysicsBody(bottomPipe));

        // Add pipes to array and entity manager
        pipes.add(topPipe);
        pipes.add(bottomPipe);
        entityManager.addEntities(topPipe);
        entityManager.addEntities(bottomPipe);
    }

    /**
     * Check for collisions between bird and pipes
     */
    private void checkCollisions() {
        // No need for Rectangle references, we'll check using entity positions and sizes
        Vector2 birdPos = bird.getPosition();
        float birdWidth = bird.getWidth();
        float birdHeight = bird.getHeight();

        // Add a smaller hitbox for more forgiving gameplay
        float margin = Math.min(birdWidth, birdHeight) * 0.2f;

        // Check collision with each pipe
        for (Pipe pipe : pipes) {
            Vector2 pipePos = pipe.getPosition();
            float pipeWidth = pipe.getWidth();
            float pipeHeight = pipe.getHeight();

            // Simple AABB collision check with margin
            boolean collision = !(
                birdPos.x + birdWidth/2 - margin < pipePos.x - pipeWidth/2 ||
                birdPos.x - birdWidth/2 + margin > pipePos.x + pipeWidth/2 ||
                birdPos.y + birdHeight/2 - margin < pipePos.y - pipeHeight/2 ||
                birdPos.y - birdHeight/2 + margin > pipePos.y + pipeHeight/2
            );

            if (collision) {
                // Trigger collision event
                onCollision(bird, pipe, CollisionType.PIPE_PLAYER);
                break;
            }

            // Check for score - bird passes the pipe
            if (!pipe.isPassed() && !pipe.isTop() && birdPos.x > pipePos.x + pipeWidth/2) {
                pipe.setPassed(true);

                // Increase score
                setScore(score + 10);

                System.out.println("FlappyBird score updated: " + score);

                // Play score sound
                if (audioManager != null) {
                    audioManager.playSoundEffect("score");
                }
            }
        }
    }

    /**
     * Handle jump input
     */
    private void handleJumpInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // Set bird's jump velocity
            bird.setVelocityY(JUMP_VELOCITY);
            bird.flap();

            if (audioManager != null) {
                audioManager.playSoundEffect("jump");
            }
        }
    }

    @Override
    protected void drawGame(SpriteBatch batch) {
        // Draw the background texture first
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        // Draw all entities including bird via the entity manager
        entityManager.draw(batch);

        // Draw pipes
        for (Pipe pipe : pipes) {
            pipe.draw(batch);
        }
    }

    @Override
    protected MiniGameUI createGameUI() {
        FlappyBirdUI ui = new FlappyBirdUI();
        ui.setScore(score);
        ui.setTimeLimit(timeLimit);
        return ui;
    }

    @Override
    public void dispose() {
        if (birdTexture != null) birdTexture.dispose();
        if (pipeTexture != null) pipeTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void load() {
        System.out.println("Starting FlappyBird load process...");
        state = GameState.READY;
        gameTime = 0;
        score = 0;
        gameOverTimer = 0;
        timeLimit = 60f;

        // Clear existing pipes
        pipes.clear();
        timeSinceLastPipe = 0;

        if (birdTexture == null || pipeTexture == null || backgroundTexture == null) {
            loadAssets();
        }

        // Create bird with correct size using constructor parameters
        Vector2 birdSize = new Vector2(50f, 50f); // Set desired size
        bird = new Bird(EntityType.PLAYER,
                       "rocket-2.png",
                       birdSize,
                       new Vector2(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f),
                       new Vector2(0, 0),
                       new Vector2(0, 0),
                       300f);

        // Assign physics body to bird
        bird.setPhysicsBody(new DummyPhysicsBody(bird));

        // Register bird with the entity manager
        entityManager.addEntities(bird);

        // Create UI
        gameUI = (FlappyBirdUI)createGameUI();

        System.out.println("FlappyBird mini-game loaded successfully");
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // Debug drawing can be left empty or implemented for visualization
    }

    @Override
    public void unload() {
        // Stop sounds
        if (audioManager != null) {
            audioManager.stopSoundEffect("minigame");
        }

        // Clean up resources
        gameCompleted = false;
        pipes.clear();

        System.out.println("Flappy Bird mini-game unloaded successfully");
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        // Handle bird-pipe collision
        if (type == CollisionType.PIPE_PLAYER) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;

            // Play collision sound
            if (audioManager != null) {
                audioManager.playSoundEffect("collision");
            }

            System.out.println("Bird collided with a pipe!");
        }
    }

    @Override
    protected void handleGameOverState(float deltaTime) {
        // Use a separate timer for game over countdown
        gameOverTimer += deltaTime;

        // Save the final score when first entering game over state
        if (gameOverTimer == 0) {
            finalScore = score;
        }

        // Use the saved final score for display
        if (gameUI != null) {
            gameUI.setScore(finalScore);
        }

        // Wait for a few seconds before auto-returning
        if (gameOverTimer > 3.0f) {
            completeGame(true);
        }

        // Let player return quickly with Enter, Q, or touch
        if (gameOverTimer > 0.5f && (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                              Gdx.input.isKeyJustPressed(Input.Keys.Q) ||
                              Gdx.input.justTouched())) {
            completeGame(true);
        }
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        super.update(deltaTime);
    }

    protected void reset() {
        // Reset game variables
        gameTime = 0;
        gameOverTimer = 0;
        score = 0;
        state = GameState.READY;

        // Reset bird
        if (bird != null) {
            bird.reset();
        }

        // Clear and reset pipes
        pipes.clear();
        timeSinceLastPipe = 0;
    }
}
