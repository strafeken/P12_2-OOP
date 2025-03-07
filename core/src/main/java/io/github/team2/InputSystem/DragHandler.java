package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.PhysicsBody;
import io.github.team2.Utils.DisplayManager;

public class DragHandler {
    private boolean isDragging;
    private Vector2 offset;
    private Body body;

    public DragHandler(PhysicsBody body, World world) {
        isDragging = false;
        offset = new Vector2(0, 0);
        this.body = body.getBody();
    }

    public void startDragging() {
    	isDragging = true;
    	body.setType(BodyDef.BodyType.DynamicBody);
    	offset.set(Gdx.input.getX() - body.getPosition().x, DisplayManager.getScreenHeight() - Gdx.input.getY() - body.getPosition().y);
    }

    public void updateDragging() {
        if (isDragging) {
            body.setTransform(new Vector2(Gdx.input.getX() - offset.x, DisplayManager.getScreenHeight() - Gdx.input.getY() - offset.y), 
            		body.getAngle());
        }
    }

    public void stopDragging() {
        isDragging = false;
    }

    public boolean isDragging() {
        return isDragging;
    }
}
