package io.github.team2.Game.Scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Abstract.EntitySystem.Entity;
import io.github.team2.Abstract.EntitySystem.EntityManager;
import io.github.team2.Abstract.EntitySystem.StaticTextureObject;
import io.github.team2.Abstract.InputSystem.GameInputManager;
import io.github.team2.Abstract.SceneSystem.ISceneManager;
import io.github.team2.Abstract.SceneSystem.Scene;
import io.github.team2.Abstract.SceneSystem.SceneManager;
import io.github.team2.Abstract.Utils.DisplayManager;
import io.github.team2.Game.Actions.Control.ResumeGame;
import io.github.team2.Game.Entity.EntityType;
import io.github.team2.Game.Manager.TextManager;

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

        // Use interface instead of concrete class
        ISceneManager sceneManager = SceneManager.getInstance();
        gameInputManager.registerKeyUp(Input.Keys.ESCAPE, new ResumeGame(sceneManager));
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
