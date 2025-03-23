package abstractengine.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import application.entity.EntityType;

public abstract class StaticGameShape extends Static implements ShapeRenderable {
    private Color color;
    private float width;
    private float height;

    public StaticGameShape() {
        super();
        color = Color.WHITE;
    }
    
    public StaticGameShape(EntityType type, Vector2 position, Vector2 direction, Color color) {
        super(type, position, direction);
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }
    
    @Override
    public abstract void draw(ShapeRenderer shape);
}