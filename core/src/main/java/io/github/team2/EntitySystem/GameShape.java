package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameShape extends Static {
    protected Color color;
    protected float width;
    protected float height;

    public GameShape() {
        super();
        setEntityType(EntityType.UNDEFINED);
        setPosition(new Vector2(0, 0));
        setDirection(new Vector2(0, 0));
        setSpeed(0);
        color = Color.WHITE;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public abstract void draw(ShapeRenderer shape);
}
