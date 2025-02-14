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
        setPosition(new Vector2(0, 0));
        this.color = Color.WHITE;
        this.radius = 10;
        updateDimensions();
    }

    public Circle(EntityType type, Vector2 position, Vector2 direction , Color color, float radius) {
        
        super( position,  direction,   color);
        //System.out.println("check if still circle ");
        setEntityType(type);
//        setDirection(direction);
        //setSpeed(speed);
        this.color = color;
        this.radius = radius;
        updateDimensions();
    }

    private void updateDimensions() {
        this.width = radius * 2;
        this.height = radius * 2;
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
        shape.setColor(color);
        Vector2 pos = getPosition();
        shape.circle(pos.x, pos.y, radius);
    }

    @Override
    public void update() {
        updateBody();
    }
}
