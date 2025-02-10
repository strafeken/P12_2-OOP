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
        setTexture(texture); // Properly set the texture through parent class
        this.direction = new Vector2(0, 0);
        this.speed = 0;
    }

    // Add constructor that takes texture path
    public DynamicTextureObject(String texturePath) {
        super(texturePath); // Use parent's constructor that handles texture loading
        this.direction = new Vector2(0, 0);
        this.speed = 0;
    }
}
