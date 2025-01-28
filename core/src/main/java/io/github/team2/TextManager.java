package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextManager {
	
	private BitmapFont font;
	
	public TextManager()
	{
		font = new BitmapFont();
	}
	
	public TextManager(BitmapFont font)
	{
		this.font = font;
	}

	public void draw(SpriteBatch batch, String text, float x, float y, Color color)
	{
		font.setColor(color);
		font.draw(batch, text, x, y);
	}
	
	public void dispose()
	{
		font.dispose();
	}
}
