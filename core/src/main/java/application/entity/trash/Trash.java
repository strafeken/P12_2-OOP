package application.entity.trash;


import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.DynamicTextureObject;
import abstractengine.input.Action;
import application.entity.EntityType;
import application.entity.movement.Floating;
import application.entity.trash.TrashBehaviour.Move;
import application.entity.trash.TrashBehaviour.State;


public abstract class Trash extends DynamicTextureObject<TrashBehaviour.State, TrashBehaviour.Move> {
    
	

    public Trash(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
            float speed, State state, Move actionState) {
        super(type, texture, size, position, direction, rotation, speed, state, actionState);

        
        initActionMap();
    }
    
    
    
    

    @Override
 	public void initActionMap() {

    	getActionMap().put(TrashBehaviour.Move.FLOAT, new Floating(this));
 	}


    public void updateMovement() {

		// move from default
		if (getCurrentState() == TrashBehaviour.State.IDLE) {

			setCurrentActionState(TrashBehaviour.Move.FLOAT);
			setCurrentState(TrashBehaviour.State.FLOAT);
		}

		else if (getCurrentState() == TrashBehaviour.State.FLOAT) {
			switch (getCurrentActionState()) {

			case NONE:
				// state not changed
				System.out.println("Trash state stuck in NONE");
				return;

			case FLOAT:


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
    	
    }


    
    public Vector2 getVelocity() {
        Action action = getAction(getCurrentActionState());
        if (action instanceof Floating) {
            return ((Floating) action).getVelocity();
        } else {
            // Fallback: velocity unknown
            return new Vector2(0, 0);
        }
    }

    public void setVelocity(Vector2 velocity) {
        Action action = getAction(getCurrentActionState());
        if (action instanceof Floating) {
            ((Floating) action).setVelocity(velocity);
        }
    }
    
}
