package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.Static;
import io.github.team2.EntitySystem.StaticGameShape;


public class Circle extends StaticGameShape {
    private float radius;


    public Circle() {
        super();
        setEntityType(EntityType.CIRCLE);
        this.radius = 10;
        updateDimensions();
    }

    public Circle(EntityType type, Vector2 position, Vector2 direction , Color color, float radius) {
        super(type, position,  direction,color);
        this.radius = radius;
        updateDimensions();
    }

    private void updateDimensions() {
        setWidth(radius * 2);
        setHeight(radius * 2);
        
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        updateDimensions();
    }



    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(getColor());
        Vector2 pos = getPosition();
        shape.circle(pos.x, pos.y, radius);
    }

    @Override
    public void update() {
        updateBody();
    }
}
