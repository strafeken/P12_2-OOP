package application.minigame.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import abstractengine.audio.AudioManager;
import abstractengine.audio.IAudioManager;
import abstractengine.entity.EntityManager;
import abstractengine.io.TextManager;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.Scene;
import abstractengine.scene.SceneManager;
import application.entity.PlayerStatus;
import application.io.GameInputManager;
import application.scene.GameOverScreen;
import application.scene.PointsManager;
import application.scene.SceneID;
import application.scene.StartMiniGameHandler;
import application.minigame.flappybird.FlappyBirdUI;
import application.minigame.asteroids.AsteroidDodgeUI;

/**
 * Abstract base class for all mini-games, providing common functionality.
 * Following the Template Method pattern for the game loop.
 */
public abstract class AbstractMiniGame extends Scene {
    // Game state
    protected GameState state;

    // Game timing
    protected float gameTime = 0;
    protected float timeLimit = 60f;
    protected float maxGameTime;
    protected boolean gameCompleted = false;

    // Score tracking - adding this field to AbstractMiniGame
    protected int score = 0;

    // Exit confirmation
    protected boolean confirmExitDialogActive = false;
    protected boolean confirmYes = true;
    private static boolean confirmYesState = true;

    // Common managers
    protected PointsManager pointsManager;
    protected IAudioManager audioManager;
    protected ISceneManager sceneManager;
    protected StartMiniGameHandler miniGameHandler;

    // UI elements
    protected MiniGameUI gameUI;

    // Game over timer
    protected float gameOverTimer = 0;

    /**
     * Creates a new AbstractMiniGame
     *
     * @param pointsManager The points manager for scoring
     * @param miniGameHandler The handler that manages mini-games
     * @param maxGameTime Maximum duration for this mini-game
     */
    public AbstractMiniGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler, float maxGameTime) {
        super();
        this.pointsManager = pointsManager;
        this.miniGameHandler = miniGameHandler;
        this.maxGameTime = maxGameTime;
        this.state = GameState.READY;
        this.score = 0; // Initialize score

        // Initialize common managers
        this.entityManager = new EntityManager();
        this.gameInputManager = new GameInputManager();
        this.textManager = new TextManager();
        this.audioManager = AudioManager.getInstance();
        this.sceneManager = SceneManager.getInstance();

        // Initialize UI
        this.gameUI = createGameUI();
    }

    /**
     * Update the game
     * This is the required abstract Scene method implementation
     */
    @Override
    public void update() {
        // Call our own update method with delta time
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        update(deltaTime);
    }

    /**
     * Update the game with a specific delta time
     */

    public void update(float deltaTime) {
        // Handle input and state-specific logic
        handleStateInput();

        switch(state) {
            case READY:
                handleReadyState(deltaTime);
                break;
            case PLAYING:
                handlePlayingState(deltaTime);
                break;
            case GAME_OVER:
                handleGameOverState(deltaTime);
                // Increment the game over timer here
                gameOverTimer += deltaTime;
                break;
            case CONFIRM_EXIT:
                handleConfirmExitState(deltaTime);
                break;
        }

        // Update UI score if needed
        updateUIScore();
    }

    /**
     * Override this method to draw the game using the sprite batch
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (batch == null) {
            System.err.println("ERROR: SpriteBatch is null in AbstractMiniGame.draw()!");
            return;
        }

        // Always draw the game - but let the concrete implementations handle
        // what gets drawn based on state (in READY they should only draw background)
        drawGame(batch);

        // Always draw UI on top
        if (gameUI != null) {
            if (state == GameState.GAME_OVER) {
                // Pass gameOverTimer (which is updated separately) instead of gameTime
                gameUI.draw(batch, state, gameOverTimer, timeLimit);
                System.out.println("Drawing GAME_OVER with timer: " + gameOverTimer);
            } else {
                gameUI.draw(batch, state, gameTime, timeLimit);
            }
        } else {
            System.err.println("ERROR: gameUI is null in AbstractMiniGame.draw()!");
        }
    }

    /**
     * Get the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the current score
     */
    protected void setScore(int score) {
        this.score = score;
        updateUIScore();
    }

    /**
     * Update UI with current score
     */
    protected void updateUIScore() {
        if (gameUI == null) return;

        // Update the UI with the current score based on game type
        if (gameUI instanceof FlappyBirdUI) {
            ((FlappyBirdUI)gameUI).setScore(score);
        } else if (gameUI instanceof AsteroidDodgeUI) {
            ((AsteroidDodgeUI)gameUI).setScore(score);
        }
    }

    /**
     * Handle state transitions based on input
     */
    protected void handleStateInput() {
        // Common input handling for all states
        switch(state) {
            case READY:
                if (isSpacePressed()) {
                    System.out.println("SPACE pressed in READY state - transitioning to PLAYING");
                    state = GameState.PLAYING;
                    gameTime = 0; // Reset game time when starting
                }
                break;

            case PLAYING:
                if (isEscapePressed()) {
                    System.out.println("ESCAPE pressed in PLAYING state - transitioning to CONFIRM_EXIT");
                    state = GameState.CONFIRM_EXIT;
                    confirmYes = false; // Default to No for safety
                }
                break;

            case GAME_OVER:
                // Handle in game-specific handleGameOverState
                break;

            case CONFIRM_EXIT:
                // Handle in handleConfirmExitState
                break;
        }
    }

    /**
     * Handle ready state (waiting to start)
     */
    protected void handleReadyState(float deltaTime) {
        // Common ready state behavior already handled in handleStateInput
    }

    /**
     * Handle playing state
     */
    protected abstract void handlePlayingState(float deltaTime);

    /**
     * Handle game over state
     */
    protected void handleGameOverState(float deltaTime) {
        // Default implementation - can be overridden
    }

    /**
     * Handle exit confirmation state
     */
    protected void handleConfirmExitState(float deltaTime) {
        handleExitDialogInput();
    }

    /**
     * Create the game-specific UI
     */
    protected abstract MiniGameUI createGameUI();

    /**
     * Draw the game-specific elements
     */
    protected abstract void drawGame(SpriteBatch batch);

    /**
     * Handle the input for exit confirmation dialog
     */
    protected void handleExitDialogInput() {
        // Allow W, UP, A, or LEFT to select "Yes"
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) ||
            Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
            Gdx.input.isKeyJustPressed(Input.Keys.A) ||
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            setConfirmYes(true);
            System.out.println("Selected YES in exit dialog");
        }
        // Allow S, DOWN, D, or RIGHT to select "No"
        else if (Gdx.input.isKeyJustPressed(Input.Keys.S) ||
                 Gdx.input.isKeyJustPressed(Input.Keys.DOWN) ||
                 Gdx.input.isKeyJustPressed(Input.Keys.D) ||
                 Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            setConfirmYes(false);
            System.out.println("Selected NO in exit dialog");
        }

        // Confirm selection with SPACE or ENTER
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (confirmYes) {
                completeGameWithPenalty();
            } else {
                state = GameState.PLAYING;
            }
        }

        // Cancel with ESCAPE
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Complete the game successfully or unsuccessfully
     *
     * @param success Whether the player completed successfully
     */
    protected void completeGame(boolean success) {
        gameCompleted = true;

        // Simply add the minigame score to the main game without additional calculations
        pointsManager.addPoints(score);

        // Stop mini-game music
        audioManager.stopSoundEffect("minigame");

        // Apply penalty if player died early
        applyEarlyDeathPenalty(success);

        // Only return to main game if player has lives remaining
        if (PlayerStatus.getInstance().getLives() > 0) {
            PlayerStatus.getInstance().setInMiniGame(false);
            sceneManager.removeOverlay();
        }

        // Notify handler that mini-game is completed
        if (miniGameHandler != null) {
            miniGameHandler.onMiniGameCompleted();
        }
    }

    /**
     * Complete the game with a life penalty for early exit
     */
    protected void completeGameWithPenalty() {
        gameCompleted = true;

        // Get player status
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        if (playerStatus.getLives() > 1) {
            // Normal case - player has more than 1 life, just decrement
            playerStatus.decrementLife();

            // Stop mini-game music
            audioManager.stopSoundEffect("minigame");

            // Return to main game
            playerStatus.setInMiniGame(false);
            sceneManager.removeOverlay();
        } else {
            // Player has only 1 life left - trigger game over
            playerStatus.decrementLife();
            playerStatus.setInMiniGame(false);

            // Stop mini-game music
            audioManager.stopSoundEffect("minigame");

            // Create and set up game over screen
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
     *
     * @param success Whether the player completed successfully
     */
    protected void applyEarlyDeathPenalty(boolean success) {
        if (!success && gameTime < 15.0f) {
            // Get player status
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            if (playerStatus != null) {
                // Decrement life
                playerStatus.decrementLife();

                // Check if player has no lives left
                if (playerStatus.getLives() <= 0) {
                    // Create game over screen
                    GameOverScreen gameOverScreen = new GameOverScreen();
                    gameOverScreen.setFinalScore(pointsManager.getPoints());
                    sceneManager.setNextScene(SceneID.GAME_OVER);
                }
            }
        }
    }

    /**
     * Get the delta time with safety capping
     */
    protected float getDeltaTime() {
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        // Cap deltaTime to prevent physics glitches
        return Math.min(deltaTime, 0.05f);
    }

    // Input helper methods
    protected boolean isSpacePressed() {
        return com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE);
    }

    protected boolean isEscapePressed() {
        return com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE);
    }

    protected boolean isUpJustPressed() {
        return com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP) ||
               com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.W);
    }

    protected boolean isDownJustPressed() {
        return com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN) ||
               com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S);
    }

    /**
     * Reset the game to its initial state
     */
    protected void reset() {
        gameTime = 0;
        score = 0;
        state = GameState.READY;
        gameCompleted = false;
    }

    /**
     * Method to be overridden for game-specific loading
     */
    public abstract void load();

    /**
     * Method to be overridden for game-specific unloading
     */
    public abstract void unload();

    /**
     * Default implementation for shape rendering
     */
    public void draw(ShapeRenderer shape) {
        // Default implementation does nothing
        // Override in subclasses to provide game-specific shape rendering
    }

    // Override the existing setting of confirmYes to also update the static version
    protected void setConfirmYes(boolean value) {
        this.confirmYes = value;
        confirmYesState = value;
    }

    // Add this static method
    public static boolean getConfirmYes() {
        return confirmYesState;
    }
}
