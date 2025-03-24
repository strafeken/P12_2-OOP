package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import abstractengine.utils.DisplayManager;

/**
 * Manages pipe generation and recycling for FlappyBirdGame.
 */
public class PipeManager {
    // Constants for pipe generation
    private static final float PIPE_WIDTH = 64;
    private static final float PIPE_GAP = 150;
    private static final float PIPE_SPACING = 200;
    private static final float PIPE_SPEED = 150;

    // Pipes
    private Array<Pipe> pipes;
    private Texture pipeTexture;
    private float timeSinceLastPipe;

    // Screen bounds
    private float screenWidth;
    private float screenHeight;

    /**
     * Creates a new PipeManager
     *
     * @param pipeTexture The texture for pipes
     */
    public PipeManager(Texture pipeTexture) {
        this.pipeTexture = pipeTexture;
        this.pipes = new Array<>();
        this.timeSinceLastPipe = 0;
        this.screenWidth = DisplayManager.getScreenWidth();
        this.screenHeight = DisplayManager.getScreenHeight();

        // Create initial pipes
        generatePipe();
    }

    /**
     * Update pipe positions and generate new pipes as needed
     *
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        // Update time since last pipe
        timeSinceLastPipe += deltaTime;

        // Generate new pipe if needed
        if (timeSinceLastPipe > PIPE_SPACING / PIPE_SPEED) {
            generatePipe();
            timeSinceLastPipe = 0;
        }

        // Update all pipes
        for (int i = 0; i < pipes.size; i++) {
            Pipe pipe = pipes.get(i);
            pipe.update(deltaTime, PIPE_SPEED);

            // Remove pipes that are off screen
            if (pipe.getX() + PIPE_WIDTH < 0) {
                pipes.removeIndex(i);
                i--;
            }
        }
    }

    /**
     * Generate a new pipe pair
     */
    private void generatePipe() {
        // Fixed gap between pipes
        float gap = PIPE_GAP;

        // Define limits for bottom pipe height (in pixels)
        float minOffset = 50f;
        float maxOffset = screenHeight - gap - 50f;

        // Randomly choose a bottom pipe height (this offset)
        float offsetY = MathUtils.random(minOffset, maxOffset);

        float bottomPipeHeight = offsetY;
        float topPipeHeight = screenHeight - gap - offsetY;

        // Bottom pipe: anchored at y = 0
        float bottomPipeY = bottomPipeHeight / 2;

        // For the top pipe, add an extra offset to have it spawn further above the screen
        float extra = 30f; // adjust as needed
        float topPipeY = screenHeight + extra - topPipeHeight / 2;

        // Create bottom pipe anchored at the bottom
        Pipe bottomPipe = new Pipe(pipeTexture,
                                   screenWidth, // starting X position (offscreen right)
                                   bottomPipeY,
                                   PIPE_WIDTH,
                                   bottomPipeHeight,
                                   false);

        // Create top pipe so that its top edge is above the screen
        Pipe topPipe = new Pipe(pipeTexture,
                                screenWidth, // starting X position (offscreen right)
                                topPipeY,
                                PIPE_WIDTH,
                                topPipeHeight,
                                true);

        pipes.add(bottomPipe);
        pipes.add(topPipe);

        // Reset timer for pipe generation
        timeSinceLastPipe = 0;
    }

    /**
     * Get all pipes
     */
    public Array<Pipe> getPipes() {
        return pipes;
    }

    /**
     * Reset all pipes
     */
    public void reset() {
        pipes.clear();
        timeSinceLastPipe = 0;
        generatePipe();
    }
}
