package io.github.team2;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.ResumeGame;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.StaticTextureObject;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class PauseMenu extends Scene {
    private Entity image;

    public PauseMenu() {
        super(); // Initialize base components
    }

    @Override
    public void load() {
        System.out.println("Pause Menu => LOAD");

        image = new StaticTextureObject("libgdx.png",
            new Vector2(SceneManager.screenWidth / 2, SceneManager.screenHeight / 2),
            new Vector2(0, 0));

        entityManager.addEntities(image);
        inputManager.registerKeyDown(Input.Keys.ESCAPE,
            new ResumeGame(SceneManager.getInstance(SceneManager.class)));
    }

    @Override
    public void update() {
        entityManager.update();
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
