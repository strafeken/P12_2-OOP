package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class DynamicTextureObject extends TextureObject {
    private TextureRegion textureRegion;
    private Vector2 direction;
    private float speed;

    public DynamicTextureObject(Texture texture) {
        super();
        this.direction = new Vector2(0, 0);
        this.speed = 0;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
