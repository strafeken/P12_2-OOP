package abstractEngine.EntitySystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import game.Entity.EntityType;

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
    public void draw(ShapeRenderer shape) {
        if (shape != null) {
            drawShape(shape);
        }
    }
    
    @Override
    public abstract void drawShape(ShapeRenderer shapeRenderer);
}