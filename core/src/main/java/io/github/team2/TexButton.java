package io.github.team2;

import com.badlogic.gdx.math.Vector2;

public class TexButton extends TextureObject implements Button {
    private String text;
    
    public TexButton() {
        text = "";
    }
    
    public TexButton(String text, String texture, Vector2 position, Vector2 direction, float speed) {
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
}