package io.github.team2;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.StartGame;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.InputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class MainMenu extends Scene {
    private EntityManager em;
    private InputManager im;
    private TextManager tm;
    private TextureObject image;

    private Button startButton;

    @Override
    public void load() {
        System.out.println("Main Menu => LOAD");

        em = new EntityManager();
        im = new InputManager();
        tm = new TextManager();

        // Start playing main menu background music using standardized ID
        AudioManager.getInstance().playSoundEffect("mainmenu");

        image = new TextureObject("libgdx.png",
                new Vector2(SceneManager.screenWidth / 2, SceneManager.screenHeight / 2),
                new Vector2(0, 0),new Vector2(0, 0),
                0);
        // Create start button
        StartGame startAction = new StartGame(SceneManager.getInstance());
        Vector2 centerPos = new Vector2(
        		SceneManager.screenWidth /2 - 20, //button x-axis
        		SceneManager.screenHeight /2 - 180 //button y-axis
        );

		startButton = new Button(1,"Start", "startBtn.png",
								centerPos, startAction,
								100, 100 );
        em.addEntities(image);
        //em.addEntities(startButton);
        im.registerKeyDown(Input.Keys.SPACE, startAction);
        //im.registerTouchDown(startButton.getID(), startButton.getAction());
        im.registerButton(startButton);
    }

    @Override
    public void update() {
        em.update();
        im.update();
        //startButton.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        em.draw(batch);
        tm.draw(batch, "Main Menu", 200, 200, Color.RED);
        tm.draw(batch, "Press SPACE to Start", 200, 150, Color.WHITE);
        startButton.draw(batch);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // No shapes to draw in main menu
    }

    @Override
    public void unload() {
        System.out.println("Main Menu => UNLOADED");
        dispose();
    }

    @Override
    public void dispose() {
        System.out.println("Main Menu => DISPOSE");
        em.dispose();
        AudioManager.getInstance().stopMusic(); // Stop music when leaving menu
    }

    @Override
    public InputManager getInputManager() {
        return im;
    }

	@Override
	protected void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
}
