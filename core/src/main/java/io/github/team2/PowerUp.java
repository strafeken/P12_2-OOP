package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.SceneSystem.SceneManager;


public class PowerUp extends DynamicTextureObject {
    private static final float SCALE = 0.1f;

    public PowerUp(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
        super(new Texture(texture));
        setEntityType(type);
        setPosition(position);
        setDirection(direction);
        setSpeed(speed);
    }

    @Override
    public void draw(SpriteBatch batch) {
        float drawX = getPosition().x - (getTexture().getWidth() * SCALE / 2);
        float drawY = getPosition().y - (getTexture().getHeight() * SCALE / 2);
        batch.draw(getTexture(),
                  drawX, drawY,
                  getTexture().getWidth() * SCALE,
                  getTexture().getHeight() * SCALE);
    }

    private boolean isOutOfBounds() {
        return getPosition().y < 0;
    }

    private void resetPosition() {
        Random random = new Random();
        getBody().setLocation(
            random.nextFloat() * SceneManager.screenWidth,
            SceneManager.screenHeight
        );
    }

    @Override
    public void update() {
        if (!isOutOfBounds()) {
            if (getAction() != null) {
                getAction().execute();
            }
        } else {
            resetPosition();
        }
        updateBody();
    }

    @Override
    public float getWidth() {
        return getTexture().getWidth() * SCALE;
    }

    @Override
    public float getHeight() {
        return getTexture().getHeight() * SCALE;
    }
}
