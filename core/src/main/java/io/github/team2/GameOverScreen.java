package io.github.team2;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.StartGame;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.InputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class GameOverScreen extends Scene {
    private int finalScore;
    private SceneManager sm;
    private EntityManager em;

    private Button restartButton;

    @Override
    public void load() {
        System.out.println("Game Over Screen => LOAD");
        im = new InputManager();
        tm = new TextManager();
        em = new EntityManager();
        sm = SceneManager.getInstance(SceneManager.class);

        finalScore = GameScene.getInstance().getPointsManager().getPoints();

        // Create restart button
        StartGame startAction = new StartGame(sm);
        Vector2 centerPos = new Vector2(
            SceneManager.screenWidth /2,      //button x-axis
            SceneManager.screenHeight /2 - 200    //button y-axis
        );

        restartButton = new Button(
        	1, "Restart", "restartBtn.png",
            centerPos, startAction,
            100,  // width
            100   // height
        );



        //em.addEntities(restartButton);
        im.registerButton(restartButton);
        im.registerKeyDown(Input.Keys.SPACE, startAction);
    }

    @Override
    public void update() {
        im.update();
        em.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        float centerX = SceneManager.screenWidth / 2 - 100;
        float centerY = SceneManager.screenHeight / 2;

        tm.draw(batch, "GAME OVER", centerX, centerY + 50, Color.RED);
        tm.draw(batch, "Final Score: " + finalScore, centerX, centerY, Color.WHITE);
        tm.draw(batch, "Press SPACE to Play Again", centerX, centerY - 50, Color.WHITE);
        em.draw(batch);

        restartButton.draw(batch);


    }

    @Override
    public void draw(ShapeRenderer shape) {}

    @Override
    public void unload() {
        System.out.println("Game Over Screen => UNLOAD");
    }

    @Override
    public void dispose() {}

	@Override
	protected void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
}
