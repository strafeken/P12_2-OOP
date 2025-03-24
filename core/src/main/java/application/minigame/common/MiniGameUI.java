package application.minigame.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import abstractengine.io.TextManager;
import abstractengine.utils.DisplayManager;

/**
 * Base class for mini-game UI rendering.
 * Follows the Strategy Pattern for UI rendering.
 */
public abstract class MiniGameUI {
    protected TextManager textManager;

    // Colors for UI elements
    protected static final Color TITLE_COLOR = Color.WHITE;
    protected static final Color INSTRUCTION_COLOR = Color.LIGHT_GRAY;
    protected static final Color SCORE_COLOR = Color.YELLOW;
    protected static final Color WARNING_COLOR = Color.RED;
    protected static final Color SELECTED_COLOR = Color.YELLOW;
    protected static final Color UNSELECTED_COLOR = Color.WHITE;

    public MiniGameUI() {
        this.textManager = new TextManager();
        System.out.println("MiniGameUI initialized with textManager");
    }

    /**
     * Draw the UI for the current game state
     *
     * @param batch The sprite batch to draw with
     * @param state The current game state
     * @param gameTime The current game time
     */
    public void draw(SpriteBatch batch, GameState state, float gameTime, float timeLimit) {
        if (batch == null) {
            System.err.println("ERROR: SpriteBatch is null in MiniGameUI.draw()");
            return;
        }

        if (textManager == null) {
            System.err.println("WARNING: TextManager is null, creating new instance");
            textManager = new TextManager();
        }

        // Draw UI based on game state
        System.out.println("MiniGameUI drawing for state: " + state);

        switch (state) {
            case READY:
                drawReadyUI(batch);
                break;

            case PLAYING:
                drawPlayingUI(batch, gameTime, timeLimit);
                break;

            case GAME_OVER:
                drawGameOverUI(batch, gameTime); // Use gameTime as gameOverTimer
                break;

            case CONFIRM_EXIT:
                // We need to get the confirmYes value from AbstractMiniGame
                // So we'll modify AbstractMiniGame to pass it when calling draw
                drawConfirmExitDialog(batch, AbstractMiniGame.getConfirmYes());
                break;
        }
    }

    // Add backward compatibility method
    public void draw(SpriteBatch batch, GameState state, float gameTime) {
        draw(batch, state, gameTime, 60f); // Default to 60 seconds
    }

    /**
     * Draw the UI for the ready state
     */
    protected void drawReadyUI(SpriteBatch batch) {
        // Draw common ready state elements (game title, instructions)
        int centerX = (int)(DisplayManager.getScreenWidth() / 2);
        int centerY = (int)(DisplayManager.getScreenHeight() / 2);

        textManager.draw(batch, getGameTitle(), centerX - 150, centerY + 50, TITLE_COLOR);
        textManager.draw(batch, "Press SPACE to start", centerX - 120, centerY - 100, INSTRUCTION_COLOR);

        // Draw game-specific instructions
        drawReadyStateInstructions(batch, centerX, centerY);
    }

    /**
     * Draw the UI for the playing state
     */
    protected void drawPlayingUI(SpriteBatch batch, float gameTime) {
        // Draw score and time remaining
        textManager.draw(batch, "Score: " + getScore(), 20, DisplayManager.getScreenHeight() - 20, SCORE_COLOR);
        textManager.draw(batch, "Time: " + (int)gameTime, DisplayManager.getScreenWidth() - 100, DisplayManager.getScreenHeight() - 20, SCORE_COLOR);
    }

    /**
     * Draw the UI for the playing state
     */
    protected void drawPlayingUI(SpriteBatch batch, float gameTime, float timeLimit) {
        // Use normal text size - no scaling

        // Draw score at the top left
        textManager.draw(batch, "SCORE: " + getScore(), 20, DisplayManager.getScreenHeight() - 20, Color.YELLOW);

        // Draw game time remaining (countdown)
        float timeRemaining = Math.max(0, timeLimit - gameTime);
        textManager.draw(batch, "TIME: " + (int)timeRemaining,
                        DisplayManager.getScreenWidth() - 150,
                        DisplayManager.getScreenHeight() - 20, Color.WHITE);

        // No need to reset scale since we didn't change it
    }

    /**
     * Draw the UI for the game over state
     */
    protected abstract void drawGameOverUI(SpriteBatch batch, float gameTime);

    /**
     * Draw game-specific ready state instructions
     */
    protected abstract void drawReadyStateInstructions(SpriteBatch batch, int centerX, int centerY);

    /**
     * Draw the exit confirmation dialog
     */
    public void drawConfirmExitDialog(SpriteBatch batch, boolean confirmYes) {
        int centerX = (int)(DisplayManager.getScreenWidth() / 2);
        int centerY = (int)(DisplayManager.getScreenHeight() / 2);

        // Draw semi-transparent background rectangle
        // This would normally use a shape renderer, but since we're only using SpriteBatch here,
        // we'll assume there's a background drawn elsewhere

        // Draw dialog text
        textManager.draw(batch, "Exit Mini-Game?", centerX - 100, centerY + 50, WARNING_COLOR);
        textManager.draw(batch, "You will lose one life!", centerX - 130, centerY, INSTRUCTION_COLOR);

        // Draw options
        textManager.draw(batch, "Yes", centerX - 80, centerY - 50,
                        confirmYes ? SELECTED_COLOR : UNSELECTED_COLOR);
        textManager.draw(batch, "No", centerX + 40, centerY - 50,
                        confirmYes ? UNSELECTED_COLOR : SELECTED_COLOR);

        // Draw navigation help
        textManager.draw(batch, "Use W/S or Up/Down to select", centerX - 160, centerY - 100, INSTRUCTION_COLOR);
        textManager.draw(batch, "Press SPACE to confirm", centerX - 130, centerY - 130, INSTRUCTION_COLOR);
    }

    /**
     * Get the current score
     */
    protected abstract int getScore();

    /**
     * Get the game title
     */
    protected abstract String getGameTitle();
}
