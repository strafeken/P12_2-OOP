package abstractEngine.InputSystem;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class UIComponent implements Clickable {
    protected Vector2 position;
    protected Rectangle bounds;

    public UIComponent(Vector2 position, float width, float height) {
        this.position = position;
        this.bounds = new Rectangle(position.x - width / 2, position.y - height / 2, width, height);
    }

    @Override
    public boolean isPressed(Vector2 touchPos) {
        return bounds.contains(touchPos);
    }
    
    @Override
    public void update() {
        bounds.setPosition(position.x - bounds.width / 2, position.y - bounds.height / 2);
    }
}
