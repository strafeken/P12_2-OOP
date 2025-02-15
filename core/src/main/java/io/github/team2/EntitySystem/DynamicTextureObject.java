package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.SceneSystem.SceneManager;

public abstract class DynamicTextureObject <S extends Enum<S>, A extends Enum<A>> extends Dynamics<S,A> {
    //private TextureRegion textureRegion;
    //private Vector2 direction;
    private Texture tex;
    

    public DynamicTextureObject(Texture texture) {
        super(0);
        setTexture(texture); // Properly set the texture through parent class
        
        
    }

    // Add constructor that takes texture path
    public DynamicTextureObject(String texturePath, Vector2 position, Vector2 direction,Vector2 rotation, float speed, S state, A actionState) {
    	super(position, direction,rotation, speed, state, actionState);
        
        
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
	    if (getTexture() == null) return; // ✅ Ensure texture exists before drawing

	    float angleDegrees = (float) Math.toDegrees(getBody().getAngle()); // ✅ Convert radians to degrees
	    Vector2 position = getBody().getPosition(); // ✅ Get entity position

	    batch.draw(
	        getTexture(),
	        position.x - getTexture().getWidth() / 2, // Centered X
	        position.y - getTexture().getHeight() / 2, // Centered Y
	        getTexture().getWidth() / 2, // Origin X (pivot point)
	        getTexture().getHeight() / 2, // Origin Y (pivot point)
	        getTexture().getWidth(),
	        getTexture().getHeight(),
	        1f, 1f, // Scale
	        angleDegrees, // ✅ Rotation angle in degrees
	        0, 0, // Texture region start
	        getTexture().getWidth(), getTexture().getHeight(), // Texture region size
	        false, false // Flip options
	    );
	}
	
	public void dispose() {
		tex.dispose();
	}
	
    
}
