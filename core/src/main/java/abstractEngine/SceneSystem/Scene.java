package abstractEngine.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import abstractEngine.EntitySystem.IEntityManager;
import abstractEngine.InputSystem.IInputManager;
import io.github.team2.Game.Manager.TextManager;

public abstract class Scene {
    protected IEntityManager entityManager;
    protected IInputManager gameInputManager;
    protected TextManager textManager;

    public abstract void load();
    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract void draw(ShapeRenderer shape);
    public abstract void unload();
    public abstract void dispose();
}
