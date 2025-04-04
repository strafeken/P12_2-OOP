package application.scene;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import abstractengine.audio.AudioManager;
import abstractengine.audio.IAudioManager;
import abstractengine.entity.Action;
import abstractengine.entity.EntityManager;
import abstractengine.entity.StaticTextureObject;
import abstractengine.io.TextManager;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.Scene;
import abstractengine.scene.SceneManager;
import abstractengine.utils.DisplayManager;
import application.entity.EntityType;
import application.entity.PlayerStatus;
import application.io.GameInputManager;

public class GameOverScreen extends Scene {
    private int finalScore;
    private String currentTrivia; // Store current random trivia
    private Random random;
    private StaticTextureObject backgroundImage;

    // Add input delay to prevent immediate key detection
    private float inputDelay = 1.0f; // 1 second delay

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

        // Reset input delay when screen loads
        inputDelay = 1.0f;

        // Load background image
        backgroundImage = new StaticTextureObject(
            EntityType.UNDEFINED,
            "space_background.jpg",  // Using the same background as level select
            new Vector2(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight()),
            new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2),
            new Vector2(0, 0)
        );
        entityManager.addEntities(backgroundImage);

        // Get random trivia
        currentTrivia = TRIVIA[random.nextInt(TRIVIA.length)];

        AudioManager.getInstance().playSoundEffect("gameover");
    }

    @Override
    public void update() {
        entityManager.update();

        // Handle input delay
        if (inputDelay > 0) {
            inputDelay -= Gdx.graphics.getDeltaTime();

            // Once delay is complete, register the key handlers
            if (inputDelay <= 0) {
                // Get scene manager and create actions
                ISceneManager sm = SceneManager.getInstance();

                // Create restart action to restart the CURRENT level, not start a new game
                Action restartAction = () -> {
                    // Get the current level and restart it
                    LevelManager levelManager = LevelManager.getInstance();
                    int level = levelManager.getCurrentLevel();
                    SceneID sceneID;

                    switch (level) {
                        case 2:
                            sceneID = SceneID.LEVEL2;
                            break;
                        case 3:
                            sceneID = SceneID.LEVEL3;
                            break;
                        case 4:
                            sceneID = SceneID.LEVEL4;
                            break;
                        default:
                            sceneID = SceneID.LEVEL1;
                            break;
                    }

                    // Reset player status (lives, etc.)
                    PlayerStatus.getInstance().reset();

                    // Transition to the level
                    sm.setNextScene(sceneID);
                    System.out.println("Restarting current level: " + level);
                };

                // Register key handlers
                gameInputManager.registerKeyUp(Input.Keys.SPACE, restartAction);
                gameInputManager.registerKeyUp(Input.Keys.L, () -> sm.setNextScene(SceneID.LEVEL_SELECT));

                System.out.println("Game Over Screen key controls now active");
            }
        }

        gameInputManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw the background image first
        entityManager.draw(batch);

        // Add semi-transparent overlay for better text contrast
        batch.end();

        ShapeRenderer localShapeRenderer = new ShapeRenderer();
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        localShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        localShapeRenderer.setColor(0, 0, 0, 0.6f); // Semi-transparent black overlay
        localShapeRenderer.rect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
        localShapeRenderer.end();

        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
        localShapeRenderer.dispose();

        // Resume batch for text
        batch.begin();

        float centerX = DisplayManager.getScreenWidth() / 2 - 100;
        float centerY = DisplayManager.getScreenHeight() / 2;

        // Draw game over text and score
        textManager.draw(batch, "GAME OVER", centerX, centerY + 100, Color.RED);
        textManager.draw(batch, "Final Score: " + finalScore, centerX, centerY + 50, Color.WHITE);

        // Draw "Did you know?" section
        textManager.draw(batch, "Did you know?", centerX, centerY, Color.YELLOW);

        // Draw trivia text with word wrapping
        drawWrappedText(batch, currentTrivia, centerX - 100, centerY - 50, 400, Color.WHITE);

        // Draw return instruction - updated to show both options
        textManager.draw(batch, "Press SPACE to Restart Current Level",
                        centerX - 100, centerY - 150, Color.WHITE);
        textManager.draw(batch, "Press L to Return to Level Select",
                        centerX - 100, centerY - 180, Color.WHITE);
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
