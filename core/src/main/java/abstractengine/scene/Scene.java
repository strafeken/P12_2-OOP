package abstractengine.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import abstractengine.entity.IEntityManager;
import abstractengine.io.IInputManager;
import abstractengine.io.ITextManager;

public abstract class Scene {
    protected IEntityManager entityManager;
    protected IInputManager gameInputManager;
    protected ITextManager textManager;

    public abstract void load();
    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract void draw(ShapeRenderer shape);
    public abstract void unload();
    public abstract void dispose();
}
