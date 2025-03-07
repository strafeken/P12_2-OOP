package io.github.team2.EntitySystem;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import io.github.team2.Circle;
import io.github.team2.Triangle;

public class PhysicsBody {
	private Body body;
	private static final float TIME_STEP = 1 / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private World world;

	public PhysicsBody(World world, Entity entity, BodyDef.BodyType bodyType) {
		this.world = world;
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

	private FixtureDef createFixtureDef(float density, float friction, float restitution) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		return fixtureDef;
	}

	private Shape createShapeForEntity(Entity entity) {
		EntityType eType = entity.getEntityType();

		switch (eType) {
		case CIRCLE:
			CircleShape circle = new CircleShape();
			circle.setRadius(((Circle) entity).getRadius());
			return circle;
		case TRIANGLE:
			PolygonShape triangle = new PolygonShape();
			float triangleSize = ((Triangle) entity).getSize();
			triangle.setAsBox(triangleSize, triangleSize);
			return triangle;

		default:
			PolygonShape box = new PolygonShape();
			
			TexturedObject textureEntity =  (TexturedObject) entity;
			box.setAsBox(textureEntity.getWidth() / 2, textureEntity.getHeight() / 2);
			return box;
		}
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}

	public void setLocation(float x, float y) {
		body.setTransform(x, y, body.getAngle());
	}

	public void setLinearVelocity(float x, float y) {
		body.setLinearVelocity(x, y);
	}
	
    public World getWorld() {
        return world;
    }

    public Body getBody() {
        return body;
    }
    
    public float getAngle() {
        if (body != null) {
            return body.getAngle(); // ✅ Gets rotation in radians
        }
        return 0; // ✅ Default to 0 if body is null (prevents errors)
    }

    public void setTransform(Vector2 position, float newAngle) {
        if (body == null) {
            System.out.println("[ERROR] setTransform() called on null body!");
            return;
        }

//        System.out.println("[DEBUG] Applying Transform: Position = " + position + ", Angle = " + newAngle);

        // ✅ Ensure setTransform does not repeatedly call itself
        if (body.getAngle() != newAngle) { 
            body.setTransform(position, newAngle);
        } else {
//            System.out.println("[DEBUG] setTransform skipped - Angle unchanged.");
        }
    }

	private float newAngle() {
		// TODO Auto-generated method stub
		return newAngle();
	}
	

	public void updateEntityPosition(Entity entity) {
		entity.setPosition(new Vector2(body.getPosition().x, body.getPosition().y));
		updateEntityRotation(entity);
	}
	

	public void updateEntityRotation(Entity entity) {
		 if ( entity instanceof Dynamics<?,?>) {
			float angle = body.getAngle(); // Get rotation in radians
			Vector2 newRotation = new Vector2((float) Math.cos(angle), (float) Math.sin(angle)); // Convert to Vector2
			((Dynamics< ?,? >)entity).setRotation(newRotation); // Update entity's rotation as a Vector2	
		}
	}

	public void updatePhysics(float deltaTime) {
		float accumulator = 0;
		accumulator += deltaTime;

		while (accumulator >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}
	}

	public void dispose() {
        if (body != null && world != null) {
            world.destroyBody(body);
            body = null;
        }
    }
}
