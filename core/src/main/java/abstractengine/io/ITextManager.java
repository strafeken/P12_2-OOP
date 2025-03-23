package abstractengine.io;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ITextManager {
    public BitmapFont getFont();
    public void setScale(float scaleX, float scaleY);
    public void resetScale();
	public void draw(SpriteBatch batch, String text, float x, float y, Color color);
	public void dispose();
}
