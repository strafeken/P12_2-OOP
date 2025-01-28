package io.github.team2;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GameScene extends Scene {
	
	private World world;
	// render collision debugger
    private Box2DDebugRenderer debugRenderer;
    
    private CollisionDetector collisionDetector;
    private CollisionResolver collisionResolver;
    
	private TextManager tm;

	private Entity droplets[];
	private Entity bucket;
	private Entity circle;
	private Entity triangle;
	
	private static final float TIME_STEP = 1/60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	
	private float accumulator = 0f;
	
	public GameScene()
	{
		
	}

	@Override
	public void load()
	{
		System.out.println("Game Scene => LOAD");
		
		world = new World(new Vector2(0, -100), true);
		
        debugRenderer = new Box2DDebugRenderer();
		
		em = new EntityManager();

		collisionResolver = new CollisionResolver(em);
		collisionDetector = new CollisionDetector(collisionResolver);
		
		tm = new TextManager();
		
		droplets = new TextureObject[10];
		
		Random random = new Random();

		for (int i = 0; i < droplets.length; ++i)
		{
			droplets[i] = new Drop(EntityType.DROP, "droplet.png", new Vector2(random.nextInt(600), random.nextInt(440)), new Vector2(0, 0), 100);
			droplets[i].InitPhysicsBody(world, BodyDef.BodyType.DynamicBody);
		}

		bucket = new Bucket(EntityType.BUCKET, "bucket.png", new Vector2(200, 50), new Vector2(0, 0), 200);
		bucket.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody);
		
		circle = new Circle(EntityType.CIRCLE, new Vector2(500, 300), new Vector2(0, 0), 200, Color.RED, 50);
		circle.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody);

		triangle = new Triangle(EntityType.TRIANGLE, new Vector2(100, 100), new Vector2(0, 0), 200, Color.GREEN, 50);
		triangle.InitPhysicsBody(world, BodyDef.BodyType.KinematicBody);

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
	    float deltaTime = Gdx.graphics.getDeltaTime();

	    accumulator += deltaTime;

	    while (accumulator >= TIME_STEP)
	    {
	        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	        accumulator -= TIME_STEP;
	    }
		
		if (Gdx.input.isKeyPressed(Keys.X))
			SceneManager.getInstance().setNextScene(SceneID.MAIN_MENU);
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		em.draw(batch);
		
		tm.draw(batch, "Game Scene", 200, 200, Color.RED);
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
