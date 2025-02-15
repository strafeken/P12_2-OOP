package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.SceneSystem.SceneManager;

public abstract class DynamicTextureObject <S extends Enum<S>, A extends Enum<A>> extends Dynamics<S,A> {
    //private TextureRegion textureRegion;
    
    private Texture tex;
    

    public DynamicTextureObject(Texture texture) {
        super();
        tex = texture; // Properly set the texture through parent class
        
        
    }

    // Add constructor that takes texture path
    public DynamicTextureObject(EntityType type, String texturePath, Vector2 position, Vector2 direction, float speed, S state, A actionState) {
    	super(type, position, direction, speed, state, actionState);
        
        
    	tex = new Texture(texturePath);
    	
        
        	
        
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
	public void draw(SpriteBatch batch) {
		batch.draw(tex, getPosition().x - tex.getWidth() / 2, getPosition().y - tex.getHeight() / 2);
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
	
	
	public void initActionMap() {
		
	};	
	
	public void dispose() {
		tex.dispose();
	}
	
    
}
