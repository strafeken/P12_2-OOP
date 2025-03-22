package io.github.team2.Game.Scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.EntitySystem.StaticTextureObject;
import io.github.team2.Game.Actions.Control.StartGame;
import io.github.team2.Game.Actions.Control.StartLevelSelect;
import io.github.team2.Game.Entity.EntityType;
import io.github.team2.Game.Manager.TextManager;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.SceneSystem.ISceneManager;
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

        // Start playing main menu background music - use interface
        IAudioManager audioManager = AudioManager.getInstance();
        audioManager.playSoundEffect("mainmenu");

        // Setup background image
        image = new StaticTextureObject(EntityType.UNDEFINED, "Main_menu_space.jpg",
                new Vector2(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight()),
                new Vector2(DisplayManager.getScreenWidth()/2, DisplayManager.getScreenHeight()/2),
                new Vector2(0, 0));

        entityManager.addEntities(image);

        Vector2 centerPos = new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2 - 180);

        // Create the level select action
        ISceneManager sceneManager = SceneManager.getInstance();
        StartLevelSelect levelSelectAction = new StartLevelSelect(sceneManager);

        // Use levelSelectAction for both button and space key
        startButton = new Button("startBtn.png", centerPos, levelSelectAction, 100, 100);
        gameInputManager.registerKeyUp(Input.Keys.SPACE, levelSelectAction);
        gameInputManager.registerClickable(startButton);

        startButton.update();
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
        textManager.draw(batch, "Astro Debris", DisplayManager.getScreenWidth()/2 - 85, DisplayManager.getScreenHeight()/2, Color.RED);
        // Update the text to match the new action
        textManager.draw(batch, "Press SPACE to Select Level", DisplayManager.getScreenWidth()/2 - 140, 170, Color.WHITE);
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
        // Use interface
        IAudioManager audioManager = AudioManager.getInstance();
        audioManager.stopMusic(); // Stop music when leaving menu
    }
}
