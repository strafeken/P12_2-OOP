package application.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import abstractengine.entity.DynamicTextureObject;
import abstractengine.entity.Entity;
import application.entity.AlienBehaviour.Move;
import application.entity.AlienBehaviour.State;
import application.entity.movement.Chase;
import application.scene.LevelManager;

public class Alien extends DynamicTextureObject<AlienBehaviour.State, AlienBehaviour.Move> {
    private Entity targetPlayer;

//    private float baseChaseSpeed = 30f; // Base speed (Level 1)
//    private float chaseSpeed; // Actual speed used
//    private float maxDistance = 500f; // Maximum distance to chase player
//    private float minDistance = 100f; // Increased from 50f to keep alien further from player
    private LevelManager levelManager;


    // Add respawn position to remember where to place the alien when respawning
    private Vector2 respawnPosition;

    public Alien(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
            float speed, State state, Move actionState) {
        super(type, texture, size, position, direction, rotation, speed, state, actionState);
        // Store initial position for respawning
        this.respawnPosition = new Vector2(position);
        this.levelManager = LevelManager.getInstance();
//        this.chaseSpeed = levelManager.getCurrentAlienSpeed();
    }



    @Override
 	public void initActionMap() {

    	getActionMap().put(AlienBehaviour.Move.CHASE, new Chase(this, targetPlayer, levelManager.getCurrentLevel()));
 	}


    public void updateMovement() {

		// move from default
		if (getCurrentState() == AlienBehaviour.State.IDLE) {

			setCurrentActionState(AlienBehaviour.Move.CHASE);
			setCurrentState(AlienBehaviour.State.MOVING);
		}

		else if (getCurrentState() == AlienBehaviour.State.MOVING) {
			switch (getCurrentActionState()) {

			case NONE:
				// state not changed
				System.out.println("Alien state stuck in NONE");
				return;

			case CHASE:


				break;


			default:
				System.out.println("Unknown direction");
				break;
			}


			getAction(getCurrentActionState()).execute();
		}
    }






    @Override
    public void update() {

    	updateMovement();
        // Update the physics body
        updateBody();
    }

    // Method to respawn the alien at its initial position
    public void respawn() {
        if (getPhysicsBody() != null) {
            getPhysicsBody().setLocation(respawnPosition.x, respawnPosition.y);
            getPhysicsBody().getBody().setLinearVelocity(0, 0);
        }
    }

    // Method to respawn the alien at a new position with proper collision handling
    public void respawnAt(Vector2 position) {
        System.out.println("Respawning alien at: " + position.x + ", " + position.y);

        // Simply set position for both the entity and physics body
        setPosition(position);

        // Set physics body position directly
        if (getPhysicsBody() != null && getPhysicsBody().getBody() != null) {
            // Reset velocity first
            getPhysicsBody().getBody().setLinearVelocity(0, 0);

            // Set position directly - use 100 as default PIXELS_PER_METER
            float pixelsPerMeter = 100f;
            getPhysicsBody().getBody().setTransform(
                position.x / pixelsPerMeter,
                position.y / pixelsPerMeter,
                0
            );

            // Important: wake up the body to ensure it processes the position change
            getPhysicsBody().getBody().setAwake(true);
        }
    }

    public void setTargetPlayer(Entity player) {
        this.targetPlayer = player;

        initActionMap();
    }

    public Entity getTargetPlayer() {
        return targetPlayer;
    }
}
