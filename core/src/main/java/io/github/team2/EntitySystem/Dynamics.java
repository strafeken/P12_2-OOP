package io.github.team2.EntitySystem;

import com.badlogic.gdx.math.Vector2;


public abstract class Dynamics extends Entity {
    protected float speed;

    public Dynamics(float speed) {
        super();
        this.speed = speed;
    }

    public Dynamics(Vector2 position, Vector2 direction, float speed) {
        super(position, direction, speed);
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void update() {
        // Update position based on direction and speed
        Vector2 currentPos = getPosition();
        Vector2 direction = getDirection();
        currentPos.add(direction.x * speed, direction.y * speed);
        setPosition(currentPos);

        // Update physics body
        updateBody();
    }

    /**
     * Check if the entity is moving
     * @return true if the entity has non-zero speed and direction
     */
    public boolean isMoving() {
        return speed != 0 && !getDirection().isZero();
    }
}