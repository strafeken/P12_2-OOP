package application.minigame.flappybird;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import abstractengine.utils.DisplayManager;

/**
 * Handles physics calculations for FlappyBirdGame.
 */
public class FlappyBirdPhysics {
    // Physics constants
    private static final float GRAVITY = 600.0f;
    private static final float JUMP_VELOCITY = 350.0f;

    // Game objects
    private Bird bird;
    private PipeManager pipeManager;

    // Collision tracking
    private boolean hasCollided;
    // Removed newlyPassedPipes and added cumulative score
    private int score;

    /**
     * Creates a new FlappyBirdPhysics instance
     *
     * @param bird The bird object
     * @param pipeManager The pipe manager
     */
    public FlappyBirdPhysics(Bird bird, PipeManager pipeManager) {
        this.bird = bird;
        this.pipeManager = pipeManager;
        this.hasCollided = false;
        this.score = 0;
    }

    /**
     * Update physics
     *
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        // Reset collision flag
        hasCollided = false;

        // Apply gravity
        bird.setVelocityY(bird.getVelocityY() - GRAVITY * deltaTime);

        // Update bird position
        Rectangle birdBounds = bird.getBounds();
        birdBounds.y += bird.getVelocityY() * deltaTime;

        // Check collisions with screen boundaries
        if (birdBounds.y < 0) {
            birdBounds.y = 0;
            hasCollided = true;
        } else if (birdBounds.y + birdBounds.height > DisplayManager.getScreenHeight()) {
            birdBounds.y = DisplayManager.getScreenHeight() - birdBounds.height;
            hasCollided = true;
        }

        // Check collisions with pipes
        Array<Pipe> pipes = pipeManager.getPipes();
        for (Pipe pipe : pipes) {
            // Check for collision
            if (Intersector.overlaps(birdBounds, pipe.getBounds())) {
                hasCollided = true;
            }
        }

        // Accumulate score from newly passed pipes
        score += checkForPassedPipes();
    }

    /**
     * Check for pipes that have been passed and return the count
     * (Internal helper method to avoid duplicated logic)
     */
    private int checkForPassedPipes() {
        int passedCount = 0;
        // Use getBounds() to access the Rectangle object properties
        Rectangle birdBounds = bird.getBounds();
        float birdLeftEdge = birdBounds.x;
        Array<Pipe> pipes = pipeManager.getPipes();

        for (int i = 0; i < pipes.size; i++) {
            Pipe pipe = pipes.get(i);

            // Only check bottom pipes to avoid double counting (since pipes come in pairs)
            if (!pipe.isTop()) {
                // Use the start/left edge of the pipe instead of the right edge
                float pipeLeftEdge = pipe.getX();

                // Check if bird has passed the pipe (bird's left edge > pipe's start x)
                if (!pipe.isPassed() && birdLeftEdge > pipeLeftEdge) {
                    pipe.setPassed(true);

                    // Mark its top counterpart as passed, if found
                    int topPipeIndex = findMatchingTopPipe(pipes, pipe);
                    if (topPipeIndex != -1) {
                        pipes.get(topPipeIndex).setPassed(true);
                    }

                    passedCount++;
                    System.out.println("PIPE PASSED! Bird left edge: " + birdLeftEdge +
                                       ", Pipe left edge: " + pipeLeftEdge);
                }
            }
        }

        if (passedCount > 0) {
            System.out.println("TOTAL NEW POINTS: " + passedCount);
        }

        return passedCount;
    }

    /**
     * Find the matching top pipe for a bottom pipe
     * @param pipes The array of pipes
     * @param bottomPipe The bottom pipe to match
     * @return The index of the top pipe or -1 if not found
     */
    private int findMatchingTopPipe(Array<Pipe> pipes, Pipe bottomPipe) {
        float bottomPipeX = bottomPipe.getX();

        // Use a larger tolerance for matching x values
        for (int i = 0; i < pipes.size; i++) {
            Pipe candidatePipe = pipes.get(i);
            if (candidatePipe.isTop() && Math.abs(candidatePipe.getX() - bottomPipeX) < 5) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Make the bird jump
     */
    public void jump() {
        bird.setVelocityY(JUMP_VELOCITY);
    }

    /**
     * Check if bird has collided
     */
    public boolean hasCollidedWithPipe() {
        return hasCollided;
    }

    /**
     * Get the cumulative score (points) from pipes that have been passed
     */
    public int getNewlyPassedPipes() {
        return score;
    }
}
