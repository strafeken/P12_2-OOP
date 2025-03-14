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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.CollisionExtensions.StartMiniGameHandler;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.Pipe;
import io.github.team2.PipeBehaviour;
import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.TextManager;
import io.github.team2.Utils.DisplayManager;
;
public class FlappyBirdMiniGame extends Scene {
    // Game state
    private enum GameState { READY, PLAYING, GAME_OVER }
    private GameState state;

    // Game objects
    private Texture birdTexture;
    private Texture pipeTopTexture;
    private Texture pipeBottomTexture;
    private Rectangle bird;
    private Array<Rectangle> topPipes;
    private Array<Rectangle> bottomPipes;

    // Pipe objects that will provide the textures
    private Pipe topPipeObject;
    private Pipe bottomPipeObject;

    // Physics
    private float gravity = 600f;
    private float jumpVelocity = -400f;
    private float velocity = 0;
    private float pipeSpeed = 200f;
    private float pipeWidth = 60f;
    private float pipeGap = 220f; // Increased gap size for easier gameplay
    private float timeSinceLastPipe = 0;
    private float pipeInterval = 1.7f;

    // Scoring
    private int score;
    private Array<Integer> passedPipes;

    // Game duration
    private float gameTime = 0;
    private float maxGameTime = 30f; // 30 seconds max
    private boolean gameCompleted = false;
    private float gameOverDelay = 2.0f; // Delay before returning to game scene
    private float currentDelay = 0f;

    // Reference to managers we need
    private PointsManager pointsManager;
    private IAudioManager audioManager;
    private ISceneManager sceneManager;

    // Shape renderer for internal use
    private ShapeRenderer localShapeRenderer;

    // Background
    private Color skyColor = new Color(0.4f, 0.7f, 1f, 1);

    // Debug flag
    private boolean debugCollision = false; // Set to true to see collision boxes

    // Add this field
    private StartMiniGameHandler miniGameHandler;

    // Update the constructor
    public FlappyBirdMiniGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        super();
        this.pointsManager = pointsManager;
        this.miniGameHandler = miniGameHandler;
        this.state = GameState.READY;
        this.score = 0;

        // Initialize managers in constructor to prevent null pointer exceptions
        this.entityManager = new EntityManager();
        this.gameInputManager = new GameInputManager();
        this.textManager = new TextManager();
        this.audioManager = AudioManager.getInstance();
        this.sceneManager = SceneManager.getInstance();
        this.localShapeRenderer = new ShapeRenderer();

        // Initialize collections
        this.topPipes = new Array<>();
        this.bottomPipes = new Array<>();
        this.passedPipes = new Array<>();
    }

    @Override
    public void load() {
        // Load textures
        try {
            birdTexture = new Texture(Gdx.files.internal("rocket-2.png"));

            // Create pipe objects to get their textures
            // Top pipe
            topPipeObject = new Pipe(
                EntityType.PIPE,
                "plastic-bottle-2.png", // Assuming this texture exists or will be handled internally
                new Vector2(pipeWidth, 400), // Size matching the dimensions used in game
                new Vector2(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight() / 2), // Position doesn't matter much here
                new Vector2(-1, 0), // Direction left
                new Vector2(0, 0), // No rotation
                pipeSpeed, // Speed matching game setting
                PipeBehaviour.State.IDLE,
                PipeBehaviour.Move.NONE
            );
            
            // Bottom pipe - similar to top but different texture
            bottomPipeObject = new Pipe(
                EntityType.PIPE,
                "plastic-bottle-2.png", // Assuming this texture exists or will be handled internally
                new Vector2(pipeWidth, 400), // Size matching the dimensions used in game
                new Vector2(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight() / 2 - 600), // Lower position
                new Vector2(-1, 0), // Direction left
                new Vector2(0, 0), // No rotation
                pipeSpeed, // Speed matching game setting
                PipeBehaviour.State.IDLE,
                PipeBehaviour.Move.NONE
            );
            
            // Get textures from pipe objects
            // Note: We're assuming DynamicTextureObject (parent of Pipe) loads the texture properly
            // and provides a way to access it. If not, we'll need alternative approach.
            
            // For now, let's create the textures directly since we're in a mini-game
            pipeTopTexture = new Texture(Gdx.files.internal("plastic-bottle-2.png"));
            pipeBottomTexture = new Texture(Gdx.files.internal("plastic-bottle-2.png"));

        } catch (Exception e) {
            System.err.println("Error loading textures: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialize bird
        bird = new Rectangle();
        bird.width = 40;
        bird.height = 32;
        bird.x = DisplayManager.getScreenWidth() / 4;
        bird.y = DisplayManager.getScreenHeight() / 2;

        // Reset game state
        velocity = 0;
        score = 0;
        gameTime = 0;
        gameCompleted = false;
        currentDelay = 0f;
        topPipes.clear();
        bottomPipes.clear();
        passedPipes.clear();

        // Play mini-game music
        audioManager.playSoundEffect("minigame");

        state = GameState.READY;
    }

    private void spawnPipe() {
        float centerPipe = MathUtils.random(
            DisplayManager.getScreenHeight() * 0.3f,
            DisplayManager.getScreenHeight() * 0.7f
        );

        float pipeHeight = 400;

        // Top pipe
        Rectangle topPipe = new Rectangle();
        topPipe.x = DisplayManager.getScreenWidth();
        topPipe.y = centerPipe + pipeGap/2;
        topPipe.width = pipeWidth;
        topPipe.height = pipeHeight;

        // Bottom pipe
        Rectangle bottomPipe = new Rectangle();
        bottomPipe.x = DisplayManager.getScreenWidth();
        bottomPipe.y = centerPipe - pipeGap/2 - pipeHeight;
        bottomPipe.width = pipeWidth;
        bottomPipe.height = pipeHeight;

        topPipes.add(topPipe);
        bottomPipes.add(bottomPipe);
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
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
                }
                break;

            case PLAYING:
                // Jump mechanic
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    velocity = jumpVelocity;
                    audioManager.playSoundEffect("jump");
                }

                // Apply gravity
                velocity += gravity * deltaTime;

                // Update bird position
                bird.y -= velocity * deltaTime;

                // Keep bird on screen
                if (bird.y < 0) {
                    bird.y = 0;
                    velocity = 0;
                }
                if (bird.y > DisplayManager.getScreenHeight() - bird.height) {
                    bird.y = DisplayManager.getScreenHeight() - bird.height;
                    velocity = 0;
                }

                // Spawn new pipes
                timeSinceLastPipe += deltaTime;
                if (timeSinceLastPipe > pipeInterval) {
                    spawnPipe();
                    timeSinceLastPipe = 0;
                }

                // Move pipes and check collision
                updatePipesAndCollisions(deltaTime);

                break;

            case GAME_OVER:
                // Wait for delay, then return to game
                currentDelay += deltaTime;
                if (currentDelay >= gameOverDelay) {
                    // Use Q key instead of R to return immediately to avoid conflict
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                        completeGame(false);
                    }
                    // Auto-return to game after delay
                    else if (currentDelay >= gameOverDelay * 2) {
                        completeGame(false);
                    }
                }
                break;
        }
    }

    private void updatePipesAndCollisions(float deltaTime) {
        for (int i = 0; i < topPipes.size; i++) {
            Rectangle topPipe = topPipes.get(i);
            Rectangle bottomPipe = bottomPipes.get(i);

            // Move pipes
            topPipe.x -= pipeSpeed * deltaTime;
            bottomPipe.x -= pipeSpeed * deltaTime;

            // Check for scoring - bird has passed pipe
            if (bird.x > topPipe.x + topPipe.width && !passedPipes.contains(i, false)) {
                passedPipes.add(i);
                score++;
                audioManager.playSoundEffect("ding");
            }

            // Improved collision detection with precise hitboxes
            boolean topCollision = bird.y < topPipe.y + topPipe.height &&
                                 bird.y + bird.height > topPipe.y &&
                                 bird.x + bird.width > topPipe.x &&
                                 bird.x < topPipe.x + topPipe.width;

            boolean bottomCollision = bird.y < bottomPipe.y + bottomPipe.height &&
                                    bird.y + bird.height > bottomPipe.y &&
                                    bird.x + bird.width > bottomPipe.x &&
                                    bird.x < bottomPipe.x + bottomPipe.width;

            // If collision detected
            if (topCollision || bottomCollision) {
                if (debugCollision) {
                    System.out.println("Collision detected with pipe " + i);
                    if (topCollision) System.out.println("Top pipe collision");
                    if (bottomCollision) System.out.println("Bottom pipe collision");
                }

                state = GameState.GAME_OVER;
                audioManager.playSoundEffect("hit");
                currentDelay = 0;
                break;
            }
        }

        // Remove pipes that are off screen
        for (int i = 0; i < topPipes.size; i++) {
            if (topPipes.get(i).x < -pipeWidth) {
                topPipes.removeIndex(i);
                bottomPipes.removeIndex(i);

                // Update passed pipes indices
                for (int j = 0; j < passedPipes.size; j++) {
                    if (passedPipes.get(j) > i) {
                        passedPipes.set(j, passedPipes.get(j) - 1);
                    }
                }

                // Remove from passedPipes if needed
                for (int j = 0; j < passedPipes.size; j++) {
                    if (passedPipes.get(j) == i) {
                        passedPipes.removeIndex(j);
                        break;
                    }
                }

                // Decrement index because we removed an item
                i--;
            }
        }
    }

    // Modify completeGame method to notify the handler
    private void completeGame(boolean success) {
        gameCompleted = true;

        // Add the minigame score to the main game score
        pointsManager.addPoints(score * 10);
        System.out.println("Mini-game completed! Added " + (score * 10) + " points from mini-game score.");
        
        // Penalize player if they died early (before 15 seconds) by reducing health instead of points
        if (!success && gameTime < 15.0f) {
            // Only reduce health if player has more than 1 life remaining
            if (PlayerStatus.getInstance().getLives() > 1) {
                PlayerStatus.getInstance().decrementLife();
                System.out.println("Died early in mini-game (before 15 seconds). Lost 1 life!");
            } else {
                System.out.println("Died early in mini-game, but only 1 life remaining. No life penalty applied.");
            }
        }

        // Stop mini-game music
        audioManager.stopSoundEffect("minigame");

        // Return to main game
        PlayerStatus.getInstance().setInMiniGame(false);
        sceneManager.removeOverlay();

        // Notify handler that mini-game is completed
        if (miniGameHandler != null) {
            miniGameHandler.onMiniGameCompleted();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // We need to end the batch before drawing shapes,
        // and begin it again afterward
        batch.end();

        // Draw sky background using our local shape renderer
        localShapeRenderer.begin(ShapeType.Filled);
        localShapeRenderer.setColor(skyColor);
        localShapeRenderer.rect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
        localShapeRenderer.end();

        // Restart the batch for sprites
        batch.begin();

        // Draw pipes
        for (int i = 0; i < topPipes.size; i++) {
            Rectangle topPipe = topPipes.get(i);
            batch.draw(pipeTopTexture,
                      topPipe.x, topPipe.y);
        }

        for (int i = 0; i < bottomPipes.size; i++) {
            Rectangle bottomPipe = bottomPipes.get(i);
            batch.draw(pipeBottomTexture,
                      bottomPipe.x, bottomPipe.y);
        }

        // Draw bird
        if (birdTexture != null) {
            batch.draw(birdTexture, bird.x, bird.y, bird.width, bird.height);
        }

        // Draw UI
        textManager.draw(batch, "Score: " + score, 20, DisplayManager.getScreenHeight() - 20, Color.WHITE);
        textManager.draw(batch, "Time: " + (int)(maxGameTime - gameTime), DisplayManager.getScreenWidth() - 150, DisplayManager.getScreenHeight() - 20, Color.WHITE);

        switch (state) {
            case READY:
                textManager.draw(batch, "FLAPPY SPACE CLEANER", DisplayManager.getScreenWidth()/2 - 180,
                                DisplayManager.getScreenHeight()/2 + 50, Color.YELLOW);
                textManager.draw(batch, "Press SPACE to start", DisplayManager.getScreenWidth()/2 - 150,
                                DisplayManager.getScreenHeight()/2, Color.WHITE);
                textManager.draw(batch, "Avoid the pipes!", DisplayManager.getScreenWidth()/2 - 100,
                                DisplayManager.getScreenHeight()/2 - 50, Color.WHITE);
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

            case PLAYING:
                // Game UI already drawn above
                break;
        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // Skip the external shape rendering to avoid conflicts with main game
        // We handle all our shape drawing internally using localShapeRenderer

        // Only draw debug collision info if enabled
        if (debugCollision && state == GameState.PLAYING) {
            localShapeRenderer.setProjectionMatrix(shape.getProjectionMatrix());
            localShapeRenderer.begin(ShapeType.Line);
            localShapeRenderer.setColor(Color.RED);

            // Draw bird hitbox
            localShapeRenderer.rect(bird.x, bird.y, bird.width, bird.height);

            // Draw pipe hitboxes
            for (Rectangle topPipe : topPipes) {
                localShapeRenderer.rect(topPipe.x, topPipe.y, topPipe.width, topPipe.height);
            }

            for (Rectangle bottomPipe : bottomPipes) {
                localShapeRenderer.rect(bottomPipe.x, bottomPipe.y, bottomPipe.width, bottomPipe.height);
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
    }
}
