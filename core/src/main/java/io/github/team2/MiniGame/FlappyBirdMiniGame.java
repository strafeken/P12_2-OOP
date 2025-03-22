package io.github.team2.MiniGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.CollisionExtensions.StartMiniGameHandler;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.GameOverScreen;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.TextManager;
import io.github.team2.Utils.DisplayManager;

/**
 * Implementation of a Flappy Bird style mini-game.
 * Following Single Responsibility Principle, this class focuses on 
 * mini-game mechanics and rendering.
 */
public class FlappyBirdMiniGame extends Scene {
    // Game state management
    private enum GameState { READY, PLAYING, GAME_OVER, CONFIRM_EXIT }
    private GameState state;

    // Game objects
    private Texture birdTexture;
    private Texture pipeTopTexture;
    private Texture pipeBottomTexture;
    private Rectangle bird;
    private Array<Rectangle> topPipes;
    private Array<Rectangle> bottomPipes;

    // Physics parameters
    private float gravity = 600f;
    private float jumpVelocity = -400f;
    private float velocity = 0;
    private float pipeSpeed = 200f;
    private float pipeWidth = 40f;
    private float pipeHeight = 300f;
    private float timeSinceLastPipe = 0;
    private float pipeInterval = 1.7f;
    
    // Collision settings
    private float collisionTolerance = 0.8f; // Reduce hitbox size by 20% for more forgiving collisions
    private boolean debugCollision = false;  // For debugging collision boundaries

    // Scoring
    private int score;
    private Array<Integer> passedPipes;

    // Game duration settings
    private float gameTime = 0;
    private float maxGameTime = 30f; // 30 seconds max
    private boolean gameCompleted = false;
    private float gameOverDelay = 2.0f; // Delay before returning to game scene
    private float currentDelay = 0f;

    // Dependencies
    private PointsManager pointsManager;
    private IAudioManager audioManager;
    private ISceneManager sceneManager;
    private StartMiniGameHandler miniGameHandler;
    private ShapeRenderer localShapeRenderer;

    // UI elements
    private Color skyColor = new Color(0.4f, 0.7f, 1f, 1);
    private boolean confirmYes = true; // Default selection in confirmation dialog
    private Color selectedColor = Color.YELLOW;
    private Color unselectedColor = Color.WHITE;

    /**
     * Creates a new FlappyBirdMiniGame
     * @param pointsManager The points manager for scoring
     * @param miniGameHandler The handler for mini-game completion
     */
    public FlappyBirdMiniGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        super();
        this.pointsManager = pointsManager;
        this.miniGameHandler = miniGameHandler;
        this.state = GameState.READY;
        this.score = 0;

        // Initialize managers and collections
        initializeCore();
    }
    
    /**
     * Initialize core components and collections
     */
    private void initializeCore() {
        this.entityManager = new EntityManager();
        this.gameInputManager = new GameInputManager();
        this.textManager = new TextManager();
        this.audioManager = AudioManager.getInstance();
        this.sceneManager = SceneManager.getInstance();
        this.localShapeRenderer = null; // Initialize in load() when GL context is ready
        
        this.topPipes = new Array<>();
        this.bottomPipes = new Array<>();
        this.passedPipes = new Array<>();
    }

    @Override
    public void load() {
        // Initialize ShapeRenderer when GL context is ready
        if (localShapeRenderer == null) {
            localShapeRenderer = new ShapeRenderer();
        }
        
        loadTextures();
        initializeGameObjects();
        resetGameState();
        playStartSound();
    }
    
    /**
     * Load required textures with error handling
     */
    private void loadTextures() {
        try {
            birdTexture = new Texture(Gdx.files.internal("rocket-2.png"));
            pipeTopTexture = new Texture(Gdx.files.internal("barrel.png"));
            pipeBottomTexture = new Texture(Gdx.files.internal("barrel.png"));
            System.out.println("Loaded textures successfully");
        } catch (Exception e) {
            System.err.println("Error loading textures: " + e.getMessage());
            e.printStackTrace();
            
            // Create fallbacks if needed
            if (birdTexture == null) createPlaceholderTexture("birdTexture");
            if (pipeTopTexture == null) createPlaceholderTexture("pipeTopTexture");
            if (pipeBottomTexture == null) createPlaceholderTexture("pipeBottomTexture");
        }
    }
    
    /**
     * Initialize game objects like the bird
     */
    private void initializeGameObjects() {
        bird = new Rectangle();
        bird.width = 32;  // Match visible size of texture
        bird.height = 26;
        bird.x = DisplayManager.getScreenWidth() / 4;
        bird.y = DisplayManager.getScreenHeight() / 2;
    }
    
    /**
     * Reset game state for a new game
     */
    private void resetGameState() {
        velocity = 0;
        score = 0;
        gameTime = 0;
        gameCompleted = false;
        currentDelay = 0f;
        topPipes.clear();
        bottomPipes.clear();
        passedPipes.clear();
        state = GameState.READY;
    }
    
    /**
     * Play the start sound
     */
    private void playStartSound() {
        try {
            audioManager.playSoundEffect("minigame");
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
    
    /**
     * Creates a placeholder texture when loading fails
     * @param name Identifier for which texture to create
     */
    private void createPlaceholderTexture(String name) {
        System.out.println("Creating placeholder texture for: " + name);
        try {
            // Create a 1x1 white pixel texture as fallback
            com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            
            if (name.equals("birdTexture")) {
                birdTexture = new Texture(pixmap);
            } else if (name.equals("pipeTopTexture")) {
                pipeTopTexture = new Texture(pixmap);
            } else if (name.equals("pipeBottomTexture")) {
                pipeBottomTexture = new Texture(pixmap);
            }
            
            pixmap.dispose();
        } catch (Exception e) {
            System.err.println("Failed to create placeholder texture: " + e.getMessage());
        }
    }

    /**
     * Spawns a new pipe at the right edge of the screen
     */
    private void spawnPipe() {
        // Generate random gap size for this pipe
        float randomGap = MathUtils.random(100f, 200f);
        float centerPipe = MathUtils.random(
            DisplayManager.getScreenHeight() * 0.3f,
            DisplayManager.getScreenHeight() * 0.7f
        );

        // Create pipe rectangles
        Rectangle topPipe = new Rectangle();
        topPipe.x = DisplayManager.getScreenWidth();
        topPipe.y = centerPipe + randomGap/2;
        topPipe.width = pipeWidth;
        topPipe.height = pipeHeight;

        Rectangle bottomPipe = new Rectangle();
        bottomPipe.x = DisplayManager.getScreenWidth();
        bottomPipe.y = centerPipe - randomGap/2 - pipeHeight;
        bottomPipe.width = pipeWidth;
        bottomPipe.height = pipeHeight;

        topPipes.add(topPipe);
        bottomPipes.add(bottomPipe);
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        
        // Cap deltaTime to prevent physics glitches if framerate drops significantly
        deltaTime = Math.min(deltaTime, 0.05f);
        
        gameTime += deltaTime;

        // Check for game completion based on time
        if (gameTime >= maxGameTime && state == GameState.PLAYING) {
            completeGame(true);
            return;
        }

        // Update based on current state
        switch (state) {
            case READY:
                handleReadyState();
                break;
                
            case PLAYING:
                handlePlayingState(deltaTime);
                break;

            case GAME_OVER:
                handleGameOverState(deltaTime);
                break;

            case CONFIRM_EXIT:
                handleConfirmExitState();
                break;
        }
    }
    
    /**
     * Handles the READY state logic
     */
    private void handleReadyState() {
        // Press space to start
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            state = GameState.PLAYING;
        }
    }
    
    /**
     * Handles the PLAYING state logic
     * @param deltaTime Time since last update
     */
    private void handlePlayingState(float deltaTime) {
        // Quick exit with Q key
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            state = GameState.CONFIRM_EXIT;
            return;
        }

        // Jump mechanic
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velocity = jumpVelocity;
            playJumpSound();
        }

        // Apply gravity
        velocity += gravity * deltaTime;

        // Update bird position
        bird.y -= velocity * deltaTime;

        // Keep bird on screen
        constrainBirdPosition();

        // Spawn new pipes
        timeSinceLastPipe += deltaTime;
        if (timeSinceLastPipe > pipeInterval) {
            spawnPipe();
            timeSinceLastPipe = 0;
        }

        // Move pipes and check collision
        updatePipesAndCollisions(deltaTime);
    }
    
    /**
     * Constrains the bird to stay within screen bounds
     */
    private void constrainBirdPosition() {
        if (bird.y < 0) {
            bird.y = 0;
            velocity = 0;
        }
        if (bird.y > DisplayManager.getScreenHeight() - bird.height) {
            bird.y = DisplayManager.getScreenHeight() - bird.height;
            velocity = 0;
        }
    }
    
    /**
     * Plays the jump sound with error handling
     */
    private void playJumpSound() {
        try {
            audioManager.playSoundEffect("jump");
        } catch (Exception e) {
            // Silently handle sound errors
        }
    }
    
    /**
     * Handles the GAME_OVER state logic
     * @param deltaTime Time since last update
     */
    private void handleGameOverState(float deltaTime) {
        // Wait for delay, then return to game
        currentDelay += deltaTime;

        if (currentDelay >= gameOverDelay) {
            // Use R key instead of Q to return immediately to avoid conflict
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                completeGame(false);
            }
            // Auto-return to game after delay
            else if (currentDelay >= gameOverDelay * 2) {
                completeGame(false);
            }
        }
    }
    
    /**
     * Handles the CONFIRM_EXIT state logic
     */
    private void handleConfirmExitState() {
        // Handle confirmation dialog navigation (up/down or W/S keys)
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            confirmYes = true;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            confirmYes = false;
        }

        // Handle confirmation selection (Enter or Space)
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (confirmYes) {
                // Player confirmed exit - take penalty and exit
                completeGameWithPenalty();
            } else {
                // Player cancelled - return to game
                state = GameState.PLAYING;
            }
        }

        // Allow escape to cancel
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Updates pipe positions and checks for collisions
     * @param deltaTime Time since last update
     */
    private void updatePipesAndCollisions(float deltaTime) {
        for (int i = 0; i < topPipes.size; i++) {
            try {
                Rectangle topPipe = topPipes.get(i);
                Rectangle bottomPipe = bottomPipes.get(i);

                // Move pipes
                topPipe.x -= pipeSpeed * deltaTime;
                bottomPipe.x -= pipeSpeed * deltaTime;

                // Check for scoring - bird has passed pipe
                checkForScoring(i, topPipe);

                // Check for collisions
                checkForCollisions(topPipe, bottomPipe);
            } catch (Exception e) {
                System.err.println("Error in collision detection: " + e.getMessage());
            }
        }

        // Remove pipes that are off screen
        removeOffscreenPipes();
    }
    
    /**
     * Checks if the bird has passed a pipe and awards score
     * @param pipeIndex Index of the pipe
     * @param pipe The pipe to check
     */
    private void checkForScoring(int pipeIndex, Rectangle pipe) {
        if (bird.x > pipe.x + pipe.width && !passedPipes.contains(pipeIndex, false)) {
            passedPipes.add(pipeIndex);
            score++;
            try {
                audioManager.playSoundEffect("ding");
            } catch (Exception e) {
                // Silent handling for sound errors
            }
        }
    }
    
    /**
     * Checks if the bird has collided with pipes
     * @param topPipe Top pipe to check
     * @param bottomPipe Bottom pipe to check
     */
    private void checkForCollisions(Rectangle topPipe, Rectangle bottomPipe) {
        // Create reduced hitboxes for more forgiving gameplay
        float reducedBirdWidth = bird.width * collisionTolerance;
        float reducedBirdHeight = bird.height * collisionTolerance;
        float birdCenterX = bird.x + bird.width/2;
        float birdCenterY = bird.y + bird.height/2;
        
        // Create bird collision box centered on the bird
        Rectangle birdCollision = new Rectangle(
            birdCenterX - reducedBirdWidth/2,
            birdCenterY - reducedBirdHeight/2,
            reducedBirdWidth,
            reducedBirdHeight
        );
        
        // Check collisions
        boolean topCollision = birdCollision.overlaps(topPipe);
        boolean bottomCollision = birdCollision.overlaps(bottomPipe);

        // If collision detected
        if (topCollision || bottomCollision) {
            if (debugCollision) {
                System.out.println("Collision detected with pipe");
            }

            state = GameState.GAME_OVER;
            try {
                audioManager.playSoundEffect("hit");
            } catch (Exception e) {
                // Silent handling for sound errors
            }
            currentDelay = 0;
        }
    }
    
    /**
     * Removes pipes that have moved off the left edge of the screen
     */
    private void removeOffscreenPipes() {
        for (int i = 0; i < topPipes.size; i++) {
            if (topPipes.get(i).x < -pipeWidth) {
                topPipes.removeIndex(i);
                bottomPipes.removeIndex(i);

                // Update passed pipes indices
                updatePassedPipesIndices(i);

                // Decrement index because we removed an item
                i--;
            }
        }
    }
    
    /**
     * Updates passed pipes indices after a pipe is removed
     * @param removedIndex Index of the removed pipe
     */
    private void updatePassedPipesIndices(int removedIndex) {
        // Update passed pipes indices
        for (int j = 0; j < passedPipes.size; j++) {
            if (passedPipes.get(j) > removedIndex) {
                passedPipes.set(j, passedPipes.get(j) - 1);
            }
        }

        // Remove from passedPipes if needed
        for (int j = 0; j < passedPipes.size; j++) {
            if (passedPipes.get(j) == removedIndex) {
                passedPipes.removeIndex(j);
                break;
            }
        }
    }

    /**
     * Completes the mini-game, awards points, and returns to main game
     * @param success Whether the player completed successfully
     */
    private void completeGame(boolean success) {
        gameCompleted = true;

        // Add the minigame score to the main game score
        pointsManager.addPoints(score * 10);
        System.out.println("Mini-game completed! Added " + (score * 10) + " points from mini-game score.");

        // Penalize player if they died early
        applyEarlyDeathPenalty(success);

        // Stop mini-game music
        try {
            audioManager.stopSoundEffect("minigame");
        } catch (Exception e) {
            // Silently handle sound errors
        }

        // Return to main game
        returnToMainGame();

        // Notify handler that mini-game is completed
        if (miniGameHandler != null) {
            try {
                miniGameHandler.onMiniGameCompleted();
            } catch (Exception e) {
                System.err.println("Error notifying mini-game handler: " + e.getMessage());
            }
        }
    }
    
    /**
     * Apply penalty if player died early in the game
     * @param success Whether the player completed successfully
     */
    private void applyEarlyDeathPenalty(boolean success) {
        if (!success && gameTime < 15.0f) {
            // Only reduce health if player has more than 1 life remaining
            PlayerStatus playerStatus = PlayerStatus.getInstance();
            if (playerStatus != null && playerStatus.getLives() > 1) {
                playerStatus.decrementLife();
                System.out.println("Died early in mini-game (before 15 seconds). Lost 1 life!");
            } else {
                System.out.println("Died early in mini-game, but only 1 life remaining. No life penalty applied.");
            }
        }
    }
    
    /**
     * Returns to the main game by removing the mini-game overlay
     */
    private void returnToMainGame() {
        PlayerStatus.getInstance().setInMiniGame(false);
        
        try {
            sceneManager.removeOverlay();
        } catch (Exception e) {
            System.err.println("Error removing scene overlay: " + e.getMessage());
            // Fallback - try to set next scene to the current scene
            try {
                sceneManager.setNextScene(sceneManager.getCurrentSceneID());
            } catch (Exception ex) {
                System.err.println("Critical error returning to main game: " + ex.getMessage());
            }
        }
    }

    /**
     * Completes the game with a life penalty for early exit
     */
    private void completeGameWithPenalty() {
        gameCompleted = true;

        // Apply penalty
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        if (playerStatus != null && playerStatus.getLives() > 1) {
            // Normal case - player has more than 1 life, just decrement
            playerStatus.decrementLife();
            System.out.println("Player skipped mini-game. Lost 1 life as penalty!");

            // Stop mini-game music
            try {
                audioManager.stopSoundEffect("minigame");
            } catch (Exception e) {
                // Silently handle sound errors
            }

            // Return to main game
            playerStatus.setInMiniGame(false);
            try {
                sceneManager.removeOverlay();
            } catch (Exception e) {
                System.err.println("Error removing overlay: " + e.getMessage());
            }
        } else {
            // Player has only 1 life left - reduce to 0 and trigger game over
            if (playerStatus != null) {
                playerStatus.decrementLife(); // This will set lives to 0
            }
            System.out.println("Player skipped mini-game with only 1 life remaining. Game over!");

            // Stop mini-game music
            try {
                audioManager.stopSoundEffect("minigame");
            } catch (Exception e) {
                // Silently handle sound errors
            }

            // Player is no longer in mini-game
            if (playerStatus != null) {
                playerStatus.setInMiniGame(false);
            }

            // Create and set up game over screen with final score
            GameOverScreen gameOverScreen = new GameOverScreen();
            gameOverScreen.setFinalScore(pointsManager.getPoints());
            try {
                sceneManager.setNextScene(SceneID.GAME_OVER);
            } catch (Exception e) {
                System.err.println("Error transitioning to game over screen: " + e.getMessage());
            }
        }

        // Notify handler that mini-game is completed
        if (miniGameHandler != null) {
            try {
                miniGameHandler.onMiniGameCompleted();
            } catch (Exception e) {
                System.err.println("Error notifying mini-game handler: " + e.getMessage());
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (batch == null) {
            System.err.println("SpriteBatch is null in draw method");
            return;
        }
        
        try {
            // End the batch before drawing shapes
            batch.end();

            // Draw background
            drawBackground();

            // Restart the batch for sprites
            batch.begin();

            // Draw game elements
            drawPipes(batch);
            drawBird(batch);
            drawUI(batch);
        } catch (Exception e) {
            System.err.println("Error in draw method: " + e.getMessage());
            e.printStackTrace();
            
            // Try to recover by restarting the batch
            try {
                if (!batch.isDrawing()) {
                    batch.begin();
                }
            } catch (Exception ex) {
                // Ignore any further exceptions
            }
        }
    }
    
    /**
     * Draws the game background
     */
    private void drawBackground() {
        // Initialize ShapeRenderer if needed
        if (localShapeRenderer == null) {
            localShapeRenderer = new ShapeRenderer();
        }

        // Draw sky background
        localShapeRenderer.begin(ShapeType.Filled);
        localShapeRenderer.setColor(skyColor);
        localShapeRenderer.rect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
        localShapeRenderer.end();
    }
    
    /**
     * Draws the pipes
     * @param batch SpriteBatch to draw with
     */
    private void drawPipes(SpriteBatch batch) {
        if (pipeTopTexture != null && pipeBottomTexture != null) {
            for (int i = 0; i < topPipes.size; i++) {
                Rectangle topPipe = topPipes.get(i);
                batch.draw(pipeTopTexture,
                          topPipe.x - pipeWidth/2, // Center the visual pipe on collision box
                          topPipe.y,
                          pipeWidth * 2,     // Double visual width
                          pipeHeight);       // Keep height

                Rectangle bottomPipe = bottomPipes.get(i);
                batch.draw(pipeBottomTexture,
                          bottomPipe.x - pipeWidth/2, // Center the visual pipe on collision box
                          bottomPipe.y,
                          pipeWidth * 2,     // Double visual width
                          pipeHeight);       // Keep height
            }
        }
    }
    
    /**
     * Draws the bird
     * @param batch SpriteBatch to draw with
     */
    private void drawBird(SpriteBatch batch) {
        if (birdTexture != null && bird != null) {
            batch.draw(birdTexture, bird.x, bird.y, bird.width, bird.height);
        }
    }
    
    /**
     * Draws the UI based on current game state
     * @param batch SpriteBatch to draw with
     */
    private void drawUI(SpriteBatch batch) {
        if (textManager == null) return;
        
        // Always draw score and time
        textManager.draw(batch, "Score: " + score, 20, DisplayManager.getScreenHeight() - 20, Color.WHITE);
        textManager.draw(batch, "Time: " + (int)(maxGameTime - gameTime), DisplayManager.getScreenWidth() - 150, DisplayManager.getScreenHeight() - 20, Color.WHITE);

        // Draw state-specific UI
        switch (state) {
            case READY:
                drawReadyStateUI(batch);
                break;
            case GAME_OVER:
                drawGameOverStateUI(batch);
                break;
            case CONFIRM_EXIT:
                drawConfirmExitUI(batch);
                break;
            case PLAYING:
                drawPlayingStateUI(batch);
                break;
        }
    }
    
    /**
     * Draws the UI for the READY state
     * @param batch SpriteBatch to draw with
     */
    private void drawReadyStateUI(SpriteBatch batch) {
        textManager.draw(batch, "FLAPPY SPACE CLEANER", DisplayManager.getScreenWidth()/2 - 180,
                        DisplayManager.getScreenHeight()/2 + 50, Color.YELLOW);
        textManager.draw(batch, "Press SPACE to start", DisplayManager.getScreenWidth()/2 - 150,
                        DisplayManager.getScreenHeight()/2, Color.WHITE);
        textManager.draw(batch, "Press Q to exit anytime (with penalty)", DisplayManager.getScreenWidth()/2 - 180,
                        DisplayManager.getScreenHeight()/2 - 50, Color.WHITE);
        textManager.draw(batch, "Avoid the pipes!", DisplayManager.getScreenWidth()/2 - 100,
                        DisplayManager.getScreenHeight()/2 - 100, Color.WHITE);
    }
    
    /**
     * Draws the UI for the GAME_OVER state
     * @param batch SpriteBatch to draw with
     */
    private void drawGameOverStateUI(SpriteBatch batch) {
        textManager.draw(batch, "GAME OVER!", DisplayManager.getScreenWidth()/2 - 100,
                        DisplayManager.getScreenHeight()/2 + 50, Color.RED);
        textManager.draw(batch, "Score: " + score, DisplayManager.getScreenWidth()/2 - 80,
                        DisplayManager.getScreenHeight()/2, Color.WHITE);
        textManager.draw(batch, "Returning to game...", DisplayManager.getScreenWidth()/2 - 140,
                        DisplayManager.getScreenHeight()/2 - 50, Color.WHITE);

        // Show countdown
        int remainingTime = (int)(gameOverDelay * 2 - currentDelay);
        textManager.draw(batch, "(" + remainingTime + ")",
                        DisplayManager.getScreenWidth()/2 - 20,
                        DisplayManager.getScreenHeight()/2 - 100, Color.WHITE);

        // Optional quick return
        if (currentDelay >= gameOverDelay) {
            textManager.draw(batch, "Press R to return now", DisplayManager.getScreenWidth()/2 - 140,
                            DisplayManager.getScreenHeight()/2 - 150, Color.WHITE);
        }
    }
    
    /**
     * Draws the confirmation dialog UI
     * @param batch SpriteBatch to draw with
     */
    private void drawConfirmExitUI(SpriteBatch batch) {
        // End the SpriteBatch before drawing shapes
        batch.end();

        try {
            // Draw dialog background
            drawConfirmationDialog();
        } catch (Exception e) {
            System.err.println("Error drawing dialog: " + e.getMessage());
        } finally {
            try {
                Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
            } catch (Exception e) {
                // Ignore GL errors
            }
        }

        // Resume batch to draw text
        batch.begin();

        // Dialog text
        float textX = DisplayManager.getScreenWidth()/2 - 180;
        float textY = DisplayManager.getScreenHeight()/2 + 60;

        textManager.draw(batch, "QUIT MINI-GAME?", textX, textY, Color.YELLOW);
        textManager.draw(batch, "You will lose one life as penalty", textX, textY - 40, Color.WHITE);
        textManager.draw(batch, "for skipping this challenge.", textX, textY - 70, Color.WHITE);

        // Options with highlighted selection
        textManager.draw(batch, "Yes, quit anyway", textX + 20, textY - 110,
                       confirmYes ? selectedColor : unselectedColor);
        textManager.draw(batch, "No, continue playing", textX + 20, textY - 140,
                       confirmYes ? unselectedColor : selectedColor);

        // Controls hint
        textManager.draw(batch, "Use UP/DOWN to select, SPACE to confirm", textX - 50, textY - 180, Color.WHITE);
    }
    
    /**
     * Draws the confirmation dialog background
     */
    private void drawConfirmationDialog() {
        // Set up blending for transparency
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        if (localShapeRenderer == null) {
            localShapeRenderer = new ShapeRenderer();
        }
        
        // Draw semi-transparent overlay for dialog background
        localShapeRenderer.begin(ShapeType.Filled);
        localShapeRenderer.setColor(0, 0, 0, 0.7f); // Semi-transparent black background
        localShapeRenderer.rect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
        localShapeRenderer.end();

        // Draw dialog box background
        localShapeRenderer.begin(ShapeType.Filled);
        float dialogWidth = 400;
        float dialogHeight = 220;
        float dialogX = DisplayManager.getScreenWidth()/2 - dialogWidth/2;
        float dialogY = DisplayManager.getScreenHeight()/2 - dialogHeight/2;
        localShapeRenderer.setColor(0.1f, 0.1f, 0.2f, 0.95f);
        localShapeRenderer.rect(dialogX, dialogY, dialogWidth, dialogHeight);
        localShapeRenderer.end();

        // Draw dialog border
        localShapeRenderer.begin(ShapeType.Line);
        localShapeRenderer.setColor(0.5f, 0.5f, 0.8f, 1);
        localShapeRenderer.rect(dialogX, dialogY, dialogWidth, dialogHeight);
        localShapeRenderer.end();
    }
    
    /**
     * Draws the UI for the PLAYING state
     * @param batch SpriteBatch to draw with
     */
    private void drawPlayingStateUI(SpriteBatch batch) {
        // During gameplay, show the Q key hint
        textManager.draw(batch, "Press Q to exit", 20, DisplayManager.getScreenHeight() - 60, Color.WHITE);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // Only draw debug collision info if enabled
        if (!debugCollision || state != GameState.PLAYING || shape == null || bird == null) {
            return;
        }
        
        // Safety check to avoid modifying external ShapeRenderer state
        boolean wasDrawing = false;
        try {
            if (shape.isDrawing()) {
                shape.end();
                wasDrawing = true;
            }

            // Initialize local ShapeRenderer if needed
            if (localShapeRenderer == null) {
                localShapeRenderer = new ShapeRenderer();
            }
            
            localShapeRenderer.setProjectionMatrix(shape.getProjectionMatrix());
            localShapeRenderer.begin(ShapeType.Line);
            
            // Show the actual collision boundary (reduced hitbox)
            float reducedBirdWidth = bird.width * collisionTolerance;
            float reducedBirdHeight = bird.height * collisionTolerance;
            float birdCenterX = bird.x + bird.width/2;
            float birdCenterY = bird.y + bird.height/2;
            
            // Draw bird collision hitbox
            localShapeRenderer.setColor(Color.GREEN);
            localShapeRenderer.rect(
                birdCenterX - reducedBirdWidth/2,
                birdCenterY - reducedBirdHeight/2,
                reducedBirdWidth,
                reducedBirdHeight
            );
            
            // Draw visual bounds of the bird
            localShapeRenderer.setColor(Color.YELLOW);
            localShapeRenderer.rect(bird.x, bird.y, bird.width, bird.height);

            // Draw pipe hitboxes
            localShapeRenderer.setColor(Color.RED);
            for (Rectangle topPipe : topPipes) {
                localShapeRenderer.rect(topPipe.x, topPipe.y, topPipe.width, topPipe.height);
            }

            for (Rectangle bottomPipe : bottomPipes) {
                localShapeRenderer.rect(bottomPipe.x, bottomPipe.y, bottomPipe.width, bottomPipe.height);
            }

            localShapeRenderer.end();
        } catch (Exception e) {
            System.err.println("Error during debug shape rendering: " + e.getMessage());
        } finally {
            // Restore original ShapeRenderer state if it was drawing
            if (wasDrawing) {
                try {
                    shape.begin(ShapeType.Filled);
                } catch (Exception e) {
                    // Ignore errors trying to restore state
                }
            }
        }
    }

    @Override
    public void unload() {
        // If somehow the game gets unloaded without completing properly,
        // make sure we clean up the player status flag
        if (!gameCompleted) {
            try {
                PlayerStatus playerStatus = PlayerStatus.getInstance();
                if (playerStatus != null) {
                    playerStatus.setInMiniGame(false);
                }
            } catch (Exception e) {
                System.err.println("Error resetting player status: " + e.getMessage());
            }
        }

        dispose();
    }

    @Override
    public void dispose() {
        try {
            if (birdTexture != null) {
                birdTexture.dispose();
                birdTexture = null;
            }
            if (pipeTopTexture != null) {
                pipeTopTexture.dispose();
                pipeTopTexture = null;
            }
            if (pipeBottomTexture != null) {
                pipeBottomTexture.dispose();
                pipeBottomTexture = null;
            }
            if (localShapeRenderer != null) {
                localShapeRenderer.dispose();
                localShapeRenderer = null;
            }
            
            // Clear collections
            if (topPipes != null) topPipes.clear();
            if (bottomPipes != null) bottomPipes.clear();
            if (passedPipes != null) passedPipes.clear();
            
            System.out.println("FlappyBirdMiniGame resources disposed successfully");
        } catch (Exception e) {
            System.err.println("Error disposing FlappyBirdMiniGame resources: " + e.getMessage());
        }
    }
}