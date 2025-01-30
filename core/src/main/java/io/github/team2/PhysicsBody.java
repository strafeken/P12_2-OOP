package io.github.team2;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsBody {
	
	private Body body;
	
	public PhysicsBody(World world, Entity entity, BodyDef.BodyType bodyType)
	{
    	BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(entity.getPosition().x, entity.getPosition().y);
        
        body = world.createBody(bodyDef);
        
        FixtureDef fixtureDef = createFixtureDef(1f, 0.5f, 0.3f);
        
        fixtureDef.shape = createShapeForEntity(entity);
        
        body.createFixture(fixtureDef);
        fixtureDef.shape.dispose();

        body.setUserData(entity);
	}
	
	private FixtureDef createFixtureDef(float density, float friction, float restitution)
	{
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.density = density;
	    fixtureDef.friction = friction;
	    fixtureDef.restitution = restitution;
	    return fixtureDef;
	}
	
	private Shape createShapeForEntity(Entity entity)
	{
		EntityType eType = entity.getEntityType();

	    switch (eType)
	    {
	        case CIRCLE:
	            CircleShape circle = new CircleShape();
	            circle.setRadius(((Circle) entity).getRadius());
	            return circle;
	        case TRIANGLE:
	            PolygonShape triangle = new PolygonShape();
	            float triangleSize = ((Triangle) entity).getSize() / 2;
	            triangle.setAsBox(triangleSize, triangleSize);
	            return triangle;
	        default:
	            PolygonShape box = new PolygonShape();
	            TextureObject textureEntity = (TextureObject) entity;
	            box.setAsBox(textureEntity.getWidth() / 2, textureEntity.getHeight() / 2);
	            return box;
	    }
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
