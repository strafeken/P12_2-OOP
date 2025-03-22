package io.github.team2.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.Game.Manager.TextManager;
import io.github.team2.InputSystem.GameInputManager;

public abstract class Scene {
    protected IEntityManager entityManager;
    protected GameInputManager gameInputManager;
    protected TextManager textManager;

    public abstract void load();
    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract void draw(ShapeRenderer shape);
    public abstract void unload();
    public abstract void dispose();
}
