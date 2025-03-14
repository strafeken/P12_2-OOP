package io.github.team2;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.StartGame;
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
    private IAudioManager audioManager;  // Using the interface type

    public GameOverScreen() {
        super(); // Initialize base components
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

        StartGame startAction = new StartGame(SceneManager.getInstance());
        Vector2 centerPos = new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2 - 200);
        restartButton = new Button("restartBtn.png", centerPos, startAction, 100, 100);

        gameInputManager.registerKeyUp(Input.Keys.SPACE, startAction);
        gameInputManager.registerClickable(restartButton);

        ISceneManager sceneManager = SceneManager.getInstance();
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
