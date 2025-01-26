package io.github.team2;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsBody {
	
	private Body body;
	
	public PhysicsBody(World world, Entity entity, BodyDef.BodyType bodyType, boolean isTextureObject, boolean isCircle)
	{
    	BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(entity.getPosition().x, entity.getPosition().y);
        
        body = world.createBody(bodyDef);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f;
        
        if (isTextureObject)
        {
        	PolygonShape shape = new PolygonShape();
            shape.setAsBox(((TextureObject)entity).getWidth() / 2, ((TextureObject)entity).getHeight() / 2);
            fixtureDef.shape = shape;
        }
        else if (isCircle)
        {
        	CircleShape shape = new CircleShape();
        	shape.setRadius(((Circle) entity).getRadius());
        	fixtureDef.shape = shape;        	
        }
        else // currently just triangle
        {
        	PolygonShape shape = new PolygonShape();
            shape.setAsBox(50, 50);
            fixtureDef.shape = shape;
        }
        
        body.createFixture(fixtureDef);
        fixtureDef.shape.dispose();

        body.setUserData(entity);
	}
	
	public Vector2 getPosition()
	{
		return body.getPosition();
	}
	
	
	
	
	public void setLocation(float x, float y)
	{
		body.setTransform(x, y, 0);
	}

	public void setLinearVelocity(float x, float y)
    {
        body.setLinearVelocity(x, y);
    }

    public void updateEntityPosition(Entity entity)
    {
        entity.setPosition(new Vector2(body.getPosition().x, body.getPosition().y));
    }

    public void dispose()
    {
        body.getWorld().destroyBody(body);
    }
}
