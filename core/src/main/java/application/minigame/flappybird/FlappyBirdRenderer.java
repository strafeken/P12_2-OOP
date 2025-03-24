package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import abstractengine.utils.DisplayManager;

/**
 * Handles rendering for FlappyBirdGame.
 */
public class FlappyBirdRenderer {
    private Texture backgroundTexture;
    private Bird bird;
    private PipeManager pipeManager;

    /**
     * Creates a new FlappyBirdRenderer
     *
     * @param backgroundTexture The background texture
     * @param bird The bird object
     * @param pipeManager The pipe manager
     */
    public FlappyBirdRenderer(Texture backgroundTexture, Bird bird, PipeManager pipeManager) {
        this.backgroundTexture = backgroundTexture;
        this.bird = bird;
        this.pipeManager = pipeManager;
    }

    /**
     * Render the game
     *
     * @param batch The sprite batch to draw with
     */
    public void render(SpriteBatch batch) {
        // Make sure SpriteBatch is properly configured
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Draw background (tiled if necessary)
        if (backgroundTexture != null) {
            int bgWidth = backgroundTexture.getWidth();
            int bgHeight = backgroundTexture.getHeight();

            // Draw background tiled to cover entire screen
            for (int x = 0; x < screenWidth; x += bgWidth) {
                for (int y = 0; y < screenHeight; y += bgHeight) {
                    batch.draw(backgroundTexture, x, y);
                }
            }
        }

        // Draw pipes - Draw them first so they appear behind the bird
        if (pipeManager != null) {
            for (Pipe pipe : pipeManager.getPipes()) {
                Texture pipeTexture = pipe.getTexture();
                Rectangle bounds = pipe.getBounds();

                if (pipeTexture != null) {
                    if (pipe.isTop()) {
                        // For top pipes, use a simpler approach - just draw it normally
                        // and we'll fix the visual appearance in the PipeManager class
                        batch.draw(pipeTexture, bounds.x, bounds.y, bounds.width, bounds.height);

                        // Debug output
                        //System.out.println("Drawing TOP pipe at x=" + bounds.x + ", y=" + bounds.y);
                    } else {
                        // Bottom pipes
                        batch.draw(pipeTexture, bounds.x, bounds.y, bounds.width, bounds.height);

                        // Debug output
                        //System.out.println("Drawing BOTTOM pipe at x=" + bounds.x + ", y=" + bounds.y);
                    }
                } else {
                    System.err.println("Pipe texture is null!");
                }
            }
        } else {
            System.err.println("PipeManager is null!");
        }

        // Draw bird
        if (bird != null) {
            Texture birdTexture = bird.getTexture();
            Rectangle bounds = bird.getBounds();

            if (birdTexture != null) {
                // Draw bird with rotation based on velocity
                float rotation = Math.min(Math.max(-90, bird.getVelocityY() * 0.2f), 45);

                // Use the rotation to tilt the bird
                batch.draw(
                    birdTexture,         // Texture
                    bounds.x,            // X position
                    bounds.y,            // Y position
                    bounds.width / 2,    // Origin X (center of bird)
                    bounds.height / 2,   // Origin Y (center of bird)
                    bounds.width,        // Width
                    bounds.height,       // Height
                    1f, 1f,              // Scale X, Y
                    rotation,            // Rotation in degrees
                    0, 0,                // Source X, Y
                    birdTexture.getWidth(), birdTexture.getHeight(), // Source width, height
                    false, false         // Flip X, Y
                );
            }
        }
    }

    /**
     * Render only the background
     *
     * @param batch The sprite batch to draw with
     */
    public void renderBackground(SpriteBatch batch) {
        // Make sure SpriteBatch is properly configured
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // First draw a solid color to ensure complete coverage
        // We need to end the batch, use shape renderer, and begin batch again
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
            int bgWidth = backgroundTexture.getWidth();
            int bgHeight = backgroundTexture.getHeight();

            // Draw background tiled to cover entire screen
            for (int x = 0; x < screenWidth; x += bgWidth) {
                for (int y = 0; y < screenHeight; y += bgHeight) {
                    batch.draw(backgroundTexture, x, y);
                }
            }
        }
    }
}
