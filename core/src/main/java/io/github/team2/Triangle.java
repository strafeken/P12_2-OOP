package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.Dynamics;


public class Triangle extends Dynamics {
    private float size;
    private float offset;
    private Color color;

    public Triangle() {
        super(0);
        setEntityType(EntityType.TRIANGLE);
        setPosition(new Vector2(0, 0));
        this.color = Color.WHITE;
        this.size = 10;
        this.offset = 0;
    }

    public Triangle(EntityType type, Vector2 position, Vector2 direction, float speed, Color color, float size, float offset) {
        super(position, direction, speed);
        setEntityType(type);
        this.color = color;
        this.size = size;
        this.offset = offset;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        Vector2 pos = getPosition();
        float x = pos.x;
        float y = pos.y;

        shape.triangle(
            x - size, y - size,     // bottom-left
            x, y + size,            // top
            x + size, y - size      // bottom-right
        );
    }

    @Override
    public boolean isOutOfBound(Vector2 direction) {
        return false; // Implement boundary checking if needed
    }
}
