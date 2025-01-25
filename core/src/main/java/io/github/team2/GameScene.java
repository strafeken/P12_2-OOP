package io.github.team2;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameScene extends Scene {
	
	private World world;
	// render collision debugger
    private Box2DDebugRenderer debugRenderer;
    
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;

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
		
		world = new World(new Vector2(0, -10), true);
		
        debugRenderer = new Box2DDebugRenderer();
		
		em = new EntityManager();

		collisionResolver = new CollisionResolver(em);
		collisionDetector = new CollisionDetector(collisionResolver);
		
		droplets = new TextureObject[10];
		
		Random random = new Random();

		for (int i = 0; i < droplets.length; ++i)
		{
			droplets[i] = new Drop(EntityType.DROP, "droplet.png", random.nextInt(600), random.nextInt(440), 100);
			droplets[i].InitPhysicsBody(world, BodyDef.BodyType.DynamicBody, true, false);
		}

		bucket = new Bucket(EntityType.BUCKET, "bucket.png", 200, 50, 20);
		bucket.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody, true, false);
		
		circle = new Circle(EntityType.CIRCLE, Color.RED, 50, 500, 300, 20);
		circle.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody, false, true);

		triangle = new Triangle(EntityType.TRIANGLE, Color.GREEN, 100, 100, 20);
		triangle.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody, false, false);

		for (int i = 0; i < droplets.length; ++i)
			em.addEntities(droplets[i]);

		em.addEntities(bucket);
		em.addEntities(circle);
		em.addEntities(triangle);
		
		world.setContactListener(collisionDetector);
	}

	@Override
	public void update()
	{
		em.update();
		
		// update physics at the end of render() loop
		// should be under draw() physics will update when game is paused
		// could add a boolean however it'll be a bit messy
		world.step(1 / 60f, 6, 2);
		
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
		
		debugRenderer.render(world, shape.getProjectionMatrix());
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
		
        world.dispose();
        
        debugRenderer.dispose();
	}
}
