package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class PauseMenu extends Scene {

    private Entity image;
    
    public PauseMenu()
    {
    	em = new EntityManager();
    }
    
	@Override
    public void load()
	{
        System.out.println("Pause Menu => LOAD");
        
		image = new TextureObject("libgdx.png", new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2), 0);
		
		em.addEntities(image);
    }

    @Override
    public void update()
    {

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
        System.out.println("Pause Menu => UNLOAD");
        image = null;
    }

    @Override
    public void dispose()
    {
    	((TextureObject)image).dispose();
    }

}
