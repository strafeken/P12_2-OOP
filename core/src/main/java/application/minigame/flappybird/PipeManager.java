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
        // Generate a random gap size between 150-200 pixels
        float gapSize = MathUtils.random(150f, 200f);

        // Calculate the maximum Y position for the bottom pipe
        // (leaves enough space at the top for at least the gap and the top pipe)
        float maxBottomPipeY = screenHeight - gapSize - 50; // 50px minimum top pipe height

        // Calculate random bottom pipe top position (Y coordinate of top edge of bottom pipe)
        float bottomPipeTopY = MathUtils.random(100f, maxBottomPipeY); // 100px minimum from bottom

        // Calculate top pipe bottom Y position (Y coordinate of bottom edge of top pipe)
        float topPipeBottomY = bottomPipeTopY + gapSize;

        // Calculate pipe heights
        float bottomPipeHeight = bottomPipeTopY;
        float topPipeHeight = screenHeight - topPipeBottomY;

        // Create bottom pipe - starts at y=0 and extends upward to bottomPipeTopY
        Pipe bottomPipe = new Pipe(pipeTexture, screenWidth, 0,
                                  PIPE_WIDTH, bottomPipeHeight, false);

        // Create top pipe - starts at topPipeBottomY and extends to the top of the screen
        Pipe topPipe = new Pipe(pipeTexture, screenWidth, topPipeBottomY,
                               PIPE_WIDTH, topPipeHeight, true);

        // Ensure the new pipes are marked as not passed
        bottomPipe.setPassed(false);
        topPipe.setPassed(false);

        // Add pipes to the array
        pipes.add(bottomPipe);
        pipes.add(topPipe);
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
