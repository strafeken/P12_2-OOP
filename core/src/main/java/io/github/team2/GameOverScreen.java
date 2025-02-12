package io.github.team2;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.StartGame;
import io.github.team2.InputSystem.Button;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class GameOverScreen extends Scene {
    private int finalScore;
    private Button restartButton;

    public GameOverScreen() {
        super(); // Initialize base components
    }

    @Override
    public void load() {
        System.out.println("Game Over Screen => LOAD");
        finalScore = GameManager.getInstance().getPointsManager().getPoints();

        // Create restart button
        StartGame startAction = new StartGame(SceneManager.getInstance(SceneManager.class));
        Vector2 centerPos = new Vector2(
            SceneManager.screenWidth / 2,
            SceneManager.screenHeight / 2 - 200
        );

        restartButton = new Button(1, "Restart", "restartBtn.png",
                                 centerPos, startAction, 100, 100);

        inputManager.registerButton(restartButton);
        inputManager.registerKeyDown(Input.Keys.SPACE, startAction);
    }

    @Override
    public void update() {
        inputManager.update();
        entityManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        float centerX = SceneManager.screenWidth / 2 - 100;
        float centerY = SceneManager.screenHeight / 2;

        textManager.draw(batch, "GAME OVER", centerX, centerY + 50, Color.RED);
        textManager.draw(batch, "Final Score: " + finalScore, centerX, centerY, Color.WHITE);
        textManager.draw(batch, "Press SPACE to Play Again", centerX, centerY - 50, Color.WHITE);
        restartButton.draw(batch);
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
