package io.github.team2;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity implements Movement {
	
	private Vector2 position;
	private float speed;
	private EntityType type;
	private PhysicsBody body;
	
	public Entity()
	{
		position = new Vector2(0, 0);
		speed = 0;
		type = null;
		body = null;
	}
	
	public Entity(Vector2 position, float speed)
	{
		this.position = position;
		this.speed = speed;
		type = null;
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
	
	public void InitPhysicsBody(World world, BodyDef.BodyType bodyType, boolean isTextureObject, boolean isCircle)
	{
		body = new PhysicsBody(world, this, bodyType, isTextureObject, isCircle);
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
