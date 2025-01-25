package io.github.team2;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity implements Movement {
	
	private float x;
	private float y;
	private float speed;
	private EntityType type;
	private PhysicsBody body;
	
	public Entity()
	{
		x = 0;
		y = 0;
		speed = 0;
		type = null;
		body = null;
	}
	
	public Entity(float x, float y, float speed)
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
		type = null;
		body = null;
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
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
