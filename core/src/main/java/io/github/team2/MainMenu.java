package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.StartGame;

public class MainMenu extends Scene {
    
    private Entity image;

	public MainMenu()
	{

	}

	@Override
	public void load()
	{
		System.out.println("Main Menu => LOAD");
		
		em = new EntityManager();
		im = new InputManager();
		tm = new TextManager();
		
		image = new TextureObject("libgdx.png", new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2), new Vector2(0, 0), 0);
		
		em.addEntities(image);
		
		im.registerKeyDown(Input.Keys.SPACE, new StartGame(SceneManager.getInstance()));
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(SpriteBatch batch)
	{
		em.draw(batch);
		
		tm.draw(batch, "Main Menu", 200, 200, Color.RED);
	}

	@Override
	public void draw(ShapeRenderer shape)
	{
		
	}

	@Override
	public void unload()
	{	
		System.out.println("Main Menu => UNLOADED");
	}

	@Override
	public void dispose()
	{
		System.out.println("Main Menu => DISPOSE");
		em.dispose();
	}
}
