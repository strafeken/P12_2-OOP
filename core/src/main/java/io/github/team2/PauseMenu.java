package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.ResumeGame;

public class PauseMenu extends Scene {

    private Entity image;
    
    public PauseMenu()
    {

    }
    
	@Override
    public void load()
	{
        System.out.println("Pause Menu => LOAD");
        
    	em = new EntityManager();
    	im = new InputManager();
    	tm = new TextManager();
        
		image = new TextureObject("libgdx.png", new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2), new Vector2(0, 0), 0);
		
		em.addEntities(image);
		
		im.registerKeyDown(Input.Keys.ESCAPE, new ResumeGame(SceneManager.getInstance()));
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(SpriteBatch batch)
    {
    	em.draw(batch);
    	
    	tm.draw(batch, "Pause Menu", 200, 150, Color.RED);
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
