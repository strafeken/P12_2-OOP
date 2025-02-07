package io.github.team2.InputSystem;



//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
	private Vector2 position;
    private String text;
    private Texture texture;
    private Action action;
    private Rectangle bounds;
    private float buttonWidth;
    private float buttonHeight;

    private int id;



    public Button(int id ,String text, String texture, Vector2 position,
                  Action action, float width, float height) {

    	this.id = id;
    	this.text = text;
    	this.texture = new Texture(texture);
        this.position = position;
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

	public int getID() {

	    	return id;
	    }

    public Vector2 getPosition() {

    	return position;
    }

    public void setPosition(Vector2 position) {

    	this.position = position;

    }


    public Action getAction() {

    	return action;
    }

    public void setAction(Action action) {

    	this.action = action;

    }

    public boolean checkIsPressed(Vector2 touchPos) {

    	return bounds.contains(touchPos);

    }


    public void execute() {
    	action.execute();

    }

    public void update() {
        // Update bounds to match current position
        bounds.setPosition(
            getPosition().x - buttonWidth/2,
            getPosition().y - buttonHeight/2
        );

        /*
        // Check for click
        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(
                Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY()
            );

            // Debug prints
            System.out.println("Touch position: " + touch);
            System.out.println("Button bounds: " + bounds);

            if (bounds.contains(touch)) {
                System.out.println("Button clicked!");
                if (action != null) {
                    action.execute();
                }
            }
        }
        */
    }



	public void draw(SpriteBatch batch) {
    	// Draw button background
    	batch.draw(texture,
        	getPosition().x - buttonWidth/2,
        	getPosition().y - buttonHeight/2,
        	buttonWidth,
        	buttonHeight);
        /*
    	// Center text
    	TextManager tm = new TextManager();
    	BitmapFont font = tm.getFont();
    	float scale = font.getScaleX();
    	float textWidth = text.length() * 8 * scale; // Approximate width
    	float textHeight = 16 * scale; // Approximate height

    	float textX = getPosition().x - textWidth/2;
    	float textY = getPosition().y + textHeight/2;

    	tm.draw(batch, text, textX, textY, Color.BLACK);
    	*/

	}
}
