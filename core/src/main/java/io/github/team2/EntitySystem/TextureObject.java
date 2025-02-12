package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextureObject extends Entity {

	private Texture tex;

	public TextureObject() {
		tex = null;
	}

	public TextureObject(String texture) {
		tex = new Texture(texture);
	}

	public TextureObject(String texture, Vector2 position, Vector2 direction, Vector2 rotation, float speed) {
		tex = new Texture(texture);
		setPosition(position);
		setDirection(direction);
		setRotation(rotation);
		setSpeed(speed);
		
		
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
	

	public float getSide(String side) {
		float value = 0;
		if (side == "TOP")
			value = this.getPosition().y + (this.getHeight() / 2);
		if (side == "BOTTOM")
			value = this.getPosition().y - (this.getHeight() / 2);

		if (side == "LEFT")
			value = this.getBody().getPosition().x - this.getWidth() / 2;
		if (side == "RIGHT")
			value = this.getPosition().x + (this.getWidth() / 2);

//		System.out.println("X:"+ this.getPosition().x + " Y:" +this.getPosition().y + "Value:" +
//				value);

		return value;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (getBody() == null) return;
	    float angle = getBody().getAngle() * (180f / (float) Math.PI); // Convert radians to degrees

	    batch.draw(
	        tex, // Texture
	        getPosition().x - tex.getWidth() / 2, getPosition().y - tex.getHeight() / 2, // Position
	        tex.getWidth() / 2, tex.getHeight() / 2, // Origin (center for rotation)
	        tex.getWidth(), tex.getHeight(), // Width & Height
	        1, 1, // Scale X, Scale Y
	        angle, // Rotation angle in degrees
	        0, 0, tex.getWidth(), tex.getHeight(), // Source rectangle (full texture)
	        false, false // Flip X, Flip Y
	    );
	}
	
	

	@Override
	public void moveUserControlled() {

	}

	@Override
	public void moveAIControlled() {

	}

	@Override
	public void moveTo(Vector2 position) {

	}

	@Override
	public void moveDirection(String direction) {

	}

	@Override
	public void rotateTo(float num) {

	}

	@Override
	public boolean checkPosition(Vector2 position) {

		return false;
	}

	@Override
	public void update() {
//		System.out.println(tex.toString() + " XY: " + getX() + " / " + getY());
	}

	public void dispose() {
		tex.dispose();
	}
}
