package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.team2.Actions.StartGame;
import io.github.team2.InputSystem.InputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class GameOverScreen extends Scene {
    private int finalScore;
    private SceneManager sm;

    @Override
    public void load() {
        System.out.println("Game Over Screen => LOAD");
        im = new InputManager();
        tm = new TextManager();
        sm = SceneManager.getInstance();
        
        finalScore = GameScene.getInstance().getPointsManager().getPoints();
        
        // Register SPACE to restart game
        im.registerKeyDown(Input.Keys.SPACE, new StartGame(sm));
    }

    @Override
    public void update() {
        im.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        float centerX = Gdx.graphics.getWidth() / 2 - 100;
        float centerY = Gdx.graphics.getHeight() / 2;
        
        tm.draw(batch, "GAME OVER", centerX, centerY + 50, Color.RED);
        tm.draw(batch, "Final Score: " + finalScore, centerX, centerY, Color.WHITE);
        tm.draw(batch, "Press SPACE to Play Again", centerX, centerY - 50, Color.WHITE);
    }

    @Override
    public void draw(ShapeRenderer shape) {}

    @Override
    public void unload() {
        System.out.println("Game Over Screen => UNLOAD");
    }

    @Override
    public void dispose() {}
}