package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Utils.DisplayManager;

public abstract class DynamicTextureObject <S extends Enum<S>, A extends Enum<A>> extends Dynamics<S,A> {
    private Texture tex;
    
    public DynamicTextureObject(Texture texture) {
        super();
        tex = texture;
    }

    public DynamicTextureObject(
    		EntityType type, String texture, Vector2 size,
    		Vector2 position, Vector2 direction, Vector2 rotation, float speed,
    		S state, A actionState) {
    	super(type, position, direction,rotation, speed, state, actionState); 
    	
        Texture originalTexture = new Texture(texture);
        tex = resizeTexture(originalTexture, Math.round(size.x), Math.round(size.y));
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
		
		if (direction.x < 0 && (projectedPos.x - getWidth()/2) < DisplayManager.getScreenOriginX()) {
			System.out.println("Hit left");
			return true;
		}
		if (direction.x > 0 && (projectedPos.x + getWidth()/2) > DisplayManager.getScreenWidth()) {
			System.out.println("hit right");
			return true;
		}
		
		if (direction.y < 0 && (projectedPos.y - getHeight()/2) < DisplayManager.getScreenOriginY()) {
			return true;
		}
		if (direction.y > 0 && (projectedPos.y + getHeight()/2) > DisplayManager.getScreenHeight()) {
			return true;
		}
		
		return false;
	}
	
	public void initActionMap() {
		
	};
	
	private Texture resizeTexture(Texture original, int newWidth, int newHeight) {
	    original.getTextureData().prepare();
	    Pixmap pixmap = original.getTextureData().consumePixmap();

	    Pixmap scaledPixmap = new Pixmap(newWidth, newHeight, Pixmap.Format.RGBA8888);

	    scaledPixmap.drawPixmap(
	        pixmap,
	        0, 0, pixmap.getWidth(), pixmap.getHeight(), // original size
	        0, 0, newWidth, newHeight // new size
	    );

	    Texture resizedTexture = new Texture(scaledPixmap);

	    pixmap.dispose();
	    scaledPixmap.dispose();
	    original.dispose();

	    return resizedTexture;
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
