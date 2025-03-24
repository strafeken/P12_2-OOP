package application.minigame.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import abstractengine.utils.DisplayManager;

/**
 * Handles all rendering for the Asteroid Dodge mini-game.
 * Following the Single Responsibility Principle, this class focuses only on rendering.
 */
public class AsteroidDodgeRenderer {
    private Texture backgroundTexture;
    private Ship ship;
    private AsteroidManager asteroidManager;
    private boolean usingFallbackTexture;
    private Color skyColor;
    private ShapeRenderer shapeRenderer;

    /**
     * Creates a new AsteroidDodgeRenderer
     *
     * @param backgroundTexture The background texture
     * @param ship The player ship
     * @param asteroidManager The asteroid manager
     * @param usingFallbackTexture Whether using fallback textures
     * @param skyColor The sky color for fallback background
     */
    public AsteroidDodgeRenderer(Texture backgroundTexture, Ship ship, AsteroidManager asteroidManager,
                               boolean usingFallbackTexture, Color skyColor) {
        this.backgroundTexture = backgroundTexture;
        this.ship = ship;
        this.asteroidManager = asteroidManager;
        this.usingFallbackTexture = usingFallbackTexture;
        this.skyColor = skyColor;
        this.shapeRenderer = new ShapeRenderer();
    }

    /**
     * Renders the game
     *
     * @param batch The sprite batch for rendering
     * @param externalShapeRenderer An external shape renderer for debug shapes
     */
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Draw background
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
        }

        // Draw ship
        if (ship != null && ship.getTexture() != null) {
            Rectangle bounds = ship.getBounds();
            batch.draw(ship.getTexture(), bounds.x, bounds.y, bounds.width, bounds.height);

            // Comment out or remove debug info
            // System.out.println("Drawing ship at: " + bounds.x + "," + bounds.y +
            //                  " size: " + bounds.width + "x" + bounds.height);
        }

        // Draw asteroids
        if (asteroidManager != null) {
            for (Asteroid asteroid : asteroidManager.getAsteroids()) {
                if (asteroid.getTexture() != null) {
                    Rectangle bounds = asteroid.getBounds();
                    batch.draw(asteroid.getTexture(), bounds.x, bounds.y, bounds.width, bounds.height);

                    // Comment out or remove debug info
                    // System.out.println("Drawing asteroid at: " + bounds.x + "," + bounds.y +
                    //                  " size: " + bounds.width + "x" + bounds.height);
                }
            }
        }
    }

    /**
     * Renders the background
     */
    private void renderBackground() {
        if (backgroundTexture == null) {
            // Fallback to solid color if texture failed to load
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(skyColor);
            shapeRenderer.rect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
            shapeRenderer.end();
        } else {
            SpriteBatch backgroundBatch = new SpriteBatch();
            backgroundBatch.begin();
            backgroundBatch.draw(backgroundTexture, 0, 0,
                               DisplayManager.getScreenWidth(),
                               DisplayManager.getScreenHeight());
            backgroundBatch.end();
            backgroundBatch.dispose();
        }

        // If using fallback texture, draw some simple stars
        if (usingFallbackTexture) {
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            for (int i = 0; i < 100; i++) {
                float x = MathUtils.random(0, DisplayManager.getScreenWidth());
                float y = MathUtils.random(0, DisplayManager.getScreenHeight());
                float size = MathUtils.random(1, 3);
                shapeRenderer.rect(x, y, size, size);
            }
            shapeRenderer.end();
        }
    }

    /**
     * Renders the player ship
     *
     * @param batch The sprite batch for rendering
     */
    private void renderShip(SpriteBatch batch) {
        Texture shipTexture = ship.getTexture();
        if (shipTexture != null) {
            batch.draw(shipTexture, ship.getBounds().x, ship.getBounds().y,
                    ship.getBounds().width, ship.getBounds().height);
        }
    }

    /**
     * Renders the asteroids
     *
     * @param batch The sprite batch for rendering
     */
    private void renderAsteroids(SpriteBatch batch) {
        Array<Asteroid> asteroids = asteroidManager.getAsteroids();
        for (Asteroid asteroid : asteroids) {
            batch.draw(asteroid.getTexture(), asteroid.getBounds().x, asteroid.getBounds().y,
                    asteroid.getBounds().width, asteroid.getBounds().height);
        }
    }

    /**
     * Renders debug information
     *
     * @param shapeRenderer The shape renderer for debug shapes
     * @param physics The physics handler for collision data
     * @param gameTime The current game time
     * @param collisionGracePeriod The collision grace period
     */
    public void renderDebug(ShapeRenderer shapeRenderer, AsteroidDodgePhysics physics,
                          float gameTime, float collisionGracePeriod) {
        // REMOVE begin/end calls - batch is already started by GameMaster

        // Draw ship hitbox
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(ship.getBounds().x, ship.getBounds().y,
                ship.getBounds().width, ship.getBounds().height);

        // Draw asteroid hitboxes
        Array<Asteroid> asteroids = asteroidManager.getAsteroids();
        for (Asteroid asteroid : asteroids) {
            shapeRenderer.rect(asteroid.getBounds().x, asteroid.getBounds().y,
                    asteroid.getBounds().width, asteroid.getBounds().height);
        }

        // Show grace period bar
        if (gameTime < collisionGracePeriod) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(10, 10, (collisionGracePeriod - gameTime) * 100, 10);
        }
    }

    /**
     * Render only the background
     *
     * @param batch The sprite batch to draw with
     */
    public void renderBackground(SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // First draw a solid color to ensure complete coverage
        batch.end();

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0.1f, 1); // Dark blue background
        shapeRenderer.rect(0, 0, screenWidth, screenHeight);
        shapeRenderer.end();
        shapeRenderer.dispose();

        batch.begin();

        // Then draw the texture on top
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
        }
    }

    /**
     * Disposes of resources
     */
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
