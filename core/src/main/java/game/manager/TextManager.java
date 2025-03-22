package game.manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextManager {
    private BitmapFont font;
    private float baseScale = 2.0f;

    public TextManager() {
        font = new BitmapFont();
        font.getData().setScale(baseScale);
    }

    public TextManager(BitmapFont font) {
    	this.font = font;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setScale(float scaleX, float scaleY) {
        font.getData().setScale(baseScale * scaleX, baseScale * scaleY);
    }

    public void resetScale() {
        font.getData().setScale(baseScale);
    }

	public void draw(SpriteBatch batch, String text, float x, float y, Color color) {
		font.setColor(color);
		font.draw(batch, text, x, y);
	}

	public void dispose() {
		font.dispose();
	}
}
