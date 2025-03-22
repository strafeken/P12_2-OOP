package abstractengine.entity;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import game.entity.EntityType;

public class StaticTextureObject extends Static implements TexturedObject {
	private Texture tex;

	public StaticTextureObject() {
		tex = null;
	}

	public StaticTextureObject(String texture) {
		tex = new Texture(texture);
	}

	public StaticTextureObject(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction) {
		super(type, position, direction);
        Texture originalTexture = new Texture(texture);
        tex = resizeTexture(originalTexture, Math.round(size.x), Math.round(size.y));
	}

	public Texture getTexture() {
		return tex;
	}

	public void setTexture(Texture texture) {
		tex = texture;
	}

	@Override
	public float getWidth() {
		return tex.getWidth();
	}

	@Override
	public float getHeight() {
		return tex.getHeight();
	}

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
		batch.draw(tex, getPosition().x - tex.getWidth() / 2, getPosition().y - tex.getHeight() / 2);
	}

	public void dispose() {
		tex.dispose();
	}
}
