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
    private static final float MIN_GAP = 150;
    private static final float MAX_GAP = 200;
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
        // Random gap between MIN_GAP and MAX_GAP
        float gap = MathUtils.random(MIN_GAP, MAX_GAP);

        // Set minimum and maximum bottom pipe heights
        float minBottomHeight = 10f; // adjust as desired
        float maxBottomHeight = screenHeight - gap - 10f; // ensure top pipe has at least 10f height

        // Generate a random bottom pipe height within the allowed range
        float bottomPipeHeight = MathUtils.random(minBottomHeight, maxBottomHeight);

        // Calculate top pipe height based on bottom pipe height and gap
        float topPipeHeight = screenHeight - gap - bottomPipeHeight;

        // Bottom pipe: position its center so that its bottom edge is at y = -10.
        // (Center = bottom edge + half the height)
        float bottomPipeY = bottomPipeHeight / 2f;

        // Top pipe: position its center so that its bottom edge aligns exactly with the top of the screen.
        float topPipeY = screenHeight - topPipeHeight / 2f;

        // Create the bottom and top pipes.
        Pipe bottomPipe = new Pipe(pipeTexture,
                                   screenWidth, // starting X position (offscreen right)
                                   bottomPipeY,
                                   PIPE_WIDTH,
                                   bottomPipeHeight,
                                   false);

        Pipe topPipe = new Pipe(pipeTexture,
                                screenWidth, // starting X position (offscreen right)
                                topPipeY,
                                PIPE_WIDTH,
                                topPipeHeight,
                                true);

        pipes.add(bottomPipe);
        pipes.add(topPipe);

        // Reset pipe generation timer.
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
