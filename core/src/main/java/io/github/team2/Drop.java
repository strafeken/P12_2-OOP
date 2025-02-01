package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;

public class Drop extends TextureObject {

	// private float dropSpeed = 100;

	public Drop(String texture) {
		setEntityType(EntityType.DROP);
		setTexture(new Texture(texture));
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(10);
	}

	public Drop(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setDirection(direction);
		setSpeed(speed);
	}

	// movement control
	@Override
    public void moveAIControlled() {
        if (this.getIsMoving() == false) {
            this.moveTo(new Vector2(this.getPosition().x, 0));
        } else {
            if (this.checkPosition(new Vector2(this.getPosition().x, 10)) == false) {
                this.moveDown();
            } else {
                this.setIsMoving(false);
                // Check if drop hit bottom
                if (this.getPosition().y < 0) {
                    // Increment fail counter through GameScene
                    if (GameScene.getInstance().getPointsManager() != null) {
                        GameScene.getInstance().getPointsManager().incrementFails();
                    }
                }
                getBody().setLocation(getPosition().x, Gdx.graphics.getHeight());
            }
        }
    }

	@Override
	public void moveTo(Vector2 targetPosition) {

		this.setIsMoving(true);

	}

	public void moveDown() {

		this.getBody().setLinearVelocity(0, -this.getSpeed());

	}

	@Override
	public boolean checkPosition(Vector2 position) {
		float threshold = 0.1f;
		if (this.getPosition().y < 0) {
//			System.out.println("reach");
			return true;
		}

		return false;
	}

	@Override
	public void update() {
		updateBody();
	}

}
