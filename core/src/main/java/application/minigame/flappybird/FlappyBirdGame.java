package application.minigame.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import application.minigame.common.GameState;
import application.minigame.common.AbstractMiniGame;
import application.minigame.common.MiniGameUI;
import application.scene.PointsManager;
import application.scene.StartMiniGameHandler;

/**
 * FlappyBirdGame - A mini-game where the player controls a bird flying through pipes.
 * This class handles the core game logic and coordinates other components.
 */
public class FlappyBirdGame extends AbstractMiniGame {
    // Game components
    private Bird bird;
    private PipeManager pipeManager;
    private FlappyBirdPhysics physics;
    private FlappyBirdRenderer renderer;
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

    /**
     * Creates a new FlappyBirdGame
     *
     * @param pointsManager The points manager for scoring
     * @param miniGameHandler The handler that manages mini-games
     */
    public FlappyBirdGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        super(pointsManager, miniGameHandler, 30f); // 30 second time limit

        // Don't initialize components in constructor
        // We'll do that in load() to ensure proper texture loading
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

        // Update pipe positions
        if (pipeManager != null) {
            pipeManager.update(deltaTime);
        }

        // Update physics
        physics.update(deltaTime);

        // Check for collision with pipes
        if (physics.hasCollidedWithPipe()) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;

            // Play collision sound
            if (audioManager != null) {
                audioManager.playSoundEffect("collision");
            }
            return;
        }

        // Update score based on passed pipes
        int newlyPassedPipes = physics.getNewlyPassedPipes();
        if (newlyPassedPipes > 0) {
            // +10 points per pipe
            // Use parent class's setScore instead of directly modifying local score
            setScore(score + (newlyPassedPipes * 10));

            System.out.println("FlappyBird score updated: " + score);

            // Play score sound
            if (audioManager != null) {
                audioManager.playSoundEffect("score");
            }
        }

        // Example snippet to add in your FlappyBirdGame.handlePlayingState() method:
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            state = GameState.CONFIRM_EXIT;
            System.out.println("Quick exit triggered in FlappyBirdGame");
        }

        // Check if game time is up
        if (gameTime >= timeLimit) {
            state = GameState.GAME_OVER;
            gameOverTimer = 0;
        }
    }

    /**
     * Handle jump input
     */
    private void handleJumpInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // Call physics.jump() which should set bird's jump velocity internally before calling flap()
            physics.jump();
            audioManager.playSoundEffect("jump");
        }
        // Optional: allow touch input or other input for jump
    }

    @Override
    protected void drawGame(SpriteBatch batch) {
        if (batch == null || renderer == null) {
            System.err.println("ERROR: Renderer or batch is null in drawGame!");
            return;
        }

        // Always draw the background only when in READY state
        if (state == GameState.READY) {
            renderer.renderBackground(batch);
        } else {
            // For all other states, draw the full game
            renderer.render(batch);
        }
    }

    @Override
    protected MiniGameUI createGameUI() {
        FlappyBirdUI ui = new FlappyBirdUI();

        // Initialize UI with current game state
        ui.setScore(score);
        ui.setTimeLimit(timeLimit);

        System.out.println("Created UI with score: " + score);
        return ui;
    }

    @Override
    public void dispose() {
        if (birdTexture != null) birdTexture.dispose();
        if (pipeTexture != null) pipeTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }

    /**
     * Get the current score
     */
    public int getScore() {
        return score;
    }

    @Override
    public void load() {
        System.out.println("Starting FlappyBird load process...");

        // Initialize game state
        state = GameState.READY;
        gameTime = 0;
        score = 0;
        gameOverTimer = 0;

        // Set the time limit to 60 seconds
        timeLimit = 60f;

        // Load assets if needed
        if (birdTexture == null || pipeTexture == null || backgroundTexture == null) {
            loadAssets();
        }

        // Create game objects with loaded textures
        bird = new Bird(birdTexture);
        pipeManager = new PipeManager(pipeTexture);
        physics = new FlappyBirdPhysics(bird, pipeManager);
        renderer = new FlappyBirdRenderer(backgroundTexture, bird, pipeManager);

        // Create UI and set initial values
        gameUI = (FlappyBirdUI)createGameUI();

        // Double-check gameUI configuration
        if (gameUI instanceof FlappyBirdUI) {
            ((FlappyBirdUI)gameUI).setTimeLimit(timeLimit);
            ((FlappyBirdUI)gameUI).setScore(score);
            System.out.println("FlappyBird UI configured with timeLimit=" + timeLimit + ", score=" + score);
        }

        // Play mini-game music
        if (audioManager != null) {
            audioManager.playSoundEffect("minigame");
        }

        System.out.println("FlappyBird mini-game loaded successfully");
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // Disable the debug hitbox visualization completely
        // This will prevent yellow rectangles from being drawn

        // If you need debug visualization in the future, uncomment this:
        /*
        if (physics != null && shape != null) {
            // Draw hitboxes for debugging
            shape.begin(ShapeRenderer.ShapeType.Line);

            // Draw bird hitbox
            shape.setColor(Color.RED); // Change from yellow to red to be less intrusive
            Rectangle birdBounds = bird.getBounds();
            shape.rect(birdBounds.x, birdBounds.y, birdBounds.width, birdBounds.height);

            // Draw pipe hitboxes
            for (Pipe pipe : pipeManager.getPipes()) {
                Rectangle pipeBounds = pipe.getBounds();
                shape.rect(pipeBounds.x, pipeBounds.y, pipeBounds.width, pipeBounds.height);
            }

            shape.end();
        }
        */
    }

    @Override
    public void unload() {
        // Stop any sounds related to this mini-game
        if (audioManager != null) {
            audioManager.stopSoundEffect("minigame");
        }

        // Clean up any resources
        gameCompleted = false;

        System.out.println("Flappy Bird mini-game unloaded successfully");
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
            ((FlappyBirdUI)gameUI).setScore(finalScore);
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
        // Call the parent update method with the current delta time
        float deltaTime = Gdx.graphics.getDeltaTime();
        super.update(deltaTime);
    }

    protected void reset() {
        // Reset game variables to starting state
        gameTime = 0;
        gameOverTimer = 0;
        score = 0;
        state = GameState.READY;

        // Reset game components
        if (bird != null) bird.reset();
        if (pipeManager != null) pipeManager.reset();
    }
}
