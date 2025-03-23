package abstractengine.entity;

import com.badlogic.gdx.graphics.Texture;

public interface TexturedObject {
	public Texture getTexture();
	public void setTexture(Texture texture);
	public float getWidth();
	public float getHeight();
}
