package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.SceneSystem.SceneManager;


public class Drop extends DynamicTextureObject {

    public Drop(String texture) {
        super(new Texture(texture));
        setEntityType(EntityType.DROP);
        setPosition(new Vector2(0, 0));
        setSpeed(10);
    }

    public Drop(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
        super(new Texture(texture));
        setEntityType(type);
        setPosition(position);
        setDirection(direction);
        setSpeed(speed);
    }

    public boolean isOutOfBounds() {
        return getPosition().y < 0;
    }

    private void resetPosition() {
        Random random = new Random();
        getBody().setLocation(
            random.nextFloat() * SceneManager.screenWidth,
            SceneManager.screenHeight
        );
    }

    private void handleDropMiss() {
        GameManager.getInstance().getPointsManager().incrementFails();
    }

    @Override
    public void update() {
        if (getBody() != null) {  // Add null check
            if (!isOutOfBounds()) {
                if (getAction() != null) {
                    getAction().execute();
                }
            } else {
                handleDropMiss();
                resetPosition();
            }
            updateBody();
        }
    }
}
