package abstractengine.io;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.Action;

public class Button extends UIComponent {
    private Texture texture;
    private Action action;

    public Button(String texturePath, Vector2 position, Action action, float width, float height) {
        super(position, width, height);
        this.texture = new Texture(texturePath);
        this.action = action;
    }

    @Override
    public void execute() {
        if (action != null)
            action.execute();
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x - bounds.width / 2, position.y - bounds.height / 2, bounds.width, bounds.height);
    }
}
