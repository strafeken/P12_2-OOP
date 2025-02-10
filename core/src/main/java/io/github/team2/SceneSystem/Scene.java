package io.github.team2.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.InputProcessor;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.CollisionSystem.CollisionManager;
import io.github.team2.InputSystem.InputManager;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.TextManager;


public abstract class Scene {
    protected EntityManager entityManager;
    protected CollisionManager collisionManager;
    protected InputManager inputManager;
    protected AudioManager audioManager;
    protected TextManager textManager;

    public Scene() {
        entityManager = new EntityManager();
        audioManager = AudioManager.getInstance();
        inputManager = new InputManager();
        textManager = new TextManager();
    }

    public abstract void load();
    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract void draw(ShapeRenderer shape);
    public abstract void unload();
    public abstract void dispose();
    protected abstract void resize(int width, int height);

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }
}
