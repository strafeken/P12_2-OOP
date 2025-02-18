package io.github.team2.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.Utils.DisplayManager;
import io.github.team2.TextManager;

public abstract class Scene {
    protected EntityManager entityManager;
    protected GameInputManager gameInputManager;
    protected TextManager textManager;

    // Add viewport variables to track screen dimensions
    protected float viewportWidth;
    protected float viewportHeight;
    protected float hudScaleX;
    protected float hudScaleY;

    public Scene() {
        viewportWidth = DisplayManager.getScreenWidth();
        viewportHeight = DisplayManager.getScreenHeight();
        hudScaleX = 1f;
        hudScaleY = 1f;
    }

    public abstract void load();
    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract void draw(ShapeRenderer shape);
    public abstract void unload();
    public abstract void dispose();

    // Make resize non-abstract and implement common behavior
    public void resize(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;

        // Calculate HUD scaling factors
        hudScaleX = width / DisplayManager.getScreenWidth();
        hudScaleY = height / DisplayManager.getScreenHeight();

        // Update SceneManager's screen dimensions
//        DisplayManager.getScreenWidth() = width;
//        DisplayManager.getScreenHeight() = height;

        // Allow scenes to perform additional resize operations
        onResize(width, height);
    }

    // Protected method for scene-specific resize operations
    protected void onResize(int width, int height) {
        // Optional override in child classes
    }
}
