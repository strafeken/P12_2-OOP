package io.github.team2;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.ResumeGame;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.InputSystem.InputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

public class PauseMenu extends Scene {

	private Entity image;

	public PauseMenu() {

	}

	@Override
	public void load() {
		System.out.println("Pause Menu => LOAD");

		em = new EntityManager();
		im = new InputManager();
		tm = new TextManager();

		image = new TextureObject("libgdx.png", new Vector2(SceneManager.screenWidth / 2, SceneManager.screenHeight / 2),
				new Vector2(0, 0), 0);

		em.addEntities(image);

		im.registerKeyDown(Input.Keys.ESCAPE, new ResumeGame(SceneManager.getInstance()));
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(SpriteBatch batch) {
		em.draw(batch);

		tm.draw(batch, "Pause Menu", 200, 150, Color.RED);
	}

	@Override
	public void draw(ShapeRenderer shape) {

	}

	@Override
	public void unload() {
		System.out.println("Pause Menu => UNLOAD");
		image = null;
	}

	@Override
	public void dispose() {
		((TextureObject) image).dispose();
	}

	@Override
	protected void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
}
