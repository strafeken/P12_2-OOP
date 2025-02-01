package io.github.team2.CollisionSystem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.github.team2.EntitySystem.Entity;

public class CollisionDetector implements ContactListener {

	private List<CollisionListener> listeners = new ArrayList<>();

	@Override
	public void beginContact(Contact contact) {
		Object userDataA = contact.getFixtureA().getBody().getUserData();
		Object userDataB = contact.getFixtureB().getBody().getUserData();

		if (userDataA != null && userDataB != null) {
			Entity a = (Entity) userDataA;
			Entity b = (Entity) userDataB;

			CollisionType type = CollisionType.getCollisionType(a, b);

			if (type != null)
				notifyListeners(a, b, type);
		}
	}

	@Override
	public void endContact(Contact contact) {
//        System.out.println("Collision ended");
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	public void addListener(CollisionListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners(Entity a, Entity b, CollisionType type) {
		for (CollisionListener listener : listeners)
			listener.onCollision(a, b, type);
	}
}
