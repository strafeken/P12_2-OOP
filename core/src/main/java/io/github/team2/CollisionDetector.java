package io.github.team2;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionDetector implements ContactListener {
	
	private CollisionResolver collisionResolver;
	
	public CollisionDetector(CollisionResolver resolver)
	{
		collisionResolver = resolver;
	}
	
	@Override
    public void beginContact(Contact contact)
	{
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (userDataA != null && userDataB != null)
        {
        	Entity a = (Entity)userDataA;
        	Entity b = (Entity)userDataB;
        	
//            System.out.println("Collision detected: " + a.getEntityType() + " & " + b.getEntityType());
            collisionResolver.resolveCollision(a, b);
        }
    }

    @Override
    public void endContact(Contact contact)
    {
//        System.out.println("Collision ended");
    }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
			
	}
}
