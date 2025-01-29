package io.github.team2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Scene {
	
	protected EntityManager em;
	protected InputManager im;
	protected TextManager tm;
	
	public abstract void load(); // load assets and heavy resources
	public abstract void update();
	public abstract void draw(SpriteBatch batch);
	public abstract void draw(ShapeRenderer shape);
	public abstract void unload(); // free resources when leaving the scene
	public abstract void dispose(); // permanently free resources when closing the game

	public InputManager getInputManager()
	{
		return im;
	}
}
