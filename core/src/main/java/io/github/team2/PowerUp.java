package io.github.team2;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.SceneSystem.SceneManager;

public class PowerUp extends TextureObject {
    private float scale = 0.1f;
    
    public PowerUp(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
        setEntityType(type);
        setTexture(new Texture(texture));
        setPosition(position);
        setDirection(direction);
        setSpeed(speed);
    }

    @Override
    public void draw(SpriteBatch batch) {
        float drawX = getPosition().x - (getTexture().getWidth() * scale / 2);
        float drawY = getPosition().y - (getTexture().getHeight() * scale / 2);
        batch.draw(getTexture(), 
            drawX, 
            drawY,
            getTexture().getWidth() * scale,
            getTexture().getHeight() * scale);
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
                getBody().setLocation(getPosition().x, SceneManager.screenHeight);
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