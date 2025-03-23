package abstractengine.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface Clickable {
    public boolean isPressed(Vector2 touchPos);
    public void execute();
    public void update();
    public void draw(SpriteBatch batch);
}
