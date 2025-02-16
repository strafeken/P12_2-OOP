package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.TriangleBehaviour;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.Utils.DisplayManager;
import io.github.team2.EntitySystem.DynamicGameShape;


public class Triangle extends DynamicGameShape<TriangleBehaviour.State, TriangleBehaviour.Move> {
    private float size;

    
 

    public Triangle() {
        super();
        setEntityType(EntityType.TRIANGLE);
        setPosition(new Vector2(0, 0));
        
        this.size = 10;
        initActionMap();
      

    }

    public Triangle(EntityType type, Vector2 position, Vector2 direction, Vector2 rotation, float speed, Color color, float size, float offset,
    	TriangleBehaviour.State state, TriangleBehaviour.Move actionState) {

        super(type, position, direction,rotation, speed, color , state, actionState);
        this.size = size;
        initActionMap();
       
        // auto calculate width and height
        this.setWidth(2 * size);
        this.setHeight(2 * size);
      
      
    }
  
  

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }



    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(getColor());
        Vector2 pos = getPosition();
        float x = pos.x;
        float y = pos.y;

        shape.triangle(
            x - size, y - size,     // bottom-left
            x, y + size,            // top
            x + size, y - size      // bottom-right
        );
    }


  
  
  
   @Override
	public void initActionMap() {

	   getActionMap().put(TriangleBehaviour.Move.LEFT, new Move(this, new Vector2(-1, 0)));
	   getActionMap().put(TriangleBehaviour.Move.RIGHT, new Move(this, new Vector2(1, 0)));

	}


	public boolean checkPositionLeft() {
		return (getPosition().x - getWidth() / 2) <= DisplayManager.getScreenOriginX();
	}

	public boolean checkPositionRight() {
		return (getPosition().x + getWidth() / 2) >= DisplayManager.getScreenWidth();
	}

	public void updateMovement() {
		// move from default
		if (getCurrentState() == TriangleBehaviour.State.IDLE) {

			setCurrentActionState(TriangleBehaviour.Move.LEFT);
			setCurrentState(TriangleBehaviour.State.MOVING);
		} 
		
		else if (getCurrentState() == TriangleBehaviour.State.MOVING) {
			switch (getCurrentActionState()) {
			
			case NONE:
				// state not changed
				System.out.println("Triangle state stuck in NONE");
				return;

			case LEFT:

				// change dir if reach
				if (checkPositionLeft()) {

					setCurrentActionState(TriangleBehaviour.Move.RIGHT);
				}

				break;

			case RIGHT:
				// change dir if reach
				if (checkPositionRight()) {
					setCurrentActionState(TriangleBehaviour.Move.LEFT);
				}
				break;

			default:
				System.out.println("Unknown direction");
				break;
			}

			getAction(getCurrentActionState()).execute();

		}

		else {
			System.out.println("Unknown state");

		}

	}
  
  
  
	@Override
	public void update() {
//		System.out.println("Triangle  XY: " + getX() + " / " + getY());
		updateMovement();
		updateBody();
	}
  
  

}
