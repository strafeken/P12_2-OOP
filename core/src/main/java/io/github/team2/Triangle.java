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
  
import io.github.team2.EntitySystem.Dynamics;


public class Triangle extends Dynamics {
    private float size;
    private float offset;
    private Color color;
  
    // TODO: when done shift to dynamic class
    private HashMap<TriangleBehaviour.Move, Action> moveMap;
    // movment states can move to dynamic
    private TriangleBehaviour.State currentState;
    private TriangleBehaviour.Move currentActionState;
    
  
 

    public Triangle() {
        super(0);
        setEntityType(EntityType.TRIANGLE);
        setPosition(new Vector2(0, 0));
        this.color = Color.WHITE;
        this.size = 10;
        this.offset = 0;
      
        moveMap = new HashMap<>();
        currentState = TriangleBehaviour.State.IDLE;
        currentActionState = TriangleBehaviour.Move.NONE;
        initActionMoveMap();
    }

    public Triangle(EntityType type, Vector2 position, Vector2 direction, float speed, Color color, float size, float offset) {
        super(position, direction, speed);
        setEntityType(type);
        this.color = color;
        this.size = size;
        this.offset = offset;
      
        moveMap = new HashMap<>();
        currentState = TriangleBehaviour.State.IDLE;
        currentActionState = TriangleBehaviour.Move.NONE;
        initActionMoveMap();
        
      /*
        // auto calculate width and height
        this.setWidth(2 * offset);
        this.setHeight(2 * offset);
      */
      
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
  
  
  
    

	// TODO: move to dynamic class

	public void setCurrentActionState(TriangleBehaviour.Move moveState) {
		currentActionState = moveState;
	}

	public TriangleBehaviour.Move getCurrentActionState() {
		return currentActionState;
	}

	public void setCurrentState(TriangleBehaviour.State state) {
		currentState = state;
	}

	public TriangleBehaviour.State getCurrentState() {
		return currentState;
	}

	public void initActionMoveMap() {

		moveMap.put(TriangleBehaviour.Move.LEFT, new Move(this, new Vector2(-1, 0)));
		moveMap.put(TriangleBehaviour.Move.RIGHT, new Move(this, new Vector2(1, 0)));

	}

	public void clearMoveMap() {

		moveMap.clear();
	}
  
  

	@Override
	public <E extends Enum<E>> Action getAction(E moveKey) {

		Action action = moveMap.get(moveKey);

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
