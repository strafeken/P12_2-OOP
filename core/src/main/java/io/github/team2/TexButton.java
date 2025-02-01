package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.InputSystem.Action;

public class TexButton extends TextureObject {
    private String text;
    private Action action;
    private Rectangle bounds;
    private float buttonWidth;
    private float buttonHeight;


    public TexButton(String text, String texture, Vector2 position, Vector2 direction, 
                    float speed, Action action, float width, float height) {
        super(texture, position, direction, speed);
        this.text = text;
        this.action = action;
        this.buttonWidth = width;
        this.buttonHeight = height;
        this.bounds = new Rectangle(
            position.x - width/2, 
            position.y - height/2,
            width, 
            height
        );
    }

    @Override
    public void update() {
        // Update bounds to match current position
        bounds.setPosition(
            getPosition().x - buttonWidth/2,
            getPosition().y - buttonHeight/2
        );
        
        // Check for click
        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(
                Gdx.input.getX(), 
                Gdx.graphics.getHeight() - Gdx.input.getY()
            );
            
            // Debug prints
            System.out.println("Touch position: " + touch);
            System.out.println("Button bounds: " + bounds);
            
            if (bounds.contains(touch.x, touch.y)) {
                System.out.println("Button clicked!");
                if (action != null) {
                    action.execute();
                }
            }
        }
    }

    
	@Override
	public void draw(SpriteBatch batch) {
    	// Draw button background
    	batch.draw(getTexture(), 
        	getPosition().x - buttonWidth/2, 
        	getPosition().y - buttonHeight/2,
        	buttonWidth,
        	buttonHeight);
        
    	// Center text
    	TextManager tm = new TextManager();
    	BitmapFont font = tm.getFont();
    	float scale = font.getScaleX();
    	float textWidth = text.length() * 8 * scale; // Approximate width
    	float textHeight = 16 * scale; // Approximate height
    
    	float textX = getPosition().x - textWidth/2;
    	float textY = getPosition().y + textHeight/2;
    
    	tm.draw(batch, text, textX, textY, Color.BLACK);
	}
}