package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import java.util.HashMap;
import io.github.team2.Actions.TriangleBehaviour;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.GameShape;
import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.EntitySystem.DynamicGameShape;
import io.github.team2.EntitySystem.Dynamics;


public class Triangle extends DynamicGameShape<TriangleBehaviour.State, TriangleBehaviour.Move> {
    private float size;
    private float offset;
    
  

    
  
 

    public Triangle() {
        super();
        setEntityType(EntityType.TRIANGLE);
        setPosition(new Vector2(0, 0));
        
        this.size = 10;
        this.offset = 0;
        initActionMoveMap();
      

    }

    public Triangle(EntityType type, Vector2 position, Vector2 direction, float speed, Color color, float size, float offset,
    	TriangleBehaviour.State state, TriangleBehaviour.Move actionState) {
        super(position, direction, speed, color , state, actionState);
        //super(position, direction, speed, color);
        setEntityType(type);
        
        this.size = size;
        this.offset = offset;
        
        //super.setCurrentState(TriangleBehaviour.State.IDLE);
        //super.setCurrentActionState(TriangleBehaviour.Move.NONE);
        initActionMoveMap();
       
      
        // auto calculate width and height
        this.setWidth(2 * offset);
        this.setHeight(2 * offset);
      
      
    }
  
  

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

  
  
  
  
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
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
    public boolean isOutOfBound(Vector2 direction) {
        return false; // Implement boundary checking if needed
    }
  
  
  
   @Override
	public void initActionMoveMap() {

		getMoveMap().put(TriangleBehaviour.Move.LEFT, new Move(this, new Vector2(-1, 0)));
		getMoveMap().put(TriangleBehaviour.Move.RIGHT, new Move(this, new Vector2(1, 0)));

	}


  
  

	@Override
	public <E extends Enum<E>> Action getAction(E moveKey) {

		Action action = getMoveMap().get(moveKey);

		return action;
	}

	public boolean checkPosition() {

		return true;
	}

	public boolean checkPositionLeft() {
		return (getPosition().x - getWidth() / 2) <= SceneManager.screenLeft;
	}

	public boolean checkPositionRight() {
		return (getPosition().x + getWidth() / 2) >= SceneManager.screenWidth;
	}

	public void updateMovement() {
		// move from default
		if (getCurrentState() == TriangleBehaviour.State.IDLE) {

			setCurrentActionState(TriangleBehaviour.Move.LEFT);
			setCurrentState(TriangleBehaviour.State.MOVING);
		} 
		
		else if (getCurrentState() == TriangleBehaviour.State.MOVING) {
			switch (getCurrentActionState()) {
			// move left at start
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
  
  
  /*
	@Override
	public void update() {
//		System.out.println("Triangle  XY: " + getX() + " / " + getY());
		updateMovement();
		updateBody();
	}
  */
  

}
