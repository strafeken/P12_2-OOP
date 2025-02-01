package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.StartGame;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.InputSystem.InputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class MainMenu extends Scene {
	private EntityManager em;
	private InputManager im;
	private TextManager tm;
	private TextureObject image;

	@Override
	public void load() {
		System.out.println("Main Menu => LOAD");

		em = new EntityManager();
		im = new InputManager();
		tm = new TextManager();

		AudioManager.getInstance().loadSoundEffect("start", "sounds/start.mp3");

		image = new TextureObject("libgdx.png", new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2),
				new Vector2(0, 0), 0);

		em.addEntities(image);

		im.registerKeyDown(Input.Keys.SPACE, new StartGame(SceneManager.getInstance()));
	}

	@Override
	public void update() {
		em.update();
		im.update();
	}

	@Override
	public void draw(SpriteBatch batch) {
		em.draw(batch);
		tm.draw(batch, "Main Menu", 200, 200, Color.RED);
		tm.draw(batch, "Press SPACE to Start", 200, 150, Color.WHITE);
	}

	@Override
	public void draw(ShapeRenderer shape) {
		// No shapes to draw in main menu
	}

	@Override
	public void unload() {
		System.out.println("Main Menu => UNLOADED");
		dispose();
	}

	@Override
	public void dispose() {
		System.out.println("Main Menu => DISPOSE");
		em.dispose();
		AudioManager.getInstance().dispose();
	}

	@Override
	public InputManager getInputManager() {
		return im;
	}
}