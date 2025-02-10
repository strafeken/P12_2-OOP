package io.github.team2.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.InputProcessor;
import io.github.team2.EntitySystem.EntityManager;

import io.github.team2.InputSystem.InputManager;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.TextManager;


public abstract class Scene {
    protected EntityManager entityManager;
    //protected CollisionManager collisionManager;
    protected InputManager inputManager;
    protected AudioManager audioManager;
    protected TextManager textManager;

    // Add viewport variables to track screen dimensions
    protected float viewportWidth;
    protected float viewportHeight;
    protected float hudScaleX;
    protected float hudScaleY;

    public Scene() {
        entityManager = new EntityManager();
        audioManager = AudioManager.getInstance();
        inputManager = new InputManager();
        textManager = new TextManager();

        // Initialize viewport with default values
        viewportWidth = SceneManager.screenWidth;
        viewportHeight = SceneManager.screenHeight;
        hudScaleX = 1f;
        hudScaleY = 1f;
    }

    // Make resize non-abstract and implement common behavior
    public void resize(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;

        // Calculate HUD scaling factors
        hudScaleX = width / SceneManager.screenWidth;
        hudScaleY = height / SceneManager.screenHeight;

        // Update SceneManager's screen dimensions
        SceneManager.screenWidth = width;
        SceneManager.screenHeight = height;

        // Allow scenes to perform additional resize operations
        onResize(width, height);
    }

    // Protected method for scene-specific resize operations
    protected void onResize(int width, int height) {
        // Optional override in child classes
    }

    public abstract void load();
    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract void draw(ShapeRenderer shape);
    public abstract void unload();
    public abstract void dispose();


    public EntityManager getEntityManager() {
        return entityManager;
    }


    public InputManager getInputManager() {
        return inputManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }
}
