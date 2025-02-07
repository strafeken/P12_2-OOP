package io.github.team2.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.team2.TextManager;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.InputSystem.InputManager;

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
	

	public InputManager getInputManager() {
		return im;
	}

	protected abstract void resize(int width, int height);
	
}
