package application.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import abstractengine.audio.AudioManager;
import abstractengine.audio.IAudioManager;
import abstractengine.entity.EntityManager;
import abstractengine.entity.IEntityManager;
import abstractengine.entity.StaticTextureObject;
import abstractengine.io.Button;
import abstractengine.io.TextManager;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.Scene;
import abstractengine.scene.SceneManager;
import abstractengine.utils.DisplayManager;
import application.entity.EntityType;
import application.io.GameInputManager;

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

        ISceneManager sm = SceneManager.getInstance();
        startButton = new Button("startBtn.png", centerPos, () -> sm.setNextScene(SceneID.LEVEL_SELECT), 100, 100);
        gameInputManager.registerKeyUp(Input.Keys.SPACE, () -> sm.setNextScene(SceneID.LEVEL_SELECT));
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
