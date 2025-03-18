package io.github.team2;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import io.github.team2.Actions.StartGame;
import io.github.team2.Actions.StartLevelSelect;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;  // Need this import for the getInstance method
import io.github.team2.SceneSystem.SceneManager;  // Use this interface for the variable type
import io.github.team2.Utils.DisplayManager;

public class GameOverScreen extends Scene {
    private int finalScore;
    private Button restartButton;
    private IAudioManager audioManager;
    private String currentTrivia; // Store current random trivia
    private Random random;

    private static final String[] TRIVIA = {
        "Space debris as small as 1cm can cause catastrophic damage to satellites due to high orbital velocities.",
        "There are over 500,000 pieces of space junk being tracked as they orbit Earth.",
        "The Great Pacific Garbage Patch is three times the size of France.",
        "It takes a plastic bottle up to 450 years to decompose in the ocean.",
        "The first piece of space debris was the 1957 Sputnik 1 mission's rocket booster.",
        "Only 9% of all plastic ever produced has been recycled.",
        "A single recycled plastic bottle saves enough energy to power a computer for 25 minutes.",
        "The International Space Station has to perform debris avoidance maneuvers about once a year.",
        "By 2050, there could be more plastic than fish in the oceans by weight.",
        "The aluminum in a single soda can, if recycled, can power a TV for three hours.",
        "Space debris travels at speeds up to 17,500 mph in orbit.",
        "Every year, 8 million metric tons of plastic enter our oceans.",
        "The average American generates about 4.4 pounds of trash per day.",
        "NASA maintains a catalog of over 47,000 pieces of orbital debris.",
        "Recycling one ton of paper saves 17 trees and 7,000 gallons of water."
    };

    public GameOverScreen() {
        super();
        random = new Random();
    }

    public void setFinalScore(int score) {
        this.finalScore = score;
    }

    @Override
    public void load() {
        System.out.println("Game Over Screen => LOAD");

        // Initialize managers
        entityManager = new EntityManager();
        gameInputManager = new GameInputManager();
        textManager = new TextManager();

        // Get random trivia
        currentTrivia = TRIVIA[random.nextInt(TRIVIA.length)];

        // Use StartLevelSelect instead of StartGame
        ISceneManager sceneManager = SceneManager.getInstance();
        StartLevelSelect levelSelectAction = new StartLevelSelect(sceneManager);
        Vector2 centerPos = new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2 - 200);
        restartButton = new Button("restartBtn.png", centerPos, levelSelectAction, 100, 100);

        gameInputManager.registerKeyUp(Input.Keys.SPACE, levelSelectAction);
        gameInputManager.registerClickable(restartButton);

        // Get instance through concrete class but store as interface type
        audioManager = AudioManager.getInstance();
    }

    @Override
    public void update() {
    	entityManager.update();
    	gameInputManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        float centerX = DisplayManager.getScreenWidth() / 2 - 100;
        float centerY = DisplayManager.getScreenHeight() / 2;

        // Draw game over text and score
        textManager.draw(batch, "GAME OVER", centerX, centerY + 100, Color.RED);
        textManager.draw(batch, "Final Score: " + finalScore, centerX, centerY + 50, Color.WHITE);

        // Draw "Did you know?" section
        textManager.draw(batch, "Did you know?", centerX, centerY, Color.YELLOW);

        // Draw trivia text with word wrapping
        drawWrappedText(batch, currentTrivia, centerX - 100, centerY - 50, 400, Color.WHITE);

        // Draw return instruction
        textManager.draw(batch, "Press SPACE to Return to Level Select",
                        centerX - 100, centerY - 150, Color.WHITE);
        restartButton.draw(batch);
    }

    // Helper method to draw wrapped text
    private void drawWrappedText(SpriteBatch batch, String text, float x, float y,
                               float maxWidth, Color color) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float lineY = y;

        // Use glyphLayout to calculate text dimensions
        textManager.getFont().getData().setScale(textManager.getFont().getScaleX());

        for (String word : words) {
            String oldLine = line.toString();
            String newLine = oldLine.length() > 0 ? oldLine + " " + word : word;
            float width = textManager.getFont().draw(batch, newLine, 0, 0).width;

            if (width > maxWidth && !oldLine.isEmpty()) {
                // Draw current line and move to next line
                textManager.draw(batch, oldLine, x, lineY, color);
                line = new StringBuilder(word);
                lineY -= 30; // Line spacing
            } else {
                // Add word to current line
                if (oldLine.length() > 0) {
                    line.append(" ");
                }
                line.append(word);
            }
        }

        // Draw the last line
        if (line.length() > 0) {
            textManager.draw(batch, line.toString(), x, lineY, color);
        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // No shapes to draw
    }

    @Override
    public void unload() {
        System.out.println("Game Over Screen => UNLOAD");
        dispose();
    }

    @Override
    public void dispose() {
        entityManager.dispose();
        textManager.dispose();
    }
}
