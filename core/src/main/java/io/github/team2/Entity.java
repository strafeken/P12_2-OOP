package io.github.team2;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity implements iMovable {
	
	private Vector2 position;
	private Vector2 direction;
	private float speed;
	private EntityType type;
	private PhysicsBody body;
	
	public Entity()
	{
		position = new Vector2(0, 0);
		direction = new Vector2(0, 0);
		speed = 0;
		type = EntityType.UNDEFINED;
		body = null;
	}
	
	public Entity(Vector2 position, Vector2 direction, float speed)
	{
		this.position = position;
		this.direction = direction;
		this.speed = speed;
		type = EntityType.UNDEFINED;
		body = null;
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector2 position)
	{
		this.position = position;
	}
	
	public Vector2 getDirection()
	{
		return direction;
	}
	
	public void setDirection(Vector2 direction)
	{
		this.direction = direction;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public EntityType getEntityType()
	{
		return type;
	}
	
	public void setEntityType(EntityType type)
	{
		this.type = type;
	}
	
	public PhysicsBody getBody()
	{
		return body;
	}
	
	public void InitPhysicsBody(World world, BodyDef.BodyType bodyType)
	{
		body = new PhysicsBody(world, this, bodyType);
	}
	
	public void draw(ShapeRenderer shape)
	{
		
	}
	
	public void draw(SpriteBatch batch)
	{
		
	}

	public abstract void update();
	
	// sync position with physics body position
	public void updateBody()
	{
        if (body == null)
        	return;
        
        body.updateEntityPosition(this);
	}
}
