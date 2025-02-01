package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;

public class PowerUp extends TextureObject {
    private float scale = 0.1f; // 50% size
    
    public PowerUp(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
        setEntityType(type);
        setTexture(new Texture(texture));
        setPosition(position);
        setDirection(direction);
        setSpeed(speed);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(getTexture(), 
            getPosition().x, 
            getPosition().y,
            getTexture().getWidth() * scale,  // 50% width
            getTexture().getHeight() * scale); // 50% height
    }

    @Override
    public void moveAIControlled() {
        if (!getIsMoving()) {
            moveTo(new Vector2(getPosition().x, 0));
        } else {
            if (!checkPosition(new Vector2(getPosition().x, 10))) {
                moveDown();
            } else {
                setIsMoving(false);
                getBody().setLocation(getPosition().x, Gdx.graphics.getHeight());
            }
        }
    }

    public void moveDown() {
        getBody().setLinearVelocity(0, -getSpeed());
    }

    @Override
    public void moveTo(Vector2 targetPosition) {
        setIsMoving(true);
    }

    @Override
    public boolean checkPosition(Vector2 position) {
        return getPosition().y < 0;
    }

    @Override
    public void update() {
        updateBody();
    }

    @Override
    public float getWidth() {
        return getTexture().getWidth() * scale;
    }

    @Override
    public float getHeight() {
        return getTexture().getHeight() * scale;
    }
}