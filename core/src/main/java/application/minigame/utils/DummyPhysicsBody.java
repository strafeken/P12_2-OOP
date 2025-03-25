package application.minigame.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import abstractengine.entity.PhysicsBody;
import abstractengine.entity.Entity;

public class DummyPhysicsBody extends PhysicsBody {
    // Use a static dummy world to avoid creating multiple worlds
    private static final World dummyWorld = new World(new Vector2(0, 0), true);

    private Vector2 dummyPosition;
    private float dummyAngle;

    /**
     * Creates a new DummyPhysicsBody for the given entity.
     * Note: We use a kinematic body type and a dummy world so that no physics is actually simulated.
     */
    public DummyPhysicsBody(Entity entity) {
        super(dummyWorld, entity, BodyDef.BodyType.KinematicBody);
        this.dummyPosition = entity.getPosition();
        this.dummyAngle = 0f;
    }

    @Override
    public Vector2 getPosition() {
        return dummyPosition;
    }

    @Override
    public float getAngle() {
        return dummyAngle;
    }

    public void setDummyPosition(Vector2 position) {
        this.dummyPosition = position;
    }

    public void setDummyAngle(float angle) {
        this.dummyAngle = angle;
    }
}
