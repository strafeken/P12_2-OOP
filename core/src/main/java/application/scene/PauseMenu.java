package application.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.Entity;
import abstractengine.entity.EntityManager;
import abstractengine.entity.StaticTextureObject;
import abstractengine.io.TextManager;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.Scene;
import abstractengine.scene.SceneManager;
import abstractengine.utils.DisplayManager;
import application.entity.EntityType;
import application.io.GameInputManager;

public class PauseMenu extends Scene {
    private Entity image;

    public PauseMenu() {
        super(); // Initialize base components
    }

    @Override
    public void load() {
        System.out.println("Pause Menu => LOAD");

        // Initialize managers
        entityManager = new EntityManager();
        gameInputManager = new GameInputManager();
        textManager = new TextManager();

        image = new StaticTextureObject(EntityType.UNDEFINED, "libgdx.png", new Vector2(600, 150),
            new Vector2(DisplayManager.getScreenWidth() / 2, DisplayManager.getScreenHeight() / 2),
            new Vector2(0, 0));

        entityManager.addEntities(image);

        ISceneManager sm = SceneManager.getInstance();
        gameInputManager.registerKeyUp(Input.Keys.ESCAPE, () -> sm.removeOverlay());
    }

    @Override
    public void update() {
        entityManager.update();
        gameInputManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        entityManager.draw(batch);
        textManager.draw(batch, "Pause Menu", 200, 150, Color.RED);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // No shapes to draw
    }

    @Override
    public void unload() {
        System.out.println("Pause Menu => UNLOAD");
        dispose();
    }

    @Override
    public void dispose() {
        if (image instanceof StaticTextureObject) {
            ((StaticTextureObject) image).dispose();
        }
        entityManager.dispose();
        textManager.dispose();
    }
}
