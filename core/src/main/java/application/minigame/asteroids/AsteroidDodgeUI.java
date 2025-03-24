package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import abstractengine.utils.DisplayManager;
import application.minigame.common.MiniGameUI;

/**
 * Handles UI rendering for AsteroidDodgeGame.
 */
public class AsteroidDodgeUI extends MiniGameUI {
    private int score = 0;
    private float timeLimit = 60f; // Set to 60 seconds

    @Override
    protected void drawReadyStateInstructions(SpriteBatch batch, int centerX, int centerY) {
        textManager.draw(batch, "Use W,A,S,D to move", centerX - 130, centerY - 30, INSTRUCTION_COLOR);
        textManager.draw(batch, "Avoid the asteroids!", centerX - 120, centerY - 60, INSTRUCTION_COLOR);
        //textManager.draw(batch, "+5 points per 2 seconds", centerX - 130, centerY - 90, SCORE_COLOR);
    }

    @Override
    protected void drawGameOverUI(SpriteBatch batch, float gameOverTime) {
        int centerX = (int)(DisplayManager.getScreenWidth() / 2);
        int centerY = (int)(DisplayManager.getScreenHeight() / 2);

        textManager.draw(batch, "GAME OVER!", centerX - 100, centerY + 50, WARNING_COLOR);
        textManager.draw(batch, "Final Score: " + score, centerX - 100, centerY, SCORE_COLOR);
        textManager.draw(batch, "Returning to game...", centerX - 140, centerY - 50, INSTRUCTION_COLOR);

        // Show proper countdown from 3 to 0
        int remainingTime = 3 - Math.min(3, (int)gameOverTime);
        textManager.draw(batch, "Press ENTER for quick return", centerX - 140, centerY - 80, INSTRUCTION_COLOR);
        textManager.draw(batch, "(" + remainingTime + "s)", centerX, centerY - 110, INSTRUCTION_COLOR);
    }

    /**
     * Draw the UI for the playing state
     */
    @Override
    protected void drawPlayingUI(SpriteBatch batch, float gameTime) {
        // Debug output (can be removed later)
        //System.out.println("AsteroidDodgeUI.drawPlayingUI - score: " + score + ", gameTime: " + gameTime);

        // Draw score at the top left (make it larger and more visible)
        textManager.draw(batch, "SCORE: " + score, 20, DisplayManager.getScreenHeight() - 20, Color.YELLOW);

        // Draw game time remaining (countdown)
        float timeRemaining = Math.max(0, timeLimit - gameTime);
        textManager.draw(batch, "TIME: " + (int)timeRemaining,
                        DisplayManager.getScreenWidth() - 150,
                        DisplayManager.getScreenHeight() - 20, Color.WHITE);
    }

    /**
     * Set the current score
     */
    public void setScore(int score) {
        this.score = score;
        System.out.println("AsteroidDodgeUI - Score updated: " + score); // Debug output
    }

    /**
     * Set the time limit
     */
    public void setTimeLimit(float timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    protected int getScore() {
        return score;
    }

    @Override
    protected String getGameTitle() {
        return "Asteroid Dodge";
    }

}
