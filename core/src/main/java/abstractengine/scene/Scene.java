package abstractengine.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import abstractengine.entity.IEntityManager;
import abstractengine.input.IInputManager;
import game.manager.TextManager;

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
