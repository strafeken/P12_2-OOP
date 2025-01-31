package io.github.team2;

import com.badlogic.gdx.math.Vector2;

public class CircleButton extends TextureObject implements Button {
    private String text;
    
    public CircleButton() {
        text = "";
    }
    
    public CircleButton(String text, Vector2 position, Vector2 direction, float speed, String texture) {
        super(texture, position, direction, speed);
        this.text = text;
    }
    
    protected void onHover() {
        // Hover effect implementation
    }
    
    @Override
    public void onClick() {
        // Click handling
    }
    
    @Override
    public void action() {
        // Action implementation
    }
    
    @Override
    public void update() {
        super.update();
    }
}