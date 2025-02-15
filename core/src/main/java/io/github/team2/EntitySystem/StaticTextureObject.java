package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;



public class StaticTextureObject extends Static {
	private Texture tex;
	private TextureRegion textureRegion;

	public StaticTextureObject() {
		tex = null;
	}

	public StaticTextureObject(String texture) {
		tex = new Texture(texture);
	}

	public StaticTextureObject(EntityType type, String texture, Vector2 position, Vector2 direction) {
		super(type, position, direction);
		tex = new Texture(texture);

	}

	public Texture getTexture() {
		return tex;
	}

	public void setTexture(Texture texture) {
		tex = texture;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
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
	public void update() {
//		System.out.println(tex.toString() + " XY: " + getX() + " / " + getY());
	}

	public void dispose() {
		tex.dispose();
	}

}
