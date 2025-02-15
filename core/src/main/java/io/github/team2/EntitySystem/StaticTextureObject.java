package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.SceneSystem.SceneManager;


public class StaticTextureObject extends Static {
    private TextureRegion textureRegion;

    private Texture tex;

    
	public StaticTextureObject() {
		tex = null;
	}

	public StaticTextureObject(String texture) {
		tex = new Texture(texture);
	}

	public StaticTextureObject(EntityType type, String texture, Vector2 position, Vector2 direction) {
		super(type, position,  direction);
		tex = new Texture(texture);


	}

	public Texture getTexture() {
		return tex;
	}

	public void setTexture(Texture texture) {
		tex = texture;
	}

	public float getWidth() {
		return tex.getWidth();
	}

	public float getHeight() {
		return tex.getHeight();
	}



	
	@Override 	
	public  boolean isOutOfBound(Vector2 direction) {
		
		
		Vector2 projectedPos = this.getPosition();
		projectedPos.add(direction);
		
		if (direction.x < 0 && (projectedPos.x - getWidth()/2) < SceneManager.screenLeft) {
			System.out.println("Hit left");
			return true;
		}
		if (direction.x > 0 && (projectedPos.x + getWidth()/2) > SceneManager.screenWidth) {
			System.out.println("hit right");
			return true;
		}
		
		if (direction.y < 0 && (projectedPos.y - getHeight()/2) < SceneManager.screenBottom) {
			return true;
		}
		if (direction.y > 0 && (projectedPos.y + getHeight()/2) > SceneManager.screenHeight) {
			return true;
		}
		
		return false;
		
	}
	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(tex, getPosition().x - tex.getWidth() / 2, getPosition().y - tex.getHeight() / 2);
	}


	


	@Override
	public void update() {
//		System.out.println(tex.toString() + " XY: " + getX() + " / " + getY());
	}

	public void dispose() {
		tex.dispose();
	}
    
    
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }
}
