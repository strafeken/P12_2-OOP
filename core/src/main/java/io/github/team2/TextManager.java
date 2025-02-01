package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextManager {

	private BitmapFont font;
	public BitmapFont getFont() {
        return font;
    }
	public TextManager() {
		font = new BitmapFont();
		font.getData().setScale(2.0f);
	}

	public TextManager(BitmapFont font) {
		this.font = font;
	}

	public void draw(SpriteBatch batch, String text, float x, float y, Color color) {
		font.setColor(color);
		font.draw(batch, text, x, y);
	}

	public void dispose() {
		font.dispose();
	}
}
