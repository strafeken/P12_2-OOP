package abstractengine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import game.Entity.EntityType;

public abstract class Entity {

    private Vector2 position;
    private Vector2 direction;

    private EntityType type;
    private PhysicsBody body;

    public Entity() {
        position = new Vector2(0, 0);
        direction = new Vector2(0, 0);
        type = EntityType.UNDEFINED;
        body = null;
    }

    public Entity(EntityType type, Vector2 position, Vector2 direction) {
        this.type = type;
        this.position = position;
        this.direction = direction;
        body = null;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public EntityType getEntityType() {
        return type;
    }

    public void setEntityType(EntityType type) {
        this.type = type;
    }

    public PhysicsBody getPhysicsBody() {
        return body;
    }

    public void initPhysicsBody(World world, BodyDef.BodyType bodyType) {
        if (world != null) {
            body = new PhysicsBody(world, this, bodyType);
        } else {
            System.err.println("Cannot initialize physics body: world is null");
        }
    }

    // Default implementations that subclasses can override
    public void draw(ShapeRenderer shape) {
        // Check if this Entity is a ShapeRenderable before attempting to draw
        if (this instanceof ShapeRenderable && shape != null) {
            ((ShapeRenderable) this).drawShape(shape);
        }
        // Default empty implementation if not a ShapeRenderable
    }

    public void draw(SpriteBatch batch) {
        // Default empty implementation
    }

    // Sync position with physics body position
    public void updateBody() {
        if (body == null)
            return;

        body.updateEntityPosition(this);
        body.updateEntityRotation(this);
    }

    public abstract void update();
}