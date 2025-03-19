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
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.TextManager;
import io.github.team2.Utils.DisplayManager;

public class AsteroidDodgeMiniGame extends Scene {
    // Game state
    private enum GameState { READY, PLAYING, GAME_OVER }
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
                asteroidTexture = new Texture(Gdx.files.internal("plastic-bottle-2.png"));
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
        asteroid.width = MathUtils.random(30, 70);
        asteroid.height = asteroid.width;
        asteroid.x = MathUtils.random(0, DisplayManager.getScreenWidth() - asteroid.width);
        asteroid.y = DisplayManager.getScreenHeight();  // Start above screen
        
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
                // Move ship with arrow keys
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && ship.x > 0) {
                    ship.x -= shipSpeed * deltaTime;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && ship.x < DisplayManager.getScreenWidth() - ship.width) {
                    ship.x += shipSpeed * deltaTime;
                }

                // Spawn new asteroids
                timeSinceLastAsteroid += deltaTime;
                if (timeSinceLastAsteroid > asteroidSpawnInterval) {
                    spawnAsteroid();
                    timeSinceLastAsteroid = 0;
                    
                    // Make game progressively harder
                    asteroidSpawnInterval = Math.max(0.3f, asteroidSpawnInterval - 0.02f);
                }

                // Update asteroids and check collisions
                updateAsteroidsAndCollisions(deltaTime);
                
                // Increase score over time
                score = (int)(gameTime * 10);
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
        }
    }

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

    private void completeGame(boolean success) {
        gameCompleted = true;

        // Add the minigame score to the main game score
        pointsManager.addPoints(score);
        System.out.println("Asteroid Dodge completed! Added " + score + " points from mini-game score.");
        
        // Penalize player if they died early (before 15 seconds) by reducing health
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
                textManager.draw(batch, "Use LEFT/RIGHT arrows to move", DisplayManager.getScreenWidth()/2 - 200,
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
        }
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