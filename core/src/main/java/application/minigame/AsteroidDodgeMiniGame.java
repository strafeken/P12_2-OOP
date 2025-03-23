package application.minigame;

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

import abstractengine.audio.AudioManager;
import abstractengine.audio.IAudioManager;
import abstractengine.entity.EntityManager;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.Scene;
import abstractengine.scene.SceneManager;
import abstractengine.utils.DisplayManager;
import application.entity.StartMiniGameHandler;
import application.input.GameInputManager;
import application.manager.PlayerStatus;
import application.manager.PointsManager;
import application.manager.TextManager;
import application.scene.GameOverScreen;
import application.scene.SceneID;

public class AsteroidDodgeMiniGame extends Scene {
    // Game state
    private enum GameState { READY, PLAYING, GAME_OVER, CONFIRM_EXIT }
    private GameState state;

    // Game objects
    private Texture shipTexture;
    private Texture asteroidTexture;
    private Rectangle ship;
    private Array<Rectangle> asteroids;
    private Array<Float> asteroidSpeeds;

    // Physics
    private float shipSpeed = 300f;
    private float asteroidSpeedMin = 150f;
    private float asteroidSpeedMax = 350f;
    private float asteroidSpawnInterval = 1.0f;
    private float timeSinceLastAsteroid = 0;
    private float asteroidSizeMultiplier = 1.0f;

    // Collision settings
    private float collisionGracePeriod = 1.5f;
    private boolean collisionEnabled = true;
    private float collisionMargin = 0.2f;

    // Scoring
    private int score;

    // Game duration
    private float gameTime = 0;
    private float maxGameTime = 30f; // 30 seconds max
    private boolean gameCompleted = false;
    private float gameOverDelay = 2.0f;
    private float currentDelay = 0f;

    // Reference to managers
    private PointsManager pointsManager;
    private IAudioManager audioManager;
    private ISceneManager sceneManager;
    private StartMiniGameHandler miniGameHandler;

    // Shape renderer
    private ShapeRenderer localShapeRenderer;

    // Background
    private Color skyColor = new Color(0.1f, 0.1f, 0.3f, 1);

    // Debug flag
    private boolean debugCollision = false;

    // Add these fields to the class
    private float initialSpawnInterval = 1.5f;
    private float initialAsteroidSpeed = 100f;
    private float asteroidSpeed = 100f;

    // Add confirmation dialog variables
    private boolean confirmYes = true; // Default to Yes selection
    private Color selectedColor = Color.YELLOW;
    private Color unselectedColor = Color.WHITE;

    public AsteroidDodgeMiniGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        super();
        this.pointsManager = pointsManager;
        this.miniGameHandler = miniGameHandler;
        this.state = GameState.READY;
        this.score = 0;

        // Initialize managers
        this.entityManager = new EntityManager();
        this.gameInputManager = new GameInputManager();
        this.textManager = new TextManager();
        this.audioManager = AudioManager.getInstance();
        this.sceneManager = SceneManager.getInstance();
        this.localShapeRenderer = new ShapeRenderer();

        // Initialize collections
        this.asteroids = new Array<>();
        this.asteroidSpeeds = new Array<>();
    }

    @Override
    public void load() {
        // Load textures
        try {
            shipTexture = new Texture(Gdx.files.internal("rocket-2.png"));
            asteroidTexture = new Texture(Gdx.files.internal("asteroid.png"));
        } catch (Exception e) {
            System.err.println("Error loading textures: " + e.getMessage());
            e.printStackTrace();

            // Fallback textures if needed
            if (shipTexture == null) {
                shipTexture = new Texture(Gdx.files.internal("player.png"));
            }
            if (asteroidTexture == null) {
                asteroidTexture = new Texture(Gdx.files.internal("item/plastic-bottle.png"));
            }
        }

        // Initialize ship
        ship = new Rectangle();
        ship.width = 50;
        ship.height = 40;
        ship.x = DisplayManager.getScreenWidth() / 2 - ship.width / 2;
        ship.y = 50;  // Position near bottom of screen

        // Reset game state
        score = 0;
        gameTime = 0;
        gameCompleted = false;
        currentDelay = 0f;
        collisionEnabled = true;
        asteroids.clear();
        asteroidSpeeds.clear();

        // Play mini-game music
        audioManager.playSoundEffect("minigame");

        state = GameState.READY;
    }

    private void spawnAsteroid() {
        Rectangle asteroid = new Rectangle();
        float baseSize = MathUtils.random(30, 70);

        // Apply size multiplier for increased difficulty
        asteroid.width = baseSize * asteroidSizeMultiplier;
        asteroid.height = asteroid.width;

        asteroid.x = MathUtils.random(0, DisplayManager.getScreenWidth() - asteroid.width);
        asteroid.y = DisplayManager.getScreenHeight();  // Start above screen

        // Use the variable speed range
        float speed = MathUtils.random(asteroidSpeedMin, asteroidSpeedMax);

        asteroids.add(asteroid);
        asteroidSpeeds.add(speed);
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Cap deltaTime to prevent extreme jumps in physics
        if (deltaTime > 0.1f) {
            deltaTime = 0.1f;
        }

        gameTime += deltaTime;

        // Check for game completion based on time
        if (gameTime >= maxGameTime && state == GameState.PLAYING) {
            completeGame(true);
            return;
        }

        // Update based on current state
        switch (state) {
            case READY:
                // Press space to start
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    state = GameState.PLAYING;
                    gameTime = 0; // Reset game time when actually starting
                }
                break;

            case PLAYING:
                // Quick exit with Q key
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    state = GameState.CONFIRM_EXIT;
                    break;
                }

                // Move ship with WASD keys
                handlePlayerMovement();

                // Spawn new asteroids
                updateAsteroidSpawning(deltaTime);

                // Update asteroids and check collisions
                updateAsteroidsAndCollisions(deltaTime);
                break;

            case GAME_OVER:
                // Wait for delay, then return to game
                currentDelay += deltaTime;
                if (currentDelay >= gameOverDelay) {
                    // Use Q key to return immediately
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                        completeGame(false);
                    }
                    // Auto-return to game after delay
                    else if (currentDelay >= gameOverDelay * 2) {
                        completeGame(false);
                    }
                }
                break;

            case CONFIRM_EXIT:
                handleConfirmExitState();
                break;
        }
    }

    private void handlePlayerMovement() {
        float speed = 300f;
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Change controls from arrow keys to WASD
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

        // Update player position
        ship.x += dx;
        ship.y += dy;

        // Keep player within screen bounds
        ship.x = MathUtils.clamp(ship.x, 0, DisplayManager.getScreenWidth() - ship.width);
        ship.y = MathUtils.clamp(ship.y, 0, DisplayManager.getScreenHeight() - ship.height);
    }

    private void updateAsteroidSpawning(float deltaTime) {
        // More aggressive spawn rate increase
        float baseInterval = 1.0f;
        float minInterval = 0.25f; // Minimum 1/4 second between asteroids

        // Rapidly decrease spawn interval over time
        asteroidSpawnInterval = Math.max(minInterval, baseInterval - (gameTime / 15f));

        timeSinceLastAsteroid += deltaTime;
        if (timeSinceLastAsteroid >= asteroidSpawnInterval) {
            spawnAsteroid();
            timeSinceLastAsteroid = 0;

            // Increase asteroid speed more aggressively
            asteroidSpeedMin = Math.min(250f, 150f + gameTime * 5f);
            asteroidSpeedMax = Math.min(450f, 350f + gameTime * 5f);

            // Increase asteroid size over time
            asteroidSizeMultiplier = Math.min(1.5f, 1.0f + gameTime / 30f);
        }
    }

    /**
     * Updates asteroid positions and checks for collisions
     * @param deltaTime The time since the last update
     */
    private void updateAsteroidsAndCollisions(float deltaTime) {
        // Check if we're in grace period
        boolean inGracePeriod = gameTime < collisionGracePeriod;

        for (int i = 0; i < asteroids.size; i++) {
            Rectangle asteroid = asteroids.get(i);
            float speed = asteroidSpeeds.get(i);

            // Move asteroid down
            asteroid.y -= speed * deltaTime;

            // Remove asteroids that go off the bottom of the screen
            if (asteroid.y + asteroid.height < 0) {
                asteroids.removeIndex(i);
                asteroidSpeeds.removeIndex(i);
                i--;

                // Add score for each successfully avoided asteroid
                // Scale points based on game time to make it harder to get points later
                float timeMultiplier = 1.0f + (gameTime / 15.0f);
                int pointsAwarded = Math.min(3, (int)(1 * timeMultiplier));
                score += pointsAwarded;

                continue;
            }

            // Skip collision detection during grace period
            if (inGracePeriod) {
                continue;
            }

            // Create smaller hitboxes for more forgiving collision detection
            float shipMarginW = ship.width * collisionMargin / 2;
            float shipMarginH = ship.height * collisionMargin / 2;
            float asteroidMargin = asteroid.width * collisionMargin / 2;

            // Smaller ship hitbox
            float shipHitboxX = ship.x + shipMarginW;
            float shipHitboxY = ship.y + shipMarginH;
            float shipHitboxWidth = ship.width - shipMarginW * 2;
            float shipHitboxHeight = ship.height - shipMarginH * 2;

            // Smaller asteroid hitbox
            float asteroidHitboxX = asteroid.x + asteroidMargin;
            float asteroidHitboxY = asteroid.y + asteroidMargin;
            float asteroidHitboxWidth = asteroid.width - asteroidMargin * 2;
            float asteroidHitboxHeight = asteroid.height - asteroidMargin * 2;

            // Check for collision
            boolean collision = shipHitboxX < asteroidHitboxX + asteroidHitboxWidth &&
                               shipHitboxX + shipHitboxWidth > asteroidHitboxX &&
                               shipHitboxY < asteroidHitboxY + asteroidHitboxHeight &&
                               shipHitboxY + shipHitboxHeight > asteroidHitboxY;

            if (collision && collisionEnabled) {
                if (debugCollision) {
                    System.out.println("Collision detected with asteroid " + i);
                }

                state = GameState.GAME_OVER;
                audioManager.playSoundEffect("hit");
                currentDelay = 0;
                break;
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
     * Completes the game and returns to the main game screen
     * @param success Whether the player completed successfully
     */
    private void completeGame(boolean success) {
        gameCompleted = true;

        // Calculate final score with time bonus
        int finalPoints = score;

        if (success) {
            // Add a survival bonus only if the player completes the full duration
            // This is scaled based on the maximum game time
            int survivalBonus = (int)(maxGameTime / 3); // Reduced to 10 points for 30 seconds
            finalPoints += survivalBonus;
            System.out.println("Asteroid Dodge completed successfully! Score: " + score +
                              ", Survival bonus: " + survivalBonus);
        } else {
            // For game over, only award points earned so far
            System.out.println("Asteroid Dodge failed. Score: " + score);
        }

        // Add the minigame score to the main game score
        pointsManager.addPoints(finalPoints);
        System.out.println("Asteroid Dodge completed! Added " + finalPoints + " points from mini-game.");

        // Stop mini-game music
        audioManager.stopSoundEffect("minigame");

        // Apply penalty if player died early - this might redirect to game over screen
        // This needs to happen before returning to the main game
        applyEarlyDeathPenalty(success);

        // Only do these if we're not going to the game over screen
        // (if applyEarlyDeathPenalty didn't send us to game over)
        if (PlayerStatus.getInstance().getLives() > 0) {
            // Return to main game
            PlayerStatus.getInstance().setInMiniGame(false);
            sceneManager.removeOverlay();

            // Notify handler that mini-game is completed
            if (miniGameHandler != null) {
                miniGameHandler.onMiniGameCompleted();
            }
        }
    }

    /**
     * Completes the game with a life penalty for early exit
     */
    private void completeGameWithPenalty() {
        gameCompleted = true;

        // Apply penalty (same as if player died early)
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        if (playerStatus.getLives() > 1) {
            // Normal case - player has more than 1 life, just decrement
            playerStatus.decrementLife();
            System.out.println("Player skipped mini-game. Lost 1 life as penalty!");

            // Stop mini-game music
            audioManager.stopSoundEffect("minigame");

            // Return to main game
            playerStatus.setInMiniGame(false);
            sceneManager.removeOverlay();
        } else {
            // Player has only 1 life left - reduce to 0 and trigger game over
            playerStatus.decrementLife(); // This will set lives to 0
            System.out.println("Player skipped mini-game with only 1 life remaining. Game over!");

            // Stop mini-game music
            audioManager.stopSoundEffect("minigame");

            // Player is no longer in mini-game
            playerStatus.setInMiniGame(false);

            // Create and set up game over screen with final score
            GameOverScreen gameOverScreen = new GameOverScreen();
            gameOverScreen.setFinalScore(pointsManager.getPoints());
            sceneManager.setNextScene(SceneID.GAME_OVER);
        }

        // Notify handler that mini-game is completed
        if (miniGameHandler != null) {
            miniGameHandler.onMiniGameCompleted();
        }
    }

    /**
     * Apply penalty if player died early in the game
     * @param success Whether the player completed successfully
     */
    private void applyEarlyDeathPenalty(boolean success) {
        if (!success && gameTime < 15.0f) {
            // Get player status
            PlayerStatus playerStatus = PlayerStatus.getInstance();
            if (playerStatus != null) {
                // Always decrement life, regardless of current life count
                playerStatus.decrementLife();

                // Check if player has no lives left after decrementing
                if (playerStatus.getLives() <= 0) {
                    System.out.println("Player died early in mini-game and lost last life. Going to Game Over!");

                    try {
                        // Create game over screen with main game score (not mini-game score)
                        GameOverScreen gameOverScreen = new GameOverScreen();

                        // Important: Use the main game's score, not the mini-game score
                        gameOverScreen.setFinalScore(pointsManager.getPoints());

                        // Transition to game over screen
                        sceneManager.setNextScene(SceneID.GAME_OVER);
                    } catch (Exception e) {
                        System.err.println("Error transitioning to game over screen: " + e.getMessage());
                    }
                } else {
                    // Player still has lives left
                    System.out.println("Player died early in mini-game. Lost 1 life as penalty! Lives remaining: "
                                      + playerStatus.getLives());
                }
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // End the batch before drawing shapes and begin it again afterward
        batch.end();

        // Draw space background
        localShapeRenderer.begin(ShapeType.Filled);
        localShapeRenderer.setColor(skyColor);
        localShapeRenderer.rect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
        localShapeRenderer.end();

        // Restart the batch for sprites
        batch.begin();

        // Draw asteroids
        for (Rectangle asteroid : asteroids) {
            batch.draw(asteroidTexture, asteroid.x, asteroid.y, asteroid.width, asteroid.height);
        }

        // Draw ship
        if (shipTexture != null) {
            batch.draw(shipTexture, ship.x, ship.y, ship.width, ship.height);
        }

        // Draw UI
        textManager.draw(batch, "Score: " + score, 20, DisplayManager.getScreenHeight() - 20, Color.WHITE);
        textManager.draw(batch, "Time: " + (int)(maxGameTime - gameTime), DisplayManager.getScreenWidth() - 150, DisplayManager.getScreenHeight() - 20, Color.WHITE);

        switch (state) {
            case READY:
                textManager.draw(batch, "ASTEROID DODGE", DisplayManager.getScreenWidth()/2 - 180,
                                DisplayManager.getScreenHeight()/2 + 50, Color.YELLOW);
                textManager.draw(batch, "Use W/A/S/D to move", DisplayManager.getScreenWidth()/2 - 200,
                                DisplayManager.getScreenHeight()/2, Color.WHITE);
                textManager.draw(batch, "Avoid the asteroids!", DisplayManager.getScreenWidth()/2 - 120,
                                DisplayManager.getScreenHeight()/2 - 50, Color.WHITE);
                textManager.draw(batch, "Press SPACE to start", DisplayManager.getScreenWidth()/2 - 150,
                                DisplayManager.getScreenHeight()/2 - 100, Color.WHITE);
                break;

            case PLAYING:
                // Show grace period indicator if active
                if (gameTime < collisionGracePeriod) {
                    textManager.draw(batch, "Immunity: " + (int)(collisionGracePeriod - gameTime + 1),
                                    20, DisplayManager.getScreenHeight() - 50, Color.GREEN);
                }
                break;

            case GAME_OVER:
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
                    textManager.draw(batch, "Press Q to return now", DisplayManager.getScreenWidth()/2 - 140,
                                    DisplayManager.getScreenHeight()/2 - 150, Color.WHITE);
                }
                break;

            case CONFIRM_EXIT:
                drawConfirmExitUI(batch);
                break;
        }
    }

    /**
     * Draws the exit confirmation dialog
     */
    private void drawConfirmExitUI(SpriteBatch batch) {
        // End the SpriteBatch before drawing shapes
        batch.end();

        // Set up blending for transparency
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

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

        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        // Resume batch to draw text
        batch.begin();

        // Dialog text
        float textX = dialogX + 20;
        float textY = dialogY + dialogHeight - 30;

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

    @Override
    public void draw(ShapeRenderer shape) {
        // Only draw debug collision info if enabled
        if (debugCollision && state == GameState.PLAYING) {
            localShapeRenderer.setProjectionMatrix(shape.getProjectionMatrix());
            localShapeRenderer.begin(ShapeType.Line);

            // Draw actual hitboxes used for collision in red
            if (gameTime >= collisionGracePeriod) {
                localShapeRenderer.setColor(Color.RED);

                // Draw smaller ship hitbox
                float shipMarginW = ship.width * collisionMargin / 2;
                float shipMarginH = ship.height * collisionMargin / 2;
                float shipHitboxX = ship.x + shipMarginW;
                float shipHitboxY = ship.y + shipMarginH;
                float shipHitboxWidth = ship.width - shipMarginW * 2;
                float shipHitboxHeight = ship.height - shipMarginH * 2;
                localShapeRenderer.rect(shipHitboxX, shipHitboxY, shipHitboxWidth, shipHitboxHeight);

                // Draw smaller asteroid hitboxes
                for (Rectangle asteroid : asteroids) {
                    float asteroidMargin = asteroid.width * collisionMargin / 2;
                    float asteroidHitboxX = asteroid.x + asteroidMargin;
                    float asteroidHitboxY = asteroid.y + asteroidMargin;
                    float asteroidHitboxWidth = asteroid.width - asteroidMargin * 2;
                    float asteroidHitboxHeight = asteroid.height - asteroidMargin * 2;
                    localShapeRenderer.rect(asteroidHitboxX, asteroidHitboxY, asteroidHitboxWidth, asteroidHitboxHeight);
                }
            }

            // Draw visual bounds in yellow
            localShapeRenderer.setColor(Color.YELLOW);
            localShapeRenderer.rect(ship.x, ship.y, ship.width, ship.height);

            for (Rectangle asteroid : asteroids) {
                localShapeRenderer.rect(asteroid.x, asteroid.y, asteroid.width, asteroid.height);
            }

            // Show grace period bar
            if (gameTime < collisionGracePeriod) {
                localShapeRenderer.setColor(Color.GREEN);
                localShapeRenderer.rect(10, 10, (collisionGracePeriod - gameTime) * 100, 10);
            }

            localShapeRenderer.end();
        }
    }

    @Override
    public void unload() {
        // If somehow the game gets unloaded without completing properly,
        // make sure we clean up the player status flag
        if (!gameCompleted) {
            PlayerStatus.getInstance().setInMiniGame(false);
        }

        dispose();
    }

    @Override
    public void dispose() {
        if (shipTexture != null) {
            shipTexture.dispose();
            shipTexture = null;
        }
        if (asteroidTexture != null) {
            asteroidTexture.dispose();
            asteroidTexture = null;
        }
        if (localShapeRenderer != null) {
            localShapeRenderer.dispose();
            localShapeRenderer = null;
        }
    }
}
