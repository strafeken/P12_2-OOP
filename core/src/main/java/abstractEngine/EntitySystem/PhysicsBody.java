package abstractEngine.EntitySystem;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * PhysicsBody handles the Box2D physics implementation for entities.
 * Following Single Responsibility Principle, this class focuses solely on physics behavior.
 */
public class PhysicsBody {
    private Body body;
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private World world;

    /**
     * Creates a physics body for an entity in the given world
     * @param world The Box2D world
     * @param entity The entity to create physics for
     * @param bodyType The type of body (static, dynamic, kinematic)
     */
    public PhysicsBody(World world, Entity entity, BodyDef.BodyType bodyType) {
        this.world = world;
        initializeBody(world, entity, bodyType);
    }
    
    /**
     * Initializes the Box2D body for the entity
     */
    private void initializeBody(World world, Entity entity, BodyDef.BodyType bodyType) {
        if (world == null || entity == null) {
            throw new IllegalArgumentException("World and entity cannot be null");
        }
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(entity.getPosition().x, entity.getPosition().y);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = createFixtureDef(1f, 0.5f, 0.3f);
        
        // Create a shape for the entity
        Shape shape = createShapeForEntity(entity);
        if (shape != null) {
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            shape.dispose();
        } else {
            System.err.println("Failed to create physics shape for entity type: " + entity.getEntityType());
        }

        body.setUserData(entity);
    }

    /**
     * Creates a fixture definition with the specified properties
     */
    private FixtureDef createFixtureDef(float density, float friction, float restitution) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        return fixtureDef;
    }

    /**
     * Creates a shape for the entity based on its properties
     * This method is responsible for determining the appropriate collision shape
     */
    private Shape createShapeForEntity(Entity entity) {
        // Base shape creation on whether the entity implements TexturedObject
        if (entity instanceof TexturedObject) {
            TexturedObject texturedEntity = (TexturedObject) entity;
            PolygonShape box = new PolygonShape();
            box.setAsBox(texturedEntity.getWidth() / 2, texturedEntity.getHeight() / 2);
            return box;
        }
        
        // Generic fallback - create a small box shape
        PolygonShape defaultShape = new PolygonShape();
        defaultShape.setAsBox(5f, 5f); // Default size of 10x10 units
        return defaultShape;
    }

    /**
     * Gets the position of this physics body
     * @return Current position as Vector2
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Sets the location of this physics body
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setLocation(float x, float y) {
        body.setTransform(x, y, body.getAngle());
    }

    /**
     * Sets the linear velocity of this physics body
     * @param x X velocity component
     * @param y Y velocity component
     */
    public void setLinearVelocity(float x, float y) {
        body.setLinearVelocity(x, y);
    }
    
    /**
     * Gets the Box2D world this body belongs to
     * @return Box2D world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the Box2D body
     * @return Box2D body
     */
    public Body getBody() {
        return body;
    }
    
    /**
     * Gets the rotation angle of this physics body
     * @return Angle in radians
     */
    public float getAngle() {
        if (body != null) {
            return body.getAngle(); // Gets rotation in radians
        }
        return 0; // Default to 0 if body is null (prevents errors)
    }

    /**
     * Sets the transform (position and rotation) of this physics body
     * @param position New position
     * @param newAngle New angle in radians
     */
    public void setTransform(Vector2 position, float newAngle) {
        if (body == null) {
            System.err.println("setTransform() called on null body!");
            return;
        }

        // Ensure setTransform does not repeatedly call itself
        if (body.getAngle() != newAngle) { 
            body.setTransform(position, newAngle);
        }
    }
    
    /**
     * Sets all fixtures of this body as sensors (no collision response)
     */
    public void setAsSensor() {
        if (body != null) {
            for (Fixture fixture : body.getFixtureList()) {
                fixture.setSensor(true);
            }
        }
    }
    
    /**
     * Updates the entity's position based on the physics body
     * @param entity The entity to update
     */
    public void updateEntityPosition(Entity entity) {
        if (body != null && entity != null) {
            entity.setPosition(new Vector2(body.getPosition().x, body.getPosition().y));
            updateEntityRotation(entity);
        }
    }
    
    /**
     * Updates the entity's rotation based on the physics body
     * @param entity The entity to update
     */
    public void updateEntityRotation(Entity entity) {
        if (body != null && entity != null && entity instanceof Dynamics<?, ?>) {
            float angle = body.getAngle(); // Get rotation in radians
            Vector2 newRotation = new Vector2((float) Math.cos(angle), (float) Math.sin(angle)); // Convert to Vector2
            ((Dynamics<?, ?>) entity).setRotation(newRotation); // Update entity's rotation as a Vector2    
        }
    }

    /**
     * Steps the physics simulation forward
     * @param deltaTime Time since last update
     */
    public void updatePhysics(float deltaTime) {
        if (world == null) return;
        
        float accumulator = 0;
        accumulator += deltaTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    /**
     * Disposes of this physics body and resources
     */
    public void dispose() {
        if (body != null && world != null) {
            world.destroyBody(body);
            body = null;
        }
    }
}