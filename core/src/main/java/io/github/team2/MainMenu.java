package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainMenu extends Scene {
    
    private Entity image;

	public MainMenu()
	{
		em = new EntityManager();
		
		image = new TextureObject("libgdx.png", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		
		em.addEntities(image);
	}

	@Override
	public void load()
	{
		System.out.println("Main Menu => LOAD");
	}

	@Override
	public void update()
	{
		if (Gdx.input.isKeyPressed(Keys.SPACE))
			SceneManager.getInstance().setNextScene(SceneID.GAME_SCENE);
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		em.draw(batch);
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
