package abstractengine.entity;

import com.badlogic.gdx.graphics.Color;

public interface ShapeRenderable {
	public Color getColor();
	public void setColor(Color color);
	public float getWidth();
	public void setWidth(float width);
	public float getHeight();
	public void setHeight(float height);
}