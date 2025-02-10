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

        // Initialize managers
        em = new EntityManager();
        im = new InputManager();
        tm = new TextManager();

        // Start playing main menu background music
        AudioManager.getInstance().playSoundEffect("mainmenu");

        // Setup background image
        image = new TextureObject("libgdx.png",
                new Vector2(SceneManager.screenWidth / 2, SceneManager.screenHeight / 2),
                new Vector2(0, 0),
                0);
        em.addEntities(image);

        // Create start action first
        StartGame startAction = new StartGame(SceneManager.getInstance(SceneManager.class));

        // Create and setup start button
        Vector2 centerPos = new Vector2(
                SceneManager.screenWidth / 2 - 50,  // Center the button
                SceneManager.screenHeight / 2 - 180
        );

        startButton = new Button(1, "Start", "startBtn.png",
                               centerPos, startAction,
                               100, 100);

        // Register both keyboard and mouse inputs
        im.registerKeyDown(Input.Keys.SPACE, startAction);
        im.registerButton(startButton);
    }

    @Override
    public void update() {
        em.update();
        im.update();
        startButton.update();  // Make sure button gets updated
    }

    @Override
    public void draw(SpriteBatch batch) {
        em.draw(batch);
        startButton.draw(batch);  // Draw button
        tm.draw(batch, "Main Menu", 200, 200, Color.RED);
        tm.draw(batch, "Press SPACE to Start", 200, 150, Color.WHITE);
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
}
