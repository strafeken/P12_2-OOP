package io.github.team2;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScene extends Scene {

	private Entity droplets[];
	private Entity bucket;
	private Entity circle;
	private Entity triangle;

	public GameScene()
	{
		
	}

	@Override
	public void load()
	{
		System.out.println("Game Scene => LOAD");
		
		em = new EntityManager();
		
		droplets = new TextureObject[10];
		
		Random random = new Random();

		for (int i = 0; i < droplets.length; ++i)
			droplets[i] = new Drop("droplet.png", random.nextInt(600), random.nextInt(440), 100);

		bucket = new Bucket("bucket.png", 200, 0, 200);

		circle = new Circle(Color.RED, 50, 500, 300, 200);

		triangle = new Triangle(Color.GREEN, 100, 100, 200);

		for (int i = 0; i < droplets.length; ++i)
			em.addEntities(droplets[i]);

		em.addEntities(bucket);
		em.addEntities(circle);
		em.addEntities(triangle);
	}

	@Override
	public void update()
	{
		em.update();
		
		if (Gdx.input.isKeyPressed(Keys.X))
			SceneManager.getInstance().setNextScene(SceneID.MAIN_MENU);
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		em.draw(batch);
	}

	@Override
	public void draw(ShapeRenderer shape)
	{
		em.draw(shape);
	}

	@Override
	public void unload()
	{
		System.out.println("Game Scene => UNLOAD");
		
		dispose();
	}

	@Override
	public void dispose()
	{
		System.out.println("Game Scene => DISPOSE");
		
		em.dispose();
	}
}
