package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.StartGame;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.StaticTextureObject;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.Utils.DisplayManager;

public class MainMenu extends Scene {
    private StaticTextureObject image;
    private Button startButton;

    @Override
    public void load() {
        System.out.println("Main Menu => LOAD");

        // Initialize managers
        entityManager = new EntityManager();
        gameInputManager = new GameInputManager();
        textManager = new TextManager();

        // Start playing main menu background music
        AudioManager.getInstance(AudioManager.class).playSoundEffect("mainmenu");

        // Setup background image
        image = new StaticTextureObject(EntityType.UNDEFINED, "libgdx.png",
                new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2),
                new Vector2(0, 0));
        
        entityManager.addEntities(image);

        Vector2 centerPos = new Vector2(DisplayManager.getScreenWidth() / 2 - 50, DisplayManager.getScreenHeight() / 2 - 180);
        StartGame startAction = new StartGame(SceneManager.getInstance(SceneManager.class));
        startButton = new Button("startBtn.png", centerPos, startAction, 100, 100);

        gameInputManager.registerKeyUp(Input.Keys.SPACE, startAction);
        gameInputManager.registerClickable(startButton);
        
        startButton.update();  // Make sure button gets updated
    }

    @Override
    public void update() {
    	entityManager.update();
    	gameInputManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
    	entityManager.draw(batch);
        startButton.draw(batch);
        textManager.draw(batch, "Main Menu", 200, 200, Color.RED);
        textManager.draw(batch, "Press SPACE to Start", 200, 150, Color.WHITE);
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
        entityManager.dispose();
        AudioManager.getInstance(AudioManager.class).stopMusic(); // Stop music when leaving menu
    }
}
