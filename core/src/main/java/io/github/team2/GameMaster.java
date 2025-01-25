package io.github.team2;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

public class GameMaster extends ApplicationAdapter {
	
	private SpriteBatch batch;
	
	private ShapeRenderer shape;

	private Entity droplets[];
	private Entity bucket;
	private Entity circle;
	private Entity triangle;
	
	private EntityManager em;
	
	@Override
	public void create()
	{
		em = new EntityManager();
		
		batch = new SpriteBatch();
		
		shape = new ShapeRenderer();
		
		droplets = new TextureObject[10];
		
		Random random = new Random();
		
		for(int i = 0; i < droplets.length; ++i)
			droplets[i] = new Drop("droplet.png", random.nextInt(600), random.nextInt(440), 100);
		
		bucket = new Bucket("bucket.png", 200, 0, 200);
		
		circle = new Circle(Color.RED, 50, 500, 300, 200);

		triangle = new Triangle(Color.GREEN, 100, 100, 200);
		
		for(int i = 0; i < droplets.length; ++i)
			em.addEntities(droplets[i]);
		
		em.addEntities(bucket);
		em.addEntities(circle);
		em.addEntities(triangle);	
	}
	
	@Override
	public void render()
	{
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		em.update();
		
		em.draw(batch);
		em.draw(shape);
	}
	
	@Override
	public void dispose()
	{
		batch.dispose();
		
		shape.dispose();
		
		for(Entity entity : droplets)
		{
			if (entity instanceof TextureObject)
				((TextureObject)entity).dispose();
		}
	}
}